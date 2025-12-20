package bjad.processor;

/**
 * Interface defining a step to take within the step 
 * processor framework. 
 *
 * @author 
 *   Ben Dougall
 * @param <T> 
 *    The object type to pass between each of the steps
 *    executed in the processor/framework 
 */
public interface IStep<T>
{
   /**
    * The name to return from a step in order to end the processor
    * framework's execution. 
    */
   public static final String FINAL_STEP = "--{FINALSTEP}--";
   
   /**
    * The method to implement to execute the logic for the 
    * step and to determine the next step to execute within
    * in the processor/framework.
    * 
    * @param data
    *    The data bean being made/populated via the various 
    *    steps within the processor/framework. 
    * @return
    *    The name of the next step to execute in the 
    *    processor/framework    
    */
   public String executeStep(T data);  
}
