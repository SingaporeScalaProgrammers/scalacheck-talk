package models;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class PokeUtils {

    public static double catchRate(List<CatchEntry> catches) {

        Comparator<CatchEntry> comp = (c1, c2) -> {
            if(c1.getCatchTime().before(c2.getCatchTime())) return -1;
            if(c1.getCatchTime().after(c2.getCatchTime())) return 1;
            else return 0;
        };
        if(catches.isEmpty()){ // fix for bug #1
            return 0.0;
        }
        Date firstCatch = Collections.min(catches,comp).getCatchTime(); //bug #1 no such element exception on empty list
        Date lastCatch = Collections.max(catches,comp).getCatchTime();
        //bug #2 division by zero error when passed a single pokemon
        if(firstCatch.getTime() - lastCatch.getTime() == 0) return 0.0; //fix for bug #2
        else return (catches.size() / (lastCatch.getTime() - firstCatch.getTime())) / 1000;
    }
}
