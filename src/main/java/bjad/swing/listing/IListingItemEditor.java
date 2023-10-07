package bjad.swing.listing;

/**
 * Interface for an editor panel in the listing item
 * system available in the BJAD libraries.
 *
 * @author 
 *    Ben Dougall
 * @param <T>
 *    The data type that will be edited in the editor 
 */
public interface IListingItemEditor<T> 
{
   /**
    * Method called when a new item is to be edited in
    * the edit, either through the new item button being
    * pressed, or if the last item in the listing being
    * deleted. 
    * 
    * @param viaLastItemDeleted
    *    True if this is triggered by the last item being
    *    deleted or not.
    */
   public void newItemStarted(boolean viaLastItemDeleted);
   
   /**
    * Sets the item in the editor to be edited. Triggered
    * when the selection within the listing is changed.
    * 
    * @param item
    *    The item selected in the listing panel.
    */
   public void setItemInEditor(T item);
   
   /**
    * Provides the item built from the editor panel and its 
    * fields. 
    * 
    * @return
    *    The item constructed by the editor panel and the fields.
    */
   public T retrieveItemFromEditor();
   
   /**
    * Returns if from the editor is the same as the item passed 
    * to the function. 
    * 
    * @param itemToCompareAgainst
    *    Item to compare the editor item against.
    * @return
    *    True if the item from the editor panel is the same 
    *    as the one passed to the function. False if a difference
    *    is found. 
    */
   public boolean isItemFromEditorSameAs(T itemToCompareAgainst);
   
   /**
    * Returns if the current state of the listing and editor allows
    * for new items to be created or not.
    * 
    * @param currentSelection
    *    The currently selected item.
    * @return
    *    True if a new item is allowed, false if it needs to be blocked
    */
   public boolean isNewAllowed(T currentSelection);
   
   /**
    * Returns if the current state of the listing and editor allows
    * for the item passed to be deleted or not.
    * 
    * @param itemToDelete
    *    The item that is about to be deleted.
    * @return
    *    True if the item is allowed to be deleted, false if it needs 
    *    to be blocked
    */
   public boolean isDeleteAllowed(T itemToDelete);
   
   /**
    * Returns if the current state of the listing and editor allows 
    * for the selected item in the listing to be changed to another 
    * item or not.
    *  
    * @param currentSelection
    *    The item that is currently selected. 
    * @return
    *    True if the selection can be changed or not. 
    */
   public boolean isSelectionAllowToChange(T currentSelection);
}
