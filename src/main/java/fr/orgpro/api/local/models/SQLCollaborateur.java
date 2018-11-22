package fr.orgpro.api.local.models;

public class SQLCollaborateur {
    private String pseudo;
    private String nom;
    private String prenom;
    private String google_id_liste;
    private String trello_id_board;
    private String trello_id_liste;
    private String trello_key;
    private String trello_token;

    public SQLCollaborateur(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getGoogle_id_liste() {
        return google_id_liste;
    }

    public void setGoogle_id_liste(String google_id_liste) {
        this.google_id_liste = google_id_liste;
    }

    public String getTrello_id_board() {
        return trello_id_board;
    }

    public void setTrello_id_board(String trello_id_board) {
        this.trello_id_board = trello_id_board;
    }

    public String getTrello_id_liste() {
        return trello_id_liste;
    }

    public void setTrello_id_liste(String trello_id_liste) {
        this.trello_id_liste = trello_id_liste;
    }

    public String getTrello_key() {
        return trello_key;
    }

    public void setTrello_key(String trello_key) {
        this.trello_key = trello_key;
    }

    public String getTrello_token() {
        return trello_token;
    }

    public void setTrello_token(String trello_token) {
        this.trello_token = trello_token;
    }
}
