package fr.orgpro.api.local.models;

public class SQLSynchro {
    private String uuid_tache;
    private String pseudo_collaborateur;
    private String google_id_tache;
    private boolean est_synchro;

    public SQLSynchro(String uuid_tache, String pseudo_collaborateur, String google_id_tache, boolean est_synchro) {
        this.uuid_tache = uuid_tache;
        this.pseudo_collaborateur = pseudo_collaborateur;
        this.google_id_tache = google_id_tache;
        this.est_synchro = est_synchro;
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

    public boolean isEst_synchro() {
        return est_synchro;
    }
}
