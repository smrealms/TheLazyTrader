package model;

/**
 * 
 * Exception thrown when a SectorList tries to exceed it's maximum size.
 * 
 * @author Pagan Gazzard
 * @see Exception
 * @status 1.0
 */

public class ListTooBigException extends RuntimeException
{

	public ListTooBigException()
	{
		super("List Too Big");
	}

	public ListTooBigException(String errorMessage)
	{
		super("List Too Big: " + errorMessage);
	}

	public ListTooBigException(Throwable errorCause)
	{
		super(errorCause);
	}

	public ListTooBigException(String message, Throwable cause)
	{
		super("List Too Big: " + message, cause);
	}

}
