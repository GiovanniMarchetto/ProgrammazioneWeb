package it.units.assistants;

import it.units.entities.storage.Attore;
import it.units.entities.support.SupportFileUpload;
import it.units.persistance.AttoreHelper;
import it.units.utils.FixedVariables;
import it.units.utils.MyException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class MailAssistant {

    // https://cloud.google.com/appengine/docs/standard/java/mail/
    // https://github.com/GoogleCloudPlatform/java-docs-samples/blob/2e5996c68440134a79f1511c57529fa5cf987628/appengine-java8/mail/src/main/java/com/example/appengine/mail/MailServlet.java

    public static String sendMail(String indirizzoFrom, String nomeFrom,
                                  String indirizzoTo, String nomeTo, String oggettoMail,
                                  String testoMail, String responseSuccess) throws MyException {
        try {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(indirizzoFrom, nomeFrom));

            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(indirizzoTo, nomeTo));
            msg.setSubject(oggettoMail);
            msg.setText(testoMail);

            Transport.send(msg);
            return responseSuccess;

        } catch (AddressException e) {
            System.out.println("Problema con indirizzo");
            throw new MyException("Indirizzo immesso per la mail non valido");
        } catch (MessagingException e) {
            System.out.println("Problema con il messaggio");
            throw new MyException("Messaggio immesso per la mail non valido");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Problema con encoding");
            throw new MyException("Encoding del messaggio non supportato");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new MyException("Eccezione generica nella spedizione della mail");
        }
    }

    public static String sendMailCreazioneAttore(Attore attore, String passwordProvvisoria, String usernameCreator) throws MyException, NullPointerException {
        Attore attoreCreator = AttoreHelper.getById(Attore.class, usernameCreator);
        if (attoreCreator == null)
            throw new NullPointerException("Attore creante non trovato");

        return sendMail(usernameCreator + "@progettomarchetto.appspotmail.com", attoreCreator.getName(),
                attore.getEmail(), attore.getName(),
                "Creazione account " + attore.getRole(),
                "Buongiorno, è stato creato un account con la sua mail.\n " +
                        "Le credenziali sono: \n " +
                        "Username: " + attore.getUsername() + "\n" +
                        "Password: " + passwordProvvisoria + "\n" +
                        "Le altre informazioni inserite: \n " +
                        "Nome: " + attore.getName() + "\n" +
                        "Email: " + attore.getEmail() + "\n" +
                        "\n Se vuole andare sul nostro sito acceda a: " + FixedVariables.HOMEPAGE + "\n" +
                        "\n Cordiali saluti,\n" + attoreCreator.getName()
                        + "\n\n\n\n Questa è un'email automatica, non rispondere.",
                "Creazione del consumer " + attore.getUsername() + " avvenuta. \n"
        );
    }

    public static String sendNotifica(SupportFileUpload supportFileUpload, String usernameUpl, String fileID) throws MyException, NullPointerException {
        String indFile = FixedVariables.BASE_IND_DIRECT_DOWNLOAD_FILES + "/" + fileID + "/" + TokenDownloadAssistant.creaTokenDownload(fileID);
        if (FixedVariables.debug)
            System.out.println(indFile);
        Attore uploader = AttoreHelper.getById(Attore.class, usernameUpl);
        if (uploader == null)
            throw new NullPointerException("Uploader non trovato");

        return sendMail(usernameUpl + "@progettomarchetto.appspotmail.com", uploader.getName(),
                supportFileUpload.getEmailCons(), supportFileUpload.getNameCons(),
                "Nuovo file caricato",
                "È stato caricato un nuovo file da parte di " + uploader.getName() + ".\n" +
                        "Il nome del file caricato è: " + supportFileUpload.getNameFile() + ".\n" +
                        "\n Se vuole andare sul nostro sito acceda a: " + FixedVariables.HOMEPAGE + "\n" +
                        "\n Se vuole scaricare direttamente il file: " + indFile + "\n"
                        + "Cordiali saluti,\n" + uploader.getName()
                        + "\n\n\n\n Questa è un'email automatica, non rispondere.",
                "Notifica inviata al consumer correttamente."
        );
    }
}
