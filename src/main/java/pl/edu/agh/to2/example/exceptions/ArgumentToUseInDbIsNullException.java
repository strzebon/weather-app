package pl.edu.agh.to2.example.exceptions;

public class ArgumentToUseInDbIsNullException extends Exception {
    private static final String MESSAGE = "Argument used during db operation was null!";

    public ArgumentToUseInDbIsNullException() {
        super(MESSAGE);
    }
}
