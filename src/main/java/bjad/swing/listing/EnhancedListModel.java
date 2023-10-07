package bjad.swing.listing;

import java.util.Collection;

import javax.swing.DefaultListModel;

/**
 * List model used to suppress refreshing when items are
 * added to list models via the beginUpdate and endUpdate
 * methods. 
 *
 * @author 
 *   Ben Dougall
 * @param <TYPE> 
 *    The class that will be contained via this model.
 */
public class EnhancedListModel<TYPE> extends DefaultListModel<TYPE>
{
   private static final long serialVersionUID = 5122588713337208464L;
   
   /**
    * Flag used to store if the model will suppress update events
    * while the model is updated. True means the update events will not 
    * occur, false means they will.
    */
   protected boolean suppressUpdates = false;
   
   /**
    * Default constructor, allowing updates to be fired by default. 
    */
   public EnhancedListModel()
   {
      super();
      suppressUpdates = false;
   }
   
   /**
    * Constructor, setting the initial set of items within
    * the model without any redraw events being fired until
    * the end of the list. 
    * 
    * @param items
    *    The items to add to the model.
    */
   public EnhancedListModel(Collection<TYPE> items)
   {
      this();
      add(items);
   }
   
   /**
    * Adds a collection of items to the model without firing the 
    * events until the end (if suppression was not on before hand)
    *  
    * @param items
    *    The items to add to the model.
    */
   public void add(Collection<TYPE> items)
   {
      // Store the suppression flag so we can restore it at 
      // the end of the function. 
      boolean updatesSuppressed = this.suppressUpdates;
      // Suppress the updates while we add all the items. 
      beginUpdate();
      // Add all the items now that the events are suppressed
      for (TYPE item : items)
      {
         this.addElement(item);
      }
      // If updates were not being suppressed before the method,
      // endUpdate to remove the suppression and fire the update
      // event.
      if (!updatesSuppressed)
      {
         endUpdate();
      }
   }
   
   /**
    * Turns off the event firing for when items are added, updated, 
    * or removed from the model so the screens doesn't repaint
    * for major updates. 
    */
   public void beginUpdate()
   {
      this.suppressUpdates = true;
   }
   
   /**
    * Re-enables the event firing for when items are added, updated,
    * or removed from the model, and fires the content changed event 
    * against all the items in the model.
    */
   public void endUpdate()
   {
      this.suppressUpdates = false;
      fireContentsChanged(this, 0, getSize());
   }
   
   /**
    * Returns the suppression state for the list box.
    * 
    *  @return
    *    True if the model is currently suppressing update events,
    *    false otherwise. 
    */
   public boolean isSuppressingUpdates()
   {
      return this.suppressUpdates;
   }
   
   /**
    * Overrides the fireContentsChanged event so it is only executed
    * if the events are not suppressed. 
    */
   @Override
   protected void fireContentsChanged(Object source, int index0, int index1)
   {
      if (!suppressUpdates)
      {
         super.fireContentsChanged(source, index0, index1);
      }
   }

   /**
    * Overrides the fireIntervalAdded event so it is only executed
    * if the events are not suppressed. 
    */
   @Override
   protected void fireIntervalAdded(Object source, int index0, int index1)
   {
      if (!suppressUpdates)
      {
         super.fireIntervalAdded(source, index0, index1);
      }
   }

   /**
    * Overrides the fireIntervalRemoved event so it is only executed
    * if the events are not suppressed. 
    */
   @Override
   protected void fireIntervalRemoved(Object source, int index0, int index1)
   {
      if (!suppressUpdates)
      {
         super.fireIntervalRemoved(source, index0, index1);
      }
   }
}
