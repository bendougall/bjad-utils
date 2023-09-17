package bjad.swing.wizard.testapp.model;

/**
 * Demographic information for individuals
 *
 * @author 
 *   Ben Dougall
 */
public class DemographicBean
{
   /**
    * The age of the individual
    */
   protected int age;
   /**
    * The gender of the individual
    */
   protected String gender;
   /**
    * The employment status of the individual
    */
   protected String employmentStatus;
   
   /**
    * Returns the value of the DemographicBean instance's 
    * age property.
    *
    * @return 
    *   The value of age
    */
   public int getAge()
   {
      return this.age;
   }
   /**
    * 
    */
   /**
    * Sets the value of the DemographicBean instance's 
    * age property.
    *
    * @param age 
    *   The value to set within the instance's 
    *   age property
    */
   public void setAge(int age)
   {
      this.age = age;
   }
   /**
    * Returns the value of the DemographicBean instance's 
    * gender property.
    *
    * @return 
    *   The value of gender
    */
   public String getGender()
   {
      return this.gender;
   }
   /**
    * 
    */
   /**
    * Sets the value of the DemographicBean instance's 
    * gender property.
    *
    * @param gender 
    *   The value to set within the instance's 
    *   gender property
    */
   public void setGender(String gender)
   {
      this.gender = gender;
   }
   /**
    * Returns the value of the DemographicBean instance's 
    * employmentStatus property.
    *
    * @return 
    *   The value of employmentStatus
    */
   public String getEmploymentStatus()
   {
      return this.employmentStatus;
   }
   /**
    * 
    */
   /**
    * Sets the value of the DemographicBean instance's 
    * employmentStatus property.
    *
    * @param employmentStatus 
    *   The value to set within the instance's 
    *   employmentStatus property
    */
   public void setEmploymentStatus(String employmentStatus)
   {
      this.employmentStatus = employmentStatus;
   }
}
