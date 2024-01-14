package pl.edu.agh.to2.example.exceptions;

public class CallToApiWentWrongException extends Exception {
    private static final String MESSAGE = "Executing call to api went wrong";

    public CallToApiWentWrongException() {
        super(MESSAGE);
    }
}
