package bjad.db;

import java.math.BigDecimal;

import bjad.cboamount.ComboFinderElement;

/**
 * Amount bean for unit tests
 *
 * @author 
 *   Ben Dougall
 */
public class SampleAmountBean implements ComboFinderElement
{
   /**
    * The amount in the bean.
    */
   public BigDecimal amount = new BigDecimal("0.00");

   /** Default Constructor */
   public SampleAmountBean()
   {
      
   }
   
   /**
    * Constructor, setting the amount in the bean
    * @param amount
    *    The amount to set in the bean.
    */
   public SampleAmountBean(BigDecimal amount)
   {
      this.amount = amount;
   }
   
   /**
    * Returns the amount for the combination finder to use.
    * @return
    *    The amount to use within the combination finder.
    */
   @Override
   public BigDecimal getComboAmount()
   {
      return this.amount;
   }
   
}
