package bjad.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Main processor/framework class that will be used to populate 
 * the steps within the framework and to trigger the execution
 * to populate the data model passed initially to the framework.
 *
 * @author 
 *    Ben Dougall
 *   
 * @param <T>
 *    The type of object to populate and pass between 
 *    the steps. 
 */
public class StepExecutor<T>
{
   /**
    * The available steps to execute within the framework.
    */
   protected Map<String, IStep<T>> availableSteps = new HashMap<String, IStep<T>>();
   
   /**
    * Adds a possible execution step to the processor framework
    * @param name
    *    The name of the step (trimmed and converted to uppercase automatically)
    * @param step
    *    The step to execute. 
    */
   public void addStep(String name, IStep<T> step)
   {
      if (!stringNotNullOrWhitespace(name))
      {
         throw new IllegalArgumentException("Name cannot be null or an empty string.");
      }
      if (step == null)
      {
         throw new IllegalArgumentException("Step cannot be null.");
      }
      
      // Add in the step with the name trimmed and converted to uppercase. 
      this.availableSteps.put(name.trim().toUpperCase(), step);
   }
   
   /**
    * Removes the step with the name that matches the name passed, returning
    * the step if one is found. 
    * 
    * @param name
    *    The name of the step to remove. 
    * @return
    *    The step that was removed, or null if a step with the name was not found. 
    */
   public IStep<T> removeStepByName(String name)
   {
      return availableSteps.remove(name.trim().toUpperCase());
   }
   
   /**
    * Removes all the steps that is an instance of the class provided 
    * to the function. 
    * 
    * @param classToRemove
    *    The class to remove its instances of from the available steps
    * @return
    *    The list of steps removed.
    */
   public List<Entry<String, IStep<T>>> removeStepsByClass(Class<? extends IStep<T>> classToRemove)
   {
      List<Entry<String, IStep<T>>> stepsRemoved = new ArrayList<Entry<String, IStep<T>>>();
      if (classToRemove != null)
      {
         Iterator<Entry<String, IStep<T>>> stepIter = availableSteps.entrySet().iterator();
         while (stepIter.hasNext())
         {
            Entry<String, IStep<T>> step = stepIter.next();
            if (classToRemove.isInstance(step.getValue()))
            {
               stepsRemoved.add(step);
               stepIter.remove();
            }
         }
      }
      return stepsRemoved;
   }
   
   /**
    * Executes the step processing framework, starting with the data bean 
    * passed, executing the step implementation provided.
    * 
    * @param initialBean
    *    The initial bean to start the framework with.
    * @param firstStep
    *    The first step to execute.
    * @return
    *    The data bean populated via the step executions. 
    */
   public T execute(T initialBean, IStep<T> firstStep)
   {
      IStep<T> nextStep = firstStep;
      
      while (nextStep != null)
      {
         String resultName = nextStep.executeStep(initialBean);
         if (stringNotNullOrWhitespace(resultName) && !resultName.equalsIgnoreCase(IStep.FINAL_STEP))
         {
            nextStep = availableSteps.get(resultName.trim().toUpperCase());
         }
         else
         {
            nextStep = null;
         }
      } 
      
      return initialBean;
   }
   
   /**
    * Executes the step processing framework, starting with the data bean
    * passed and the step whose name is passed. 
    * 
    * @param initialBean
    *    The initial data bean to start the execution with.
    * @param name
    *    The name of the step to start with.
    * @return
    *    The data bean populated via the step executions.
    */
   public T execute(T initialBean, String name)
   {
      if (stringNotNullOrWhitespace(name))
      {
         IStep<T> stepViaName = availableSteps.get(name.trim().toUpperCase());
         if (stepViaName != null)
         {
            return execute(initialBean, stepViaName);
         }
      }
      
      // Step with the name passed as null or with one that could 
      // not be found, returning the initial object to the calling 
      // function.
      return initialBean;
   }
   
   /**
    * Returns true if the string passed is not null and has at least
    * one non-whitespace character in it.
    * 
    * @param val
    *    The string to test
    * @return
    *    True if the string is not null and its trimmed value is not empty.
    */
   private boolean stringNotNullOrWhitespace(String val)
   {
      return val != null && !val.trim().isEmpty();
   }
}
