package it.units.api;

import it.units.assistants.JWTAssistant;
import it.units.assistants.ListToProxiesAssistant;
import it.units.entities.proxies.AttoreInfo;
import it.units.entities.proxies.FilesInfo;
import it.units.entities.storage.Attore;
import it.units.entities.storage.Files;
import it.units.entities.support.FromTo;
import it.units.entities.support.ResumeForAdmin;
import it.units.persistance.AttoreHelper;
import it.units.persistance.FilesHelper;
import it.units.utils.FixedVariables;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/list")
public class ListManager {

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    /*
     * https://stackoverflow.com/questions/47327162/messagebodywriter-not-found-for-media-type-application-xml
     * Per risolvere il problema delle liste nelle Response da trasformare in json si è utilizzato il GenericEntity.
     */


    /**
     * Web Service che espone la possibilità ad un consumer autenticato di reperire
     * la lista degli uploader (con le loro informazioni) che gli hanno inviato qualche file.
     *
     * @return una Response contenente una lista di uploader con le loro informazioni.
     * Nel caso si presentassero errori sarebbe ritornata un BAD_REQUEST.
     */
    @GET
    @Path("/uploaders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUploadersWithDocumentsForTheConsumer() {
        try {
            List<Attore> uploadersList = new ArrayList<>();
            String consumer = JWTAssistant.getUsernameFromHttpServletRequest(request);
            List<Files> filesConsumer = FilesHelper.listaFilesConsumer(consumer);
            for (Files file : filesConsumer) {
                Attore a = AttoreHelper.getById(Attore.class, file.getUsernameUpl());
                if (!uploadersList.contains(a))
                    uploadersList.add(a);
            }
            return Response
                    .status(Response.Status.OK)
                    .entity(new GenericEntity<List<AttoreInfo>>(ListToProxiesAssistant.getAttoreInfoList(uploadersList)) {
                    })
                    .build();
        } catch (Exception e) {
            if (FixedVariables.debug) System.out.println(e.getMessage() + "\n");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * Web Service che espone la possibilità ad un consumer autenticato di reperire
     * la lista delle informazioni dei file che gli sono stati inviati.
     *
     * @return una Response contenente la lista delle informazioni dei file che sono
     * stati inviati al consumer autenticato.
     * Nel caso si presentassero errori sarebbe ritornata un BAD_REQUEST.
     */
    @GET
    @Path("/filesConsumer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilesConsumer() {
        try {
            String consumer = JWTAssistant.getUsernameFromHttpServletRequest(request);
            return Response
                    .status(Response.Status.OK)
                    .entity(new GenericEntity<List<FilesInfo>>(ListToProxiesAssistant.listaInfoFilesConsumer(consumer)) {
                    })
                    .build();
        } catch (Exception e) {
            if (FixedVariables.debug) System.out.println(e.getMessage() + "\n");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * Web Service che espone la possibilità ad un uploader autenticato di reperire
     * la lista di tutti i consumer.
     *
     * @return una Response contenente una lista di consumer con le loro informazioni.
     * Nel caso si presentassero errori sarebbe ritornata un BAD_REQUEST.
     */
    @GET
    @Path("/consumers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConsumers() {
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(new GenericEntity<List<AttoreInfo>>(ListToProxiesAssistant.ListaInfoAttoriRuolo(FixedVariables.CONSUMER)) {
                    })
                    .build();
        } catch (Exception e) {
            if (FixedVariables.debug) System.out.println(e.getMessage() + "\n");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * Web Service che espone la possibilità ad un uploader autenticato di reperire
     * la lista delle informazioni dei file che ha caricato (e che non ha eliminato).
     *
     * @return una Response contenente la lista delle informazioni dei file che sono
     * stati caricati dall'uploader autenticato.
     * Nel caso si presentassero errori sarebbe ritornata un BAD_REQUEST.
     */
    @GET
    @Path("/filesUploader")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilesUploader() {
        try {
            String uploader = JWTAssistant.getUsernameFromHttpServletRequest(request);
            return Response
                    .status(Response.Status.OK)
                    .entity(new GenericEntity<List<FilesInfo>>(ListToProxiesAssistant.listaInfoFilesUploader(uploader)) {
                    })
                    .build();
        } catch (Exception e) {
            if (FixedVariables.debug) System.out.println(e.getMessage() + "\n");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * Web Service che espone la possibilità ad un administrator autenticato di reperire
     * un resoconto in cui per ogni uploader vengono allegate le sue informazioni,
     * il numero di file caricati e il numero di consumer destinatari distinti in un certo
     * lasso di tempo indicato nei parametri di ingresso.
     *
     * @param date contiene la data di inizio e la data di fine del resoconto.
     * @return una Response con il resoconto per il periodo selezionato.
     * Nel caso si presentassero errori sarebbe ritornata un BAD_REQUEST.
     */
    @POST
    @Path("/resumeForAdmin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilesAll(FromTo date) {
        try {
            Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(date.getFrom());
            Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(date.getTo());

            List<AttoreInfo> uploaders = ListToProxiesAssistant.ListaInfoAttoriRuolo(FixedVariables.UPLOADER);
            List<FilesInfo> allFiles = ListToProxiesAssistant.listaInfoFilesCompleta();

            List<ResumeForAdmin> resoconto = new ArrayList<>();
            if (FixedVariables.debug)
                System.out.println("-----------RESOCONTO--------------");

            for (AttoreInfo upl : uploaders) {
                ArrayList<String> consDiversi = new ArrayList<>();

                if (FixedVariables.debug)
                    System.out.println("USERNAME " + upl.getUsername());

                Iterator<FilesInfo> i = allFiles.iterator();
                AtomicInteger numDocCaricati = new AtomicInteger();
                while (i.hasNext()) {
                    FilesInfo file = i.next();
                    if (upl.getUsername().equals(file.getUsernameUpl())) {
                        Date docDate = new SimpleDateFormat("yyyy-MM-dd").parse(file.getDataCaricamento().substring(0, 10));

                        if ((docDate.after(fromDate) || docDate.equals(fromDate)) && docDate.before(toDate)) {
                            numDocCaricati.getAndIncrement();
                            if (!consDiversi.contains(file.getUsernameCons())) {
                                consDiversi.add(file.getUsernameCons());
                            }
                        }
                        i.remove();
                    }
                }
                if (FixedVariables.debug) {
                    System.out.println("End for " + upl.getUsername() + " - " + numDocCaricati.get() + " - " + consDiversi.size());
                    System.out.println("File rimasti: " + allFiles.size());
                }
                resoconto.add(new ResumeForAdmin(upl.getUsername(), upl.getName(), upl.getEmail(), numDocCaricati.get(), consDiversi.size()));
            }

            return Response
                    .status(Response.Status.OK)
                    .entity(new GenericEntity<List<ResumeForAdmin>>(resoconto) {
                    })
                    .build();
        } catch (Exception e) {
            if (FixedVariables.debug) System.out.println(e.getMessage() + "\n");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * Web Service che espone la possibilità ad un administrator autenticato di reperire
     * la lista di tutti gli amministratori presenti sulla piattaforma.
     *
     * @return una Response con la lista degli amministratori.
     * Nel caso si presentassero errori sarebbe ritornata un BAD_REQUEST.
     */
    @GET
    @Path("/administrators")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdministrators() {
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(new GenericEntity<List<AttoreInfo>>(ListToProxiesAssistant.ListaInfoAttoriRuolo(FixedVariables.ADMINISTRATOR)) {
                    })
                    .build();
        } catch (Exception e) {
            if (FixedVariables.debug) System.out.println(e.getMessage() + "\n");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}
