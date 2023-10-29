package bjad.swing.listing;

import java.util.Collection;

import bjad.swing.beans.CountryBean;
import bjad.swing.beans.ICountryDisplay;

/**
 * Country list model used for country lists/dropdowns, applying
 * a display interface to the elements in the model so they display
 * in the UI with a common display implementation.
 *
 * @author 
 *   Ben Dougall
 */
public class CountryComboBoxModel extends EnhancedComboBoxModel<CountryBean>
{
   private static final long serialVersionUID = 1079800871750518227L;

   /**
    * The display impl to apply against all the elements in the list model.
    */
   protected ICountryDisplay displayInterface;

   /**
    * Constructor, setting the display interface for the 
    * model to apply to the beans in the model.
    * 
    * @param displayInterface
    *    The display interface to use for the display of the
    *    country data in the list/dropdown. Cannot be null.
    * @throws IllegalArgumentException
    *    If the display impl is null, an illegal argument exception
    *    will be thrown as we need a display impl to use for country
    *    display.
    */
   public CountryComboBoxModel(ICountryDisplay displayInterface) throws IllegalArgumentException
   {
      this.setDisplayInterface(displayInterface);
   }
   
   /**
    * Constructor, setting the display interface for the 
    * model to apply to the beans in the model.
    * 
    * @param countries
    *    Array of countries to add to the model by default.
    * @param displayInterface
    *    The display interface to use for the display of the
    *    country data in the list/dropdown. Cannot be null.
    * @throws IllegalArgumentException
    *    If the display impl is null, an illegal argument exception
    *    will be thrown as we need a display impl to use for country
    *    display.
    */
   public CountryComboBoxModel(CountryBean[] countries, ICountryDisplay displayInterface) throws IllegalArgumentException
   {
      this.setDisplayInterface(displayInterface);
      this.add(countries);
   }
   
   /**
    * Constructor, setting the display interface for the 
    * model to apply to the beans in the model.
    * 
    * @param countries
    *    Array of countries to add to the model by default.
    * @param displayInterface
    *    The display interface to use for the display of the
    *    country data in the list/dropdown. Cannot be null.
    * @throws IllegalArgumentException
    *    If the display impl is null, an illegal argument exception
    *    will be thrown as we need a display impl to use for country
    *    display.
    */
   public CountryComboBoxModel(Collection<CountryBean> countries, ICountryDisplay displayInterface) throws IllegalArgumentException
   {
      this.setDisplayInterface(displayInterface);
      this.add(countries);
   }

   /**
    * Adds the country element to the combo box model after ensuring
    * it's display formatter is set to the model's display formatter. 
    * 
    * @param country
    *    The country element to add to the combo box model after 
    *    ensuring the display formatter is set to match the one
    *    set within the model.
    */
   public void addElement(CountryBean country)
   {
      country.setDisplayFormatter(this.getDisplayInterface());
      super.addElement(country);
   }
   
   /**
    * Returns the value of the CountryListModel instance's 
    * displayInterface property.
    *
    * @return 
    *   The value of displayInterface
    */
   public ICountryDisplay getDisplayInterface()
   {
      return this.displayInterface;
   }

   /**
    * Sets the value of the CountryListModel instance's 
    * displayInterface property.
    *
    * @param displayInterface 
    *   The value to set within the instance's 
    *   displayInterface property
    */
   public void setDisplayInterface(ICountryDisplay displayInterface)
   {
      if (displayInterface == null)
      {
         throw new IllegalArgumentException("Cannot provide the CountryListModel a null ICountryDisplay implementation.");
      }
      this.displayInterface = displayInterface;
    
      // Fire the content changed listeners after setting the 
      // new impl for the display against all the elements. 
      fireContentsChanged(this, 0, getSize());
   }
   
   
   @Override
   protected void fireContentsChanged(Object source, int index0, int index1)
   {      
      // Apply the display impl to the elements.
      applyDisplayImplToElementsInRange(index0, index1);
      // Fire the super class' event to trigger UI updates.
      super.fireContentsChanged(source, index0, index1);
   }

   @Override
   protected void fireIntervalAdded(Object source, int index0, int index1)
   {  
      // Apply the display impl to the elements.
      applyDisplayImplToElementsInRange(index0, index1);
      // Fire the super class' event to trigger UI updates.
      super.fireIntervalAdded(source, index0, index1);
   }   
   
   private void applyDisplayImplToElementsInRange(int index0, int index1)
   {
      // Apply the display impl to each of the elements 
      // in the range provided to the function.
      int start = Math.min(index0, index1);
      int end = Math.max(index0, index1);
      ICountryDisplay impl = getDisplayInterface();
      for (int i = start; i < end; ++i)
      {
         getElementAt(i).setDisplayFormatter(impl);
      }
   }
}
