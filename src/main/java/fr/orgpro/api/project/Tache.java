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
    private static final String ID = "ID";
    private static final String CLOSED = "CLOSED:";
    private static final String DEADLINE = "DEADLINE:";
    private static final String SCHEDULED = "SCHEDULED:";
    private static final String CLOCK = "CLOCK:";
    private static final String PROPERTIES = ":PROPERTIES:";
    private static final String END = ":END:";
    private static final String LOGBOOK = ":LOGBOOK:";

    private OrgHead tache;

    private OrgParserWriter ecriture;
    private SimpleDateFormat dateFormat;

    private String id;
    private Long valMinuteur = null;

    private Tache(){
        this.ecriture = new OrgParserWriter();
        this.tache = new OrgHead();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public Tache(String title) {
        this.ecriture = new OrgParserWriter();
        this.tache = new OrgHead(title);
        this.tache.setLevel(1);
        this.tache.setState(State.TODO);
        this.id = UUID.randomUUID().toString();
        this.ajoutProperty("ID", this.id, true);
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
        this.id = UUID.randomUUID().toString();
        this.ajoutProperty("ID", this.id, true);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public Tache(String title, Tache tache) {
        this.ecriture = new OrgParserWriter();
        this.tache = new OrgHead(title);
        this.tache.setLevel(tache.getLevel() + 1);
        this.tache.setState(State.TODO);
        this.id = UUID.randomUUID().toString();
        this.ajoutProperty("ID", this.id ,true);
        this.ajoutProperty( "DEPENDENCE", tache.getId(), true);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public void setDependance(Tache tache){
        this.supprimerProperty("DEPENDENCE", true);
        this.tache.setLevel(tache.getLevel() + 1);
        this.ajoutProperty( "DEPENDENCE", tache.getId(), true);
    }

    public void removeDependance(){
        this.supprimerProperty("DEPENDENCE", true);
        this.tache.setLevel(1);
    }

    private void setId(String id) {
        this.id = id;
        this.ajoutProperty("ID", this.id, true);
    }

    private void setState(State state){
        tache.setState(state);
    }

    private void setClock(Long time){
        tache.setClock(time);
    }

    /**
     * Lance ou stop le minuteur
     * @return True si le minuteur est actif
     */
    public boolean minuteur(){
        if(valMinuteur == null){
            valMinuteur = System.currentTimeMillis();
            return true;
        }else{
            Long valEndMinuteur = System.currentTimeMillis();
            String s = new OrgRange(new OrgDateTime(valMinuteur , true), new OrgDateTime(valEndMinuteur , true), valMinuteur, valEndMinuteur).toString();
            this.ajoutLogBookString(s);
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
     * Remet à 0 la valeur du minuteur de la tache
     */
    public void resetMinuteur(){
        tache.setClock(null);
        valMinuteur = null;
    }

    public State getState() {
        return tache.getState();
    }

    public Long getClock(){
        return tache.getClock();
    }

    public String getClockString(){
        return tache.getClockString();
    }

    public String getTitle(){
        return tache.getTitle();
    }

    public int getLevel(){
        return tache.getLevel();
    }

    public List<String> getTags(){
        return tache.getTags();
    }

    public Date getDeadline(){
        return tache.getDeadline();
    }

    public Date getScheduled(){
        return tache.getScheduled();
    }

    public Date getClosed(){
        return tache.getClosed();
    }

    public OrgProperties getProperties(){
        return tache.getProperties();
    }

    public String getId() {
        return id;
    }

    public boolean changeLevel(int level){
        if(level < 1 || this.getProperties().get("DEPENDENCE")!= null){
            return false;
        }else{
            tache.setLevel(level);
        }
        return true;
    }

    public void changeTitle(String title){
        tache.setTitle(title);
    }

    public boolean changeState(State state){
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
        this.ajoutLogBook(log);
        this.tache.setState(state);

        return true;
    }

    public boolean nextState() {
        if(tache.getState().toString().equals(State.TODO.toString())){
            this.setState(State.ONGOING);
        }
        else if(tache.getState().toString().equals(State.ONGOING.toString())){
            this.setState(State.DONE);
        }
        else if(tache.getState().toString().equals(State.DONE.toString()) || tache.getState().toString().equals(State.CANCELLED.toString())){
            return false;
        }
        return true;
    }

    public void ajoutTag(String tag){
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

    public void supprimerTag(String tag){
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

    public boolean ajoutDeadline(String deadline){
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

    public boolean ajoutScheduled(String scheduled){
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

    public boolean ajoutClosed(String closed){
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

    public boolean ajoutProperty(String name, String value, boolean constructor){
        String name2 = name.toUpperCase();
        if(constructor){
            tache.addProperty(name,value);
        }else{
            if(name2.equals("ID") || name2.equals("DEPENDENCE")){
                return false;
            }else{
                tache.addProperty(name,value);
            }
        }
        return true;
    }

    public boolean supprimerProperty(String name, boolean interne){
        if(!interne && (name.toUpperCase().equals("ID") || name.toUpperCase().equals("DEPENDENCE"))){
            return false;
        }
        OrgProperties properties = tache.getProperties();
        properties.remove(name);
        tache.setProperties(properties);
        return true;
    }

    private void ajoutLogBook(String log){
        Date date = new Date();
        log += new SimpleDateFormat("<yyyy-MM-dd HH:mm>").format(date);
        tache.addLog(log);
    }

    private void ajoutLogBookString(String log){
        tache.addLog(log);
    }

    public boolean ecritureFichier(String path, boolean append){
        OrgParserWriter ecriture = new OrgParserWriter();
        String ecrire = ecriture.whiteSpacedHead(tache,tache.getLevel(),true);
        try {
            FileWriter ffw = new FileWriter(path,append);
            ffw.write(ecrire);
            ffw.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static boolean setDependanceListe(List<Tache> list, int numTache, int numTacheDep){
        if(numTache < 0 || list.size() - 1 < numTache || numTacheDep < 0 || list.size() - 1 < numTacheDep || numTache == numTacheDep){
            return false;
        }
        if(list.size() > numTache + 1){
            if (list.get(numTache + 1).getLevel() > list.get(numTache).getLevel()){
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

    public static List<Tache> lectureFichier(String path){
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
                                tache.setState(State.TODO);
                            }else if(temp[1].equals(State.DONE.toString())){
                                tache.setState(State.DONE);
                            }else if(temp[1].equals(State.ONGOING.toString())){
                                tache.setState(State.ONGOING);
                            }else if(temp[1].equals(State.CANCELLED.toString())){
                                tache.setState(State.CANCELLED);
                            }
                            tache.changeLevel(temp[0].trim().length());

                            temp = temp[2].split(":");
                            tache.changeTitle(temp[0].trim());
                            for (int i = 1; i < temp.length; i++){
                                tache.ajoutTag(temp[i]);
                            }
                        }else if(line.trim().startsWith(CLOCK) && tache != null){
                            temp = line.split(":");
                            long heure = Long.parseLong(temp[1].trim());
                            long minute = Long.parseLong(temp[2].trim());
                            long seconde = Long.parseLong(temp[3].trim());
                            tache.setClock((heure * 3600000) + (minute * 60000) + (seconde * 1000 ));
                        }else if((line.trim().startsWith(CLOSED) || line.trim().startsWith(DEADLINE) || line.trim().startsWith(SCHEDULED)) && tache != null){
                            temp = line.split(">");
                            for (String ele : temp){
                                String[] s = ele.split("<");
                                switch (s[0].trim()){
                                    case CLOSED:{
                                        tache.ajoutClosed(s[1].trim());
                                        break;
                                    }
                                    case DEADLINE:{
                                        tache.ajoutDeadline(s[1].trim());
                                    }
                                    case SCHEDULED:{
                                        tache.ajoutScheduled(s[1].trim());
                                    }
                                }
                            }
                        }else if(line.trim().startsWith(PROPERTIES) && tache != null){
                            line = br.readLine().trim();
                            while(!line.equals(END)){
                                temp = line.split(":", 3);
                                if(temp[1].equals(ID)){
                                    tache.setId(temp[2].trim());
                                }else{
                                    tache.ajoutProperty(temp[1].trim(), temp[2].trim(), false);
                                }
                                line = br.readLine().trim();
                            }
                        }else if(line.trim().startsWith(LOGBOOK) && tache != null){
                            line = br.readLine().trim();
                            while(!line.equals(END)){
                                tache.ajoutLogBookString(line);
                                line = br.readLine().trim();
                            }
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

    public static boolean supprimerTache (List<Tache> taches, int numTache) {
        if(numTache < 0 || numTache >= taches.size() || taches == null){
            return false;
        }
        List<Tache> tacheSup = new ArrayList<Tache>();
        int level = taches.get(numTache).getLevel();
        tacheSup.add(taches.get(numTache));
        int i;
        for(i=numTache+1; i < taches.size(); i++){
            if(taches.get(i).getLevel() <= level){
                break;
            }else{
                tacheSup.add(taches.get(i));
            }
        }
        for(Tache tSup : tacheSup){
            taches.remove(tSup);
        }
        return true;
    }
    
    public static boolean deleteDependanceListe(List<Tache> list, int numTache){
        if(numTache < 0 || list.size() - 1 < numTache){
            return false;
        }
        if(list.size() > numTache + 1){
            if (list.get(numTache + 1).getLevel() > list.get(numTache).getLevel()){
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
