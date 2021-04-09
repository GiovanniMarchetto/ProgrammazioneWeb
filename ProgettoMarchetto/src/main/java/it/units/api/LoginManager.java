package it.units.api;

import it.units.assistants.JWTAssistant;
import it.units.assistants.PasswordAssistant;
import it.units.entities.storage.Attore;
import it.units.persistance.AttoreHelper;
import it.units.utils.FixedVariables;
import it.units.utils.MyException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginManager {

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    /**
     * Web Service che espone la possibilità di fare il login.
     * <p>
     * Siccome non vogliamo che nell'indirizzo di richiesta siano presenti username e password
     * usiamo una POST e non una GET, anche se questo va contro le specifiche della suddetta operazione.
     *
     * @param attoreLogin l'attore che richiede di fare login nel sistema.
     *                    In realtà gli unici campi che saranno coperti sono username e password,
     *                    ma per economia di entità abbiamo utilizzato Attore.
     * @return ritorna una Response di conferma con in allegato la stringa con il token jwt.
     * Altrimenti risponde con NOT_FOUND e con la descrizione dell'errore riscontrato.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(Attore attoreLogin) {
        try {
            Attore accountChiesto = AttoreHelper.getById(Attore.class, attoreLogin.getUsername());

            if (accountChiesto == null)
                throw new MyException("Username errato! (inesistente)");

            if (PasswordAssistant.isPasswordWrong(attoreLogin.getPassword(), accountChiesto.getPassword(), accountChiesto.getSalt()))
                throw new MyException("Password errata!");

            if (FixedVariables.debug)
                System.out.println("Login eseguito con successo: " + attoreLogin.getUsername() + "\n");

            return Response
                    .status(Response.Status.OK)
                    .entity(JWTAssistant.creaJWT(attoreLogin.getUsername(), accountChiesto.getRole()))
                    .build();
        } catch (MyException e) {
            if (FixedVariables.debug) System.out.println(e.getMessage() + "\n");
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("ERR - " + e.getMessage())
                    .build();
        }
    }
}
