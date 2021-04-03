package it.units.utils;

import it.units.assistants.JWTAssistant;
import it.units.entities.storage.Attore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsMiscellaneous {

    public static boolean isSyntaxUsernameWrong(String username, String role) {
        Pattern pattern;
        switch (role) {
            case FixedVariables.ADMINISTRATOR:
                pattern = Pattern.compile("^[a-zA-Z0-9.]+@[a-z]+\\.[a-z]+$");
                break;
            case FixedVariables.UPLOADER:
                pattern = Pattern.compile("^[a-zA-Z0-9]{4}$");
                break;
            case FixedVariables.CONSUMER:
                pattern = Pattern.compile("^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$");
                break;
            default:
                return true;
        }
        Matcher matcher = pattern.matcher(username);
        return !matcher.matches();
    }

    public static String getDataString() {
        LocalDateTime dataCorrente = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormatter.format(dataCorrente);
    }

    public static void controlloPrivilegi(String token, String role) throws MyException {
        switch (JWTAssistant.getRoleFromJWT(token)) {
            case FixedVariables.CONSUMER:
                throw new MyException("Un consumer non può effettuare questa operazione.");
            case FixedVariables.UPLOADER:
                if (!role.equals(FixedVariables.CONSUMER))
                    throw new MyException("Un uploader non può effettuare questa operazione.");
                break;
            case FixedVariables.ADMINISTRATOR:
                if (role.equals(FixedVariables.CONSUMER))
                    throw new MyException("Un amministratore non può agire sui consumer.");
                break;
            default:
                throw new MyException("Ruolo mancante...");
        }
    }

    public static void stampaDatiPassati(Attore attore, String azione) {
        System.out.println("\n------------" + azione + "-----------");
        System.out.println("------------DATI UTENTE-----------");
        System.out.println(" Username: " + attore.getUsername());
        System.out.println(" Password: " + attore.getPassword());
        System.out.println(" Nome: " + attore.getName());
        System.out.println(" Email: " + attore.getEmail());
        System.out.println(" Ruolo: " + attore.getRole());
        System.out.println(" Logo: " + !attore.getLogo().isEmpty());
        System.out.println("----------------------------------------");
    }

}
