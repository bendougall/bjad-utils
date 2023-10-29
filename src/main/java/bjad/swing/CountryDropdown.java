package bjad.swing;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.ComboBoxModel;

import bjad.swing.beans.CountryBean;
import bjad.swing.beans.ICountryDisplay;
import bjad.swing.listing.CountryComboBoxModel;

/**
 * Dropdown used to display countries to the user. 
 *
 * @author 
 *   Ben Dougall
 */
public class CountryDropdown extends BJADComboBox<CountryBean>
{
   private static final long serialVersionUID = -6227001906185492195L;
   
   /**
    * The combobox model used to store the items that will be displayed
    * within the dropdown, as well as ensuring the display format set within
    * the model is applied to all elements in the combobox when they are 
    * added, or when the format is updated against the dropdown or the 
    * model itself.
    */
   protected CountryComboBoxModel countryModel;
   
   /**
    * Default constructor, creating an empty dropdown.
    */
   public CountryDropdown()
   {
      this(new CountryComboBoxModel(ICountryDisplay.createEnglishDisplaySafeImpl()));
   }

   /**
    * Constructor setting the model to use for the dropdown
    * @param aModel
    *    The model the dropdown will use to show items.
    */
   public CountryDropdown(CountryComboBoxModel aModel)
   {
      super(aModel);
      this.countryModel = aModel;
      setEditable(true);
   }

   /**
    * Constructor, setting the array items to display.
    * @param countries
    *    the countries to display.
    */
   public CountryDropdown(CountryBean[] countries)
   {
      this();
      this.countryModel.add(countries);
   }

   /**
    * @param countries
    *    The collection of countries to add. 
    */
   public CountryDropdown(Collection<CountryBean> countries)
   {
      this();
      this.countryModel.add(countries);
   }

   /**
    * Returns the selected country bean from the dropdown if 
    * a selection is made, or null if no selection is found.
    * 
    * @return
    *    The selected country bean, or null if no selection has 
    *    been made. 
    */
   public CountryBean getSelectedCountry()
   {
      int index = getSelectedIndex();
      if (index > -1)
      {
         return getItemAt(index);
      }
      return null;
   }
   
   /**
    * Returns the selected countries from the dropdowm, or null
    * if no selections are found. 
    * 
    * @return
    *    The array of selected countries.
    */
   public CountryBean[] getSelectedCountries()
   {
      Object selectedObject = super.getSelectedItem();
      if (selectedObject != null && selectedObject instanceof CountryBean)
      {
         CountryBean result[] = new CountryBean[1];
         result[0] = (CountryBean)selectedObject;
         return result;
      }
      else 
      {
         return new CountryBean[0];
      }
   }
   
   /**
    * Sets the selected country in the dropdown if its found.
    * @param country
    *    The country to select.
    */
   public void setSelectedCountry(CountryBean country)
   {
      super.setSelectedItem(country);
   }   
   
   /**
    * Sets the selected country in the dropdown by finding
    * the Country that matches the text passed (see
    * bjad.swing.CountryDropdown.findCountryByText(String))
    * 
    * @see 
    *    #findCountryIndexByText(String)
    * @param text
    *    The text to find in the country beans in the dropdown. 
    *    Selection will be set to null if the text cannot be
    *    matched.
    */
   public void setSelectedCountryByText(String text)
   {
      super.setSelectedIndex(findCountryIndexByText(text));
   }
   
   /**
    * Sets the display formatter for the dropdown so the 
    * countries display can be customed to the needs of the user. 
    * 
    * @param formatter
    *    The formatter to use. If null, no change will be made.
    */
   public void setDisplayFormatter(ICountryDisplay formatter)
   {
      this.countryModel.setDisplayInterface(formatter);
   }
      
   /**
    * Sets the model for the dropdown as long as its a CountryComboBoxModel
    * as we need to use the custom model to ensure formatting of the beans
    * is maintained. 
    * 
    * @param aModel
    *    The model to apply to the dropdown, cannot be null and must be  
    *    an instance of CountryComboBoxModel.
    * @throws IllegalArgumentException
    *    Thrown if the model is null, or if its not an instance of a CountryComboBoxModel
    */
   @Override
   public void setModel(ComboBoxModel<CountryBean> aModel) throws IllegalArgumentException
   {
      if (aModel == null)
      {
         throw new IllegalArgumentException("The model cannot be null");
      }
      if (!(aModel instanceof CountryComboBoxModel))
      {
         throw new IllegalArgumentException("The model for the CountryDropdown must be a CountryComboBoxModel to ensure formatting.");
      }
      else
      {
         super.setModel(aModel);
         this.countryModel = (CountryComboBoxModel)aModel;         
      }
   }
   
   /**
    * Gets the comboboxmodel for the dropdown, but developer 
    * should be using the direct getCountryModel() function for 
    * this. 
    * 
    * @deprecated
    *    Should be using getCountryModel()
    * @return
    *    The combobox model used by the dropdown.
    */
   @Deprecated
   public ComboBoxModel<CountryBean> getModel()
   {
      return countryModel;
   }

   /**
    * Returns the value of the CountryDropdown instance's 
    * countryModel property.
    *
    * @return 
    *   The value of countryModel
    */
   public CountryComboBoxModel getCountryModel()
   {
      return this.countryModel;
   }

   /**
    * Sets the value of the CountryDropdown instance's 
    * countryModel property.
    *
    * @param countryModel 
    *   The value to set within the instance's 
    *   countryModel property
    */
   public void setCountryModel(CountryComboBoxModel countryModel)
   {
      this.setModel(countryModel);
   }

   /**
    * Find the index for the country that matches the text
    * passed by comparing the fields in the following order:
    * <ol>
    * <li>The result of toString()</li>
    * <li>The ISO 3166-1 alpha 3 code</li>
    * <li>The ISO 3166-1 alpha 2 code</li>
    * <li>The english text</li>
    * <li>The non english text values</li>
    * </ol>
    * If the text cannot be matched to a country, -1 
    * will be returned.
    * 
    * @param text
    *    The text to match against the countries in the 
    *    dropdown.
    * @return
    *    The index of the first matching country (See 
    *    description for search order), or -1 if the text 
    *    could not be matched.
    */
   public int findCountryIndexByText(String text)
   {
      int retVal = -1;
      if (text != null && !text.trim().isEmpty())
      {
         for (int index = 0; index != getItemCount(); ++index)
         {
            CountryBean bean = getItemAt(index);
            String val = bean.toString();
            if (val != null && val.equalsIgnoreCase(text))
            {
               retVal = index;
               break;
            }            
            val = bean.getAlpha3Code();
            if (val != null && val.equalsIgnoreCase(text))
            {
               retVal = index;
               break;
            }
            val = bean.getAlpha2Code();
            if (val != null && val.equalsIgnoreCase(text))
            {
               retVal = index;
               break;
            }
            val = bean.getEnglishText();
            if (val != null && val.equalsIgnoreCase(text))
            {
               retVal = index;
               break;
            }
            if (bean.getOtherLanguageText() != null)
            {
               for (String nonEnglishText : bean.getOtherLanguageText().values())
               {
                  if (nonEnglishText.equalsIgnoreCase(text))
                  {
                     retVal = index;
                     break;
                  }
               }
            }
            
            if (retVal != -1)
            {
               break;
            }
         }
      }
      System.out.println("Returning " + retVal + ":: " + getItemAt(retVal).getEnglishText());
      return retVal;
   }
   
   /**
    * Returns the selected objects in the dropdown, or null
    * if nothing is selected. 
    * 
    * @deprecated
    *    Should use getSelectedCountries() instead.
    * @return
    *    The selected objects in the dropdown.
    */
   @Override
   @Deprecated
   public Object[] getSelectedObjects()
   {
      return super.getSelectedObjects();
   }
   /**
    * Returns the selected object in the dropdown, or null
    * if nothing is selected. 
    * 
    * @deprecated
    *    Should use getSelectedCountry() instead.
    * @return
    *    The selected object in the dropdown.
    */
   @Override
   @Deprecated
   public Object getSelectedItem()
   {
      return super.getSelectedItem();
   }
   
   /**
    * Creates an instance of the dropdown with the list of ISO 3166-1 country
    * information stored within the library added as the items.
    * 
    * @return
    *    The dropdown with the pre-packaged countries added to it.
    */
   public static CountryDropdown createDropdownFromPackagedISO3166List()
   {
      List<CountryBean> countries = new LinkedList<>();
      try (Scanner s = new Scanner(ClassLoader.getSystemResourceAsStream("Country_ISO-3166-1_tilda-separated.txt")) )
      {
         while (s.hasNextLine())
         {
            String line = s.nextLine();
            StringTokenizer st = new StringTokenizer(line, "~~");
                        
            String eng = null;
            String a2c = null;
            String a3c = null;
            String num = null;
            
            eng = st.nextToken();
            if (st.hasMoreTokens())
            {
               a2c = st.nextToken();
            }
            if (st.hasMoreTokens())
            {
               a3c = st.nextToken();
            }
            if (st.hasMoreTokens())
            {
               num = st.nextToken();
            }
                        
            if (num != null)
            {
               CountryBean bean = new CountryBean();
               bean.setEnglishText(eng);
               bean.setAlpha2Code(a2c);
               bean.setAlpha3Code(a3c);
               try
               {
                  bean.setNumericCode(Integer.parseInt(num));
               }
               catch (Exception ex)
               {
                  ; // do nothing, num is the least important field
               }
               countries.add(bean);
            }
         }
      }
      catch (Exception ex)
      {
         ; // do nothing
      }
      
      return new CountryDropdown(countries);
   }
}
