package fr.ShareProject.api.project;

import fr.ShareProject.api.orgzly.*;
import fr.ShareProject.api.orgzly.datetime.OrgDateTime;
import fr.ShareProject.api.orgzly.datetime.OrgRange;
import fr.ShareProject.api.orgzly.parser.OrgParserWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Share Project");
        OrgHead task = new OrgHead("Aller faire les courses");
        OrgDateTime time = new OrgDateTime(true);
        time = time.doParse("<2017-02-02>");
        OrgRange deadline = new OrgRange(time);
        task.setDeadline(deadline);
        time = time.doParse("<2017-01-31>");
        OrgRange scheduled = new OrgRange(time);
        task.setScheduled(scheduled);
        time = time.doParse("<2017-02-01>");
        OrgRange closed = new OrgRange(time);
        task.setClosed(closed);
        task.setState("DONE");
        String[] tags = new String[2];
        tags[0] = "COURSE";
        tags[1] = "URGENT";
        task.setTags(tags);
        OrgProperties properties = new OrgProperties();
        properties.put("LISTE_PRINCIPAL","riz,dinde,huile");
        properties.put("LISTE_SECONDAIRE","coca,gateaux");
        task.setProperties(properties);
        String log = "- State 'DONE' FROM 'TODO' " + closed;
        task.addLog(log);
        OrgParserWriter ecriture = new OrgParserWriter();
        String ecrire = ecriture.whiteSpacedHead(task,1,true);
        System.out.println(ecrire);

        try {
            File ff=new File("C://Users/sartr/OneDrive/Documents/Université/ProjetM1/liste.org");
            ff.createNewFile();
            FileWriter ffw=new FileWriter(ff);
            ffw.write(ecrire);  // écrire une ligne dans le fichier resultat.txt
            ffw.close(); // fermer le fichier à la fin des traitements
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
