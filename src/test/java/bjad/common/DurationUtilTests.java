package bjad.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * Unit Tests for the duration util. 
 *
 * @author 
 *   Ben Dougall
 */
public class DurationUtilTests
{
   /**
    * Tests the basic function of the duration 
    * util.
    */
   @Test
   public void testBasicMeasure()
   {
       DurationUtil milli = new DurationUtil();
       DurationUtil nano = new DurationUtil(true);
       
       milli.start();
       nano.start();
       try
       {
         Thread.sleep(1000L);
       }
       catch (InterruptedException ignore)
       {
       }
       nano.stop();
       milli.stop();
      
       assertThat(
             "duration in milli in seconds greater than or equal to 1", 
             new BigDecimal(milli.getSecondsString()).compareTo(BigDecimal.ONE) > -1, 
             is(true));
       assertThat(
             "duration in nano in seconds greater than or equal to 1", 
             new BigDecimal(nano.getSecondsString()).compareTo(BigDecimal.ONE) > -1, 
             is(true));
   }
   
   /**
    * Tests the util with set nanosecond values.
    */
   @Test
   public void testWithSetNanoValues()
   {
      DurationUtil util = new DurationUtil(true);
      util.endTime = 82618927552900L;
      util.startTime = 82617907767300L;

      assertThat(
            "duration in seconds is correct",
            util.getSecondsString(), 
            is("1.01979"));
      assertThat(
            "duration in milliseconds is correct",
            util.getMilliseconds(), 
            is(1019L));
      assertThat(
            "duration in milliseconds is correct",
            util.getNanoseconds(), 
            is(1019785600L));
   }
   
   /**
    * Tests the util with set millisecond values.
    */
   @Test
   public void testWithSetMilliValues()
   {
      DurationUtil util = new DurationUtil();
      util.endTime = 1605364695397L;
      util.startTime = 1605364694377L;

      assertThat(
            "duration in seconds is correct",
            util.getSecondsString(), 
            is("1.02000"));
      assertThat(
            "duration in milliseconds is correct",
            util.getMilliseconds(), 
            is(1020L));
      assertThat(
            "duration in milliseconds is correct",
            util.getNanoseconds(), 
            is(1020000000L));
   }
}
