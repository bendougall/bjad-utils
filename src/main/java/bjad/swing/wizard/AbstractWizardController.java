package bjad.swing.wizard;

import java.awt.BorderLayout;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * The abstract wizard controller for developers to extend 
 * for their own wizard implementation. 
 *
 * @author 
 *   Ben Dougall
 * @param <DATA_MODEL>
 *    The data model handled by the controller implementation 
 * @param <PAGE_IMPL> 
 *    The page type used by the controller implementation that 
 *    handles the data model used by the controller implementation.
 */
public abstract class AbstractWizardController<DATA_MODEL, PAGE_IMPL extends AbstractWizardPage<DATA_MODEL>>
{
   /**
    * The data model being used and updated within the wizard and its pages
    */
   protected DATA_MODEL dataModel; 
   /**
    * The pages the wizard will be showing.
    */
   protected List<PAGE_IMPL> pages = new LinkedList<>();
   /**
    * The panel that will hold the page panel that the wizard
    * framework is showing.
    */
   protected JPanel containingPanel;
   /**
    * The index of the current page being showed. 
    */
   protected int currentPageIndex = 0; 
   /**
    * The next button from the container holding the wizard.
    */
   protected JButton nextButton;
   /**
    * The previous button from the container holding the wizard.
    */
   protected JButton previousButton;
   /**
    * Flag used to store if the framework is going to disable the next
    * and previous buttons automatically when the last or first page is
    * displayed to the user. 
    */
   protected boolean autoDisableButtons = true;
   
   /**
    * Set of listeners that will be triggered when the page changes
    * and when the first or last pages are displayed in the framework.
    */
   private Set<WizardListener<DATA_MODEL, PAGE_IMPL>> listeners = new LinkedHashSet<WizardListener<DATA_MODEL,PAGE_IMPL>>();
   
   /**
    * Constructor, setting the panel that will contain the 
    * pages shown by the wizard.
    * 
    * @param containingPanel
    *    The panel that will contain the pages shown by 
    *    the wizard.
    */
   public AbstractWizardController(JPanel containingPanel)
   {
      this.containingPanel = containingPanel;
   }
   
   /**
    * Returns the value of the AbstractWizardController instance's 
    * nextButton property.
    *
    * @return 
    *   The value of nextButton
    */
   public JButton getNextButton()
   {
      return this.nextButton;
   }

   /**
    * Sets the value of the AbstractWizardController instance's 
    * nextButton property.
    *
    * @param nextButton 
    *   The value to set within the instance's 
    *   nextButton property
    */
   public void setNextButton(JButton nextButton)
   {
      this.nextButton = nextButton;
   }

   /**
    * Returns the value of the AbstractWizardController instance's 
    * previousButton property.
    *
    * @return 
    *   The value of previousButton
    */
   public JButton getPreviousButton()
   {
      return this.previousButton;
   }

   /**
    * 
    */
   /**
    * Sets the value of the AbstractWizardController instance's 
    * previousButton property.
    *
    * @param previousButton 
    *   The value to set within the instance's 
    *   previousButton property
    */
   public void setPreviousButton(JButton previousButton)
   {
      this.previousButton = previousButton;
   }

   /**
    * Returns the value of the AbstractWizardController instance's 
    * autoDisableButtons property.
    *
    * @return 
    *   The value of autoDisableButtons
    */
   public boolean isAutoDisableButtons()
   {
      return this.autoDisableButtons;
   }

   /**
    * Displays the initial page while hiding the other 
    * pages. 
    */
   public void doInitialDisplay()
   {
      // Hide all the pages. 
      for (AbstractWizardPage<DATA_MODEL> page : pages)
      {
         page.setVisible(false);
      }
      // Show the first page.
      performPageChange(0, PageChangeReason.INITAL_SCREEN);
   }
   /**
    * Sets the value of the AbstractWizardController instance's 
    * autoDisableButtons property.
    *
    * @param autoDisableButtons 
    *   The value to set within the instance's 
    *   autoDisableButtons property
    */
   public void setAutoDisableButtons(boolean autoDisableButtons)
   {
      this.autoDisableButtons = autoDisableButtons;
   }

   /**
    * Triggers the framework to show the next page in the 
    * wizard if the current page is allowed to be switched and  
    * the wizard is currently not showing the last page already. 
    * If the last page is showing, the method will simply return 
    * true. 
    *   
    * @return
    *    True if the page is allowed to change, false otherwise.
    */
   public boolean nextPage()
   {      
      if (pages.get(currentPageIndex).onNextPageOrFinishBeingPressed())
      {      
         // Save the data model from the page if the page provides us one 
         DATA_MODEL retData = pages.get(this.currentPageIndex).saveDataToModel(this.dataModel);
         
         if (retData != null)
         {
            this.dataModel = retData;
         }  
         if (currentPageIndex < pages.size())
         {            
            // Change the page before increasing the page index. 
            performPageChange(currentPageIndex+1, PageChangeReason.NEXT);
         }
         
         return true;
      }
      else
      {
         return false;
      }
   }
   
   /**
    * Triggers the framework to show the previous page in the 
    * wizard if the current page is allowed to be switched and  
    * the wizard is currently not showing the first page already. 
    * If the first page is showing, the method will simply return 
    * true. 
    *   
    * @return
    *    True if the page is allowed to change, false otherwise.
    */
   public boolean previousPage()
   {
      if (pages.get(currentPageIndex).onPreviousPageBeingPressed())
      {         
         if (currentPageIndex > 0)
         {            
            // Change the page before decreasing the page index. 
            performPageChange(currentPageIndex-1, PageChangeReason.PREVIOUS);
         }
         
         return true;
      }
      else
      {
         return false;
      }
   }
   
   /**
    * Adds the wizard listener to the collection of listeners as long as it 
    * isn't in the collection already 
    * 
    * @param listener   
    *    The listener to add
    * @return
    *    True if the listener was added, false if the listener was not added due
    *    to being in the collection of listeners already.
    */
   public final boolean addWizardListener(WizardListener<DATA_MODEL, PAGE_IMPL> listener)
   {
      return listeners.add(listener);
   }
   
   /**
    * Removes the wizard listener to the collection of listeners if was in the 
    * collection.
    * 
    * @param listener   
    *    The listener to remove. 
    * @return
    *    True if the listener was removed, false if the listener was not removed 
    *    as it wasn't in the collection.
    */
   public final boolean removeWizardListener(WizardListener<DATA_MODEL, PAGE_IMPL> listener)
   {
      return listeners.remove(listener);
   }
   
   /**
    * Returns the pages in the wizard so the pages can be added to a content panel
    * or other manipulations can be done. 
    * 
    * @return
    *    The list of pages in the wizard.
    */
   public final List<PAGE_IMPL> getPages()
   {
      return this.pages;
   }
   
   /**
    * Changes the page in the wizard to the first page whose class matches
    * the class passed to the function. 
    * 
    * @param clazz
    *    The class of the page to change to.
    */
   public final void changePage(Class<? extends AbstractWizardPage<DATA_MODEL>> clazz)
   {
      for (int i = 0; i < pages.size(); ++i)
      {
         // Does the page class match the class we are looking for?
         if (pages.get(i).getClass().equals(clazz))
         { 
            // yes, change the page.
            performPageChange(i, PageChangeReason.MANUAL);
            return;
         }
      }
   }
   
   /**
    * Changes the page to the page in the wizard whose class matches the class 
    * passed, and has the index of the matching classes matching the index passed.
    * If the class is found, but the index cannot be found, the first page of the 
    * matching class will be displayed.
    * 
    * @param clazz   
    *    The class to match against.
    * @param clazzIndex
    *    The index of the page whose class' matches the class passed.
    */
   public final void changePage(Class<? extends AbstractWizardPage<DATA_MODEL>> clazz, int clazzIndex)
   {
      int matchingClassCount = 0;
      
      for (int i = 0; i < pages.size(); ++i)
      {
         // Does the page class match the class we are looking for?
         if (pages.get(i).getClass().equals(clazz))
         {
            // Yes. Add to the count of matching classes.
            matchingClassCount++; 
            // Now see if the matching class count matches the index
            // of the matching page class passed.
            if ((matchingClassCount-1) == clazzIndex)
            {
               // Change the page.
               performPageChange(i, PageChangeReason.MANUAL);
               // Exit the method now that the page is changed.
               return;
            }
         }
      }
      // If we made it here, we did not find the matching class and the matching
      // index, so we will attempt to change the page to the first page whose class
      // matches the one passed to the method.
      changePage(clazz);
   }
   
   /**
    * Changes the page within the wizard framework, hiding the 
    * previously visible page, and displaying the page whose 
    * index is passed to the method. 
    * 
    * @param newIndex
    *    The index of the page to be displayed.
    */
   public final void changePage(int newIndex)
   {
      performPageChange(newIndex, PageChangeReason.MANUAL);
   }
   
   /**
    * Returns true if the wizard is showing the first page,
    * false otherwise. 
    * 
    * @return
    *    True if the wizard is showing the first page.
    */
   public boolean isFirstPage()
   {
      return this.currentPageIndex == 0;
   }
   
   /**
    * Returns true if the wizard is showing the last page,
    * false otherwise.
    * 
    * @return
    *    True if the wizard is showing the last page.
    */
   public boolean isLastPage()
   {
      return this.currentPageIndex == this.pages.size() - 1;
   }
   
   /**
    * Executes the page changing logic, including all the 
    * page events.
    * 
    * @param newIndex
    *    The index of the page to change to.
    * @param reason
    *    The reason the page change happened.
    */
   protected void performPageChange(int newIndex, PageChangeReason reason)
   {  
      if (!reason.equals(PageChangeReason.INITAL_SCREEN))
      {
         // Hide the current page. 
         pages.get(this.currentPageIndex).setVisible(false);
         
         // Remove the existing controls from the panel containing
         // the pages of the wizard.
         containingPanel.removeAll();
      }
      
      // Set the data model on the page so it has the most 
      // up to date data to work from 
      pages.get(newIndex).dataModel = this.dataModel;
      
      // Trigger the restoration of the data from the model
      // onto the screen. 
      pages.get(newIndex).restoreDataFromModel(this.dataModel);
            
      // Trigger any additional logic on the page before 
      // it is displayed to the user. 
      pages.get(newIndex).beforePageDisplayed(reason);
      
      // Add the new page to the containing panel. 
      if (containingPanel.getLayout() != null && containingPanel.getLayout() instanceof BorderLayout)
      {
         containingPanel.add(pages.get(newIndex), BorderLayout.CENTER);
      }
      else
      {
         containingPanel.add(pages.get(newIndex));
      }
      
      // Display the page to the user.
      pages.get(newIndex).setVisible(true);
      
      // Invalidate and validate the containing panel
      // so the new page will be displayed.
      containingPanel.invalidate();
      containingPanel.validate();
            
      // Execute the after page displayed function in the next 
      // available "slot" within the EDT now that the page is 
      // displayed.
      SwingUtilities.invokeLater(new Runnable()
      {         
         @Override
         public void run()
         {
            pages.get(newIndex).afterPageDisplayed(reason);
            
            // Mark the page displayed flag as true for the new page. 
            pages.get(newIndex).markPageDisplayed();
         }
      });
      
      this.currentPageIndex = newIndex;
      
      // Disable the previous/next button if the option is set, the 
      // buttons are not null, and the index is either 0 or the last
      // page in the list of pages.
      if (this.autoDisableButtons)
      {
         if (previousButton != null)
         {
            previousButton.setEnabled(this.currentPageIndex != 0);
         }
         if (nextButton != null)
         {
            nextButton.setEnabled(this.currentPageIndex != (pages.size()-1));
         }
      }
      
      // Fire the wizard listener event based on the index of the page being shown now.
      listeners.forEach((listener) -> 
         listener.pageChanged(pages.get(currentPageIndex), currentPageIndex, currentPageIndex == 0, currentPageIndex == pages.size()-1));
   }
   
   /**
    * Abstract function to implement so all the form creation can 
    * be done in the common location for all pages using the 
    * wizard framework.
    */
   protected abstract void setupPageList();
}
