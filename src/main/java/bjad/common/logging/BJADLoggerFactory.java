package bjad.common.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class used as a factory to create some standard logger implementations, such 
 * as a "nothing" implementation option, or "Send to Console" options. 
 *
 * @author 
 *   Ben Dougall
 */
public class BJADLoggerFactory
{
   /**
    * Private constructor implemented to prevent instances of the 
    * factory class from being made.
    */
   private BJADLoggerFactory()
   {}
   
   /**
    * Creates a logger implementation that does nothing with the 
    * log events (useful for objects with a logger property that
    * is optional to set, avoiding the need of using a bunch of 
    * "if (logger != null)" lines in that class.
    * 
    * @return
    *    The logger implementation that does nothing with the events
    *    sent to it.
    */
   public static AbstractBJADLogger createDoNothingLogger()
   {
      return new AbstractBJADLogger()
      { 
         @Override
         protected void sendToLog(String textToLog)
         {
            ; // do nothing.
         }
      };
   }
   
   /**
    * Creates the logger implementation that will send the text to the 
    * standard output stream for the JVM.
    * 
    * @return
    *    The logger implementation that sends the data to the standard
    *    output stream. 
    */
   public static AbstractBJADLogger createConsoleLogger()
   {
      return new AbstractBJADLogger()
      { 
         @Override
         protected void sendToLog(String textToLog)
         {
            System.out.println(textToLog);
         }
      };
   }
   
   /**
    * Creates the logger implementation that will add the date string to 
    * the front of the log events in whatever format passed to the method
    * before sending the text to the standard output stream for the JVM.
    * 
    * @param dateFormat
    *    The date format to use for the log events.
    * @return
    *    The logger implementation that sends the data to the console with
    *    the date prefixed to it. 
    */
   public static AbstractBJADLogger createConsoleLoggerWithDate(final String dateFormat)
   {
      return new AbstractBJADLogger()
      {  
         SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
         
         @Override
         protected void sendToLog(String textToLog)
         {
            System.out.println(sdf.format(new Date()) + " : " + textToLog);
         }
      };
   }
   
   /**
    * Creates the logger implementation that will add the date string with 
    * the date and time information prefixed to the events with the 
    * format string "yyyy/MM/dd HH:mm:ss.SSS" before sending the text to
    * the standard output stream for the JVM.
    * 
    * @return
    *    The logger implementation that sends the data to the console with
    *    the date prefixed to it. 
    */
   public static AbstractBJADLogger createConsoleLoggerWithDate()
   {
      return createConsoleLoggerWithDate("yyyy/MM/dd HH:mm:ss.SSS");
   }
}
