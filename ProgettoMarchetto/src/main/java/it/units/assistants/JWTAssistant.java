package it.units.assistants;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.units.utils.FixedVariables;
import it.units.utils.MyException;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

public class JWTAssistant {

    // https://github.com/auth0/java-jwt

    private static final String secret = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCrVUGkFzAvIR9RdygtRbifB" +
            "KTFqbxNWUSEyE06t1i9+aN11GvYlN61SvYE+5qkdKLJfdzCSvcOLpGeyao3vblzUz8qhLpPDbNN5x84KTdNXgAWKhTKbRGCI6xgvFg" +
            "PaTcewIB1WbVaZQMdCT7HxkZQ5RqdjEmbPTwrCh3unDXmrSG+CmdBghpTAydVTJ6gVsSNSP2knwTYbvoRkycYCTGWlQYZVo7JgY70x" +
            "sralKA/NpfsiccoiEovpXZoNjsNafxeLjcaiQwCgbxbPkKwaOB03bzyysNTmFuqFnXpNxuWPAAj6PiRTK95nRMAdsTKiELVIqu/pPq" +
            "NhoTXoEuadb89AgMBAAECggEABchYm7pu9U7roO2sijdLHM4vZn7M/MT+x5wquR4vjVKOi7SfQt3Rah0tJ6wQmIIu7ViFZHxbEXGNF" +
            "3whyy7+LPAvbAFd+BA+ZT/KoV6zamB6EHAb/Vyy2tQr5k7tCkYdkWOODQfkU6L1o4ZTRghWiWFA0Eudagh7ePi8MQ240ap855TSfSk" +
            "hLFf08JurNXGppcYiok6GGzBhlFFyfwz5RxzHQnbxoYgzrDeciO1IqZsTuS45SMMQn6JIGU3rt4qg88rW0CJJk2692U5WzltsBoKcq" +
            "L+3kZGjPe8BOkOyLVKpbJqEXwfNHsdnrnrM+RJYjNhXH20JX88wEUJwk/AFmQKBgQDg/oicGTSoBUNJljUn0tP8CJ05WIXDCK6jY1p" +
            "83Q9jhajUICBy7XTHc0E5VuKyRFxRa/8s/yCNEH/YZn6WjQO2AneW1SwMxUhpYQXIj9KL8/+0JO44yD63JqLimHEzJyXlNQe2RuCwB" +
            "lIlWMhA26aeADNVaU2WpGJzHeAX2xg7eQKBgQDC8aIiRUVugb0iA5Nwdnjx1/WC6afsHN88+TwmHfy6VSmH/Gi+Am+dNDIuCz5lDBR" +
            "O5tvae+x2pNfuUWtK3ioD6kvWS0/k/Vu+FhTu0zhCZgAg0Ps9/+bwxB6nfXTP7KtxyrCnBj4cLq/Tj2EI/L4ZPaaXPpFlwtMqC8m7D" +
            "F3s5QKBgAm/HP3eHajVqYeLiTvzZxl46g6s5gZ/3sjXKflUvjDqfljKF33DCZcGSCIKwIFGctVYH1K8rpVNzKv8LKmq7Ck7TCDtlR3" +
            "sgemGuXBpwo4rgL02CFfPXOAkPVQhlG3J2Vtng7ECuwMFExPpF6uvmDUp/w9JI4JOFrGz7pw58n/JAoGBAKoOQ5OFviXCQ4zydaG7l" +
            "5HRBDipP3U3Un5MJO5TZm3dcJdR1sU6Nsd6CvQjltBrEyQpO0yCoip1bnBU0jXDS/+SWYmctRta8LcHaEdWNlOlyyLVavYQ0BLj4qH" +
            "VTRs8p+piGGv7hB7tkzAIeWtvnx1BrJtvV0LgC3k4Q0hKZ30JAoGBAI85MtfBUjN8JEyBQOXOqtMO5gbb/28RJYPlwTUHzZ0Ii54bM" +
            "2vMQiif/IWZj4GeD7Xhw+mXPjqHthj/CsPIQvASe6bG1HB1owlWkHXjycBHL6JkSy/sbZDzpNXKXAPl16KOQfHl6tkbCPKgsTNYkk6" +
            "IeDbLNrfiJUfalALpcwK2";
    private static final Algorithm algoritmo = Algorithm.HMAC256(secret);

    public static String creaJWT(String username, String ruolo) throws MyException {
        try {
            Date dataCorrente = new Date();
            Calendar dataScadenzaCalendar = Calendar.getInstance();
            dataScadenzaCalendar.setTime(dataCorrente);
            dataScadenzaCalendar.add(Calendar.DATE, 10);
            Date dataScadenza = dataScadenzaCalendar.getTime();

            String token = JWT.create()
                    .withIssuer("Giovanni Marchetto")
                    .withSubject(username)
                    .withIssuedAt(dataCorrente)
                    .withExpiresAt(dataScadenza)
                    .withClaim("role", ruolo)
                    .sign(algoritmo);

            if (FixedVariables.debug)
                System.out.println("Generato il token");

            return token;
        } catch (JWTCreationException exception) {
            System.out.println(exception.getMessage());
            throw new MyException("Errore nella creazione del jwt");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new MyException("Eccezione generica durante la creazione del jwt");
        } catch (NoSuchMethodError e) {
            System.out.println(e.getMessage());
            throw new MyException("Non esiste il metodo per la creazione del jwt");
        }
    }

    public static boolean verificaJWT(String token) {
        try {
            JWTVerifier verifier = JWT.require(algoritmo)
                    .withIssuer("Giovanni Marchetto")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return jwt != null;
        } catch (JWTVerificationException exception) {
            System.out.println("Invalid signature/claims");
            return false;
        }
    }

    public static DecodedJWT decodificaJWT(String token) {
        try {
            return JWT.decode(token);
        } catch (JWTDecodeException exception) {
            System.out.println("Invalid token");
            return null;
        }
    }

    public static String getTokenJWTFromRequest(HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }

    public static String getRoleFromJWT(String token) throws MyException {
        DecodedJWT decodedJWT = decodificaJWT(token);
        if (decodedJWT == null)
            throw new MyException("Token non valido");
        Claim ruolo = decodedJWT.getClaim("role");
        return ruolo.asString();
    }

    public static String getUsernameFromJWT(String token) throws MyException {
        DecodedJWT decodedJWT = decodificaJWT(token);
        if (decodedJWT == null)
            throw new MyException("Token non valido");
        return decodedJWT.getSubject();
    }

    public static String getUsernameFromHttpServletRequest(HttpServletRequest request) throws MyException {
        String token = getTokenJWTFromRequest(request);
        return getUsernameFromJWT(token);
    }
}
