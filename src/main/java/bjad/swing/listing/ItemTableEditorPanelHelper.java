package bjad.swing.listing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Helper/framework for panels with a listing of items 
 * and an editor panel for creating, viewing, and 
 * editing items within the panel.
 *
 * @author 
 *    Ben Dougall
 *   
 * @param <T>
 *    The type of object showing in the listing panel system.
 */
public class ItemTableEditorPanelHelper<T>
{
   /**
    * The table displaying the items and storing the 
    * selected index.
    */
   protected JTable table;
   
   /**
    * The model for the table storing the items 
    * displayed in the table. 
    */
   protected AbstractIItemTableModel<T> tableModel;
   
   /**
    * The new button for the helper to pick up
    * press events for.
    */
   protected JButton newButton;
   
   /**
    * The delete button for the helper to pick up
    * press events for.
    */
   protected JButton deleteButton;
   
   /**
    * The editor panel for the helper to interact with
    * for events within the listing panel.
    */
   protected IListingItemEditor<T> editorPanel;
   
   /**
    * The maximum items allowed to be in the listing panel.
    * -1 (default) means no limit.
    */
   protected int maximumItemCount = -1;
   
   /**
    * The item the helper believes is the selected item within
    * the listing. 
    */
   protected T selectedItem = null;
   
   /**
    * The index for what the helper believes is the selected index
    * within the listing.
    */
   protected int selectedIndex = -1;
   
   /**
    * Flag used to store if the selection is changing while
    * certain logic executes in the helper so we can maintain
    * the current selected item and selected index values.
    */
   protected boolean selectionChanging = false;
   
   /**
    * Flag used to store if the delete item logic is running while
    * certain logic executes in the helper so we can maintain
    * the current selected item and selected index values.
    */
   protected boolean deletionHappening = false;
   
   /**
    * Listener for list selection events that needs to be added and 
    * removed through out the events on the screen and the logic 
    * in the helper.
    */
   protected ListSelectionListener selectionListener = new ListSelectionListener()
   {      
      @Override
      public void valueChanged(ListSelectionEvent e)
      {
         handleTableSelection(e);
      }
   };
   
   /**
    * Constructor, setting up the controls for the helper to interact with
    * and bind listeners to as well as the table model to use for the item
    * storage. 
    * 
    * @param table
    *    The table to play with the selections of. 
    * @param tableModel
    *    The model used to store/modify items within the table.
    * @param newButton
    *    The new button to bind events to.
    * @param deleteButton
    *    The delete button to bind events to.
    * @param editorPanel
    *    The editor panel to interact with for item details and events.
    * @throws IllegalArgumentException
    *    Thrown if the table, table model, buttons, or editor are null
    */
   public ItemTableEditorPanelHelper(JTable table, AbstractIItemTableModel<T> tableModel, JButton newButton, JButton deleteButton, IListingItemEditor<T> editorPanel) throws IllegalArgumentException
   {
      if (table == null) 
      {
         throw new IllegalArgumentException("Table cannot be null.");
      }
      this.table = table;
      if (tableModel == null) 
      {
         throw new IllegalArgumentException("Table Model cannot be null.");
      }
      this.tableModel = tableModel;
      if (tableModel != table.getModel()) 
      {
         throw new IllegalArgumentException("Table Model must be the table model for the table passed.");
      }
      this.tableModel = tableModel;
      if (newButton == null) 
      {
         throw new IllegalArgumentException("New Button cannot be null.");
      }
      this.newButton = newButton;
      if (deleteButton == null) 
      {
         throw new IllegalArgumentException("Delete Button cannot be null.");
      }
      this.deleteButton = deleteButton;
      if (editorPanel == null) 
      {
         throw new IllegalArgumentException("Editor Panel cannot be null.");
      }
      this.editorPanel = editorPanel;
      
      // Now that we know we have valid controls, we will 
      // add initial settings to the controls and wire the
      // controls with the listeners to handle the key events
      // on the screen.
      configureBaseControlSettingsAndWireListeners();
   }

   /**
    * Returns the value of the ItemTableEditorPanelHelper instance's 
    * maximumItemCount property.
    *
    * @return 
    *   The value of maximumItemCount
    */
   public int getMaximumItemCount()
   {
      return this.maximumItemCount;
   }

   /**
    * Sets the value of the ItemTableEditorPanelHelper instance's 
    * maximumItemCount property.
    *
    * @param maximumItemCount 
    *   The value to set within the instance's 
    *   maximumItemCount property
    */
   public void setMaximumItemCount(int maximumItemCount)
   {
      this.maximumItemCount = maximumItemCount;
   }
   
   /**
    * Returns if new mode is active within the listing screen
    * by seeing if the table doesn't have a selection.
    * 
    * @return
    *    True if the screen is in "new" mode.
    */
   public boolean isNewModeActive()
   {
      return table.getSelectedRow() == -1; 
   }
   
   /**
    * Adds or updates the item within the listing panel based 
    * on the selection in the table. No selection, item is added
    * and selected. With an item selected, model is updated and
    * selection retained 
    * 
    * @param item
    *    The item to add or update.
    */
   public void addOrUpdateItem(T item)
   {
      // Its a new item when there is no selection present
      if (table.getSelectedRow() == -1)
      {
         // Remove the selection listener so we can add the new
         // item without triggering events on the editor.
         table.getSelectionModel().removeListSelectionListener(selectionListener);
         
         // Add the item to the end of the listing.
         tableModel.addItem(item);
         
         // Select the last item within the table.
         setSelectedRow(table.getRowCount() - 1);
         
         // Item and selection updated, re-add the listener
         table.getSelectionModel().addListSelectionListener(selectionListener);
         
         // Update the state of the controls with 
         // the new item added and selected.
         updateControlState();
      }
      // Its existing when a selection is present, so update
      // the model using the selected index
      else
      {
         // replace the item, which will trigger the display refresh.
         tableModel.setItemAt(table.getSelectedRow(), item);
      }
      
      // Now that the item is saved, move the focus on the screen
      // to the table
      table.requestFocusInWindow();
   }
   
   protected void handleTableSelection(ListSelectionEvent event)
   {
      // Make sure the controls are in the proper state when the 
      // selection logic is occuring.
      updateControlState();
      
      // If the selection is already changing, skip the logic.
      if (!selectionChanging)
      {
         // Store the previously selected index.
         int oldIndex = selectedIndex;
         if (oldIndex >= tableModel.getRowCount())
         {
            oldIndex = tableModel.getRowCount() - 1;
         }
         // Mark the fact the selection is changing. 
         selectionChanging = true;
         
         // Get the currently selected item (or null) for no selection.
         T currentSelection = oldIndex > -1 && oldIndex < tableModel.getRowCount() ?
               tableModel.getItemAt(oldIndex) : null;
         
         // See if the selection is allowed to change based on the currently
         // selected item. 
         boolean allowedToChange = deletionHappening ? true : editorPanel.isSelectionAllowToChange(currentSelection);
         
         // Change is allowed? Set the new selection information in the helper, and 
         // fire the newly selected item to the editor.
         if (allowedToChange)
         {
            // Adjust the index and item stored by the helper.
            selectedIndex = table.getSelectedRow();
            selectedItem = selectedIndex == -1 || tableModel.getRowCount() == 0 ? 
                  null : 
                     tableModel.getItemAt(table.getSelectedRow());
            
            // Send the updated item to the editor panel after all the EDT 
            // events have finished. 
            SwingUtilities.invokeLater(new Runnable()
            {               
               @Override
               public void run()
               {
                  editorPanel.setItemInEditor(selectedItem);
               }
            });
         }
         // Selection was blocked, re-adjust the selection to the previous state
         else
         {
            // No selection before, wipe the selection 
            if (selectedIndex == -1)
            {
               table.getSelectionModel().clearSelection();
            }
            // Selection before, select that item again.
            else
            {
               setSelectedRow(oldIndex);
            }
         }
      }
      
      // All logic complete, reset flags and update the control states
      selectionChanging = false;
      deletionHappening = false;
      updateControlState();
   }
   
   protected void handleNewButtonPressed()
   {
      // see if a new item is allowed if there
      // is a selected item in the table
      if (table.getSelectedRow() > -1)
      {
         T selectedItem = tableModel.getItemAt(table.getSelectedRow());
         if (!editorPanel.isNewAllowed(selectedItem))
         {
            return;
         }
      }
      
      // Now that we know a new item is allowed, setup
      // the editor and clear the selection within the 
      // table. First, remove the selection listener
      table.getSelectionModel().removeListSelectionListener(selectionListener);

      // Next, clear the selection in the table and the selection
      // properties in the helper.
      table.getSelectionModel().clearSelection();
      selectedIndex = -1;
      selectedItem = null;
      selectionChanging = true;
      
      // Now that selections have been wiped from the table and 
      // the helper, fire the new item event to the editor panel
      // on the EDT queue
      SwingUtilities.invokeLater(new Runnable()
      {         
         @Override
         public void run()
         {
            editorPanel.newItemStarted(false);
         }
      });
      
      // Lastly, re-add the selection listener and update
      // the enabled state of the controls based on the 
      // state of the editor and listing.
      table.getSelectionModel().addListSelectionListener(selectionListener);
      selectionChanging = false;
      updateControlState();
   }
   
   protected void handleDeleteButtonPressed()
   {
      int selectedIndex = table.getSelectedRow();
      
      // If an item is selected in the table, we first 
      // need to see if the deletion is allowed. 
      if (selectedIndex > -1)
      {
         T selectedItem = tableModel.getItemAt(selectedIndex);
         if (!editorPanel.isDeleteAllowed(selectedItem))
         {
            return;
         }
      }
      
      // Make a runnable for the new item started for last item
      // deletion.
      Runnable lastItemDeletedRunnable = new Runnable()
      {         
         @Override
         public void run()
         {
            editorPanel.newItemStarted(true);
         }
      };
      
      // Remove the listener as the indexes will be changing.
      table.getSelectionModel().removeListSelectionListener(selectionListener);
      
      // Empty list, and yet delete logic was triggered, send 
      // the new item event to ensure the editor knows a new 
      // item needs to be started.
      if (tableModel.getRowCount() == 0)
      {
         selectedIndex = -1;
         SwingUtilities.invokeLater(lastItemDeletedRunnable);
      }
      // Items in the list, move the selected index to the 
      // previous entry if it wasn't on the first one to
      // begin with. 
      else
      {
         if (tableModel.getRowCount() > 0)
         {
            if (selectedIndex > 0)
            {
               --selectedIndex;
            }
         }
         
         // Remove the selected item from the table
         tableModel.deleteItemAt(table.getSelectedRow());
         
         // If that was the last item to be deleted, fire
         // the new item event with the last deletion marker
         // to the editor panel and update the control state
         if (tableModel.getRowCount() == 0)
         {
            SwingUtilities.invokeLater(lastItemDeletedRunnable);
            updateControlState();
         }
      }
      
      // Mark that a deletion has occurred. 
      this.deletionHappening = true;
      this.selectedIndex = -1;
      
      // All done with the deletion, re-add the listener
      // and select the new index following the delete
      table.getSelectionModel().addListSelectionListener(selectionListener);
      setSelectedRow(selectedIndex);
   }

   private void updateControlState()
   {
      int selectedIndex = table.getSelectedRow();;
      int itemCount = tableModel.getRowCount(); 
      
      // Delete is enabled if the listing has items and the list box has a selection within it.
      deleteButton.setEnabled(itemCount > 0 && selectedIndex > -1);
      
      // New is enabled if the listing has a selection, and the helper
      // has no item cap or the current item count is less than the 
      // max item setting.
      newButton.setEnabled(selectedIndex > -1 && maximumItemCount == -1 || itemCount < maximumItemCount);
      
      // enable / disable the table based on if it has items or not
      table.setEnabled(tableModel.getRowCount() != 0);
   }
   
   /**
    * Configures base properties for the table and other controls, as
    * well as wires listeners to the table and the buttons. 
    */
   private void configureBaseControlSettingsAndWireListeners()
   {
      // "New" Mode is active by default, so disable the new 
      // and delete buttons. 
      newButton.setEnabled(false);
      deleteButton.setEnabled(false);
      
      // Make sure the table is in single selection mode as 
      // only item can be viewed/edited at a time. 
      table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      
      // Add in the list selection listener to start with
      table.getSelectionModel().addListSelectionListener(selectionListener);
      
      // Add in the settings to allow for row selection but not column selection,
      // as well as the viewport filling setting so the background is shown through
      // the whole table.
      table.setColumnSelectionAllowed(false);
      table.setRowSelectionAllowed(true);
      table.setFillsViewportHeight(true);
      
      // Create the listener for the new/delete buttons to trigger
      // the pressed events.
      ActionListener buttonHandler = new ActionListener()
      {         
         @Override
         public void actionPerformed(ActionEvent e)
         {
            if (e.getSource().equals(newButton))
            {
               handleNewButtonPressed();
            }
            else if (e.getSource().equals(deleteButton))
            {
               handleDeleteButtonPressed();
            }
         }
      };
      
      // Add the button handler to buttons now that we have the impl made.
      newButton.addActionListener(buttonHandler);
      deleteButton.addActionListener(buttonHandler);
   }
   
   private void setSelectedRow(int index)
   {
      table.getSelectionModel().setSelectionInterval(tableModel.getRowCount()-1, tableModel.getRowCount()-1);
   }
}
