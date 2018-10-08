package fr.orgpro.api.collaborateur;

public class Collaborateur implements CollaborateurInterface {

    private String nom;

    public Collaborateur(String nom) {
        this.nom = nom;
    }

    public Collaborateur() {
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return CollaborateurType.CLASSIC.toString() + ';' + nom;
    }
}
