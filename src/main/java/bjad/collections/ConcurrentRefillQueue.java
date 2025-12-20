package bjad.collections;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

/**
 * Concurrent Queue implementation that has the ability
 * to trigger refilling operations when items are removed
 * from the queue as long as a threshold is set and a 
 * refilling implementation is applied to the queue.
 *
 * @param <T>
 *    The object type stored within the collection 
 * @author 
 *   Ben Dougall
 */
public class ConcurrentRefillQueue<T> extends ConcurrentLinkedQueue<T>
{
   private static final long serialVersionUID = -113978505950530512L;

   /**
    * The element count threshold that when the queue's size is 
    * equal to or less than it, the refill operation will be triggered.
    */
   protected int refillThreshold; 
   /**
    * The refiller implementation to use to refill the queue once the
    * element count mets or is lower than the threshold count.
    */
   protected ICollectionRefiller<T> refiller;
   /**
    * Flag used to determine if a refill operation is in progress
    * or not.
    */
   protected boolean refillInProgress = false;
   
   /**
    * Default constructor with no initial elements, threshold, or
    * a refiller set within it. 
    */
   public ConcurrentRefillQueue()
   {
      super();
      this.setRefillThreshold(-1);
      this.setRefiller(null);
   }

   /**
    * Constructor with initial elements from the collection passed
    * but no threshold or refiller implementation applied to it.
    * @param c
    *    The collection with the initial elements to add to the queue.
    */
   public ConcurrentRefillQueue(Collection<? extends T> c)
   {
      super(c);
      this.setRefillThreshold(-1);
      this.setRefiller(null);
   }

   /**
    * Constructor setting the refill threshold and the refiller implementation 
    * the queue will use but without any initial elements added to the queue.
    * @param refillThreshold
    *    The threshold to trigger the auto-refill at when items are removed
    *    or cleared from the queue. 
    * @param refiller
    *    The refiller implementation to perform the refill operations with.
    */
   public ConcurrentRefillQueue(int refillThreshold, ICollectionRefiller<T> refiller)
   {
      super();
      this.setRefillThreshold(refillThreshold);
      this.setRefiller(refiller);
   }
   
   /**
    * Constructor setting the refill threshold and the refiller implementation 
    * the queue will use as well as the initial items within the queue. 
    * @param c
    *    The collection with the initial elements to add to the queue.
    * @param refillThreshold
    *    The threshold to trigger the auto-refill at when items are removed
    *    or cleared from the queue. 
    * @param refiller
    *    The refiller implementation to perform the refill operations with.
    */
   public ConcurrentRefillQueue(Collection<? extends T> c, int refillThreshold, ICollectionRefiller<T> refiller)
   {
      super(c);
      this.setRefillThreshold(refillThreshold);
      this.setRefiller(refiller);
   }

   /**
    * Returns the value of the ConcurrentRefillQueue instance's 
    * refillThreshold property.
    *
    * @return 
    *   The value of refillThreshold
    */
   public int getRefillThreshold()
   {
      return this.refillThreshold;
   }

   /**
    * Sets the value of the ConcurrentRefillQueue instance's 
    * refillThreshold property.
    *
    * @param refillThreshold 
    *   The value to set within the instance's 
    *   refillThreshold property
    */
   public void setRefillThreshold(int refillThreshold)
   {
      this.refillThreshold = refillThreshold;
   }

   /**
    * Returns the value of the ConcurrentRefillQueue instance's 
    * refiller property.
    *
    * @return 
    *   The value of refiller
    */
   public ICollectionRefiller<T> getRefiller()
   {
      return this.refiller;
   }

   /**
    * Sets the value of the ConcurrentRefillQueue instance's 
    * refiller property.
    *
    * @param refiller 
    *   The value to set within the instance's 
    *   refiller property
    */
   public void setRefiller(ICollectionRefiller<T> refiller)
   {
      this.refiller = refiller;
   }

   /**
    * Returns the value of the ConcurrentRefillQueue instance's 
    * refillInProgress property.
    *
    * @return 
    *   The value of refillInProgress
    */
   public boolean isRefillInProgress()
   {
      return this.refillInProgress;
   }

   /**
    * Sets the value of the ConcurrentRefillQueue instance's 
    * refillInProgress property.
    *
    * @param refillInProgress 
    *   The value to set within the instance's 
    *   refillInProgress property
    */
   public void setRefillInProgress(boolean refillInProgress)
   {
      this.refillInProgress = refillInProgress;
   }

   /**
    * <p>
    * Retrieves the first element in the queue and triggers the 
    * refill if polling for the element pushes the queue to the 
    * or below the threshold.
    * </p>
    * <p>
    * If the queue is empty and a refill operation is currently 
    * in progress. 
    * </p>
    * 
    * @return 
    *    The first element from the queue, or null if no elements 
    *    are found and a refill operation is not currently executing.
    */
   @Override
   public T poll()
   {
      // See if the queue is empty so we can 
      // wait for any ongoing refill operations 
      // to be complete before doing the removal
      // from the queue.
      if (this.isEmpty())
      {
         waitForRefillToComplete();
      }
      
      T retVal = super.poll();
      
      // Following the elements in the queue having 
      // its element count lowered, see if the 
      // refill is needed.
      triggerRefillIfNeeded();
      return retVal;
   }
   
   /**
    * Removes the passed object from the queue if its found, triggering
    * the refill if need be.
    * 
    * @param o
    *    The object to find and remove from the queue.
    * @return
    *    True if the object was found and removed, false
    *    otherwise
    */
   @Override
   public boolean remove(Object o)
   {
      boolean retVal = super.remove(o);
      
      if (retVal)
      {
         // Following the elements in the queue having 
         // its element count lowered, see if the 
         // refill is needed.
         triggerRefillIfNeeded();
      }
      
      return retVal;
   }

   /**
    * Returns the first element from the queue if one
    * is found, and removes it from the queue. Triggers
    * refresh if needed. 
    * 
    * @return
    *    The first element from the queue that gets removed
    *    as well.
    * @throws NoSuchElementException
    *    If the queue is empty, the exception is thrown.
    */
   @Override
   public T remove() throws NoSuchElementException
   {
      // See if the queue is empty so we can 
      // wait for any ongoing refill operations 
      // to be complete before doing the removal
      // from the queue.
      if (this.isEmpty())
      {
         waitForRefillToComplete();
      }
      
      T retVal = super.remove();
      
      // Following the elements in the queue having 
      // its element count lowered, see if the 
      // refill is needed.
      triggerRefillIfNeeded();
      
      return retVal;
   }

   /**
    * Removes the items from the collection passed 
    * from the queue.
    * 
    * @param c
    *    The collection of objects to remove from the 
    *    queue
    * @return
    *    True if the queue elements were updated, false
    *    if the queue remains the same.   
    */
   @Override
   public boolean removeAll(Collection<?> c)
   {
      boolean retVal = super.removeAll(c);
      
      if (retVal)
      {
         // Following the elements in the queue having 
         // its element count lowered, see if the 
         // refill is needed.
         triggerRefillIfNeeded();
      }
      
      return retVal;
   }

   /**
    * Removes the items from the queue that match the 
    * predicate passed. 
    * 
    * @param filter
    *    The predicate used to filter out which items to
    *    remove from the queue
    * @return
    *    True if items were removed, false if no items 
    *    were removed
    */
   @Override
   public boolean removeIf(Predicate<? super T> filter)
   {
      boolean retVal = super.removeIf(filter);
      if (retVal)
      {
         // Following the elements in the queue having 
         // its element count lowered, see if the 
         // refill is needed.
         triggerRefillIfNeeded();
      }
      return retVal;
   }

   /**
    * Returns if a refresh needs to be trigger based 
    * on the threshold being set and the item count
    * is equal to or less than the threshold set.
    * 
    * @return
    *    True if the refill needs to be triggered or
    *    not. 
    */
   protected boolean isRefillNeeded()
   {
      return 
            this.refillThreshold > -1 &&          // Make sure threshold is set 
            this.size() <= this.refillThreshold;  // See if the current count is at or less than threshold.
   }
   
   /**
    * Triggers the refill for the collection 
    * if the conditions of isRefillNeeded() are 
    * met, the refill implementation is set within
    * the objects, and the refill operation is current
    * not in progress.
    */
   protected void triggerRefillIfNeeded()
   {
      if (isRefillNeeded() && refiller != null && !isRefillInProgress())
      {
         setRefillInProgress(true);
         try
         {
            this.addAll(refiller.refillCollection());
         }
         finally
         {
            setRefillInProgress(false);
         }
      }
   }
   
   /**
    * Makes the current thread wait for the refill in 
    * progress flag to be false so we can wait in case 
    * the object is out of elements when remove or poll
    * functions are called.
    */
   protected void waitForRefillToComplete()
   {
      while (isRefillInProgress())
      {
         try 
         { 
            // Add a little delay so we don't spike the CPU usage.
            Thread.sleep(50L); 
         }
         catch (InterruptedException ex) 
         {
            // do nothing as this should never happen.
         }
      }
   }
}
