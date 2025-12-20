package bjad.common.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Abstract logger class that contains all the common logic
 * as well as an abstract method to actually perform the log
 * operation.
 *
 * @author 
 *    Ben Dougall
 */
public abstract class AbstractBJADLogger
{
   /**
    * Executor for the debug events sent to the logger.
    */
   protected LogExecution debugExec = new DoNothingExecution();
   
   /**
    * Executor for the info events sent to the logger.
    */
   protected LogExecution infoExec = new SendToLogExecution();
   
   /**
    * Executor for the warning events sent to the logger.
    */
   protected LogExecution warnExec = new SendToLogExecution();
   
   /**
    * Executor for the warning events sent to the logger.
    */
   protected LogExecution errorExec = new SendToLogExecution();
   
   /**
    * Enables/disables debug logging for the logger. (Off by default)
    * @param state
    *    True to enable the debug logging events, false to disable.
    */
   public void enableDebugLogging(boolean state)
   {
      debugExec = state ? new SendToLogExecution() : new DoNothingExecution();
   }
   
   /**
    * Enables/disables info logging for the logger. (On by default)
    * @param state
    *    True to enable the info logging events, false to disable.
    */
   public void enableInfoLogging(boolean state)
   {
      infoExec = state ? new SendToLogExecution() : new DoNothingExecution();
   }
   
   /**
    * Enables/disables warning logging for the logger. (On by default)
    * @param state
    *    True to enable the warning logging events, false to disable.
    */
   public void enableWarningLogging(boolean state)
   {
      warnExec = state ? new SendToLogExecution() : new DoNothingExecution();
   }
   
   /**
    * Enables/disables error logging for the logger. (On by default)
    * @param state
    *    True to enable the error logging events, false to disable.
    */
   public void enableErrorLogging(boolean state)
   {
      errorExec = state ? new SendToLogExecution() : new DoNothingExecution();
   }
   
   /**
    * Logs the debug event if the debug logging is enabled.
    * @param message
    *    The message (in a format string format) to send to the log.
    * @param args
    *    The arguments for the format string to be filled in with.
    */
   public void debug(String message, Object... args)
   {
      debug(String.format(message, args));
   }
   
   /**
    * Logs the debug event if the debug logging is enabled.
    * @param message
    *    The message to send to the log.
    */
   public void debug(String message)
   {
      debugExec.executeLoggingOperation(String.format("[DEBUG] %s", message));
   }
   
   /**
    * Logs the info event if the info logging is enabled.
    * @param message
    *    The message (in a format string format) to send to the log.
    * @param args
    *    The arguments for the format string to be filled in with.
    */
   public void info(String message, Object... args)
   {
      info(String.format(message, args));
   }
   
   /**
    * Logs the info event if the info logging is enabled.
    * @param message
    *    The message to send to the log.
    */
   public void info(String message)
   {
      infoExec.executeLoggingOperation(String.format(" [INFO] %s", message));
   }

   /**
    * Logs the warning event if the warn logging is enabled.
    * @param message
    *    The message (in a format string format) to send to the log.
    * @param args
    *    The arguments for the format string to be filled in with.
    */
   public void warning(String message, Object... args)
   {
      warning(String.format(message, args));
   }
   
   /**
    * Logs the warn event if the warn logging is enabled.
    * @param message
    *    The message to send to the log.
    */
   public void warning(String message)
   {
      warnExec.executeLoggingOperation(String.format(" [WARN] %s", message));
   }
   
   /**
    * Logs the error event if the error logging is enabled.
    * @param message
    *    The message (in a format string format) to send to the log.
    * @param args
    *    The arguments for the format string to be filled in with.
    */
   public void error(String message, Object... args)
   {
      error(String.format(message, args));
   }
   
   /**
    * Logs the error event if the error logging is enabled.
    * @param message
    *    The message to send to the log.
    */
   public void error(String message)
   {
      errorExec.executeLoggingOperation(String.format("[ERROR] %s", message));
   }
   
   /**
    * Converts the stacktrace of the throwable passed to a string that 
    * can be logged. 
    * 
    * @param throwable  
    *    The throwable containing the stacktrace to convert into a string
    * @return
    *    The string of the stacktrace, or blank if the throwable was null 
    *    or an IOException occurs when doing the conversion (which should 
    *    never happen).
    */
   public String stacktraceToString(Throwable throwable)
   {
      if (throwable == null) 
      {
         return "";
      }
      
      String returnVal = "";
      try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw))
      {
         throwable.printStackTrace(pw);
         returnVal = sw.toString();
      }
      catch (IOException ex)
      {
         ; // do nothing, should never happen
      }
      return returnVal;
   }
   
   /**
    * Converts the throwable passed to a logable string with options to 
    * include the throwable's type, the throwable's message, and/or the
    * throwable's stacktrace. 
    *  
    * @param throwable  
    *    The throwable containing the details to convert to a logable string.
    * @param includeType
    *    True to include the throwable's simple class name in the string, false to omit
    * @param includeMessage
    *    True to include the throwables's message in the string, false to omit
    * @param includeStacktrace
    *    True to include the throwable's stacktrace in the string, false to omit.
    *    If the other include flags are true, the stacktrace will be started on a
    *    new line from the type/message.
    * @return
    *    The string representing the throwable's details and the details requested
    *    to be included.
    */
   public String convertThrowableToString(Throwable throwable, boolean includeType, boolean includeMessage, boolean includeStacktrace)
   {
      if (throwable == null)
      {
         return "";
      }
      
      StringBuilder sb = new StringBuilder();
      if (includeType)
      {
         sb.append(throwable.getClass().getSimpleName());
      }
      if (includeMessage)
      {
         if (sb.length() > 0) 
         {
            sb.append(" : ");
         }
         sb.append(throwable.getMessage());
      }
      if (includeStacktrace)
      {
         if (sb.length() > 0) 
         {
            sb.append(" : Stacktrace:").append(System.lineSeparator());
         }
         sb.append(stacktraceToString(throwable));
      }
      return sb.toString();
   }
   
   /**
    * Converts the throwable passed to a logable string with the type, 
    * message, and stacktrace included within it. 
    *  
    * @param throwable  
    *    The throwable containing the details to convert to a logable string.
    * @return
    *    The string representing the throwable's details including the type,
    *    message, and stacktrace.
    */
   public String convertThrowableToString(Throwable throwable)
   {
      return convertThrowableToString(throwable, true, true, true);
   }
   
   /**
    * Abstract method that will be implemented in sub-classes to 
    * send the text to whatever logging mechcanism is being used.
    * 
    * @param textToLog
    *    The text to send to the log.
    */
   protected abstract void sendToLog(String textToLog);

   /**
    * Implementation of the log execution interface used to 
    * do nothing with the log events (such as debug events 
    * when debug logging is disabled).
    *
    * @author 
    *   Ben Dougall
    */
   class DoNothingExecution implements LogExecution
   {
      @Override
      public void executeLoggingOperation(String textToLog)
      {
         ; // Intentionally do nothing.
      }
   }
   
   /**
    * Implementation of the log execution interface that will
    * send the events to the logger's sendToLog(String) function
    *
    * @author 
    *   Ben Dougall
    */
   class SendToLogExecution implements LogExecution
   {
      @Override
      public void executeLoggingOperation(String textToLog)
      {
         sendToLog(textToLog);
      }      
   }
}

/**
 * Interface used to trigger the logging operation, used 
 * instead of IF statements for debug operations. 
 *
 * @author 
 *   Ben Dougall
 */
interface LogExecution
{
   /**
    * Method to implement in order to handle the logging event
    * 
    * @param textToLog
    *    The text to log.
    */
   public void executeLoggingOperation(String textToLog);
}
