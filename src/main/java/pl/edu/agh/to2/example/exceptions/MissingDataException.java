package pl.edu.agh.to2.example.exceptions;

public class MissingDataException extends RuntimeException {
    public MissingDataException(String message) {
        super(message);
    }
}
