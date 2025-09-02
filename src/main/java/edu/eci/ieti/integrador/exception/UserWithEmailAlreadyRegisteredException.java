package edu.eci.ieti.integrador.exception;


public class UserWithEmailAlreadyRegisteredException extends ServerErrorException {
    public UserWithEmailAlreadyRegisteredException() {
        super("User with email already registered");
    }
}

