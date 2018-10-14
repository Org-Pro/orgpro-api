package fr.orgpro.api.project;

import fr.orgpro.api.collaborateur.Collaborateur;
import fr.orgpro.api.orgzly.OrgHead;
import fr.orgpro.api.orgzly.OrgProperties;
import fr.orgpro.api.orgzly.datetime.OrgDateTime;
import fr.orgpro.api.orgzly.datetime.OrgRange;
import fr.orgpro.api.orgzly.parser.OrgParserWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sartr on 01/02/2018.
 */
public class Tache {
    private static final String CLOSED = "CLOSED:";
    private static final String DEADLINE = "DEADLINE:";
    private static final String SCHEDULED = "SCHEDULED:";
    private static final String CLOCK = "CLOCK:";
    private static final String PROPERTIES = ":PROPERTIES:";
    private static final String END = ":END:";
    private static final String LOGBOOK = ":LOGBOOK:";

    private static final String PROP_CLOCK = "CLOCK";
    private static final String PROP_ID = "ID";
    private static final String PROP_DEPENDENCE = "DEPENDENCE";
    private static final String PROP_COLLABORATOR = "COLLABORATOR";
    private static final String PROP_COST = "COST";

    public static final String HEADER_COST = "COST";
    public static final String HEADER_COLLABORATOR = "COLLABORATOR";

    private OrgHead tache;
    private List<String> lstCollaborateur;
    private static LinkedHashMap<String, String> lstHeader = null;

    private OrgParserWriter ecriture;
    private SimpleDateFormat dateFormat;

    private String id;
    //private double cout;
    private Long valMinuteur = null;

    private Tache(){
        this.ecriture = new OrgParserWriter();
        this.tache = new OrgHead();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //this.ajoutProperty(PROP_COST,"0",true);
    }

    /**
     * Création d'une tâche
     * @param title Titre de la tâche
     */
    public Tache(String title) {
        this.ecriture = new OrgParserWriter();
        this.tache = new OrgHead(title);
        this.tache.setLevel(1);
        this.tache.setState(State.TODO);
        this.addPropriete(PROP_COST,"0",true);
        this.id = UUID.randomUUID().toString();
        this.addPropriete(PROP_ID, this.id, true);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    }

    /**
     * Création d'une tâche
     * @param title Titre de la tâche
     * @param lvl Niveau de la tâche
     */
    public Tache(String title,int lvl) {
        int level = lvl;
        if(level < 1){
            level = 1;
        }
        this.ecriture = new OrgParserWriter();
        this.tache = new OrgHead(title);
        this.tache.setLevel(level);
        this.tache.setState(State.TODO);
        this.addPropriete(PROP_COST,"0",true);
        this.id = UUID.randomUUID().toString();
        this.addPropriete(PROP_ID, this.id, true);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    }

    /**
     * Création d'une tâche avec dépendance
     * @param title Titre de la tâche
     * @param tache Tâche à dépendre
     */
    public Tache(String title, Tache tache) {
        this.ecriture = new OrgParserWriter();
        this.tache = new OrgHead(title);
        this.tache.setLevel(tache.getNiveau() + 1);
        this.tache.setState(State.TODO);
        this.id = UUID.randomUUID().toString();
        this.addPropriete(PROP_COST,"0",true);
        this.addPropriete(PROP_ID, this.id ,true);

        this.addPropriete( PROP_DEPENDENCE, tache.getId(), true);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    }

    /**
     * Remet à null la liste de l'en-tête
     */
    public static void resetEnTete(){
        lstHeader = null;
    }

    /**
     * Ajout d'un collaborateur dans l'en-tête
     * @param nom Nom du collaborateur (Ne peut pas contenir ":" ou être vide | Le collaborateur ne doit pas déjà exister)
     * @return True si l'ajout du collaborateur est effectué, false sinon
     */
    public static boolean addCollaborateurEnTete(String nom){
        String col = nom.toLowerCase().trim();
        if ("".equals(col) || col.contains(":")){
            return false;
        }
        if(lstHeader == null){
            lstHeader = new LinkedHashMap<String, String>();
            lstHeader.put(HEADER_COLLABORATOR, col);
            return true;
        }
        String temp = getEnTete(HEADER_COLLABORATOR);
        if(temp != null){
            String[] listCol = temp.split(":");
            for(String ele : listCol){
                if(ele.equals(col)){
                    return false;
                }
            }
            lstHeader.replace(HEADER_COLLABORATOR, temp + ":" + col);
            return true;
        }else{
            lstHeader.put(HEADER_COLLABORATOR, col);
            return true;
        }
    }

    /**
     * Modifie le nom d'un collaborateur dans l'en-tête ainsi que dans la liste des tâches dont il est lié
     * @param list Liste des tâches
     * @param oldName Nom du collaborateur (Ne peut pas contenir ":" ou être vide | Le collaborateur doit déjà exister)
     * @param newName Nouveau nom  (Ne peut pas contenir ":" ou être vide | Le collaborateur ne doit pas déjà exister)
     * @return True si la modification du nom est effectuée, false sinon
     */
    public static boolean setCollaborateurEnTete(List<Tache> list, String oldName, String newName){
        String oldCol = oldName.toLowerCase().trim();
        String newCol = newName.toLowerCase().trim();
        if ("".equals(oldCol) || oldCol.contains(":") || "".equals(newCol) || newCol.contains(":")){
            return false;
        }
        if(lstHeader == null || lstHeader.isEmpty()){
            return false;
        }
        String temp = getEnTete(HEADER_COLLABORATOR);
        boolean existe = false;
        if(temp != null){
            String[] listCol = temp.split(":");

            for(String ele : listCol){
                if(ele.equals(newCol)){
                    return false;
                }
            }

            for(int i = 0; i < listCol.length; i++){
                if(listCol[i].equals(oldCol)){
                    listCol[i] = newCol;
                    existe = true;
                    break;
                }
            }
            if(!existe){
                return false;
            }

            boolean premier = true;
            StringBuilder rst = new StringBuilder();
            for (String ele : listCol){
                if (premier){
                    rst.append(ele.trim());
                    premier = false;
                }else{
                    rst.append(":").append(ele.trim());
                }
            }
            lstHeader.replace(HEADER_COLLABORATOR, rst.toString());

            for (Tache tache : list){
                if(tache.lstCollaborateur != null) {
                    for(int i = 0; i < tache.lstCollaborateur.size(); i++){
                        if(tache.lstCollaborateur.get(i).equals(oldCol)){
                            tache.lstCollaborateur.set(i, newCol);
                            tache.writeCollaborateur();
                            break;
                        }
                    }
                }
            }
            return true;
        }else{
            return false;
        }
    }

    /**
     * Supprime le nom d'un collaborateur dans l'en-tête ainsi que dans la liste des tâches dont il est lié
     * @param list Liste des tâches
     * @param nom Nom du collaborateur (Ne peut pas contenir ":" ou être vide | Le collaborateur doit déjà exister)
     * @return True si la suppression du nom est effectuée, false sinon
     */
    public static boolean removeCollaborateurEnTete(List<Tache> list, String nom){
        String col = nom.toLowerCase().trim();
        if ("".equals(col) || col.contains(":")){
            return false;
        }
        if(lstHeader == null || lstHeader.isEmpty()){
            return false;
        }
        String temp = getEnTete(HEADER_COLLABORATOR);
        boolean existe = false;
        if(temp != null){
            String[] listCol = temp.split(":");
            List<String> newListCol = new ArrayList<String>();
            for (String ele : listCol) {
                if (ele.equals(col)) {
                    existe = true;
                } else {
                    newListCol.add(ele);
                }
            }
            if(!existe){
                return false;
            }

            boolean premier = true;
            StringBuilder rst = new StringBuilder();
            for (String ele : newListCol){
                if (premier){
                    rst.append(ele.trim());
                    premier = false;
                }else{
                    rst.append(":").append(ele.trim());
                }
            }

            if("".equals(rst.toString().trim())){
                Tache.removeEnTete(HEADER_COLLABORATOR, true);
            }else{
                lstHeader.replace(HEADER_COLLABORATOR, rst.toString());
            }

            for (Tache tache : list){
                if(tache.lstCollaborateur != null) {
                    for(int i = 0; i < tache.lstCollaborateur.size(); i++){
                        if(tache.lstCollaborateur.get(i).equals(col)){
                            tache.lstCollaborateur.remove(i);
                            tache.writeCollaborateur();
                            break;
                        }
                    }
                }
            }
            return true;
        }else{
            return false;
        }
    }

    /**
     * Liste les collaborateurs de l'en-tête
     * @return La liste des collaborateurs
     */
    public static List<String> getCollaborateurEnTeteListe(){
        List<String> list = new ArrayList<String>();
        String colHeader = getEnTete(HEADER_COLLABORATOR);
        if (colHeader == null){
            return null;
        }else {
            String[] tab = colHeader.split(":");
            Collections.addAll(list, tab);
            return list;
        }
    }

    public List<String> getCollaborateurFromTache(Tache t) {
        List<String> list = new ArrayList<>();
        if(t.lstCollaborateur != null) {
            for(int i = 0; i < t.lstCollaborateur.size(); i++) {
                    list.add(lstCollaborateur.get(i));
                }
            }
        return list;
    }

    /**
     * Ajoute un collaborateur à la tâche
     * @param nom Nom du collaborateur (Ne peut pas contenir ":" ou être vide | Le collaborateur doit exister dans l'en-tête | Le collaborateur ne doit pas déjà être lié à la tâche)
     * @return True si l'ajout est effectué, false sinon
     */
    public boolean addCollaborateur(String nom){
        String val = nom.toLowerCase().trim();
        if (val.contains(":") || val.isEmpty()){
            return false;
        }
        if(lstCollaborateur == null){
            lstCollaborateur = new ArrayList<String>();
        }
        for (String ele : lstCollaborateur){
            if(ele.equals(val)){
                return false;
            }
        }
        boolean existe = false;
        List<String> listColHeader = getCollaborateurEnTeteListe();
        if (listColHeader == null || listColHeader.isEmpty()){
            return false;
        }
        for(String col : listColHeader){
            if(col.equals(val)){
                existe = true;
            }
        }
        if (!existe){
            return false;
        }
        lstCollaborateur.add(val);
        writeCollaborateur();
        return true;
    }

    /**
     * Supprime un collaborateur de la tâche
     * @param nom Nom du collaborateur (Ne peut pas contenir ":" ou être vide |Le collaborateur doit déjà être lié à la tâche)
     * @return True si la suppression est effectuée, false sinon
     */
    public boolean removeCollaborateur(String nom){
        String val = nom.toLowerCase().trim();
        if (val.contains(":")|| val.isEmpty()){
            return false;
        }
        if(lstCollaborateur == null ||lstCollaborateur.isEmpty()){
            return false;
        }
        Iterator<String> iterator = lstCollaborateur.iterator();
        while(iterator.hasNext()){
            String tmp = iterator.next();
            if(tmp.equals(val)){
                iterator.remove();
                writeCollaborateur();
                return true;
            }
        }
        return false;
    }

    private void writeCollaborateur(){
        if (lstCollaborateur == null || lstCollaborateur.isEmpty()){
            this.removePropriete(PROP_COLLABORATOR, true);
        }else{
            boolean premier = true;
            StringBuilder rst = new StringBuilder();
            for (String ele : lstCollaborateur){
                if (premier){
                    rst.append(ele.trim());
                    premier = false;
                }else{
                    rst.append(":").append(ele.trim());
                }
            }
            this.addPropriete(PROP_COLLABORATOR, rst.toString(), true);
        }
    }

    /**
     * Modifie la dépendance de la tâche
     * @param tache Tâche à dépendre
     */
    public void setDependance(Tache tache){
        this.removePropriete(PROP_DEPENDENCE, true);
        this.tache.setLevel(tache.getNiveau() + 1);
        this.addPropriete( PROP_DEPENDENCE, tache.getId(), true);
    }

    /**
     * Supprime la dépendance de la tâche
     */
    public void removeDependance(){
        this.removePropriete(PROP_DEPENDENCE, true);
        this.tache.setLevel(1);
    }

    /**
     * Ajoute un coût à la tâche
     * @param cout Le coût à attribuer
     * @return True si l'ajout du coût est effectué, false sinon
     */
    public boolean addCout(int cout){
        if(cout < 0){
            return false;
        }
        this.removePropriete(PROP_COST,true);
        this.addPropriete(PROP_COST, String.valueOf(cout),true);
        return true;
    }

    private void setId(String id) {
        this.id = id;
        this.addPropriete(PROP_ID, this.id, true);
    }

    private void setEtatTache(State state){
        tache.setState(state);
    }

    private void setMinuteur(Long time){
        tache.setClock(time);
    }

    /**
     * Lance ou stop le minuteur et écrit dans le registre chaque temps
     * @return True si le minuteur est actif, false s'il est stoppé
     */
    public boolean useMinuteur(){
        if(valMinuteur == null){
            valMinuteur = System.currentTimeMillis();
            return true;
        }else{
            Long valEndMinuteur = System.currentTimeMillis();
            String s = new OrgRange(new OrgDateTime(valMinuteur , true), new OrgDateTime(valEndMinuteur , true), valMinuteur, valEndMinuteur).toString();
            this.addRegistreTexte(s);
            if(tache.getClock() == null){
                tache.setClock(valEndMinuteur - valMinuteur);
            }else{
                tache.setClock(tache.getClock() + (valEndMinuteur - valMinuteur));
            }
            valMinuteur = null;
            return false;
        }
    }

    /**
     * Lance ou stop le minuteur en sauvegardant le temps initial lors du lancement et écrit dans le registre chaque temps
     * @return True si le minuteur est actif, false s'il est stoppé
     */
    public boolean useMinuteurParPropriete(){
        boolean rst;
        String val = this.getProperties().get(PROP_CLOCK);
        if(val == null){
            valMinuteur = null;
            rst = useMinuteur();
            this.addPropriete(PROP_CLOCK, valMinuteur.toString(), true);
        }else{
            valMinuteur = Long.parseLong(val);
            rst = useMinuteur();
            this.removePropriete(PROP_CLOCK, true);
        }
        return rst;
    }

    /**
     * Coût actuel de la tâche
     * @return Le coût de la tâche
     */
    public int getCout() {
        return Integer.parseInt(getProperties().get(PROP_COST));
    }

    /**
     * Etat actuel de la tâche
     * @return L'etat de la tâche
     */
    public State getEtat() {
        return tache.getState();
    }

    /**
     * Temps total actuel de la tâche
     * @return Le temps passé sur la tâche en millisecondes
     */
    public Long getMinuteur(){
        return tache.getClock();
    }

    /**
     * Temps total actuel de la tâche
     * @return Le temps passé sur la tâche au format texte
     */
    public String getMinuteurTexte(){
        return tache.getClockString();
    }

    /**
     * Titre actuel de la tâche
     * @return Le titre de la tâche
     */
    public String getTitre(){
        return tache.getTitle();
    }

    /**
     * Niveau actuel de la tâche
     * @return Le niveau de la tâche
     */
    public int getNiveau(){
        return tache.getLevel();
    }

    /**
     * Liste des tags associés à la tâche
     * @return La liste des tags
     */
    public List<String> getTagListe(){
        return tache.getTags();
    }

    /**
     * Date limite actuelle de la tâche
     * @return La date limite
     */
    public Date getDateLimite(){
        return tache.getDeadline();
    }

    /**
     * Date de commencement actuelle de la tâche
     * @return La date de commencement de la tâche
     */
    public Date getDateDebut(){
        return tache.getScheduled();
    }

    /**
     * Date d'aboutissement actuelle de la tâche
     * @return La date d'aboutissement de la tâche
     */
    public Date getDateFin(){
        return tache.getClosed();
    }

    /**
     * Proriétés liées à la tâche
     * @return Un objet contenant la liste des propriétés liées à la tâche
     */
    public OrgProperties getProperties(){
        return tache.getProperties();
    }

    /**
     * L'id actuel de la tâche
     * @return L'id de la tâche
     */
    public String getId() {
        return id;
    }

    /**
     * Modifie le niveau de la tâche
     * @param level Nouveau niveau de la tâche (Le niveau doit être positif)
     * @return True si la modification du niveau est effectuée, false sinon
     */
    public boolean setNiveau(int level){
        if(level < 1 || this.getProperties().get(PROP_DEPENDENCE)!= null){
            return false;
        }else{
            tache.setLevel(level);
        }
        return true;
    }

    /**
     * Modifie le titre la tâche
     * @param title Nouveau titre
     */
    public void setTitre(String title){
        tache.setTitle(title);
    }

    /**
     * Change l'état de la tâche et écrit dans le registre le changement (TODO -> ONGOING -> DONE -> CANCELLED)
     * @param state Nouvel état de la tâche (Doit respecter les règles du kanban)
     * @return True si le changement d'état de la tâche est effectué, false sinon
     */
    public boolean setEtat(State state){
        if(tache.getState().equals(state)){
            return false;
        }
        if(tache.getState().toString().equals(State.ONGOING.toString()) && state.toString().equals(State.TODO.toString())){
            return false;
        }else if(tache.getState().toString().equals(State.DONE.toString()) && state.toString().equals(State.TODO.toString())){
            return false;
        }else if(tache.getState().toString().equals(State.DONE.toString()) && state.toString().equals(State.ONGOING.toString())){
            return false;
        }else if(tache.getState().toString().equals(State.CANCELLED.toString()) && state.toString().equals(State.TODO.toString())){
            return false;
        }else if(tache.getState().toString().equals(State.CANCELLED.toString()) && state.toString().equals(State.ONGOING.toString())){
            return false;
        }else if(tache.getState().toString().equals(State.CANCELLED.toString()) && state.toString().equals(State.DONE.toString())){
            return false;
        }

        String log = "- State \"" + state.toString() + "\" FROM \"" + tache.getState().toString() + "\" ";
        this.addRegistre(log);
        this.tache.setState(state);

        return true;
    }

    /**
     * Passe l'état de la tâche à l'état suivant en respectant les règles du kanban (TODO -> ONGOING -> DONE)
     * @return True si le changement d'état est effectué, false sinon
     */
    public boolean setEtatSuivant() {
        if(tache.getState().toString().equals(State.TODO.toString())){
            this.setEtatTache(State.ONGOING);
        }
        else if(tache.getState().toString().equals(State.ONGOING.toString())){
            this.setEtatTache(State.DONE);
        }
        else if(tache.getState().toString().equals(State.DONE.toString()) || tache.getState().toString().equals(State.CANCELLED.toString())){
            return false;
        }
        return true;
    }

    /**
     * Ajoute un tag à la tâche
     * @param nomTag Tag à ajouter à la tâche (Ne peut pas contenir ":" ou être vide |Le tag ne doit pas déjà être lié à la tâche)
     * @return True si l'ajout est effectué, false sinon
     */
    public boolean addTag(String nomTag){
        String tag = nomTag.toUpperCase().trim();
        if ("".equals(tag) || tag.contains(":")){
            return false;
        }
        for (String ele : tache.getTags()){
            if(ele.equals(tag)){
                return false;
            }
        }
        List<String> tags = tache.getTags();
        tags.add(tag);
        int i = 0;
        String[] tagsTemp = new String[tags.size()];
        for (String tagTemp: tags) {
            tagsTemp[i]=tagTemp;
            i++;
        }
        tache.setTags(tagsTemp);
        return true;
    }

    /**
     * Supprime un tag de la tâche
     * @param nomTag Tag à supprimer à la tâche (Ne peut pas contenir ":" ou être vide |Le tag doit déjà être lié à la tâche)
     * @return True si la suppression est effectuée, false sinon
     */
    public boolean removeTag(String nomTag){
        String tag = nomTag.toUpperCase().trim();
        if ("".equals(tag) || tag.contains(":")){
            return false;
        }
        boolean existe = false;
        for (String ele : tache.getTags()){
            if(ele.equals(tag)){
                existe = true;
                break;
            }
        }
        if(!existe){
            return false;
        }
        List<String> tags = tache.getTags();
        int i = 0;

        Iterator<String> it = tags.iterator();
        while (it.hasNext()) {
            String ele = it.next();
            if(ele.equals(tag)){
                it.remove();
                break;
            }
        }

        String[] tagsTemp = new String[tags.size()];
        for (String tagTemp: tags) {
            tagsTemp[i]=tagTemp;
            i++;
        }
        tache.setTags(tagsTemp);
        return true;
    }

    /**
     * Ajoute ou modifie la date limite de la tâche
     * @param deadline Date limite de la tâche (Au format YYYY-MM-DD ou YYYY/MM/DD)
     * @return True si le changement de la date limite est effectué, false sinon
     */
    public boolean addDateLimite(String deadline){
        try {
            Date deadlineDate = dateFormat.parse(deadline);
            String dateValide = dateFormat.format(deadlineDate);
            if(dateValide.compareTo(deadline) != 0){
                return false;
            }
            tache.setDeadline(deadlineDate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Ajoute ou modifie la date de commencement de la tâche
     * @param scheduled Date de commencement de la tâche (Au format YYYY-MM-DD ou YYYY/MM/DD)
     * @return True si le changement de la date de commencement est effectué, false sinon
     */
    public boolean addDateDebut(String scheduled){
        try {
            Date scheduledDate = dateFormat.parse(scheduled);
            String dateValide = dateFormat.format(scheduledDate);
            if(dateValide.compareTo(scheduled) != 0){
                return false;
            }
            tache.setScheduled(scheduledDate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Ajoute ou modifie la date d'aboutissement de la tâche
     * @param closed Date d'aboutissement de la tâche (Au format YYYY-MM-DD ou YYYY/MM/DD)
     * @return True si le changement de la date d'aboutissement est effectué, false sinon
     */
    public boolean addDateFin(String closed){
        try {
            Date closedDate = dateFormat.parse(closed);
            String dateValide = dateFormat.format(closedDate);
            if(dateValide.compareTo(closed) != 0){
                return false;
            }
            tache.setClosed(closedDate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Ajoute une propriété à la tâche
     * @param name Clef de la propriété
     * @param value Valeur de la propriété
     * @param constructor Si true, les valeurs des clefs suivantes peuvent être modifiées : ID, DEPENDENCE, CLOCK, COLLABORATOR, COST
     * @return True l'ajout de la propriété de la tâche est effectué, false sinon
     */
    public boolean addPropriete(String name, String value, boolean constructor){
        String name2 = name.toUpperCase();
        if(constructor){
            tache.addProperty(name,value);
        }else{
            if(PROP_ID.equals(name2) || PROP_DEPENDENCE.equals(name2)
                    || PROP_CLOCK.equals(name2) || PROP_COLLABORATOR.equals(name2)
                    || PROP_COST.equals(name2)){
                return false;
            }else{
                tache.addProperty(name,value);
            }
        }
        return true;
    }

    /**
     * Supprime une propriété de la tâche
     * @param name Clef de la propriété
     * @param constructor Si true, les valeurs des clefs suivantes peuvent être modifiées : ID, DEPENDENCE, CLOCK, COLLABORATOR, COST
     * @return True la suppression de la propriété de la tâche est effectuée, false sinon
     */
    public boolean removePropriete(String name, boolean constructor){
        if(!constructor && (PROP_ID.equalsIgnoreCase(name) || PROP_DEPENDENCE.equalsIgnoreCase(name)
                || PROP_CLOCK.equalsIgnoreCase(name) || PROP_COLLABORATOR.equalsIgnoreCase(name)
                || PROP_COST.equalsIgnoreCase(name))){
            return false;
        }
        OrgProperties properties = tache.getProperties();
        properties.remove(name);
        tache.setProperties(properties);
        return true;
    }

    /**
     * Ajoute une ligne au registre de la tâche
     * @param log Valeur à ajouter au registre de la tâche
     */
    private void addRegistre(String log){
        Date date = new Date();
        String newLog = log + new SimpleDateFormat("<yyyy-MM-dd HH:mm>").format(date);
        tache.addLog(newLog);
    }

    private void addRegistreTexte(String log){
        tache.addLog(log);
    }

    /**
     * Création ou modification du fichier
     * @param path Chemin du fichier
     * @param append True pour une écrire à la fin du fichier, false pour réécrire tout le fichier
     * @return True si la modification du fichier est effectuée
     */
    public boolean writeFichier(String path, boolean append){
        OrgParserWriter ecriture = new OrgParserWriter();
        String ecrire = "";
        if(!append && lstHeader != null && !lstHeader.isEmpty()){
            StringBuilder s = new StringBuilder();
            lstHeader.forEach((k, v) -> {
                s.append("#+").append(k).append(": ").append(v).append("\n");
            });
            s.append("\n");
            ecrire += s.toString();
        }
        ecrire += ecriture.whiteSpacedHead(tache,tache.getLevel(),true);
        try {
            FileWriter ffw = new FileWriter(path,append);
            ffw.write(ecrire);
            ffw.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Ajout d'une valeur dans l'en-tête
     * @param clef Clef de l'en-tête
     * @param valeur Valeur de l'en-tête
     * @param constructor Si true, les valeurs des clefs suivantes peuvent être ajoutées : COLLABORATOR, COST
     * @return True si l'ajout dans l'en-tête est effectué, false sinon
     */
    public static boolean addEnTete(String clef, String valeur, boolean constructor){
        if(lstHeader == null){
            lstHeader = new LinkedHashMap<String, String>();
        }
        if(!constructor){
            if(HEADER_COST.equalsIgnoreCase(clef.trim()) || HEADER_COLLABORATOR.equalsIgnoreCase(clef.trim())){
                return false;
            }
        }
        if(lstHeader.get(clef.trim()) != null || "".equals(clef.trim()) || "".equals(valeur)) {
            return false;
        }
        lstHeader.put(clef.trim(), valeur.trim());
        return true;
    }

    /**
     * Valeur de l'en-tête selon la clef
     * @param clef La clef liée à la valeur
     * @return La valeur de l'en-tête. Null s'il elle n'existe pas
     */
    public static String getEnTete(String clef){
        if(lstHeader == null || "".equals(clef.trim())){
            return null;
        }
        return lstHeader.get(clef.trim());
    }

    /**
     * Modifie la valeur de l'en-tête selon la clef
     * @param clef La clef liée à la valeur
     * @param valeur La nouvelle valeur
     * @param constructor Si true, les valeurs des clefs suivantes peuvent être modifiées : COLLABORATOR, COST
     * @return True si la modification de valeur de l'en-tête est effectuée, false sinon
     */
    public static boolean setEnTete(String clef, String valeur, boolean constructor){
        if(!constructor){
            if(HEADER_COST.equalsIgnoreCase(clef.trim())|| HEADER_COLLABORATOR.equalsIgnoreCase(clef.trim())){
                return false;
            }
        }
        if(lstHeader == null || "".equals(clef.trim()) || "".equals(valeur)){
            return false;
        }
        if(lstHeader.get(clef.trim()) == null){
            return false;
        }
        lstHeader.replace(clef.trim(), valeur.trim());
        return true;
    }

    /**
     * Supprime la valeur et la clef de l'en-tête selon la clef
     * @param clef La clef à supprimer
     * @param constructor Si true, les valeurs des clefs suivantes peuvent être supprimée : COLLABORATOR, COST
     * @return True si la suppression de la clef / valeur est effectuée, false sinon
     */
    public static boolean removeEnTete(String clef, boolean constructor){
        if(!constructor){
            if(HEADER_COST.equalsIgnoreCase(clef.trim())|| HEADER_COLLABORATOR.equalsIgnoreCase(clef.trim())){
                return false;
            }
        }
        if(lstHeader == null || "".equals(clef.trim())){
            return false;
        }
        if(lstHeader.get(clef.trim()) == null){
            return false;
        }
        lstHeader.remove(clef.trim());
        return true;
    }

    /**
     * Ajoute une dépendance à une tâche
     * @param list Liste des tâches
     * @param numTache La tâche qui dépend (La tâche doit exister dans la liste)
     * @param numTacheDep La tâche qui reçoit la dépendance (La tâche doit exister dans la liste)
     * @return True si la modification de la dépendance est effectuée, false sinon
     */
    public static boolean setDependanceListe(List<Tache> list, int numTache, int numTacheDep){
        if(numTache < 0 || list.size() - 1 < numTache || numTacheDep < 0 || list.size() - 1 < numTacheDep || numTache == numTacheDep){
            return false;
        }
        if(list.size() > numTache + 1){
            if (list.get(numTache + 1).getNiveau() > list.get(numTache).getNiveau()){
                return false;
            }
        }
        list.get(numTache).setDependance(list.get(numTacheDep));
        Tache tache = list.get(numTache);
        list.remove(numTache);
        if(numTache < numTacheDep){
            list.add(numTacheDep, tache);
        }else {
            list.add(numTacheDep + 1, tache);
        }

        return true;
    }

    /**
     * Lecture du fichier
     * @param path Chemin du fichier
     * @return La liste des tâches du fichier. Null s'il y eu un problème lors de la lecture
     */
    public static List<Tache> readFichier(String path){
        lstHeader = null;
        List<Tache> list = new ArrayList<Tache>();

        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            String[] temp;
            Tache tache = null;
            while (line != null) {
                switch (line.trim()){
                    case "":{
                        if(tache != null){
                            list.add(tache);
                            tache = null;
                        }
                        break;
                    }
                    default:{
                        if(line.startsWith("*")){
                            tache = new Tache();

                            temp = line.split("( )+", 3);
                            if(temp[1].equals(State.TODO.toString())){
                                tache.setEtatTache(State.TODO);
                            }else if(temp[1].equals(State.DONE.toString())){
                                tache.setEtatTache(State.DONE);
                            }else if(temp[1].equals(State.ONGOING.toString())){
                                tache.setEtatTache(State.ONGOING);
                            }else if(temp[1].equals(State.CANCELLED.toString())){
                                tache.setEtatTache(State.CANCELLED);
                            }
                            tache.setNiveau(temp[0].trim().length());

                            temp = temp[2].split(":");
                            tache.setTitre(temp[0].trim());
                            for (int i = 1; i < temp.length; i++){
                                tache.addTag(temp[i]);
                            }
                        }else if(line.trim().startsWith(CLOCK) && tache != null){
                            temp = line.split(":");
                            long heure = Long.parseLong(temp[1].trim());
                            long minute = Long.parseLong(temp[2].trim());
                            long seconde = Long.parseLong(temp[3].trim());
                            tache.setMinuteur((heure * 3600000) + (minute * 60000) + (seconde * 1000 ));
                        }else if((line.trim().startsWith(CLOSED) || line.trim().startsWith(DEADLINE) || line.trim().startsWith(SCHEDULED)) && tache != null){
                            temp = line.split(">");
                            for (String ele : temp){
                                String[] s = ele.split("<");
                                switch (s[0].trim()){
                                    case CLOSED:{
                                        tache.addDateFin(s[1].trim());
                                        break;
                                    }
                                    case DEADLINE:{
                                        tache.addDateLimite(s[1].trim());
                                        break;
                                    }
                                    case SCHEDULED:{
                                        tache.addDateDebut(s[1].trim());
                                        break;
                                    } default:{
                                        break;
                                    }
                                }
                            }
                        }else if(line.trim().startsWith(PROPERTIES) && tache != null){
                            line = br.readLine().trim();
                            while(!line.equals(END)){
                                temp = line.split(":", 3);
                                if(temp[1].equals(PROP_ID)){
                                    tache.setId(temp[2].trim());
                                }else if(temp[1].equals(PROP_DEPENDENCE) || temp[1].equals(PROP_CLOCK)) {
                                    tache.addPropriete(temp[1].trim(), temp[2].trim(), true);
                                }else if(temp[1].equals(PROP_COLLABORATOR)){
                                    tache.addPropriete(PROP_COLLABORATOR, temp[2].trim(), true);
                                    tache.lstCollaborateur = new ArrayList<String>();
                                    temp = temp[2].trim().split(":");
                                    tache.lstCollaborateur.addAll(Arrays.asList(temp));
                                }else if(temp[1].equals(PROP_COST)){
                                    //tache.cout = Double.parseDouble(temp[2].trim());
                                    tache.addPropriete(temp[1].trim(), temp[2].trim(), true);
                                }else{
                                    tache.addPropriete(temp[1].trim(), temp[2].trim(), false);
                                }
                                line = br.readLine().trim();
                            }
                        }else if(line.trim().startsWith(LOGBOOK) && tache != null){
                            line = br.readLine().trim();
                            while(!line.equals(END)){
                                tache.addRegistreTexte(line);
                                line = br.readLine().trim();
                            }
                        }else if(line.trim().startsWith("#+")){
                            if(lstHeader == null){
                                lstHeader = new LinkedHashMap<String, String>();
                            }
                            temp = line.split(":", 2);
                            temp[0] = temp[0].substring(2);
                            lstHeader.put(temp[0].trim(), temp[1].trim());
                        }
                        break;
                    }
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            return null;
        }
        return list;
    }

    /**
     * Supprime une tâche
     * @param taches Liste des tâches
     * @param numTache Numéro de la tâche (La tâche doit exister dans la liste)
     * @return True si la suppression est effectuée, false sinon
     */
    public static boolean removeTache(List<Tache> taches, int numTache) {
        if(numTache < 0 || numTache >= taches.size() || taches == null){
            return false;
        }
        List<Tache> tacheSup = new ArrayList<Tache>();
        int level = taches.get(numTache).getNiveau();
        tacheSup.add(taches.get(numTache));
        if(taches.size() > numTache + 1) {
            int i;
            for (i = numTache + 1; i < taches.size(); i++) {
                if (taches.get(i).getNiveau() <= level) {
                    break;
                } else {
                    tacheSup.add(taches.get(i));
                }
            }
        }
        for(Tache tSup : tacheSup){
            taches.remove(tSup);
        }
        return true;
    }

    /**
     * Supprime la dépendance d'une tâche
     * @param list La liste des tâches
     * @param numTache Numéro de la tâche (Elle ne doit pas avoir une tâche qui dépend d'elle)
     * @return True si la suppression de la dépendance tâche est effectué, false sinon
     */
    public static boolean removeDependanceListe(List<Tache> list, int numTache){
        if(numTache < 0 || list.size() - 1 < numTache){
            return false;
        }
        if(list.size() > numTache + 1){
            if (list.get(numTache + 1).getNiveau() > list.get(numTache).getNiveau()){
                return false;
            }
        }
        
        list.get(numTache).removeDependance();
        Tache tache = list.get(numTache);
        list.remove(numTache);
        list.add(tache);
        return true;
    }

    @Override
    public String toString() {
        return ecriture.whiteSpacedHead(tache,tache.getLevel(),true);
    }
}
