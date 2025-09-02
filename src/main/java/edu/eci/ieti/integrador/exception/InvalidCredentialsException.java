package edu.eci.ieti.integrador.exception;

public class InvalidCredentialsException extends ServerErrorException {

    public InvalidCredentialsException() {
        super("invalid username or password");
    }
}
