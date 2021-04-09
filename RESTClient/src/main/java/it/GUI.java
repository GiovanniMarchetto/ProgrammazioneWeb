package it;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class GUI {

    public static String loginWithGUI() {
        Scanner tastiera = new Scanner(System.in);
        AtomicBoolean continueOperation = new AtomicBoolean(true);
        String username = "";
        String password = "";
        System.out.println("//// GUI LOGIN ////");
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
        tastiera.close();
        return Main.loginWS(username, password);
    }

    public static void uploadWithGUI(String token) {
        Scanner tastiera = new Scanner(System.in);
        AtomicBoolean continueOperation = new AtomicBoolean(true);

        String usernameConsumer = "";
        String emailConsumer = "";
        String nameConsumer = "";

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
            System.out.println("\nInserisci il percorso del file da caricare (sostituisci separatori con '//'): ");
            filePath = tastiera.nextLine();
            fileBase64 = Main.getFileBase64(filePath);
            if (fileBase64 == null) continue;
            int lastDot = filePath.lastIndexOf(".");
            extension = filePath.substring(lastDot);

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
        tastiera.close();

        Main.fileUpload(token,
                usernameConsumer, emailConsumer, nameConsumer,
                fileBase64, nameFile, hashtag);
    }
}
