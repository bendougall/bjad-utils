package bjad.collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

/**
 * Unit Tests for the duration util. 
 *
 * @author 
 *   Ben Dougall
 */
public class ConcurrentRefillQueueTests implements ICollectionRefiller<Long>
{
   private static int ELEMENT_COUNT_FOR_REFILL = 50;
   protected boolean refillRun = false;
   
   @SuppressWarnings("javadoc")
   @Test
   public void testConstructors()
   {
      ConcurrentRefillQueue<Long> collection = new ConcurrentRefillQueue<Long>();
      assertThat("No default threshold", collection.getRefillThreshold() < 0,  is(true));
      assertThat("No refiller", collection.getRefiller() == null, is(true));
      assertThat("No initial elements", collection.isEmpty(), is(true));
      
      collection = new ConcurrentRefillQueue<Long>(refillCollection());
      assertThat("No default threshold", collection.getRefillThreshold() < 0,  is(true));
      assertThat("No refiller", collection.getRefiller() == null, is(true));
      assertThat(ELEMENT_COUNT_FOR_REFILL + " initial elements", collection.size() == ELEMENT_COUNT_FOR_REFILL, is(true));
      
      collection = new ConcurrentRefillQueue<Long>(10, this);
      assertThat("Threshold set", collection.getRefillThreshold() == 10, is(true));
      assertThat("Refiller set", collection.getRefiller() != null, is(true));
      assertThat("No initial elements", collection.isEmpty(), is(true));
      
      collection = new ConcurrentRefillQueue<Long>(refillCollection(), 10, this);
      assertThat("Threshold set", collection.getRefillThreshold() == 10, is(true));
      assertThat("Refiller set", collection.getRefiller() != null, is(true));
      assertThat(ELEMENT_COUNT_FOR_REFILL + " initial elements", collection.size() == ELEMENT_COUNT_FOR_REFILL, is(true));
   }
   
   @SuppressWarnings("javadoc")
   @Test
   public void testRemovals()
   {
      ConcurrentRefillQueue<Long> collection = new ConcurrentRefillQueue<Long>(refillCollection(), 49, this);
      refillRun = false;
      collection.remove();
      collection.poll();
      assertThat("Refiller ran", this.refillRun, is(true));
      
      collection = new ConcurrentRefillQueue<Long>(refillCollection());
      refillRun = false;
      collection.remove();
      collection.poll();
      assertThat("Refiller did not run", this.refillRun, is(false));
      
      collection.removeAll(refillCollection());
      collection.removeIf(t -> t < 0L);
      collection.removeIf(t -> t > 0L);
      
      collection.add(100L);
      collection.remove(100L);
   }
   
   @SuppressWarnings("javadoc")
   @Test
   public void testInProgress()
   {
      ICollectionRefiller<Long> refiller = () ->
      {
         ArrayList<Long> items = new ArrayList<Long>();
         for (int i = 0; i != 2; ++i)
         {
            try 
            { 
               // Add a little delay so we don't spike the CPU usage.
               Thread.sleep(100L); 
            }
            catch (InterruptedException ex) 
            {
               // do nothing as this should never happen.
            }
            items.add(System.nanoTime());
         }
         return items;
      };
      
      ConcurrentRefillQueue<Long> collection = new ConcurrentRefillQueue<Long>(1, refiller);
      collection.add(100L);

      new Thread(()->collection.remove()).start();
      
      collection.remove();
      
      ConcurrentRefillQueue<Long> collection2 = new ConcurrentRefillQueue<Long>(1, refiller);
      collection2.add(100L);

      new Thread(()->collection2.poll()).start();
      
      collection2.poll();
      
   }

   @Override
   public Collection<Long> refillCollection()
   {
      ArrayList<Long> items = new ArrayList<Long>();
      for (int i = 0; i != ELEMENT_COUNT_FOR_REFILL; ++i)
      {
         items.add(System.nanoTime());
      }
      refillRun = true;
      return items;
   }
}
