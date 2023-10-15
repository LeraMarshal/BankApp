package de.marshal.bankapp.exception;

public class ClientNotFoundException extends Exception {
    public ClientNotFoundException() {
        super("Client not found");
    }
}
