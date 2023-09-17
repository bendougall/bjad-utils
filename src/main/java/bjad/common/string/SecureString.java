package bjad.common.string;

import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Wrapper for keeping strings secured by encrypting them in memory, or 
 * to decrypt/encrypt strings from setting files or other external sources.
 *
 * @author 
 *   Ben Dougall
 */
public class SecureString
{   
   /**
    * The Algorithm to use for generating the secret key to
    * encrypt the string in the object.
    */
   protected char[] secretKeyFactoryAlgorithm = null;
   
   /**
    * The Algorithm used for the encryption base.
    */
   protected char[] encryptionAlgorithm = null;
   
   /**
    * The Algorithm to use as the cipher for encrypting the data.
    */
   protected char[] cipherAlgorithm = null;
   
   private IvParameterSpec ivspec = new IvParameterSpec(
         new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
   
   private byte[] encryptedValue;
   
   private SecretKeySpec secretKey = null;
   
   /**
    * Creates an empty secure string with a randomized
    * key and salt value. 
    */
   public SecureString()
   {
      this("", "", "");
   }
   
   /**
    * Creates a securestring for the value passed using 
    * a randomized key and salt value. Useful for securely
    * storing a password or sensitive information during 
    * the runtime for the application. 
    * 
    * @param value
    *    The string to encrypt.
    */
   public SecureString(String value)
   {
      this("", "", value);
   }
   
   /**
    * Creates an empty Secure String but sets the encryption
    * key and the salting value for future operations. 
    * 
    * @param key
    *    The encryption key to use. If empty, the key will be
    *    randomized.
    * @param salt
    *    The salting value to use. If empty, the salt value will be
    *    randomized.      
    */
   public SecureString(String key, String salt)
   {
      this(key, salt, "");
   }
   
   /**
    * Creates the secure string with the key, salt, and initial
    * value set within it. 
    * 
    * @param key
    *    The encryption key to use. If empty, the key will be
    *    randomized.
    * @param salt
    *    The salting value to use. If empty, the salt value will be
    *    randomized.
    * @param value
    *    The string to store securely within the object.
    */
   public SecureString(String key, String salt, String value)
   {
      initializeCryptoObjects(key, salt);
      setString(value);
   }
   
   /**
    * Provides the string stored securely within the object. 
    *  
    * @return
    *    The actual string stored in the object. 
    * @throws SecureStringException
    *    Any encryption exceptions will be thrown. If the value 
    *    if coming from an external source, the calling function 
    *    should be catching for this exception in case the value
    *    set was invalid. 
    */
   public String getString() throws SecureStringException
   {
      if (encryptedValue == null)
      {
         return "";
      }
      
      try
      {
         Cipher cipher = Cipher.getInstance(getStringFromCharArray(cipherAlgorithm, "AES/CBC/PKCS5PADDING"));
         cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
         return new String(cipher.doFinal(encryptedValue));
      }      
      catch (Exception ex)
      {
         throw new SecureStringException(ex);
      }
   }
   
   /**
    * Sets the string value that is to be stored securely 
    * in the object. 
    * @param value
    *    The value to store securely. 
    * @throws SecureStringException
    *    Any encryption exceptions will be thrown.
    */
   public void setString(String value) throws SecureStringException
   {
      if (value == null || value.trim().isEmpty())
      {
         encryptedValue = null;
         return;
      }
      
      try
      {
         Cipher cipher = Cipher.getInstance(getStringFromCharArray(cipherAlgorithm, "AES/CBC/PKCS5PADDING"));
         cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
         encryptedValue = cipher.doFinal(value.getBytes("UTF-8"));
      }      
      catch (Exception ex)
      {
         throw new SecureStringException(ex);
      }
   }
   
   /**
    * Returns the base64 string for the encrypted string 
    * in the object. 
    * 
    * @return 
    *    The base 64 string of the encrypted value, or empty
    *    string if nothing is stored. 
    */
   public String getBase64String()
   {
      if (encryptedValue == null || encryptedValue.length == 0)
      {
         return "";
      }
      return Base64.getEncoder().encodeToString(encryptedValue);
   }
   
   /**
    * Sets the encrypted value within the object from the 
    * base 64 value passed to the function. 
    * 
    * @param base64String  
    *    The base 64 to decode and store in the object
    * @return
    *    True if the base 64 string could be decoded and 
    *    stored, false otherwise.
    */
   public boolean setFromBase64String(String base64String)
   {
      if (base64String == null || base64String.trim().isEmpty())
      {
         encryptedValue = null;
      }
      else
      {
         try
         {
            encryptedValue = Base64.getDecoder().decode(base64String);
         }
         catch (Exception ex)
         {
            encryptedValue = null;
         }
      }
      return encryptedValue != null && encryptedValue.length > 0;
   }

   private String getStringFromCharArray(char[] val, String defaultValue)
   {
      String result = defaultValue;
      if (val != null && val.length > 0)
      {
         result = new String(val);
      }
      return result;
   }
   
   /**
    * Initializes the crypto objects for the secured string
    * with the key and salt to use. 
    * @param key
    *    The encryption key to use.
    * @param salt
    *    The salting value to use.
    * @throws SecureStringException
    *    Any exceptions initializing the encryption objects will be thrown.
    */
   protected void initializeCryptoObjects(String key, String salt)
   {
      try
      {
         char[] keyArray;
         
         if (key == null || key.trim().isEmpty())
         {
            keyArray = UUID.randomUUID().toString().toCharArray();
         }
         else
         {
            keyArray = key.toCharArray();
         }
         
         String saltValue = 
               (salt == null || salt.trim().isEmpty()) ? 
                     String.valueOf(System.nanoTime()) : 
                     salt;
         
         String method = getStringFromCharArray(secretKeyFactoryAlgorithm, "PBKDF2WithHmacSHA256");
      
         SecretKeyFactory factory = SecretKeyFactory.getInstance(method);
         KeySpec spec = new PBEKeySpec(keyArray, saltValue.getBytes(), 65536, 256);
         SecretKey temp = factory.generateSecret(spec);
         
         method = getStringFromCharArray(encryptionAlgorithm, "AES");
         secretKey = new SecretKeySpec(temp.getEncoded(), method);
         
         try
         {
            if (!temp.isDestroyed())
            {
               temp.destroy();
            }
         }
         catch (Exception ignore) 
         {
            // Do nothing, purely cleanup.
         }
      } 
      catch (Exception ex)
      {
         throw new SecureStringException(ex);
      }
   }
}
