package it;

import javax.ws.rs.client.*;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

/**
 * @author Giovanni Marchetto
 */
public class Main {

    private static final Client CLIENT = ClientBuilder.newClient();
    //    private static final WebTarget TARGET_BASE = CLIENT.target("http://localhost:8080/api");
    private static final WebTarget TARGET_BASE = CLIENT.target("https://progettomarchetto.oa.r.appspot.com/api");
    private static final boolean GUI = false;

    public static String loginWS(String username, String password) {
        try {
            System.out.println("---------    LOGIN   ---------" +
                    "\n--- username: " + username +
                    "\n--- password: " + password + "\n");
            WebTarget loginTarget = TARGET_BASE.path("/login");
            Invocation.Builder invocationBuilder = loginTarget.request();
            User user = new User(username, password);
            String token = invocationBuilder.post(Entity.json(user), String.class);

            if (token == null || token.startsWith("ERR")) throw new Exception(token);

            System.out.println("------- LOGIN ESEGUITO -------");
            return token;
        } catch (Exception e) {
            System.out.println("*************************"
                    + "\n" + "********  ERROR  ********"
                    + "\n" + e.getMessage()
                    + "\n" + "*************************");
            return null;
        }
    }

    public static void fileUpload(String token,
                                    String usernameConsumer, String emailConsumer, String nameConsumer,
                                    String file, String nameFile, String hashtag) {
        try {
            System.out.println("\n\n--------- UPLOAD FILE --------" +
                    "\n--- Destinatario: " +
                    "\n--- username consumer: " + usernameConsumer +
                    "\n--- email consumer: " + emailConsumer +
                    "\n--- nome consumer: " + nameConsumer +
                    "\n--- File:" +
                    "\n--- lunghezza file in base64: " + file.length() +
                    "\n--- nome del file: " + nameFile +
                    "\n--- hashtag: " + hashtag);


            WebTarget fileTarget = TARGET_BASE.path("/files/upload");
            Invocation.Builder invocationBuilder
                    = fileTarget.request().header("Authorization", "Bearer " + token);
            FileUpload fileUpload = new FileUpload(file, nameFile, hashtag,
                    usernameConsumer, nameConsumer, emailConsumer);
            String rispostaJson = invocationBuilder.post(Entity.json(fileUpload), String.class);

            if (rispostaJson.startsWith("ERR")) throw new Exception(rispostaJson);

            System.out.println("------- UPLOAD ESEGUITO ------");
        } catch (Exception e) {
            System.out.println("*************************"
                    + "\n" + "********  ERROR  ********"
                    + "\n" + e.getMessage()
                    + "\n" + "*************************");
        }
    }

    public static String getFileBase64(String filePath) {
        try {
            File fileFromPath = new File(filePath);
            byte[] data = Files.readAllBytes(fileFromPath.toPath());
            return Base64.getEncoder().encodeToString(data);
        } catch (Exception e) {
            System.out.println("*************************" + "\n"
                    + "********  ERROR  ********" + "\n"
                    + "***     Problema      ***" + "\n"
                    + "*** acquisizione file ***" + "\n"
                    + e.getMessage() + "\n"
                    + "*************************");
            return null;
        }
    }
    public static void main(String[] args) {

        String token = GUI ? it.GUI.loginWithGUI() : loginWS("upl1", "upl1");

        if (token != null) {
            if (GUI)
                it.GUI.uploadWithGUI(token);
            else {
                String file = getFileBase64("C:\\Users\\march\\Desktop\\Tesina Programmazione Web.docx");
                fileUpload(
                        token,
                        "ABCDEF21D01F205L",
                        "marchetto.giovanni97@gmail.com",
                        "Topolino", file,
                        "Tesina.docx",
                        "tesina programmazione web"
                );
            }
        }

        System.out.println("\n");
        System.out.println("------------------------------");
        System.out.println("--------- END PROGRAM --------");
        System.out.println("------------------------------");
    }

}
