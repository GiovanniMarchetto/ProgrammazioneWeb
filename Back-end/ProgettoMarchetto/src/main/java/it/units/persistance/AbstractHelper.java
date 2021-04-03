package it.units.persistance;

import com.googlecode.objectify.Key;
import it.units.utils.FixedVariables;

import static com.googlecode.objectify.ObjectifyService.ofy;

public abstract class AbstractHelper {

    public static <T> void saveDelayed(T t) {
        ofy().save().entity(t);
    }

    public static <T> void saveNow(T t) {
        ofy().save().entity(t).now();
    }

    public static <T> void deleteEntity(T t) {
        ofy().delete().entity(t);
    }

    public static <T> T getById(Class<T> c, String id) {
        try {
            Key<T> k = Key.create(c, id);
            return ofy().load().key(k).now();
        } catch (Exception e) {
            if (FixedVariables.debug)
                System.out.println("ERR - Non non ho trovato niente nel db (" + id + ")\n");
            return null;
        }
    }
}