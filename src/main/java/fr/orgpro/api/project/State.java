package fr.orgpro.api.project;

import java.util.ArrayList;
import java.util.Arrays;

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
