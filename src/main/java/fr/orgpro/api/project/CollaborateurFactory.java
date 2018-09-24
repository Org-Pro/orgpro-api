package fr.orgpro.api.project;

public class CollaborateurFactory {

    public CollaborateurInterface getCollaborateur(CollaborateurType type){
        if(type == null){
            return null;
        }
        if(CollaborateurType.CLASSIC.toString().equals(type.toString())){
            return new Collaborateur();
        }else {
            return null;
        }
    }
}
