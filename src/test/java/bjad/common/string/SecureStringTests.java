package bjad.common.string;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;


/**
 * (Description)
 *
 *
 * @author 
 *   bendo
 */
public class SecureStringTests
{
   /**
    * Tests the object using the randomized key and salt 
    * constructor.
    */
   @Test
   public void testRandomKeyAndSalt()
   {
      SecureString ss = new SecureString("Hi");
      assertThat("Constructor set value and retrieved it correctly", ss.getString(), is("Hi"));
      
      ss.setString("Bye");
      assertThat("setString worked and retrieved it correctly", ss.getString(), is("Bye"));
      
      ss = new SecureString();
      assertThat("Default Constructor returns blank", ss.getString(), is(""));
      
      ss.setString(null);
      assertThat("SetString with null returns blank", ss.getString(), is(""));
      assertThat("SetString with null returns blank for base64", ss.getBase64String(), is(""));
      
      ss.setString("");
      assertThat("SetString with blank string returns blank", ss.getString(), is(""));
      assertThat("SetString with blank returns blank for base64", ss.getBase64String(), is(""));
      
      ss = new SecureString(null, null);
      assertThat("Default Constructor returns blank", ss.getString(), is(""));
      ss.setString("Hi Again");
      assertThat("setString worked and retrieved it correctly", ss.getString(), is("Hi Again"));
      
      ss.setFromBase64String(null);
      assertThat("SetString with null returns blank", ss.getString(), is(""));
      assertThat("SetString with null returns blank for base64", ss.getBase64String(), is(""));
      
      ss.setFromBase64String("");
      assertThat("SetString with blank string returns blank", ss.getString(), is(""));
      assertThat("SetString with blank returns blank for base64", ss.getBase64String(), is(""));
   }
   
   /**
    * Tests the object using set keys and salt values in the constructor
    */
   @Test
   public void testSetKeyAndSaltConstructor()
   {
      SecureString ss = new SecureString("Key", "Salt", "Value");
      assertThat("Constructor set value and retrieved it correctly", ss.getString(), is("Value"));
      assertThat("Base64 generation creates the correct string", ss.getBase64String(), is("obaroZbL+7Fx2Hg6tIScZQ=="));
      
      ss.setString("Another Value");
      assertThat("setString worked and retrieved it correctly", ss.getString(), is("Another Value"));
   }
   
   /**
    * Tests the decryption from the base 64 string. 
    */
   @Test
   public void testSetKeyAndSaltWithSetFromBase64()
   {
      SecureString ss = new SecureString("Key", "Salt");
      assertThat("True is returned from setFromBase54String()", ss.setFromBase64String("obaroZbL+7Fx2Hg6tIScZQ=="), is(true));
      assertThat("Decrypted string retrieved correctly", ss.getString(), is("Value"));
   }
   
   /**
    * Tests the secure string exception
    */
   @Test
   public void testException()
   {
      Exception inner = new Exception("Inner");
      SecureStringException ssException = new SecureStringException(inner);
      assertThat("Inner Exception set correctly", ssException.getCause(), is(inner));
   }
   
   /**
    * Tests the overridden algorithms given bad values will 
    * cause exceptions.
    */
   @Test
   public void testBadKeys()
   {
      try
      {
         SecureString ss = new SecureString();
         ss.secretKeyFactoryAlgorithm = "BLAH".toCharArray();
         ss.initializeCryptoObjects("", "");
         ss.setString("HI");
         fail("Bad secret key should have thrown exception");
      }
      catch (SecureStringException e)
      {
         
      }
      
      try
      {
         SecureString ss = new SecureString();
         ss.cipherAlgorithm = "BLAH".toCharArray();
         ss.initializeCryptoObjects("", "");
         ss.setString("HI");
         fail("Bad cipher should have thrown exception");
      }
      catch (SecureStringException e)
      {
         
      }
      
      try
      {
         SecureString ss = new SecureString();
         ss.encryptionAlgorithm = "BLAH".toCharArray();
         ss.initializeCryptoObjects("", "");
         ss.setString("HI");
         fail("Bad encryption should have thrown exception");
      }
      catch (SecureStringException e)
      {
         
      }
      
      try
      {
         SecureString ss = new SecureString();
         ss.setString("HI");
         ss.initializeCryptoObjects("sss", "ss");
         ss.getString();
         
         
         fail("Bad encryption should have thrown exception");
      }
      catch (SecureStringException e)
      {
         
      }
      
      try
      {
         SecureString ss = new SecureString("Key", "Salt", "Value");
         ss.setFromBase64String("obaroZbL+7Fx2Hg6tI5cZQ==");
         ss.getString();
         fail("Bad encryption should have thrown exception");
      }
      catch (SecureStringException e)
      {
         
      }
      
      try
      {
         SecureString ss = new SecureString("Key", "Salt", "Value");
         ss.setFromBase64String("obaroZbL+7Fx2Hg6tI~*ZQ==");
         ss.getString();
      }
      catch (SecureStringException e)
      {
         
      }
   }
}
