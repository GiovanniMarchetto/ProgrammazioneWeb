package it;

import javax.ws.rs.client.*;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Giovanni Marchetto
 */
public class Main {

    public static final Client CLIENT = ClientBuilder.newClient();
//    public static final WebTarget TARGET_BASE = CLIENT.target("http://localhost:8080/api");
    public static final WebTarget TARGET_BASE = CLIENT.target("https://progettomarchetto.oa.r.appspot.com/api");

    public static String loginWS(String username, String password) {
        try {
            WebTarget loginTarget = TARGET_BASE.path("/login");
            Invocation.Builder invocationBuilder = loginTarget.request();
            User user = new User(username, password);
            return invocationBuilder.post(Entity.json(user), String.class);
        } catch (Exception e) {
            System.out.println(
                    "*************************" + "\n"
                    + "********  ERROR  ********" + "\n"
                    + e.getMessage() + "\n"
                    + "*************************"
            );
            return null;
        }
    }

    public static String fileUpload(String token,
            String usernameConsumer, String emailConsumer, String nameConsumer,
            String file, String nameFile, String hashtag) {
        try {
            WebTarget fileTarget = TARGET_BASE.path("/files/upload");
            Invocation.Builder invocationBuilder
                    = fileTarget.request().header("Authorization", "Bearer " + token);
            FileUpload fileUpload = new FileUpload(file, nameFile, hashtag,
                    usernameConsumer, nameConsumer, emailConsumer);
            return invocationBuilder.post(Entity.json(fileUpload), String.class);
        } catch (Exception e) {
            System.out.println("*************************" + "\n"
                    + "********  ERROR  ********" + "\n"
                    + e.getMessage() + "\n"
                    + "*************************");
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner tastiera = new Scanner(System.in);

        String username = "";
        String password = "";

        AtomicBoolean continueOperation = new AtomicBoolean(true);

        System.out.println("-------   LOGIN   -------");

        while (continueOperation.get()) {
            System.out.println("Username: ");
            username = tastiera.nextLine();
            System.out.println("Password: ");
            password = tastiera.nextLine();
            System.out.println("\nHai immesso questa coppia username-password:"
                    + "\n username: " + username
                    + "\n password: " + password);
            System.out.println("Confermi le credenziali? (s/n)");
            if (tastiera.nextLine().equals("s")) {
                continueOperation.set(false);
            }
        }

        String token = loginWS(username, password);

        if (token != null && !token.startsWith("ERR")) {
            System.out.println("Login di " + username);
        } else {
            System.out.println("*************************" + "\n"
                    + "********  ERROR  ********" + "\n"
                    + "***  Problema  login  ***" + "\n"
                    + token + "\n"
                    + "*************************");
        }

        if (token != null) {
            System.out.println("\n\n------ UPLOAD FILE ------");

            String usernameConsumer = "";
            String emailConsumer = "";
            String nameConsumer = "";

            continueOperation.set(true);
            while (continueOperation.get()) {
                System.out.println("Inserisci l'username del consumer:");
                usernameConsumer = tastiera.nextLine();
                System.out.println("Inserisci il nome del consumer");
                nameConsumer = tastiera.nextLine();
                System.out.println("Inserisci l'email del consumer: ");
                emailConsumer = tastiera.nextLine();

                System.out.println("\nHai immesso le seguenti informazioni sul destinatario:"
                        + "\n Username consumer: " + usernameConsumer
                        + "\n Email consumer: " + emailConsumer
                        + "\n Nome consumer: " + nameConsumer);

                while (true) {
                    System.out.println("Confermi consumer? (s/n)");
                    String risposta = tastiera.nextLine();
                    if (risposta.equals("s")) {
                        continueOperation.set(false);
                        break;
                    }
                    if (risposta.equals("n")) {
                        break;
                    }
                }
            }

            String fileBase64 = null;
            String nameFile = "";
            String hashtag = "";

            continueOperation.set(true);
            while (continueOperation.get()) {

                String filePath;
                String extension;
                System.out.println();
                System.out.println("Inserisci il percorso del file da caricare (sostituisci '/' con '//'): ");
                filePath = tastiera.nextLine();
                try {
                    File fileFromPath = new File(filePath);
                    byte[] data = Files.readAllBytes(fileFromPath.toPath());
                    fileBase64 = Base64.getEncoder().encodeToString(data);
                    int lastDot = filePath.lastIndexOf(".");
                    extension = filePath.substring(lastDot);
                } catch (Exception e) {
                    System.out.println("*************************" + "\n"
                            + "********  ERROR  ********" + "\n"
                            + "***     Problema      ***" + "\n"
                            + "*** acquisizione file ***" + "\n"
                            + e.getMessage() + "\n"
                            + "*************************");
                    continue;
                }

                System.out.println("Inserisci il nome da dare al file:");
                nameFile = tastiera.nextLine() + extension;
                System.out.println("Inserisci gli hashtag (una sola linea)");
                hashtag = tastiera.nextLine();

                System.out.println("\nHai immesso le seguenti informazioni per l'upload:"
                        + "\n Lunghezza file in base64: " + fileBase64.length()
                        + "\n Nome del file: " + nameFile
                        + "\n Hashtag: " + hashtag);

                while (true) {
                    System.out.println("Confermi il file? (s/n)");
                    String risposta = tastiera.nextLine();
                    if (risposta.equals("s")) {
                        continueOperation.set(false);
                        break;
                    }
                    if (risposta.equals("n")) {
                        break;
                    }
                }
            }

            String response = fileUpload(token,
                    usernameConsumer, emailConsumer, nameConsumer,
                    fileBase64, nameFile, hashtag);

            System.out.println("--------RISPOSTA UPLOAD FILE--------");
            System.out.println(response);
        }

    }
}
