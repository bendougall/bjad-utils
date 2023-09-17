package bjad.swing;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Text Field used for entering postal codes/zip codes
 * within an UI screen.
 * 
 * @author 
 *    Ben Dougall
 */
public class PostalCodeTextField extends TextField
{
   private static final long serialVersionUID = -8443182646308344091L;
   
   /**
    * The charcater to use to represent a character being entered
    * as part of a format string. 
    */
   public static final char ALPHA = 'A';
   /**
    * The character to use to represent a number being entered 
    * as part of a format string.
    */
   public static final char NUMERIC = '#';
   
   private List<String> allowedFormats = new LinkedList<>();
   
   /**
    * Constructor, setting the initial formats allowed in the controls.
    * @param allowedFormats
    *    The formats allowed within the field. 
    */
   public PostalCodeTextField(Collection<String> allowedFormats)
   {  
      // only allow for alpha characters, numbers, spaces, and hyphens
      this.addAllowableCharactersFromString("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 -");
      this.setMaxLength(11);
      
      // Apply the formats.
      setAllowedFormats(allowedFormats);
   }
   
   /**
    * Validates the input within the field to ensure the content 
    * matches one of the allowed formats set within the field. 
    * 
    * @return
    *    True if the input is valid (aka, no formats applied or input 
    *    matches one of the allowed formats), false otherwise.
    */
   public boolean validateInput()
   {
      // No formats to validate against, validation
      // passes.
      if (allowedFormats.isEmpty())
      {
         return true;
      }
      
      // get the content from the field.
      String content = getText().toUpperCase();
      
      // No content, validation failed.
      if (content.trim().isEmpty())
      {
         return false;
      }
      
      boolean matchingLengthFound = false;
      
      for (String format : allowedFormats)
      {      
         if (content.length() != format.length())
         {
            continue;
         }
         else
         {
            matchingLengthFound = true;
         }
         for (int index = 0; index != format.length(); ++index)
         {
            switch (format.charAt(index))
            {
            case ' ':
               if (content.charAt(index) != ' ')
               {
                  return false;
               }
               break;
            case NUMERIC:
               if (!Character.isDigit(content.charAt(index)))
               {
                  return false;
               }
               break;
            case ALPHA:
            default:
               if (!Character.isAlphabetic(content.charAt(index)))
               {
                  return false;
               }
               break;
            }
         }
      }
      return !matchingLengthFound;
   }
   
   /**
    * Sets the formats allowed within the text field.
    * 
    * @param formats
    *    The allowed formats. 
    */
   public void setAllowedFormats(Collection<String> formats)
   {
      // Wipe the existing formats.
      this.allowedFormats.clear();
      
      // Add the new formats in.
      this.allowedFormats.addAll(formats);
      
      // Find the max length of the formats allowed, and set that as 
      // the max length for the text field. 
      int formatLength = 0;
      for (String format : allowedFormats)
      {
         formatLength = Math.max(formatLength, format.length());
      }
      if (formatLength > 0)
      {
         this.setMaxLength(formatLength);
      }
   }
}
