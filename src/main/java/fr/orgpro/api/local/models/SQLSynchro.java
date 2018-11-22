package fr.orgpro.api.local.models;

public class SQLSynchro {
    private String uuid_tache;
    private String pseudo_collaborateur;
    private String google_id_tache;
    private String trello_id_card;
    private boolean google_est_synchro;
    private boolean trello_est_synchro;

    public SQLSynchro(String uuid_tache, String pseudo_collaborateur) {
        this.uuid_tache = uuid_tache;
        this.pseudo_collaborateur = pseudo_collaborateur;
        this.google_est_synchro = false;
        this.trello_est_synchro = false;
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

    public boolean isGoogle_est_synchro() {
        return google_est_synchro;
    }

    public void setGoogle_est_synchro(boolean google_est_synchro) {
        this.google_est_synchro = google_est_synchro;
    }

    public boolean isTrello_est_synchro() {
        return trello_est_synchro;
    }

    public void setTrello_est_synchro(boolean trello_est_synchro) {
        this.trello_est_synchro = trello_est_synchro;
    }
}
