package bjad.processor;

/**
 * Interface used by the object processor to transform an  
 * object into a different type of object
 *
 *
 * @author 
 *   Ben Dougall
 * @param <T> 
 *    The source type of object that will be used to transform
 *    into the new data type.
 * @param <R>  
 *    The destination data type based on the object provided
 *    to the transformer implementation.
 */
public interface IProcessorTransformer<T, R>
{
   /**
    * Method to implement in order to convert the object passed
    * into the intended object type, or null if the object should
    * not be transformed at all. 
    * 
    * @param obj
    *    The object to transform
    * @return
    *    The transformed object, or null if the transformation was 
    *    not needed.
    */
   public R transformAgainst(T obj);
}
