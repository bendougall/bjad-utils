package bjad.swing.listing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
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
public class ItemListingEditorPanelHelper<T>
{
   /**
    * The listbox displaying the items and storing the 
    * selected index.
    */
   protected JList<T> listBox;
   
   /**
    * The model for the listbox storing the items 
    * displayed in the listbox. 
    */
   protected DefaultListModel<T> listModel;
   
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
         handleListSelection(e);
      }
   };
   
   /**
    * Constructor, setting up the controls for the helper to interact with
    * and bind listeners to. Uses the list model from the passed listbox
    * for the model in the helper. 
    * 
    * @param listbox
    *    The listbox with the default list model attached to it.
    * @param newButton
    *    The new button to bind events to.
    * @param deleteButton
    *    The delete button to bind events to.
    * @param editorPanel
    *    The editor panel to interact with for item details and events.
    * @throws IllegalArgumentException
    *    Thrown if the listbox, buttons, or editor is null OR if the 
    *    model attached to the listbox is not a implementation of a 
    *    DefaultListModel
    */
   public ItemListingEditorPanelHelper(JList<T> listbox, JButton newButton, JButton deleteButton, IListingItemEditor<T> editorPanel) throws IllegalArgumentException 
   {
      this(
            listbox, 
            (listbox.getModel() instanceof DefaultListModel<?>) ?  (DefaultListModel<T>)listbox.getModel() : null,
            newButton,
            deleteButton, 
            editorPanel);
   }
   
   /**
    * Constructor, setting up the controls for the helper to interact with
    * and bind listeners to as well as the list model to use for the item
    * storage. 
    * 
    * @param listbox
    *    The listbox with the default list model attached to it.
    * @param listModel
    *    The model used to store/modify items within the listbox with.
    * @param newButton
    *    The new button to bind events to.
    * @param deleteButton
    *    The delete button to bind events to.
    * @param editorPanel
    *    The editor panel to interact with for item details and events.
    * @throws IllegalArgumentException
    *    Thrown if the listbox, list model, buttons, or editor are null
    */
   public ItemListingEditorPanelHelper(JList<T> listbox, DefaultListModel<T> listModel, JButton newButton, JButton deleteButton, IListingItemEditor<T> editorPanel) throws IllegalArgumentException
   {
      if (listbox == null) 
      {
         throw new IllegalArgumentException("Listbox cannot be null.");
      }
      this.listBox = listbox;
      if (listModel == null)
      {
         throw new IllegalArgumentException("List Model cannot be null. If use Listbox only constructor, make sure the model attached to it is a DefaultListModel");
      }
      this.listModel = listModel;
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
    * Returns the value of the ItemListingPanelHelper instance's 
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
    * Sets the value of the ItemListingPanelHelper instance's 
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
    * by seeing if the listbox doesn't have a selection.
    * 
    * @return
    *    True if the screen is in "new" mode.
    */
   public boolean isNewModeActive()
   {
      return listBox.getSelectedIndex() == -1; 
   }
   
   /**
    * Adds or updates the item within the listing panel based 
    * on the selection in the listbox. No selection, item is added
    * and selected. With an item selected, model is updated and
    * selection retained 
    * 
    * @param item
    *    The item to add or update.
    */
   public void addOrUpdateItem(T item)
   {
      // Its a new item when there is no selection present
      if (listBox.getSelectedIndex() == -1)
      {
         // Remove the selection listener so we can add the new
         // item without triggering events on the editor.
         listBox.removeListSelectionListener(selectionListener);
         
         // Add the item to the end of the listing.
         listModel.addElement(item);
         
         // Select the last item within the list box.
         listBox.setSelectedIndex(listModel.getSize()-1);
         
         // Item and selection updated, re-add the listener
         listBox.addListSelectionListener(selectionListener);
         
         // Update the state of the controls with 
         // the new item added and selected.
         updateControlState();
      }
      // Its existing when a selection is present, so update
      // the model using the selected index
      else
      {
         // replace the item, which will trigger the display refresh.
         listModel.setElementAt(item, listBox.getSelectedIndex());
      }
      
      // Now that the item is saved, move the focus on the screen
      // to the listbox
      listBox.requestFocusInWindow();
   }
   
   protected void handleListSelection(ListSelectionEvent event)
   {
      // Make sure the controls are in the proper state when the 
      // selection logic is occuring.
      updateControlState();
      
      // If the selection is already changing, skip the logic.
      if (!selectionChanging)
      {
         // Store the previously selected index.
         int oldIndex = selectedIndex;
         if (oldIndex >= listModel.getSize())
         {
            oldIndex = listModel.getSize() - 1;
         }
         // Mark the fact the selection is changing. 
         selectionChanging = true;
         
         // Get the currently selected item (or null) for no selection.
         T currentSelection = oldIndex > -1 && oldIndex < listModel.getSize() ?
               listModel.getElementAt(oldIndex) : null;
         
         // See if the selection is allowed to change based on the currently
         // selected item. 
         boolean allowedToChange = deletionHappening ? true : editorPanel.isSelectionAllowToChange(currentSelection);
         
         // Change is allowed? Set the new selection information in the helper, and 
         // fire the newly selected item to the editor.
         if (allowedToChange)
         {
            // Adjust the index and item stored by the helper.
            selectedIndex = listBox.getSelectedIndex();
            selectedItem = selectedIndex == -1 || listModel.getSize() == 0 ? 
                  null : 
                     listModel.get(listBox.getSelectedIndex());
            
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
               listBox.clearSelection();
            }
            // Selection before, select that item again.
            else
            {
               listBox.setSelectedIndex(oldIndex);
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
      // is a selected item in the listbox
      if (listBox.getSelectedIndex() > -1)
      {
         T selectedItem = listModel.get(listBox.getSelectedIndex());
         if (!editorPanel.isNewAllowed(selectedItem))
         {
            return;
         }
      }
      
      // Now that we know a new item is allowed, setup
      // the editor and clear the selection within the 
      // listbox. First, remove the selection listener
      listBox.removeListSelectionListener(selectionListener);

      // Next, clear the selection in the listbox and the selection
      // properties in the helper.
      listBox.clearSelection();
      selectedIndex = -1;
      selectedItem = null;
      selectionChanging = true;
      
      // Now that selections have been wiped from the listbox and 
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
      listBox.addListSelectionListener(selectionListener);
      selectionChanging = false;
      updateControlState();
   }
   
   protected void handleDeleteButtonPressed()
   {
      int selectedIndex = listBox.getSelectedIndex();
      
      // If an item is selected in the listbox, we first 
      // need to see if the deletion is allowed. 
      if (selectedIndex > -1)
      {
         T selectedItem = listModel.get(selectedIndex);
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
      listBox.removeListSelectionListener(selectionListener);
      
      // Empty list, and yet delete logic was triggered, send 
      // the new item event to ensure the editor knows a new 
      // item needs to be started.
      if (listModel.getSize() == 0)
      {
         selectedIndex = -1;
         SwingUtilities.invokeLater(lastItemDeletedRunnable);
      }
      // Items in the list, move the selected index to the 
      // previous entry if it wasn't on the first one to
      // begin with. 
      else
      {
         if (listModel.getSize() > 0)
         {
            if (selectedIndex > 0)
            {
               --selectedIndex;
            }
         }
         
         // Remove the selected item from the listbox
         listModel.removeElementAt(listBox.getSelectedIndex());
         
         // If that was the last item to be deleted, fire
         // the new item event with the last deletion marker
         // to the editor panel and update the control state
         if (listModel.getSize() == 0)
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
      listBox.addListSelectionListener(selectionListener);
      listBox.setSelectedIndex(selectedIndex);
   }

   private void updateControlState()
   {
      int selectedIndex = listBox.getSelectedIndex();;
      int itemCount = listModel.getSize(); 
      
      // Delete is enabled if the listing has items and the list box has a selection within it.
      deleteButton.setEnabled(itemCount > 0 && selectedIndex > -1);
      
      // New is enabled if the listing has a selection, and the helper
      // has no item cap or the current item count is less than the 
      // max item setting.
      newButton.setEnabled(selectedIndex > -1 && maximumItemCount == -1 || itemCount < maximumItemCount);
      
      // enable / disable the listbox based on if it has items or not
      listBox.setEnabled(listModel.getSize() != 0);
   }
   
   /**
    * Configures base properties for the listbox and other controls, as
    * well as wires listeners to the listbox and the buttons. 
    */
   private void configureBaseControlSettingsAndWireListeners()
   {
      // "New" Mode is active by default, so disable the new 
      // and delete buttons. 
      newButton.setEnabled(false);
      deleteButton.setEnabled(false);
      
      // Make sure the list box is in single selection mode as 
      // only item can be viewed/edited at a time. 
      listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      
      // Add in the list selection list to start with
      listBox.addListSelectionListener(selectionListener);
      
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
}
