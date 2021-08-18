package fi.bytecraft.spock.review;

public class ReviewException extends Exception {

    public ReviewException(String message) {
        super(message);
    }

    public ReviewException(String message, Exception e) {
        super(message, e);
    }

}
