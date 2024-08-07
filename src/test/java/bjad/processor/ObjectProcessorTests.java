package bjad.processor;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the ObjectProcessor functionality.
 *
 * @author 
 *   Ben Dougall
 */
public class ObjectProcessorTests
{
   protected BigDecimal NEGATIVE_ONE = new BigDecimal(-1);
   
   /**
    * Tests the filtering and the runAgainst logic in the object processor.
    */
   @Test
   public void testFilterAndRunAgainst()
   {
      ObjectProcessor<BigDecimal> processor = new ObjectProcessor<BigDecimal>();
      processor = processor.addObject(NEGATIVE_ONE).addObjects(BigDecimal.ZERO, BigDecimal.ONE);
      
      assertThat(
            "Entries added, 3 items returned.",
            processor.getUnmodifiableListOfObjectsToProcess().size(), 
            is(3));
      
      processor = processor.filterWith((o) -> o.compareTo(BigDecimal.ZERO) != -1);
      
      assertThat(
            "Negative entry was filtered, only 2 items remain", 
            processor.getUnmodifiableListOfObjectsToProcess().size(), 
            is(2));
      
      List<BigDecimal> numbersGreaterThan0 = processor.runAgainst((o) -> o.compareTo(BigDecimal.ONE) >= 0);
      
      assertThat(
            "Ran the processor to find the items whose value is greater than 0, 1 should be returned", 
            numbersGreaterThan0.size(), 
            is(1));
      
      assertThat(
            "First Item returned is 1", 
            numbersGreaterThan0.get(0), 
            is(BigDecimal.ONE));
   }
   
   /**
    * Tests the transforming object within the object processor.
    */
   @Test
   public void testTransformation()
   {      
      ObjectProcessor<BigDecimal> processor = new ObjectProcessor<BigDecimal>(BigDecimal.ONE, NEGATIVE_ONE);
      List<BigDecimal> results = processor.transformWith((t) -> t.multiply(NEGATIVE_ONE));
      
      assertThat(
            "Results after transform has a size of 2", 
            results.size() == 2, 
            is(true));
      
      assertThat(
            "First result made 1 into -1", 
            results.get(0).compareTo(NEGATIVE_ONE) == 0, 
            is(true));
      
      assertThat(
            "Second result made -1 into 1", 
            results.get(1).compareTo(BigDecimal.ONE) == 0, 
            is(true));
   }
}
