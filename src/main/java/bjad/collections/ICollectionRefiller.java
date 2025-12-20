package bjad.collections;

import java.util.Collection;

/**
 * Interface to implement in order for the refilling collections
 * to refill their data when a threshold is met while taking 
 * items out of the collection.
 *
 * @author 
 *   Ben Dougall
 * @param <T>
 *    The type of object that will be retrieved for the 
 *    refill operation. 
 */
public interface ICollectionRefiller<T>
{
   /**
    * Method to implement in order to retrieve elements
    * for a refilling collection. 
    * 
    * @return
    *    The items to refill the refilling collection 
    *    with.
    */
   Collection<T> refillCollection();
}
