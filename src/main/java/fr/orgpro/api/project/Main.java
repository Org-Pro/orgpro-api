package fr.orgpro.api.project;

//import fr.orgpro.api.scrum.Scrum;

//import java.util.ArrayList;
//import java.util.List;

public class Main {



    public static void main(String[] args){

        /*system.out.println("Org-Pro");
        Tache tache = new Tache("Faire les courses : 0");
        Tache t = new Tache("Test : 1");
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
        System.out.println(collab.getNom());*/
    }


}
