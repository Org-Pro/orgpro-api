package fr.trellorg.api.project;


public class Main {

    public static void main(String[] args){
        System.out.println("Trellorg");
        Tache tache = new Tache("Faire les courses",1);
        tache.changeState("DONE");
        String tag = "COURSE";
        tache.ajoutTag(tag);
        tag = "URGENT";
        tache.ajoutTag(tag);
        tache.ajoutDeadline("2018-03-03");
        tache.ajoutScheduled("2018-01-31");
        tache.ajoutClosed("2018-02-02");
        tache.ajoutProperty("Liste Principal","riz,dinde,huile");
        tache.ajoutProperty("Liste Secondaire","coca,gateaux");
        System.out.println(tache);
        tache.ecritureFichier("liste.org",false);
    }
}
