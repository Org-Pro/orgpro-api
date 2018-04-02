package fr.orgpro.api.project;


/**
 * Created by sartr on 16/02/2018.
 */
public enum State {
    TODO,
    ONGOING,
    DONE,
    CANCELLED;

    public static State stringIsState(String val){
        try {
            return State.valueOf(val.toUpperCase());
        }catch (Exception ex){
            return null;
        }
    }
}
