package it.units.utils;

public class MyException extends Exception {
    public MyException() {
        super("Errore specifico per il progetto");
    }

    public MyException(String messaggio) {
        super(messaggio);
    }
}
