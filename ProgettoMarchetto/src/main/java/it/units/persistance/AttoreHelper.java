package it.units.persistance;

import it.units.assistants.PasswordAssistant;
import it.units.entities.storage.Attore;
import it.units.utils.MyException;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class AttoreHelper extends AbstractHelper {

    public static void saveDelayed(Attore attore, boolean nuovaPassword) throws MyException {
        if (nuovaPassword)
            setSaleAndPassword(attore);
        ofy().save().entity(attore);
    }

    public static void saveNow(Attore attore, boolean nuovaPassword) throws MyException {
        if (nuovaPassword)
            setSaleAndPassword(attore);
        ofy().save().entity(attore).now();
    }

    private static void setSaleAndPassword(Attore attore) throws MyException {
        String sale = PasswordAssistant.generateSalt();
        attore.setSalt(sale);
        String passwordChiara = attore.getPassword();
        String passwordHash = PasswordAssistant.hashPassword(passwordChiara, sale);
        if (passwordHash == null)
            throw new MyException("Non sono riuscito a fare l'hash della password");
        attore.setPassword(passwordHash);
    }

    public static List<Attore> ListaAttoriRuolo(String ruolo) {
        return ofy().load().type(Attore.class).filter("role", ruolo).list();
    }

}
