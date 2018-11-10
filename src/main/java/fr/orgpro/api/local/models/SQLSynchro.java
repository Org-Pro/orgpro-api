package fr.orgpro.api.local.models;

public class SQLSynchro {
    private String uuid_tache;
    private String pseudo_collaborateur;
    private String google_id_tache;
    private String trello_id_card;
    private boolean est_synchro;

    public SQLSynchro(String uuid_tache, String pseudo_collaborateur) {
        this.uuid_tache = uuid_tache;
        this.pseudo_collaborateur = pseudo_collaborateur;
        this.est_synchro = false;
    }

    public String getUuid_tache() {
        return uuid_tache;
    }

    public String getPseudo_collaborateur() {
        return pseudo_collaborateur;
    }

    public String getGoogle_id_tache() {
        return google_id_tache;
    }

    public void setGoogle_id_tache(String google_id_tache) {
        this.google_id_tache = google_id_tache;
    }

    public String getTrello_id_card() {
        return trello_id_card;
    }

    public void setTrello_id_card(String trello_id_card) {
        this.trello_id_card = trello_id_card;
    }

    public boolean isEst_synchro() {
        return est_synchro;
    }

    public void setEst_synchro(boolean est_synchro) {
        this.est_synchro = est_synchro;
    }
}
