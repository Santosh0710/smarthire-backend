package smarthirebackend.exception;

// We extend RuntimeException so we don't need
// to declare it in every method signature

public class EmailAlreadyExistsException extends RuntimeException
{
    public EmailAlreadyExistsException(String message)
    {
        super(message);
    }
}
