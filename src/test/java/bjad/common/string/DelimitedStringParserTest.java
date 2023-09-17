package bjad.common.string;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test cases for the Delimited String Parser 
 * object.
 *
 * @author 
 *  Ben Dougall
 */
@SuppressWarnings("javadoc")
public class DelimitedStringParserTest
{
   private static DelimitedStringParser parser = new DelimitedStringParser
         ("a,\"fir,st\",second,,3", ',');

   @Test
   public void testNumberOfSections()
   {
      assertEquals(5, parser.getNumberOfSections(), "Number of sections");
   }

   @Test
   public void testSections()
   {
      assertEquals("fir,st", parser.section(1), "Section 2");
      assertEquals("second", parser.section(2), "Section 3");
      assertEquals(true, parser.section(3).isEmpty(), "Section 4 empty?");
   }
   
   @Test
   public void testSectionsAgain()
   {
      DelimitedStringParser parser = new DelimitedStringParser("a,\"fir,st\",second,,", ',');
      assertEquals(5, parser.getNumberOfSections(), "Number of sections");      
   }
}
