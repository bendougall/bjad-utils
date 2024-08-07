package bjad.swing;

/**
 * Enumeration of auto complete logic options for combobox
 * to complete the auto fill operation when comboboxes lose
 * focus. 
 *
 * @author 
 *   Ben Dougall
 */
public enum AutoCompleteMode
{
   /**
    * Nothing will happen when focus is lost. 
    */
   DO_NOTHING, 
   /**
    * Fill in the selected index if a matching 
    * entry is found, or keep the text if no matches
    * are found.
    */
   FILL_BUT_KEEP_UNKNOWN_TEXT,
   /**
    * Fill in the selected index if a matching 
    * entry is found, but clear the text if no matches
    * are found.
    */
   FILL_BUT_CLEAR_UNKNOWN_TEXT
}
