package bjad.swing.wizard;

/**
 * Reason for the page change.
 *
 * @author 
 *   Ben Dougall
 */
public enum PageChangeReason
{
   /**
    * Manual page change
    */
   MANUAL,
   /**
    * First page shown
    */
   INITAL_SCREEN,
   /**
    * The previous page selected
    */
   PREVIOUS,
   /**
    * The next page selected
    */
   NEXT
}
