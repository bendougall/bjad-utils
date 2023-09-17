package bjad.swing.wizard.testapp.model;

/**
 * The data model for an individual that is used within
 * the test application. 
 *
 * @author 
 *   Ben Dougall
 */
public class IndividualBean
{
   /**
    * The unique key for the individual
    */
   protected String key;
   /**
    * The given (first) name of the individual
    */
   protected String givenName;
   /**
    * The family (last) name of the individual
    */
   protected String familyName;
   /**
    * The address of the individual.
    */
   protected AddressBean address = new AddressBean();
   /**
    * The demographic information of the individual
    */
   protected DemographicBean demographicInformation = new DemographicBean();
   /**
    * Returns the value of the IndividualBean instance's 
    * key property.
    *
    * @return 
    *   The value of key
    */
   public String getKey()
   {
      return this.key;
   }
   /**
    * 
    */
   /**
    * Sets the value of the IndividualBean instance's 
    * key property.
    *
    * @param key 
    *   The value to set within the instance's 
    *   key property
    */
   public void setKey(String key)
   {
      this.key = key;
   }
   /**
    * Returns the value of the IndividualBean instance's 
    * givenName property.
    *
    * @return 
    *   The value of givenName
    */
   public String getGivenName()
   {
      return this.givenName;
   }
   /**
    * 
    */
   /**
    * Sets the value of the IndividualBean instance's 
    * givenName property.
    *
    * @param givenName 
    *   The value to set within the instance's 
    *   givenName property
    */
   public void setGivenName(String givenName)
   {
      this.givenName = givenName;
   }
   /**
    * Returns the value of the IndividualBean instance's 
    * familyName property.
    *
    * @return 
    *   The value of familyName
    */
   public String getFamilyName()
   {
      return this.familyName;
   }
   /**
    * 
    */
   /**
    * Sets the value of the IndividualBean instance's 
    * familyName property.
    *
    * @param familyName 
    *   The value to set within the instance's 
    *   familyName property
    */
   public void setFamilyName(String familyName)
   {
      this.familyName = familyName;
   }
   /**
    * Returns the value of the IndividualBean instance's 
    * address property.
    *
    * @return 
    *   The value of address
    */
   public AddressBean getAddress()
   {
      return this.address;
   }
   /**
    * 
    */
   /**
    * Sets the value of the IndividualBean instance's 
    * address property.
    *
    * @param address 
    *   The value to set within the instance's 
    *   address property
    */
   public void setAddress(AddressBean address)
   {
      this.address = address;
   }
   /**
    * Returns the value of the IndividualBean instance's 
    * demographicInformation property.
    *
    * @return 
    *   The value of demographicInformation
    */
   public DemographicBean getDemographicInformation()
   {
      return this.demographicInformation;
   }
   /**
    * 
    */
   /**
    * Sets the value of the IndividualBean instance's 
    * demographicInformation property.
    *
    * @param demographicInformation 
    *   The value to set within the instance's 
    *   demographicInformation property
    */
   public void setDemographicInformation(DemographicBean demographicInformation)
   {
      this.demographicInformation = demographicInformation;
   }
}
