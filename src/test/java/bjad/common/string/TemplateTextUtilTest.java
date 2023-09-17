package bjad.common.string;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test utility for the TemplateTextUtil object. 
 *
 * @author 
 *  Ben Dougall
 */
@SuppressWarnings("javadoc")
public class TemplateTextUtilTest
{
   private static final String TEMPLATE_CONTENTS_DEFAULT_ENDINGS = 
         "This is a test. You can also use a custom format, like tpl:so";
   
   private static final String TEMPLATE_CONTENTS_CUSTOM_ENDINGS = 
         "This is a {{text}}. You can also use a custom format, like test";
   
   private static final String TEMPLATE_CONTENTS_NO_REPLACEMENTS = 
         "This is a {{text}}. You can also use a custom format, like tpl:so";
   
   private static final String TEMPLATE_FILE_URL = 
         "classpath:///TemplateText.sample.txt";
   
   @Test
   public void testDefaultTemplateEndings() throws Exception
   {
      TemplateTextUtil util = new TemplateTextUtil(TEMPLATE_FILE_URL);
      HashMap<String, String> replacements = new HashMap<>();
      replacements.put("text", "test");
      
      assertTrue(TEMPLATE_CONTENTS_DEFAULT_ENDINGS.equals(util.getTemplateText(replacements)), "CUSTOM Endings Text should match");
   }

   @Test
   public void testCustomTemplateEndings() throws Exception
   {
      TemplateTextUtil util = new TemplateTextUtil(TEMPLATE_FILE_URL, "tpl:", "");
      HashMap<String, String> replacements = new HashMap<>();
      replacements.put("so", "test");
      
      assertTrue(
            TEMPLATE_CONTENTS_CUSTOM_ENDINGS.equals(util.getTemplateText(replacements)),
            "CUSTOM Endings Text should match");
   }
   
   @Test
   public void testNullReplacements() throws Exception
   {
      TemplateTextUtil util = new TemplateTextUtil(TEMPLATE_FILE_URL, "tpl:", "");
      
      assertTrue(
            TEMPLATE_CONTENTS_NO_REPLACEMENTS.equals(util.getTemplateText(null)),
            "CUSTOM Endings Text should match");
   }
   
   @Test
   public void testBadPage() throws Exception
   {
      assertThrows(IOException.class, () -> 
      {
         new TemplateTextUtil("http://wakawaka.fake.error", "tpl:", "");
      });
   }
   
   @Test
   public void testBadFile() throws Exception
   {
      assertThrows(IOException.class, () -> 
      {
         new TemplateTextUtil("classpath:///TemplateText.missing.txt", "text", "replacement");
      });
   }
   
}
