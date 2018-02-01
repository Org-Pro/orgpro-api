package fr.ShareProject.api.project;

import fr.ShareProject.api.orgzly.*;
import fr.ShareProject.api.orgzly.datetime.OrgDateTime;
import fr.ShareProject.api.orgzly.datetime.OrgRange;
import fr.ShareProject.api.orgzly.parser.OrgParserWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws ParseException {
        System.out.println("Share Project");
        Tache tache = new Tache("Faire les courses",1);
        String[] tags = new String[2];
        tags[0] = "COURSE";
        tags[1] = "URGENT";
        tache.ajoutTag(tags);
        tache.ajoutDeadline("2018-03-03");
        tache.ajoutScheduled("2018-01-31");
        tache.ajoutClosed("2018-02-02");
        OrgProperties properties = new OrgProperties();
        properties.put("LISTE_PRINCIPAL","riz,dinde,huile");
        properties.put("LISTE_SECONDAIRE","coca,gateaux");
        tache.ajoutProperties(properties);
        /*String log = "- State 'DOING' FROM 'TODO' ";
        tache.ajoutLogBook(log,"2018-02-02");
        log = "- State 'DONE' FROM 'DOING' ";
        tache.ajoutLogBook(log,"2018-02-02");*/
        System.out.println(tache);
        tache.ecritureFichier("C://Users/sartr/OneDrive/Documents/Université/ProjetM1/liste.org",false);
    }
}
