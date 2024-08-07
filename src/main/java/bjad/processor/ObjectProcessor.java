package bjad.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * Centralized class that will process a collection of objects 
 * that can be compared and/or filtered against implementation(s)
 * of AbstractProcessorRule or transformed using implementation(s)
 * of AbstractProcessorTransformer
 *    
 * @author 
 *    Ben Dougall
 *    
 * @param <T>
 *    The type of object that will be processed against the rules
 *    and/or transformers implementing their abstract classes with
 *    that type.  
 */ 
public class ObjectProcessor<T>
{
   /**
    * The list of objects the processor will filter/process/transform
    * via the options it provides. 
    */
   protected List<T> objectsToProcess;
   
   /**
    * Default constructor, initializing the objects to process 
    * list as as a blank list.
    */
   public ObjectProcessor()
   {
      objectsToProcess = new ArrayList<T>();
   }
   
   /**
    * Constructor, setting the initial object(s) within the 
    * list of objects to be processed. 
    * 
    * @param objectsToProcess
    *    The initial object(s) to add to the processing list.
    */
   public ObjectProcessor(@SuppressWarnings("unchecked") T... objectsToProcess)
   {
      this(Arrays.asList(objectsToProcess));
   }
   
   /**
    * Constructor, setting the initial list of objects within  
    * the list of objects to be processed. 
    * 
    * @param objectsToProcess
    *    The initial list of objects to add to the processing list.
    */
   public ObjectProcessor(List<T> objectsToProcess)
   {
      this.objectsToProcess = new ArrayList<T>();
      this.objectsToProcess.addAll(objectsToProcess);
   }
   
   /**
    * Adds an object to the list of objects to process. 
    * @param objectToProcess
    *    The object to add to the list to process.
    * @return
    *    The copy of the processor with the new object
    *    added to the list of items to be processed.
    */
   public final ObjectProcessor<T> addObject(T objectToProcess)
   {
      ObjectProcessor<T> newCopy = new ObjectProcessor<T>();
      
      newCopy.objectsToProcess.addAll(this.objectsToProcess);
      newCopy.objectsToProcess.add(objectToProcess);
      
      return newCopy;
   }
   
   /**
    * Adds object(s) to the list of objects to process. 
    * @param objectsToProcess
    *    The object(s) to add to the list to process.
    * @return
    *    The copy of the processor with the new object(s)
    *    added to the list of items to be processed.
    */
   public final ObjectProcessor<T> addObjects(@SuppressWarnings("unchecked") T... objectsToProcess)
   {
      return addObjects(Arrays.asList(objectsToProcess));
   }
   
   /**
    * Adds objects to the list of objects to process. 
    * @param objectsToProcess
    *    The objects to add to the list to process.
    * @return
    *    The copy of the processor with the new object(s)
    *    added to the list of items to be processed.
    */
   public final ObjectProcessor<T> addObjects(Collection<T> objectsToProcess)
   {
      ObjectProcessor<T> newCopy = new ObjectProcessor<T>();
      
      newCopy.objectsToProcess.addAll(this.objectsToProcess);
      newCopy.objectsToProcess.addAll(objectsToProcess);
      
      return newCopy;
   }
   
   /**
    * Runs the collection of objects against the rule(s) provided 
    * to return a collection of objects who result in "PASSING".
    * 
    * @param rules
    *    The rule(s) to process against. 
    * @return
    *    The list of objects that "PASS" the rule(s) provided.
    */
   @SafeVarargs
   public final List<T> runAgainst(Predicate<T>... rules)
   {
      return runAgainst(Arrays.asList(rules));
   }
   
   /**
    * Runs the collection of objects against the rule(s) provided 
    * to return a collection of objects who result in "PASSING".
    * 
    * @param rules
    *    The rule(s) to process against. 
    * @return
    *    The list of objects that "PASS" the rule(s) provided.
    */
   public final List<T> runAgainst(List<Predicate<T>> rules)
   {
      final ArrayList<T> results = new ArrayList<T>();
      for (T o : objectsToProcess)
      {
         for (Predicate<T> rule : rules)
         {
            if (rule.test(o))
            {
               results.add(o);
               break;
            }
         }
      }
      return results;
   }
   
   /**
    * Filters the collection of object in the processor against the 
    * rule(s) provided, returning a copy of the ObjectProcessor whose
    * object collection only has the filtered objects in it. 
    * 
    * @param rules
    *    The rule(s) to filter the object collection against. 
    * @return
    *    The ObjectProcessor with the collection of objects being 
    *    set to filtered objects passing the rule(s) passed.
    */
   @SafeVarargs
   public final ObjectProcessor<T> filterWith(Predicate<T>... rules)
   {
      return filterWith(Arrays.asList(rules));
   }
   
   /**
    * Filters the collection of object in the processor against the 
    * rule(s) provided, returning a copy of the ObjectProcessor whose
    * object collection only has the filtered objects in it. 
    * 
    * @param rules
    *    The rule(s) to filter the object collection against. 
    * @return
    *    The ObjectProcessor with the collection of objects being 
    *    set to filtered objects passing the rule(s) passed.
    */
   public final ObjectProcessor<T> filterWith(List<Predicate<T>> rules)
   {
      ObjectProcessor<T> newCopy = new ObjectProcessor<T>();
      
      newCopy.objectsToProcess.addAll(this.runAgainst(rules));
      
      return newCopy; 
   }
   
   /**
    * Returns a list of objects that were transformed with the tranformer(s) provided
    * using the objects in the processor as the source material to transform from.
    * 
    * @param <R>
    *    The type of object the objects in the processor will be transformed into if 
    *    the transformer(s) do not return null.  
    * @param transformers
    *    The transformer(s) to use to convert the objects in the processor to the 
    *    result type specified.
    * @return
    *    The list of transformed object(s) if the transformer did not return null.
    */
   @SafeVarargs
   public final <R> List<R> transformWith(IProcessorTransformer<T, R>... transformers)
   {
      return transformWith(Arrays.asList(transformers));
   }
   
   /**
    * Returns a list of objects that were transformed with the tranformer(s) provided
    * using the objects in the processor as the source material to transform from.
    * 
    * @param <R>
    *    The type of object the objects in the processor will be transformed into if 
    *    the transformer(s) do not return null.  
    * @param transformers
    *    The transformer(s) to use to convert the objects in the processor to the 
    *    result type specified.
    * @return
    *    The list of transformed object(s) if the transformer did not return null.
    */
   public final <R> List<R> transformWith(final List<IProcessorTransformer<T, R>> transformers)
   {
      final ArrayList<R> results = new ArrayList<R>();
      objectsToProcess.forEach((o) -> {
        transformers.forEach((t) -> {
           R result = t.transformAgainst(o);
           if (result != null)
           {
              results.add(result);
           }
        });
      });
      
      return results;
   }
   
   /**
    * Provides the list of objects to process stored by the 
    * processor in an unmodifiable list to ensure the integrity
    * of the current processor. 
    * 
    * @return  
    *    The objects set to be processed by the processor as
    *    an Unmodifiable List. 
    */
   public final List<T> getUnmodifiableListOfObjectsToProcess()
   {
      return Collections.unmodifiableList(objectsToProcess);
   }
}
