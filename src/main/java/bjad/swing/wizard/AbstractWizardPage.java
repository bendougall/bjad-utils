package bjad.swing.wizard;

import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * Abstract class for developers to implement in order 
 * to use version two of the wizard framework. 
 *
 * @param <DATA_MODEL>
 *    The data model class that will be used to share data
 *    throughout the wizard. 
 *    
 * @author 
 *    Ben Dougall
 */
public abstract class AbstractWizardPage<DATA_MODEL> extends JPanel
{
   private static final long serialVersionUID = 3341436796597789736L;
   
   /**
    * The data model used in the wizard framework to share information 
    * between the pages and possibly with the wizard at the start of
    * the wizard.
    */
   protected DATA_MODEL dataModel;
   
   /**
    * A flag to store if the page has been displayed previously 
    * or not. The flag will be set to true following the call to 
    * afterPageDisplayed() from the wizard controller.
    */
   protected boolean pageDisplayed = false;
   
   /**
    * Default Constructor, building the panel implementation with 
    * default options. 
    */
   protected AbstractWizardPage()
   {
      this(new FlowLayout(), true);
   }

   /**
    * Returns if the page has been displayed in the past or not. 
    * @return
    *    True if the page has been displayed in the past, false if
    *    the page has not be displayed. 
    */
   public boolean hasPageBeenDisplayedPreviously()
   {
      return this.pageDisplayed;
   }
   
   /**
    * Constructor, passing the double buffered flag to the 
    * super constructor.
    * 
    * @param isDoubleBuffered
    *    a boolean, true for double-buffering, whichuses additional 
    *    memory space to achieve fast, flicker-freeupdates
    */
   protected AbstractWizardPage(boolean isDoubleBuffered)
   {
      this(new FlowLayout(), isDoubleBuffered);
   }

   /**
    * Constructor, passing the layout manager to use on the panel.
    * @param layout
    *    The layout manager to use on the panel.
    */
   protected AbstractWizardPage(LayoutManager layout)
   {
      this(layout, true);
   }
   
   /**
    * Constructor, passing the layout and the double buffered
    * flag to the super constructor.
    * 
    * @param layout
    *    The layout manager to use on the panel
    * @param isDoubleBuffered
    *    a boolean, true for double-buffering, whichuses additional 
    *    memory space to achieve fast, flicker-freeupdates
    */
   protected AbstractWizardPage(LayoutManager layout, boolean isDoubleBuffered)
   {
      super(layout, isDoubleBuffered);
      buildPanel();
   }

   /**
    * Method to override in order to apply logic to the page after 
    * restoreDataFromModel() is called but before the page is 
    * actually displayed. 
    * 
    * @param reason
    *    The reason the page change happened.
    */
   protected void beforePageDisplayed(PageChangeReason reason)
   {
      ; // Do nothing in the base class, implementations can choose 
        // to implement if they see fit. 
   }
   
   /**
    * <p>
    * Method to override in order to apply logic to the page after
    * the page is displayed via setVisible(true) in the framework. 
    * </p>
    * <p>
    * <b>Note:</b> This method will be triggered in the framework 
    * on the EDT after the visibility is modified using invokeLater
    * </p> 
    * 
    * @param reason
    *    The reason the page change happened.
    */
   protected void afterPageDisplayed(PageChangeReason reason)
   {
      ; // Do nothing in the base class, implementations can choose 
      // to implement if they see fit.
   }
   
   /**
    * Method to override when the next page or finish operation 
    * is triggered on the UI. Return true (default) to allow the 
    * next page/finish to execute, Return false to stop the wizard
    * from proceeding to the next step. 
    *  
    * @return
    *    True to allow the wizard framework to proceed, false to
    *    stay on the current screen.
    */
   protected boolean onNextPageOrFinishBeingPressed()
   {
      return true;
   }
   
   /**
    * Method to override when the previous page operation is 
    * triggered on the UI. Return true (default) to allow the 
    * next page/finish to execute, Return false to stop the 
    * wizard from proceeding to the previous page. 
    *  
    * @return
    *    True to allow the wizard framework to proceed, false to
    *    stay on the current screen.
    */
   protected boolean onPreviousPageBeingPressed()
   {
      return true;
   } 
   
   /**
    * Builds the panel's controls and properties in a 
    * central method for a consistant layout within 
    * implementation classes. 
    */
   protected abstract void buildPanel();
   
   /**
    * Method to implement in order to save data from the page to 
    * the data model being shared within the wizard framework.
    * 
    * @param model
    *    The data model that has been getting built in the wizard
    * @return
    *    The data model that has been updated when before next page 
    *    is displayed.
    */
   protected abstract DATA_MODEL saveDataToModel(DATA_MODEL model);
   
   /**
    * Method to implement in order to restore data from the data 
    * model to the screen when the screen is activated in the framework
    * 
    * @param model
    *    The data model that has been getting built in the wizard.
    */
   protected abstract void restoreDataFromModel(DATA_MODEL model);  
   
   /**
    * Marks the page as displayed so the first display logic can be 
    * seperated from subsequent displays.
    */
   protected void markPageDisplayed()
   {
      this.pageDisplayed = true;
   }
}
