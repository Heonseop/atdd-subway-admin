package nextstep.subway.exception;

public class InvalidDistanceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidDistanceException() {
        super();
    }

    public InvalidDistanceException(String message) {
        super(message);
    }
}
