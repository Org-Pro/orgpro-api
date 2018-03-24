package fr.orgpro.api.project;

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

    public Tache(String title,int level) {
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

    public static void resetEnTete(){
        lstHeader = null;
    }

    public static boolean addCollaborateurEnTete(String col){
        col = col.toLowerCase().trim();
        if (col.equals("") || col.contains(":")){
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

    public static boolean setCollaborateurEnTete(List<Tache> list, String oldCol, String newCol){
        oldCol = oldCol.toLowerCase().trim();
        newCol = newCol.toLowerCase().trim();
        if (oldCol.equals("") || oldCol.contains(":") || newCol.equals("") || newCol.contains(":")){
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

    public static boolean removeCollaborateurEnTete(List<Tache> list, String col){
        col = col.toLowerCase().trim();
        if (col.equals("") || col.contains(":")){
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

            if(rst.toString().trim().equals("")){
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

    public boolean addCollaborateur(String val){
        val = val.toLowerCase().trim();
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

    public boolean removeCollaborateur(String val){
        val = val.toLowerCase().trim();
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

    /*private String valeurCollaborateurHeader(){
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

    }*/

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

    public void setDependance(Tache tache){
        this.removePropriete(PROP_DEPENDENCE, true);
        this.tache.setLevel(tache.getNiveau() + 1);
        this.addPropriete( PROP_DEPENDENCE, tache.getId(), true);
    }

    public void removeDependance(){
        this.removePropriete(PROP_DEPENDENCE, true);
        this.tache.setLevel(1);
    }

    public boolean addCout(int cout){
        if(cout < 0){
            return false;
        }
        //this.cout = cout;
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
     * Lance ou stop le minuteur
     * @return True si le minuteur est actif
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

    public int getCout() {
        return Integer.parseInt(getProperties().get(PROP_COST));
    }

    public State getEtat() {
        return tache.getState();
    }

    public Long getMinuteur(){
        return tache.getClock();
    }

    public String getMinuteurTexte(){
        return tache.getClockString();
    }

    public String getTitre(){
        return tache.getTitle();
    }

    public int getNiveau(){
        return tache.getLevel();
    }

    public List<String> getTagListe(){
        return tache.getTags();
    }

    public Date getDateLimite(){
        return tache.getDeadline();
    }

    public Date getDateDebut(){
        return tache.getScheduled();
    }

    public Date getDateFin(){
        return tache.getClosed();
    }

    public OrgProperties getProperties(){
        return tache.getProperties();
    }

    public String getId() {
        return id;
    }

    public boolean setNiveau(int level){
        if(level < 1 || this.getProperties().get(PROP_DEPENDENCE)!= null){
            return false;
        }else{
            tache.setLevel(level);
        }
        return true;
    }

    public void setTitre(String title){
        tache.setTitle(title);
    }

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

    public void addTag(String tag){
        List<String> tags = tache.getTags();
        tags.add(tag);
        int i = 0;
        String[] tagsTemp = new String[tags.size()];
        for (String tagTemp: tags) {
            tagsTemp[i]=tagTemp;
            i++;
        }
        tache.setTags(tagsTemp);
    }

    public void removeTag(String tag){
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
    }

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

    public boolean addPropriete(String name, String value, boolean constructor){
        String name2 = name.toUpperCase();
        if(constructor){
            tache.addProperty(name,value);
        }else{
            if(name2.equals(PROP_ID) || name2.equals(PROP_DEPENDENCE)
                    || name2.equals(PROP_CLOCK) || name2.equals(PROP_COLLABORATOR)
                    || name2.equals(PROP_COST)){
                return false;
            }else{
                tache.addProperty(name,value);
            }
        }
        return true;
    }

    public boolean removePropriete(String name, boolean interne){
        if(!interne && (name.toUpperCase().equals(PROP_ID) || name.toUpperCase().equals(PROP_DEPENDENCE)
                || name.toUpperCase().equals(PROP_CLOCK) || name.toUpperCase().equals(PROP_COLLABORATOR)
                || name.toUpperCase().equals(PROP_COST))){
            return false;
        }
        OrgProperties properties = tache.getProperties();
        properties.remove(name);
        tache.setProperties(properties);
        return true;
    }

    private void addRegistre(String log){
        Date date = new Date();
        log += new SimpleDateFormat("<yyyy-MM-dd HH:mm>").format(date);
        tache.addLog(log);
    }

    private void addRegistreTexte(String log){
        tache.addLog(log);
    }

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

    public static boolean addEnTete(String clef, String valeur, boolean constructor){
        if(lstHeader == null){
            lstHeader = new LinkedHashMap<String, String>();
        }
        if(!constructor){
            if(clef.trim().toUpperCase().equals(HEADER_COST)){
                return false;
            }
        }
        if(lstHeader.get(clef.trim()) != null || clef.trim().equals("") || valeur.equals("")){
            return false;
        }
        lstHeader.put(clef.trim(), valeur.trim());
        return true;
    }

    public static String getEnTete(String clef){
        if(lstHeader == null || clef.trim().equals("")){
            return null;
        }
        return lstHeader.get(clef.trim());
    }

    public static boolean setEnTete(String clef, String valeur, boolean constructor){
        if(!constructor){
            if(clef.trim().toUpperCase().equals(HEADER_COST)){
                return false;
            }
        }
        if(lstHeader == null || clef.trim().equals("") || valeur.equals("")){
            return false;
        }
        if(lstHeader.get(clef.trim()) == null){
            return false;
        }
        lstHeader.replace(clef.trim(), valeur.trim());
        return true;
    }

    public static boolean removeEnTete(String clef, boolean constructor){
        if(!constructor){
            if(clef.trim().toUpperCase().equals(HEADER_COST)){
                return false;
            }
        }
        if(lstHeader == null || clef.trim().equals("")){
            return false;
        }
        if(lstHeader.get(clef.trim()) == null){
            return false;
        }
        lstHeader.remove(clef.trim());
        return true;
    }

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
