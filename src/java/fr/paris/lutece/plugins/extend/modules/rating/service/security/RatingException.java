package fr.paris.lutece.plugins.extend.modules.rating.service.security;


/**
 * This Exception is thrown when the rating is not permitted
 *
 */
public class RatingException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9059760699217472548L;

	/**
     * Constructor
     */
    public RatingException(  )
    {
        super(  );
    }
	/**
     * Constructor
     *
     * @param strMessage
     *            The error message
     */
    public RatingException( String strMessage )
    {
        super( strMessage );
    }
    
    /**
     * Constructs a new exception with the specified detail message and cause.  
     *
     * @param  message the detail message 
     * @param  cause the cause 
     *        
     */
    public RatingException( String strMessage,  Throwable cause )
    {
        super( strMessage, cause );
    }

	
}
