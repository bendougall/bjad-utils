package bjad.swing;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Text field for swing applications that will
 * allow for users to enter only Integer values
 * into the field. 
 *
 * @author 
 *  Ben Dougall
 */
public class NumericTextField extends AbstractRestrictiveTextField
{
   private static final long serialVersionUID = 548220705280538015L;
   
   /**
    * The document object used to control the values the user can
    * enter into the field. 
    */
   NumericFieldDocument numDoc;
   
   /**
    * Flag representing if the field is for money, so we can format the 
    * amount when focus is lost to include the decimal and BOTH decimal 
    * places. 
    */
   protected boolean moneyField = false;
   
   /**
    * Custom constructor that defines the minimum and maximum 
    * parameters for the field.
    * 
    * @param allowDecimals
    *    Flag to allow decimals in the field 
    * @param allowNegatives 
    *    Flag to allow negatives in the field.
    * @param maximumValue
    *    The maximum value allowed within the text field.
    */
   private NumericTextField(boolean allowDecimals, boolean allowNegatives, BigDecimal maximumValue)
   {  
      numDoc = new NumericFieldDocument(
            this,
            maximumValue, 
            allowDecimals,
            allowNegatives);
      
      addCaretListener(null);
      setDocument(numDoc);
   }
   
   /**
    * Sets the decimal allowed flag on the field. 
    * @param allowed
    *    True to allow decimals, false to only allow ints.
    */
   public void setDecimalValuesAllowed(boolean allowed)
   {
      numDoc.setAllowDecimals(allowed);
   }
   
   /**
    * Sets the negative allowed flag on the field.
    * @param allowed
    *    True to allow negative values, false to only allow 
    *    0 or positive values. 
    */
   public void setNegativeValuesAllowed(boolean allowed)
   {
      numDoc.setAllowNegatives(allowed);
   }
   
   /**
    * Sets the maximum value allowed in the field. 
    * @param maxValue
    *    The maximum value allowed in the field.
    */
   public void setMaximumValueAllowed(BigDecimal maxValue)
   {
      numDoc.setMaximumValue(maxValue);
   }
   
   /**
    * Sets the maximum value allowed in the field.
    * @param maxValue
    *    The maximum value allowed in the field.
    */
   public void setMaximumValueAllowed(BigInteger maxValue)
   {
      numDoc.setMaximumValue(new BigDecimal(maxValue));
   }
   
   /** 
    * Applies new number of decimal place limit, or removes
    * if the value is less than 0 
    * 
    * @param decimalPlaces
    *    Maximum number of decimal places, or remove the 
    *    limit with a value less than 1.
    */   
   public void setMaximumDecimalPlaces(int decimalPlaces)
   {
      numDoc.setNumberOfDecimalPlaces(decimalPlaces);
   }
   
   /**
    * Sets the value for the field to display
    * 
    * @param val
    *    The value to display in the field.
    */
   public void setValue(Number val)
   {
      if (val instanceof BigDecimal)
      {
         super.setText(((BigDecimal)val).toPlainString());
      }
      else
      {
         super.setText(val.toString());
      }
   }
   
   /**
    * Gathers the value from the text field, or null
    * if the text is an invalid number.
    * 
    * @return
    *    the value from the text field, or null
    *    if the text is an invalid number.
    */
   public BigDecimal getDecimalValue()
   {
      BigDecimal retValue = isFieldEmpty() ? BigDecimal.ZERO : new BigDecimal(getTextContent());
      if (isMoneyField())
      {
         retValue = retValue.setScale(2, RoundingMode.HALF_UP);
      }
      return retValue;
   }
   
   /**
    * Gathers the value from the text field, or null
    * if the text is an invalid number.
    * 
    * @return
    *    the value from the text field, or null
    *    if the text is an invalid number.
    */
   public BigInteger getIntegerValue()
   {
      BigDecimal val = getDecimalValue();
      if (val != null)
      {
         return val.toBigInteger();
      }
      return null;         
   }
   
   /**
    * Throws an exception right away, as you should use the 
    * setValue method to apply values to the field.
    * 
    * @see javax.swing.text.JTextComponent#setText(java.lang.String)
    */
   @Override
   public void setText(String text) throws IllegalArgumentException
   {
      throw new IllegalArgumentException("Please use setValue method to apply values to the field");
   }
   
   /**
    * Throws an exception right away, as you should use the 
    * getIntegerValue or getDecimalValue method to the field content.
    * 
    * @see javax.swing.text.JTextComponent#setText(java.lang.String)
    */
   public String getText()
   {
      throw new IllegalArgumentException("Please use getIntegerValue or getDecimalValue method to get the value from the field.");
   }
   
   /**
    * Returns true if the field has no values in it.
    * @return
    *    True if the text content in the field is empty.
    */
   public boolean isFieldEmpty()
   {
      return this.getTextContent().trim().isEmpty();
   }
   
   /**
    * Wipes the text from the field as setText is blocked to the 
    * programmer with this class.
    */
   public void clearField()
   {
      super.setText("");
   }
   
   /** 
    * When focus is lost from the field, if this field is marked 
    * as a money field, ensure we show the formatted amount with
    * the decimal place and both decimal places.
    */
   @Override
   public void onFocusLost()
   {
      if (this.moneyField)
      {
         if (!isFieldEmpty())
         {
            super.setText(getDecimalValue().setScale(2, RoundingMode.DOWN).toPlainString());
         }
      }
   }
   
   /**
    * Returns the value of the NumericTextField instance's 
    * moneyField property.
    *
    * @return 
    *   The value of moneyField
    */
   public boolean isMoneyField()
   {
      return this.moneyField;
   }
   
   /**
    * Sets the value of the NumericTextField instance's 
    * moneyField property.
    *
    * @param moneyField 
    *   The value to set within the instance's 
    *   moneyField property
    */
   public void setMoneyField(boolean moneyField)
   {
      this.moneyField = moneyField;
   }

   /**
    * Creates a new integer text field which will be customized
    * to allow negative values and not have a maximum limit 
    * on the field.
    * 
    * @return
    *    The constructed numeric text field with the customized settings.
    */
   public static NumericTextField newIntegerFieldNoLimits()
   {
      return newIntegerField(true, null);
   }
   
   /**
    * Creates a new integer text field which will be customized
    * to allow negative values and not have a maximum limit 
    * on the field.
    * 
    * @param maximumValue
    *    The maximum value allowed for the field. Null for no limit.   
    * @return
    *    The constructed numeric text field with the customized settings.
    */
   public static NumericTextField newIntegerFieldWithLimit(Number maximumValue)
   {
      return newIntegerField(true, maximumValue);
   }
   
   /**
    * Creates a new integer text field which will be customized
    * to disallow negative values and not have a maximum limit 
    * on the field.
    * 
    * @return
    *    The constructed numeric text field with the customized settings.
    */
   public static NumericTextField newIntegerFieldNoNegatives()
   {
      return newIntegerField(false, null);
   }
   
   /**
    * Creates a new integer text field which will be customized
    * to either allow or disallow negative values and set a 
    * maximum value for the field.
    * 
    * @param allowNegatives
    *    True to allow negative values
    * @param maximumValue
    *    The maximum value allowed for the field. Null for no limit.
    * @return
    *    The constructed numeric text field with the customized settings.
    */
   public static NumericTextField newIntegerField(boolean allowNegatives, Number maximumValue)
   {      
      BigDecimal maxVal = maximumValue == null ? null : new BigDecimal(maximumValue.toString());
      return new NumericTextField(false, allowNegatives, maxVal);
   }
   
   /**
    * Creates a new decimal text field which will be customized
    * to allow negative values and not have a maximum limit 
    * on the field.
    * 
    * @return
    *    The constructed numeric text field with the customized settings.
    */
   public static NumericTextField newDecimalFieldNoLimits()
   {
      return newDecimalField(true, null);
   }
   
   /**
    * Creates a new decimal text field which will be customized
    * to allow negative values and not have a maximum limit 
    * on the field.
    * 
    * @param maximumValue
    *    The maximum value allowed for the field. Null for no limit.   
    * @return
    *    The constructed numeric text field with the customized settings.
    */
   public static NumericTextField newDecimalFieldWithLimit(Number maximumValue)
   {
      return newDecimalField(true, maximumValue);
   }
   
   /**
    * Creates a new decimal text field which will be customized
    * to disallow negative values and not have a maximum limit 
    * on the field.
    * 
    * @return
    *    The constructed numeric text field with the customized settings.
    */
   public static NumericTextField newDecimalFieldNoNegatives()
   {
      return newDecimalField(false, null);
   }
   
   /**
    * Creates a numeric decimal field that has no upper limit, and 
    * a decimal place limit of 2.
    * 
    * @return
    *    The constructed and configured numeric text field.
    */
   public static NumericTextField newMoneyField()
   {
      return newMoneyField(null);
   }
   
   /**
    * Creates a numeric decimal field with an upper limit, and 
    * a decimal place limit of 2.
    * 
    * @param maximumValue
    *    The upper limit for the text field.
    * @return
    *    The constructed and configured numeric text field.
    */
   public static NumericTextField newMoneyField(Number maximumValue)
   {      
      NumericTextField field = newDecimalField(false, maximumValue);
      field.setMoneyField(true);
      field.setMaximumDecimalPlaces(2);
      
      return field;
   }
   /**
    * Creates a new decimal text field which will be customized
    * to either allow or disallow negative values and set a 
    * maximum value for the field.
    * 
    * @param allowNegatives
    *    True to allow negative values
    * @param maximumValue
    *    The maximum value allowed for the field. Null for no limit.
    * @return
    *    The constructed numeric text field with the customized settings.
    */
   public static NumericTextField newDecimalField(boolean allowNegatives, Number maximumValue)
   {      
      BigDecimal maxVal = maximumValue == null ? null : new BigDecimal(maximumValue.toString());
      return new NumericTextField(true, allowNegatives, maxVal);
   }
}
