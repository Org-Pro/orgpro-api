package fr.orgpro.api.remote.google;

public enum GoogleStateEnum {
    NEEDSACTION,
    COMPLETED;

    public static GoogleStateEnum stringIsGoogleStateEnum(String val){
        try {
            return GoogleStateEnum.valueOf(val.toUpperCase());
        }catch (Exception ex){
            return null;
        }
    }
}


