package Syntax;

public class WrongSyntaxException extends Exception{
    public WrongSyntaxException() {
    }

    public WrongSyntaxException(String message) {
        super(message);
    }

    public WrongSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongSyntaxException(Throwable cause) {
        super(cause);
    }
}
