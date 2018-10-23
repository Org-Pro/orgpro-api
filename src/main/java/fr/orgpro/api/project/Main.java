package fr.orgpro.api.project;

//import fr.orgpro.api.scrum.Scrum;

//import java.util.ArrayList;
//import java.util.List;

//import fr.orgpro.api.collaborateur.CollaborateurFactory;
//import fr.orgpro.api.collaborateur.CollaborateurFichier;
//import fr.orgpro.api.collaborateur.CollaborateurInterface;
//import fr.orgpro.api.collaborateur.CollaborateurType;

//import fr.orgpro.api.local.SQLiteConnection;
//import fr.orgpro.api.local.SQLiteDataBase;

//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;

public class Main {
    public static void main(String[] args){
/*

        /*System.out.println("Org-Pro");
        Tache tache = new Tache("Faire les courses : 0");
        Tache.addEnTete("test", "ok", false);

        Tache.readFichier("liste.org");
        tache.writeFichier("liste.org", true);
        SQLiteDataBase.addTache(tache);
        //SQLiteDataBase.addCollaborateur("bob", null, null, null);
        //SQLiteDataBase.deleteCollaborateur("test");
        SQLiteDataBase.updateCollaborateur("bob", "alex");
        //SQLiteDataBase.synchroAddTacheCollaborateur(tache, "alex", null, null);
        SQLiteConnection.closeConnection();
        tache.writeFichier("liste.org", false);*/



        /*Tache t = new Tache("Test : 1");
        tache.changeState(State.ONGOING);
        tache.changeState(State.DONE);
        String tag = "COURSE";
        tache.ajoutTag(tag);
        tag = "URGENT";
        tache.ajoutTag(tag);
        tache.ajoutDeadline("2018-03-03");
        tache.ajoutScheduled("2018-01-31");
        tache.ajoutClosed("2018-02-02");
        tache.ajoutProperty("Liste Principal", "riz, : dinde, : huile", false);
        tache.ajoutProperty("Liste Secondaire", "coca,gateaux", false);
       // System.out.println(tache);
        //System.out.println(t);
        tache.minuteur();
        tache.minuteur();
        Tache t1 = new Tache("2");
        t1.setDependance(tache);
        tache.ecritureFichier("liste.org",false);
        t.ecritureFichier("liste.org",true);
        t1.ecritureFichier("liste.org",true);
        List<Tache> list;
        list = Tache.lectureFichier("liste.org");
        System.out.println(list.size());
        for (Tache ele : list){
            System.out.println(ele.toString());
        }*/
        /*Tache t1 = new Tache("t1");
        Tache t2 = new Tache("t2");
        Tache t3 = new Tache("t3");
        Tache t4 = new Tache("t4");
        t2.setDependance(t1);
        t3.setDependance(t1);
        t4.setDependance(t3);
        List<Tache> taches = new ArrayList<Tache>();
        taches.add(t1);
        taches.add(t2);
        taches.add(t3);
        taches.add(t4);
        Tache.supprimerTache(taches,1);
        for (Tache ele : taches){
            System.out.println(ele.toString());
        }**/

        /*List<Tache> list = Tache.lectureFichier("test.org");
        //System.out.println(Tache.ajoutCollaborateurHeader("bob"));
        //System.out.println(Tache.supprimerCollaborateurHeader(list,"aze"));
        System.out.println(list.get(1).supprimerCollaborateur("aaa"));
        boolean premier = true;
        for (Tache t : list){
            if(premier){
                premier = false;
                t.ecritureFichier("test.org", false);
            }else {
                t.ecritureFichier("test.org", true);
            }
        }
        CollaborateurFactory fac = new CollaborateurFactory();
        CollaborateurInterface collab = fac.getCollaborateur(CollaborateurType.CLASSIC);
        if(Collaborateur.class.isInstance(collab)){
            System.out.println("OK");
        }else{
            System.out.println("KO");
        }
        collab.setNom("Jean");
        collab.setNom("Thibault");
        System.out.println(collab.getNom());
        String path = "test.txt";
        CollaborateurFactory fac = new CollaborateurFactory();
        CollaborateurInterface collab = fac.getCollaborateur(CollaborateurType.CLASSIC);
        collab.setNom("Thibault");
        CollaborateurInterface collab2 = fac.getCollaborateur(CollaborateurType.CLASSIC);
        collab2.setNom("Alex");
        List<CollaborateurInterface> lstC = new ArrayList<CollaborateurInterface>();
        lstC.add(collab);
        lstC.add(collab2);
        CollaborateurFichier.ecritureFichier(lstC,path,false);
        List<CollaborateurInterface> liste = new ArrayList<CollaborateurInterface>();
        liste = CollaborateurFichier.lectureFichier(path);
        for (CollaborateurInterface c : liste) {
            System.out.println(c.toString() + "\n");
        }
       Tache tache = new Tache("tache");
        String date = "2018-10-23";
       // tache.setDateSprint(date);
       // System.out.println(tache.getDateSprint());
        //

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateSprint = dateFormat.parse(date);
            String dateValide = dateFormat.format(dateSprint);
            if(dateValide.compareTo(date) != 0){
                System.out.println("WRONG");
            }else{
                tache.addEnTete(tache.HEADER_SPRINT_DATE,date,true);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        tache.writeFichier("test.org",false);
        Date test = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(dateFormat.format(test));*/
    }


}