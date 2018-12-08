package fr.orgpro.api.remote.google;

public enum GoogleStateEnum {
    NEEDSACTION("needsAction"),
    COMPLETED("completed");
    private String gse;

    GoogleStateEnum(String gse) {
        this.gse = gse;
    }

    @Override
    public String toString() {
        return this.gse;
    }

    public static GoogleStateEnum stringIsGoogleStateEnum(String val){
        try {
            return GoogleStateEnum.valueOf(val.toUpperCase());
        }catch (Exception ex){
            return null;
        }
    }
}


