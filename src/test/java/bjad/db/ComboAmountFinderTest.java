package bjad.db;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import bjad.cboamount.ComboFinder;
import bjad.cboamount.ComboFinderElement;
import bjad.cboamount.FoundComboResultList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the combination amount finder.
 *
 * @author 
 *   Ben Dougall
 */
public class ComboAmountFinderTest
{
   static List<ComboFinderElement> smallSampleData = new ArrayList<>();
   
   static 
   {
      smallSampleData.add(new SampleAmountBean(new BigDecimal("1.00")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("10.00")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("5.00")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("9.00")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("15.00")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("1.01")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("2.02")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("3.3")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("1.96")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("5.56")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("100.01")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("26.02")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("39.3")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("12.46")));
      smallSampleData.add(new SampleAmountBean(new BigDecimal("54.56")));
   }
   
   /**
    * no elements test
    */
   @Test
   public void testFoundComboResultNoNullLogic()
   {
      TestingFoundComboResult resultList = new TestingFoundComboResult();
      assertNotNull(resultList.getComboElements(), "Initial List should not be null");
      
      // Now explictly set to null.
      resultList.setComboElements(null);
      assertNotNull(resultList.getComboElements(), "Even after explict setting of null, List should not be null");      
   }
   
   /**
    * no match test
    */
   @Test
   public void testNoMatches()
   {
      List<FoundComboResultList> results = new ComboFinder(smallSampleData, new BigDecimal("1000.00")).findCombinationsForAmount();
      assertEquals(0, results.size(), "Searching for 1000 should have no results");
   }
   
   /**
    * one combination result test
    */
   @Test
   public void testOneMatch()
   {
      List<FoundComboResultList> results = new ComboFinder(smallSampleData, new BigDecimal("12.46")).findCombinationsForAmount();
      assertEquals(1, results.size(), "Searching for 12.46 should have 1 result.");
   }
   
   /**
    * two combination result test
    */
   @Test
   public void testTwoMatches()
   {
      List<FoundComboResultList> results = new ComboFinder(smallSampleData, new BigDecimal("10.00")).findCombinationsForAmount();
      assertEquals(2, results.size(), "Searching for 10.00 should have 2 result.");
   }
}

class TestingFoundComboResult extends FoundComboResultList
{
   void setComboElements(List<ComboFinderElement> comboElements)
   {
      this.comboElements = comboElements;
   }
}
