package bjad.swing.listing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Abstract Table Model that is typed specifically for a
 * item, and provides functions to retrieve and set specific
 * items in the model. 
 *
 * @author 
 *    Ben Dougall
 * @param <T>
 *    The type of object the table model will be storing
 *    for display and retrieval. 
 */
public abstract class AbstractIItemTableModel<T> extends AbstractTableModel
{
   private static final long serialVersionUID = -1015252786036136900L;
   
   /**
    * The list of data elements to show in the model.
    */
   protected List<T> elements;
      
   /**
    * Initializes an empty model but creating the list for
    * elements to be added.
    */
   public AbstractIItemTableModel()
   {
      this.elements = new ArrayList<T>();
   }
   
   /**
    * Initializes the table model with the list provided and 
    * any elements it may contain. If the list is null, the 
    * table model will initialize the list it contains as
    * an ArrayList. 
    * 
    * @param initialItems
    *    The list to use within the table model, or if null,
    *    the model will initialize with an empty array list.
    */
   public AbstractIItemTableModel(List<T> initialItems)
   {
      this.elements = initialItems;
      
      // Make sure the list is a list to avoid null pointers if
      // user was a fool and provided null as the initial list.
      if (this.elements == null)
      {
         this.elements = new ArrayList<T>();
      }
   }
   
   @Override
   public int getRowCount()
   {
      return elements.size();
   }
   
   /**
    * Returns the item in the model whose index matches 
    * the one passed. 
    * 
    * @param index
    *    The index for the item to retrieve. 
    * @return
    *    The item, or null if the index is invalid.
    */
   public T getItemAt(int index)
   {
      if (index > -1 && index < elements.size())
      {
         return elements.get(index);
      }
      else
      {
         return null;
      }
   }
   
   /**
    * Updates the item at the passed index with the item
    * passed, as long as the index is a valid row, and fires 
    * the table cell updated event against each of the columns
    * in the row that was updated. 
    * 
    * @param index
    *    The index to update the item at.
    * @param item
    *    The item to update the model with.
    * @return
    *    True if the item was set, false if the index
    *    provided was not a valid index.
    */
   public boolean setItemAt(int index, T item)
   {
      if (index > -1 && index < elements.size())
      {
         elements.set(index, item);

         for (int col = 0; col != getColumnCount(); ++col)
         {
            fireTableCellUpdated(index, col);
         }
         
         return true;
      }
      
      // Invalid index, return false
      return false;
   }
   
   /**
    * Adds an item to the model and fires the 
    * table data changed event
    * 
    * @param item
    *    The item to add to the end of the model.
    */
   public void addItem(T item)
   {
      elements.add(item);
      fireTableDataChanged();
   }
   
   /**
    * Adds a collection of items to the model and 
    * fires the table data changed event once all
    * the items are added.
    * 
    * @param items
    *    The items to add to the model.
    */
   public void addItems(Collection<T> items)
   {
      elements.addAll(items);
      fireTableDataChanged();
   }
   
   /**
    * Removes the item passed from the model if its found
    * and fires the table changed event.
    * 
    * @param item
    *    The item to remove
    * @return
    *    True if the item was removed, false if the 
    *    item was not found in the model.
    */
   public boolean deleteItem(T item)
   {
      if (elements.remove(item))
      {
         fireTableDataChanged();
         return true;
      }
      return false;
   }
   
   /**
    * Deletes the item located at the index
    * provided, and if its a valid index, fires
    * the table changed event. 
    * 
    * @param index
    *    The index to delete the item from. 
    */
   public void deleteItemAt(int index)
   {
      if (index > -1 && index < elements.size())
      {
         elements.remove(index);
         fireTableDataChanged();
      }
   }
}
