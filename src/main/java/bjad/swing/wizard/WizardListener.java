package bjad.swing.wizard;

/**
 * Listener interface for wizard events, including the 
 * page being changed, the first page being shown, and 
 * the last page being shown.
 *
 * @param <DATA_MODEL>
 *    The data model class that the wizard is populating.
 *      
 * @param <PAGE_IMPL>
 *    The page implementation that is being displayed
 *    within the wizard. 
 *
 * @author 
 *   Ben Dougall
 */
public interface WizardListener<DATA_MODEL, PAGE_IMPL extends AbstractWizardPage<DATA_MODEL>>
{   
   /**
    * Triggered when the page has been displayed 
    * within the wizard framework.
    * 
    * @param pageDisplayed
    *    The page that has been displayed.
    * @param pageIndex
    *    The index of the page that was displayed.
    * @param firstPage
    *    True if the page being displayed is the first page in the wizard
    * @param lastPage
    *    True if the page being displayed is the last page in the wizard
    */
   public void pageChanged(PAGE_IMPL pageDisplayed, int pageIndex, boolean firstPage, boolean lastPage);
}
