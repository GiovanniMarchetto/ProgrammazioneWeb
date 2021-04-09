package it.units.api;

import it.units.assistants.JWTAssistant;
import it.units.assistants.MailAssistant;
import it.units.assistants.TokenDownloadAssistant;
import it.units.entities.proxies.FilesInfo;
import it.units.entities.storage.Attore;
import it.units.entities.storage.Files;
import it.units.entities.support.SupportFileUpload;
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
import java.util.Objects;
import java.util.UUID;

@Path("/files")
public class FilesManager {

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    /**
     * Web Service che espone la possibilità di fare il download di un file dal sito.
     * Se mancanti, aggiorna la data di visualizzazione e l'indirizzo IP richiedente.
     *
     * @param id l'id del file da scaricare
     * @return ritorna una Response con il file in formato octet_stream (il browser poi si occuperà di come salvarlo).
     * Se non è il consumer corretto ritorna una Response BAD_REQUEST invece se non trova il file ne ritorna una NOT_FOUND.
     */
    @GET
    @Path("/download/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response downloadFileFromSite(@PathParam("id") String id) {
        try {
            String usernameFromJWT = JWTAssistant.getUsernameFromHttpServletRequest(request);
            if (!usernameFromJWT.equals(Objects.requireNonNull(FilesHelper.getById(Files.class, id)).getUsernameCons()))
                throw new MyException("Non è destinato a questo consumer");
            return downloadFile(id);
        } catch (Exception e) {
            if (FixedVariables.debug) System.out.println(e.getMessage() + "\n");
            return Response.status(Response.Status.BAD_REQUEST).entity("ERR - " + e.getMessage()).build();
        }
    }

    /**
     * Web Service che espone la possibilità di fare il download di un file dal sito.
     * Se mancanti aggiorna la data di visualizzazione e l'indirizzo IP richiedente.
     *
     * @param fileId        l'id del file da scaricare
     * @param tokenDownload il token jwt per la verifica
     * @return ritorna una Response con il file in formato octet_stream (il browser poi si occuperà di come salvarlo).
     * Se il token non è valido ritorna una Response BAD_REQUEST, oppure se non trova il file ritorna una Response NOT_FOUND.
     */
    @GET
    @Path("/downloadDirect/{fileId}/{tokenDownload}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response downloadFileFromLink(@PathParam("fileId") String fileId, @PathParam("tokenDownload") String tokenDownload) {
        if (!TokenDownloadAssistant.verificaTokenDownload(tokenDownload, fileId))
            return Response.status(Response.Status.BAD_REQUEST).entity("ERR - Token errato").build();
        return downloadFile(fileId);
    }

    /**
     * Metodo che si occupa della procedura di download.
     * Se mancanti aggiorna la data di visualizzazione e l'indirizzo IP richiedente.
     *
     * @param id l'identificativo del file da scaricare
     * @return una Response con il file oppure NOT_FOUND.
     */
    private Response downloadFile(String id) {
        Files fileDaScaricare = FilesHelper.getById(Files.class, id);

        if (fileDaScaricare == null || fileDaScaricare.getFile() == null)
            return Response.status(Response.Status.NOT_FOUND).entity("ERR - Il file richiesto non è presente nel database").build();

        if (fileDaScaricare.getDataVisualizzazione().equals("")) {
            String dataVisualizzazione = UtilsMiscellaneous.getDataString();
            fileDaScaricare.setDataVisualizzazione(dataVisualizzazione);
            fileDaScaricare.setIndirizzoIP(request.getRemoteAddr());

            FilesHelper.saveNow(fileDaScaricare);
        }

        // Response ispirazione: https://www.java2novice.com/restful-web-services/jax-rs-download-file/
        // da questa ispirazione si è estesa la Response a tutti i web services sfruttando le specifiche e i suggerimenti di IntelliJ
        // Ispirazione utilizzo octet stream: https://stackoverflow.com/questions/27375192/how-can-i-put-a-downloadable-file-into-the-httpservletresponse
        Response.ResponseBuilder responseBuilder = Response.ok(fileDaScaricare.getFile(), MediaType.APPLICATION_OCTET_STREAM_TYPE);
        responseBuilder.header("Content-Disposition",
                "attachment; filename=\"" + fileDaScaricare.getName() + "\"");
        return responseBuilder.build();
    }

    /**
     * Web Service che espone la possibilità (ad un uploader) di caricare un file.
     * <p>
     * Se il consumer a cui è diretto non esiste allora lo crea e
     * gli manda una mail con i dati di accesso (password generata casualmente).
     * Dopo aver caricato il file invia una mail di notifica (organizzata come da specifiche).
     *
     * @param supportFileUpload entità di supporto che prevede in entrata:
     *                          - il file in formato di stringa in base64
     *                          - il nome del file
     *                          - una string per gli hashtag
     *                          - username del consumer a cui è diretto
     *                          - il nome del consumer (utilizzato solo se è un nuovo consumer)
     *                          - email del consumer (utilizzato solo se è un nuovo consumer)
     * @return una Response con un messaggio di conferma se va tutto a buon fine.
     * Altrimenti ritorna una BAD_REQUEST con in allegato una stringa con la spiegazione dell'errore.
     */
    @POST
    @Path("/upload")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response uploadFile(SupportFileUpload supportFileUpload) {
        try {
            String usernameUpl = JWTAssistant.getUsernameFromHttpServletRequest(request);
            supportFileUpload.setUsernameUpl(usernameUpl);
            supportFileUpload.setDataCaricamento(UtilsMiscellaneous.getDataString());

            Attore attore = AttoreHelper.getById(Attore.class, supportFileUpload.getUsernameCons());

            if (attore == null) {

                if (UtilsMiscellaneous.isSyntaxUsernameWrong(supportFileUpload.getUsernameCons(), FixedVariables.CONSUMER))
                    throw new MyException("Username per il consumer non conforme alle regole.");

                if (supportFileUpload.getEmailCons().equals("") || supportFileUpload.getNameCons().equals(""))
                    throw new MyException("Informazioni mancanti per la creazione del nuovo username");

                String passwordProvvisoria = UUID.randomUUID().toString().substring(0, 12);
                Attore nuovoConsumer = new Attore(supportFileUpload.getUsernameCons(), passwordProvvisoria,
                        supportFileUpload.getNameCons(), supportFileUpload.getEmailCons(), FixedVariables.CONSUMER, "");
                AttoreHelper.saveNow(nuovoConsumer, true);

                String mailCreazioneAttore = MailAssistant.sendMailCreazioneAttore(nuovoConsumer, passwordProvvisoria, usernameUpl);

                if (FixedVariables.debug)
                    System.out.println(mailCreazioneAttore);

            } else {
                supportFileUpload.setNameCons(attore.getName());
                supportFileUpload.setEmailCons(attore.getEmail());
            }

            Files nuovoFile = new Files(supportFileUpload.getUsernameUpl(), supportFileUpload.getUsernameCons(), supportFileUpload.getFile(),
                    supportFileUpload.getNameFile(), supportFileUpload.getDataCaricamento(), supportFileUpload.getHashtag());
            FilesHelper.saveDelayed(nuovoFile);

            String mailNotifica = MailAssistant.sendNotifica(supportFileUpload, usernameUpl, nuovoFile.getId());
            if (FixedVariables.debug)
                System.out.println(mailNotifica);


            return Response
                    .status(Response.Status.OK)
                    .entity(new FilesInfo(nuovoFile))
                    .build();

        } catch (Exception e) {
            if (FixedVariables.debug) System.out.println(e.getMessage() + "\n");
            return Response.status(Response.Status.BAD_REQUEST).entity("ERR - " + e.getMessage()).build();
        }
    }


    /**
     * Web Service che espone la possibilità di eliminare un file.
     *
     * @param fileId l'id del file da cancellare.
     * @return una Response con un messaggio di conferma se va tutto a buon fine.
     * Altrimenti se non trova il file ritorna un NOT_FOUND
     * invece se non è l'uploader che l'ha cancellato ritorna una BAD_REQUEST,
     * entrambe avranno un messaggio allegato con una descrizione dell'errore.
     */
    @Path("/delete/{fileId}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteFile(@PathParam("fileId") String fileId) {
        try {
            Files file = FilesHelper.getById(Files.class, fileId);
            if (file == null || file.getFile() == null)
                return Response.status(Response.Status.NOT_FOUND).entity("ERR - File da eliminare inesistente!").build();

            String usernameFromJWT = JWTAssistant.getUsernameFromHttpServletRequest(request);
            if (!file.getUsernameUpl().equals(usernameFromJWT))
                throw new MyException("Lo può cancellare direttamente solo lo stesso uploader che lo ha caricato");

            file.setFile(null);
            FilesHelper.saveDelayed(file);
            return Response
                    .status(Response.Status.OK)
                    .entity("delete file " + fileId + " completed")
                    .build();
        } catch (Exception e) {
            if (FixedVariables.debug) System.out.println(e.getMessage() + "\n");
            return Response.status(Response.Status.BAD_REQUEST).entity("ERR - " + e.getMessage()).build();
        }
    }


}