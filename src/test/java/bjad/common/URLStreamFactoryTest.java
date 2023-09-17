package bjad.common;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test cases for the URLStreamFactory class.
 *
 * @author 
 *  Ben Dougall
 */
@SuppressWarnings("javadoc")
public class URLStreamFactoryTest
{
   private static final String CP_TEST_FILE_NAME = "URLSteamFactory.cp.sample.txt";
   
   @Test
   public void testClassPathFile()
   {
      // Stupid coverage expects the CTOR to be called for some stupid
      // reason, so here is a call for no reason whatsoever.
      new URLStreamFactory();
      
      try (InputStream is = URLStreamFactory.getStream("classpath:///" + CP_TEST_FILE_NAME))
      {         
         assertEquals(BufferedInputStream.class, is.getClass(), "Classpath resource should return BufferedInputStream");
      }
      catch (IOException ex)
      {
         fail(CP_TEST_FILE_NAME + " with the classpath url prefix should have returned something");
      }
      
      try (InputStream is = URLStreamFactory.getStream(CP_TEST_FILE_NAME))
      {    
         assertEquals(BufferedInputStream.class, is.getClass(), "Classpath resource should return BufferedInputStream");
      }
      catch(IOException ex)
      {
         fail(CP_TEST_FILE_NAME + " without the classpath url prefix should have returned something");
      }
      
      try (InputStream is = URLStreamFactory.getStream("classpath:///superfakefile.classpathfailure"))
      {    
         fail("classpath:///superfakefile.classpathfailure should have caused an IOException.");
      }
      catch(IOException ex)
      {        
      }
   }
   
   @Test
   public void testFileStream()
   {
      File tempDir = new File(System.getenv("TEMP"));
      File tempFile = new File(tempDir, "URLTest.sample." + System.currentTimeMillis());
      try
      {
         tempFile.createNewFile();
      }
      catch (IOException ex1)
      {
         fail("Could not create test file: " + tempFile.getAbsolutePath());
      }
      
      try
      {         
         try (InputStream is = URLStreamFactory.getStream(tempFile.toURI().toString()))
         {    
            assertEquals(BufferedInputStream.class, is.getClass(), "File resource using URL should return FileInputStream");
         }
         catch(IOException ex)
         {
            ex.printStackTrace();
            fail("A file the test case creates should be available to the Stream Factory");
         }
         
         try (InputStream is = URLStreamFactory.getStream(tempFile.getAbsolutePath()))
         {    
            assertEquals(FileInputStream.class, is.getClass(), "File resource should return FileInputStream");
         }
         catch(IOException ex)
         {
            fail("A file the test case creates should be available to the Stream Factory");
         }
      }
      finally
      {
         tempFile.delete();
      }
   }

   @Test
   public void testHTTPConnection()
   {
      try (InputStream is = URLStreamFactory.getStream("http://www.google.com"))
      {         
      }
      catch (IOException ex)
      {
         fail("Should be able to open http stream to http://www.google.com");
         ex.printStackTrace();
      }
      
      try (InputStream is = URLStreamFactory.getStream("http://localhost:34567"))
      {   
         fail("Stream to localhost, port 34567 should fail.");
      }
      catch (IOException ex)
      {
      }
   }
}
