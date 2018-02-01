package fr.ShareProject.api.project;

import fr.ShareProject.api.orgzly.OrgHead;
import fr.ShareProject.api.orgzly.OrgProperties;
import fr.ShareProject.api.orgzly.parser.OrgParserWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sartr on 01/02/2018.
 */
public class Tache {
    private OrgHead tache;

    private OrgParserWriter ecriture;
    private SimpleDateFormat dateFormat;

    public Tache(String title,int level) {
        ecriture = new OrgParserWriter();
        this.tache = new OrgHead(title);
        this.tache.setLevel(level);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public void ajoutTag(String[] tag){
        tache.setTags(tag);
    }

    public boolean ajoutDeadline(String deadline){
        try {
            Date deadlineDate = dateFormat.parse(deadline);
            tache.setDeadline(deadlineDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean ajoutScheduled(String scheduled){
        try {
            Date scheduledDate = dateFormat.parse(scheduled);
            tache.setScheduled(scheduledDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean ajoutClosed(String closed){
        try {
            Date closedDate = dateFormat.parse(closed);
            tache.setClosed(closedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void ajoutProperties(OrgProperties properties){
        tache.setProperties(properties);
    }

    /*public void ajoutLogBook(String log,String logDate){
        try {
            log += new SimpleDateFormat("<yyyy-MM-dd>").format(dateFormat.parse(logDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tache.addLog(log);
    }*/

    public void ecritureFichier(String path, boolean append){
        OrgParserWriter ecriture = new OrgParserWriter();
        String ecrire = ecriture.whiteSpacedHead(tache,tache.getLevel(),true);
        try {
            FileWriter ffw=new FileWriter(path,append);
            ffw.write(ecrire);  // écrire une ligne dans le fichier resultat.txt
            ffw.close(); // fermer le fichier à la fin des traitements
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return ecriture.whiteSpacedHead(tache,tache.getLevel(),true);
    }
}
