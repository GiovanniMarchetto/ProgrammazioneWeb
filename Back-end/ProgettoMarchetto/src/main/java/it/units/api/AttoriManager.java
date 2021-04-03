package it.units.api;

import it.units.assistants.FilterAssistant;
import it.units.assistants.JWTAssistant;
import it.units.assistants.PasswordAssistant;
import it.units.entities.proxies.AttoreInfo;
import it.units.entities.storage.Attore;
import it.units.entities.storage.Files;
import it.units.persistance.AttoreHelper;
import it.units.persistance.FilesHelper;
import it.units.utils.FixedVariables;
import it.units.utils.MyException;
import it.units.utils.UtilsMiscellaneous;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Path("/attori")
public class AttoriManager {

    @Context
    public HttpServletRequest request;
    @Context
    public HttpServletResponse response;

    /**
     * Web Service che espone la possibilità di registrare un attore nel sistema.
     * Da specifiche:
     * - solo gli amministratori possono registrare altri amministratori e uploader.
     * <p>
     * Siccome i consumer devono poter registrarsi al sistema,
     * non c'è un filtro esterno ma nel caso non si possa si torna una risposta negativa.
     * Di conseguenza chiunque può registrare un consumer nel sistema.
     *
     * @param attore l'attore da registrare nel sistema, se non è un uploader il logo viene ignorato.
     * @return ritorna una Response di conferma o BAD_REQUEST se si rileva un errore,
     * in ogni caso la risposta viene accompagnata da una stringa con dei dettagli.
     */
    @POST
    @Path("/registration")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registration(Attore attore) {
        try {
            if (FixedVariables.debug)
                UtilsMiscellaneous.stampaDatiPassati(attore, "registration");

            if (!attore.getRole().equals(FixedVariables.CONSUMER)) {
                if (!FilterAssistant.filtroPerRuolo(request, FixedVariables.ADMINISTRATOR, true))
                    throw new MyException("Solo gli administrator possono registrare questo tipo di utenti");
            }

            if (AttoreHelper.getById(Attore.class, attore.getUsername()) != null)
                throw new MyException("Username già esistente");

            if (UtilsMiscellaneous.isSyntaxUsernameWrong(attore.getUsername(), attore.getRole()))
                throw new MyException("Username non conforme alle regole.");

            if (!attore.getRole().equals(FixedVariables.UPLOADER))
                attore.setLogo("");

            AttoreHelper.saveDelayed(attore, true);

            if (FixedVariables.debug)
                System.out.println("REGISTRAZIONE EFFETTUATA --> " + attore.getUsername());

            return Response
                    .status(Response.Status.OK)
                    .entity("Registrazione eseguita - " + attore.getUsername())
                    .build();
        } catch (MyException e) {
            if (FixedVariables.debug) System.out.println(e.getMessage() + "\n");
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("ERR - " + e.getMessage())
                    .build();
        }
    }

    /**
     * Web Service che espone la possibilità di modificare un attore del sistema.
     * Da specifiche:
     * - gli username non si possono mai modificare;
     * - i consumer possono modificare solo il loro nome e la loro email;
     * - gli uploader possono modificare le informazioni (compresa la password) proprie e dei consumer;
     * - gli amministratori possono modificare le informazioni (compresa la password) proprie, degli amministratori e degli uploader.
     *
     * @param attoreModificato l'attore con le informazioni da modificare.
     *                         Se l'username è vuoto allora le modifiche verranno fatte all'account a cui appartiene il token.
     * @return ritorna una Response di conferma con le informazioni dell'attore appena modificato.
     * Se non vengono modificate informazioni viene tornata una Response NO_CONTENT con una stringa di warning.
     * Se incorrono errori viene tornata una Response BAD_REQUEST accompagnata da una stringa con dei dettagli.
     */
    @POST
    @Path("/modInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modificaInformazioni(Attore attoreModificato) {
        try {
            if (FixedVariables.debug)
                UtilsMiscellaneous.stampaDatiPassati(attoreModificato, "modifica attore");

            String token = JWTAssistant.getTokenJWTFromRequest(request);
            String ruoloAttoreModifica = attoreModificato.getRole();
            String usernameAttoreModifica;
            if (attoreModificato.getUsername().equals("")) {
                usernameAttoreModifica = JWTAssistant.getUsernameFromJWT(token);
            } else {
                usernameAttoreModifica = attoreModificato.getUsername();
                UtilsMiscellaneous.controlloPrivilegi(token, ruoloAttoreModifica);
            }

            Attore attoreDatabase = AttoreHelper.getById(Attore.class, usernameAttoreModifica);
            if (attoreDatabase == null || !ruoloAttoreModifica.equals(attoreDatabase.getRole()))
                throw new MyException("Username da modificare inesistente");

            AtomicBoolean modifiche = new AtomicBoolean(false);
            AtomicBoolean modifichePassword = new AtomicBoolean(false);

            if (!attoreModificato.getName().equals("")
                    && !attoreDatabase.getName().equals(attoreModificato.getName())) {
                attoreDatabase.setName(attoreModificato.getName());
                modifiche.set(true);
            }
            if (!attoreModificato.getEmail().equals("")
                    && !attoreDatabase.getEmail().equals(attoreModificato.getEmail())) {
                attoreDatabase.setEmail(attoreModificato.getEmail());
                modifiche.set(true);
            }
            if (ruoloAttoreModifica.equals(FixedVariables.UPLOADER)
                    && !attoreModificato.getLogo().equals("")
                    && !attoreDatabase.getLogo().equals(attoreModificato.getLogo())) {
                attoreDatabase.setLogo(attoreModificato.getLogo());
                modifiche.set(true);
            }
            if (!attoreModificato.getPassword().equals("")
                    && PasswordAssistant.isPasswordWrong(attoreModificato.getPassword(), attoreDatabase.getPassword(), attoreDatabase.getSalt())) {
                attoreDatabase.setPassword(attoreModificato.getPassword());
                modifiche.set(true);
                modifichePassword.set(true);
            }

            if (!modifiche.get()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("WARN Nessun dato da modificare immesso")
                        .build();
            }
            AttoreHelper.saveNow(attoreDatabase, modifichePassword.get());
            return Response
                    .status(Response.Status.OK)
                    .entity(new AttoreInfo(attoreDatabase))
                    .build();

        } catch (Exception e) {
            if (FixedVariables.debug) System.out.println(e.getMessage() + "\n");
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("ERR - " + e.getMessage())
                    .build();
        }
    }

    /**
     * Web Service che espone la possibilità di eliminare un attore del sistema.
     * Da specifiche:
     * - nessun attore può eliminare sè stesso;
     * - i consumer non possono eliminare attori;
     * - gli uploader possono eliminare soltanto i consumer;
     * - gli amministratori possono eliminare soltanto altri amministratori o gli uploader.
     * Quando si va ad eliminare un uploader si vanno ad eliminare completamente dal database
     * anche i suoi files. Se invece eliminiamo un consumer viene eliminato soltanto il contenuto
     * del files, rimangono però le informazioni in quanto servono per i resoconti
     * richiesti dagli amministratori.
     *
     * @param username l'username dell'utente da eliminare
     * @return ritorna una Response di conferma o BAD_REQUEST se si rileva un errore,
     * in ogni caso la risposta viene accompagnata da una stringa con dei dettagli.
     */
    @DELETE
    @Path("/delete/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteActor(@PathParam("username") String username) {
        try {
            Attore attoreDaEliminare = AttoreHelper.getById(Attore.class, username);

            if (attoreDaEliminare == null)
                throw new MyException("Username inesistente.");

            String token = JWTAssistant.getTokenJWTFromRequest(request);
            if (JWTAssistant.getUsernameFromJWT(token).equals(attoreDaEliminare.getUsername()))
                throw new MyException("Non puoi eliminare te stesso.");

            AttoreHelper.deleteEntity(attoreDaEliminare);

            switch (attoreDaEliminare.getRole()) {
                case FixedVariables.UPLOADER:
                    List<Files> listaFilesUploaderEliminato = FilesHelper.listaFilesCompletaUploader(attoreDaEliminare.getUsername());
                    listaFilesUploaderEliminato.forEach(FilesHelper::deleteEntity);
                    break;
                case FixedVariables.CONSUMER:
                    List<Files> listaFilesConsumerEliminato = FilesHelper.listaFilesConsumer(attoreDaEliminare.getUsername());
                    for (Files fileDaEliminare : listaFilesConsumerEliminato) {
                        fileDaEliminare.setFile(null);
                        FilesHelper.saveNow(fileDaEliminare);
                    }
                    break;
                default:
                    break;
            }

            return Response
                    .status(Response.Status.OK)
                    .entity("Delete actor " + username + " completed")
                    .build();
        } catch (Exception e) {
            if (FixedVariables.debug) System.out.println(e.getMessage() + "\n");
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("ERR - " + e.getMessage())
                    .build();
        }
    }

}
