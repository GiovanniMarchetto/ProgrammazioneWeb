package it.units.persistance;


import it.units.entities.storage.Files;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class FilesHelper extends AbstractHelper {

    public static List<Files> listaFilesCompleta() {
        return ofy().load().type(Files.class).list();
    }

    public static List<Files> listaFilesConsumer(String usernameCons) {
        List<Files> listaFilesConsumer = ofy().load().type(Files.class).filter("usernameCons", usernameCons).list();
        listaFilesConsumer.removeIf(file -> file.getFile() == null);
        return listaFilesConsumer;
    }

    public static List<Files> listaFilesUploader(String usernameUploader) {
        List<Files> listaFilesUploader = listaFilesCompletaUploader(usernameUploader);
        listaFilesUploader.removeIf(file -> file.getFile() == null);
        return listaFilesUploader;
    }

    public static List<Files> listaFilesCompletaUploader(String usernameUploader) {
        return ofy().load().type(Files.class).filter("usernameUpl", usernameUploader).list();
    }


}
