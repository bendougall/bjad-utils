package bjad.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class used to determine duration
 * with milliseconds or nanoseconds. 
 *
 * @author 
 *   Ben Dougall
 */
public class DurationUtil
{
   /** The start time for the duration measurement */
   protected long startTime = 0L;
   /** The end time for the duration measurement */
   protected long endTime = -1L;
   
   /** Flag to state if milliseconds are going to be used */
   protected boolean useNanoSeconds;
   
   /**
    * Creates the millisecond duration util, setting
    * the start time to the current time. 
    */
   public DurationUtil()
   {
      this(false);
   }
   
   /**
    * Creates the duration util, setting if we are 
    * using nanoseconds if true is passed, milliseconds
    * if false is passed. The start time is also marked
    * to the current time.
    * 
    * @param useNanoSeconds
    *    True to measure in nano-seconds, false to measure
    *    in milliseconds. 
    */
   public DurationUtil(boolean useNanoSeconds)
   {
      this.useNanoSeconds = useNanoSeconds;
      start();
   }
   
   /**
    * Sets the start time for the duration marker. 
    */
   public void start()
   {
      this.startTime = getCurrentTime();
   }
   
   /**
    * Sets the end time for the duration marker.
    */
   public void stop()
   {
      this.endTime = getCurrentTime();
   }
   
   /**
    * Returns the duration in seconds.
    * @return
    *    The duration, as a string, in seconds rounded to 5 decimals.
    */
   public String getSecondsString()
   {
      return getSecondsString(5);
   }
   
   /**
    * Returns the dureation in seconds, rounding to the 
    * decimal places passed. 
    * @param decimalPlaces
    *    The number of decimal places to round to.
    * @return
    *    The number of seconds, as a string rounded to the decimal 
    *    places passed.
    */
   public String getSecondsString(int decimalPlaces)
   {
      BigDecimal bd = new BigDecimal(endTime);
      bd = bd.subtract(new BigDecimal(startTime));
      
      bd = bd.divide(new BigDecimal(useNanoSeconds ? 1_000_000_000 : 1_000), decimalPlaces, RoundingMode.HALF_UP);
      return bd.toPlainString();
   }
   
   /** 
    * Returns the number of milliseconds elapsed. 
    * @return
    *    The number of milliseconds elapsed. 
    */
   public long getMilliseconds()
   {
      if (this.useNanoSeconds)
      {
         return ((endTime - startTime) / 1_000_000);
      }
      else
      {
         return endTime - startTime;
      }
   }
   
   /** 
    * Returns the number of nanoseconds elapsed. 
    * @return
    *    The number of nanoseconds elapsed (if 
    *    using milliseconds, its not exact).
    */
   public long getNanoseconds()
   {
      if (this.useNanoSeconds)
      {
         return endTime - startTime;
      }
      else
      {
         return (endTime - startTime) * 1_000_000;
      }
   }
   
   /**
    * Returns the current time in milliseconds or 
    * nanoseconds depending on the flag on the object.
    * @return
    *    The time in milliseconds or nanoseconds.
    */
   private long getCurrentTime()
   {
      return this.useNanoSeconds ? System.nanoTime() : System.currentTimeMillis();
   }
}
