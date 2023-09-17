package bjad.cboamount;

import java.math.BigDecimal;

/**
 * Interface for the ComboFinder class to use in order to
 * know how to get the amount from the object from the data
 * object.
 * 
 * @author 
 *    Ben Dougall
 */
public interface ComboFinderElement
{
   /** 
    * Provides the amount for the element being considered by the combination finder 
    * @return
    *    The amount for the combination finder to use.
    */
   public BigDecimal getComboAmount();
}
