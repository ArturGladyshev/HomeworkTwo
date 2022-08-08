package DaoCache;

public class NoFoundValueException extends RuntimeException
{
    public NoFoundValueException(String message)
    {
        super(message);
    }
}
