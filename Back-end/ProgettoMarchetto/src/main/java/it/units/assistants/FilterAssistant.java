package it.units.assistants;

import it.units.utils.FixedVariables;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class FilterAssistant {

    public static boolean filtroPerRuolo(ServletRequest servletRequest, String ruoloRichiesto, boolean controlloRuolo) {
        try {
            if (FixedVariables.debug)
                System.out.println("\n" + ruoloRichiesto.toUpperCase(Locale.ROOT) + " FILTER for path " + ((HttpServletRequest) servletRequest).getPathInfo());

            String token = JWTAssistant.getTokenJWTFromRequest((HttpServletRequest) servletRequest);

            if (!JWTAssistant.verificaJWT(token))
                throw new Exception("Il token non ha passato la verifica!");


            if (controlloRuolo) {
                String role = JWTAssistant.getRoleFromJWT(token);
                if (!role.equals(ruoloRichiesto))
                    throw new Exception("Non è un " + ruoloRichiesto + "...");
            }

            if (FixedVariables.debug)
                System.out.println("Filtro superato");

            return true;

        } catch (Exception e) {

            if (FixedVariables.debug) {
                System.out.println("DENIED ACCESS (Auth filter): " + e.getMessage());
                System.out.println("---------------------------------------------------");
            }

            return false;
        }
    }

}
