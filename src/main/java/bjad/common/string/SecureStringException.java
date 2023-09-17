package bjad.common.string;

/**
 * Exception for the secure string class that will capture
 * any of the encryption exceptions and encapsulate them 
 * in a RuntimeException so that you don't need to catch
 * them for things like bad Algorithm exceptions unless you
 * need to.
 *
 * @author 
 *   Ben Dougall
 */
public class SecureStringException extends RuntimeException
{
   private static final long serialVersionUID = 4824614593140140160L;

   /**
    * Constructor, setting the original exception for the
    * secure string exception that will be optional to catch.
    * 
    * @param ex
    *    The original encryption exception.
    */
   public SecureStringException(Exception ex)
   {
      super(ex.getClass().getCanonicalName() + ": " + ex.getMessage(), ex);
   }
}
