/*
 * Created on 2011-10-31
 * 
 * TODO
 */
package jacky.song.android.pattern.command.chain;

import jacky.song.android.pattern.command.DataProcessor;

import java.util.*;

/**
 * Data processor applied the Chain of Responsibility Pattern, responsible for processing the response data from server.
 * 
 * @author Jacky.Song
 */
public class ProcessorChainManager {
  
  // [ ProcessorChain class -> ProcessorChain ]
  private static final Map<Class<? extends ProcessorChain>, ProcessorChain> CHAINS = new HashMap<Class<? extends ProcessorChain>, ProcessorChain>();
  
  // [ DataProcessor class -> DataProcessor ]
  private static final Map<Class<? extends DataProcessor>, DataProcessor> PROCESSOR_CACHE = new WeakHashMap<Class<? extends DataProcessor>, DataProcessor>();

  private static void failFastCheck(ProcessorChain chain) {
    Class<? extends ProcessorChain> chainType = chain.getClass();
    Class<? extends DataProcessor>[] proArray = chain.processors();
    if (proArray == null || proArray.length == 0)
      throw new IllegalArgumentException("ProcessorChain: [" + chainType + "] is empty!");
    
    List<DataProcessor> processors = getProcessorsOfType(proArray);
    
    DataProcessor former = processors.get(0);
    for (int i = 1; i < processors.size(); i++) {
      DataProcessor latter = processors.get(i);
      Class<?> formerOutType = former.outputType();
      Class<?> latterInType = latter.inputType();
      // Fail-Fast check: whether the output of the former can be cast to the input of the latter
      if (!latterInType.isAssignableFrom(formerOutType))
        throw new RuntimeException("[" + former.getClass() + "]'s output type doesn't match [" + latter.getClass()
            + "]'s input type, check [" + chainType + "]");
      former = latter;// move on to the next node
    }
  }

  /**
   * Register one {@link ProcessorChain} to the manager.
   * 
   * @param chainType
   * @return The registered chain
   * @throws RuntimeException
   *           if any pair of the processors in the chain can't work together, i.e. The former's output type can't match the latter's input type
   */
  private static ProcessorChain registerChain(Class<? extends ProcessorChain> chainType) {
    ProcessorChain chain = null;
    try {
      chain = chainType.newInstance();
    }
    catch (Exception e) {
      throw new IllegalArgumentException(chainType + " can't be initialized !", e);
    }
    // check chain's integrality as soon as the chain is trying to register
    failFastCheck(chain);
    
    List<DataProcessor> processors = getProcessorsOfType(chain.processors());
    chain.setProcessors(processors);

    CHAINS.put(chainType, chain);
    return chain;
  }
  
  // we use class, rather than the real object to ensure the DataProcessor is singleton
  private static List<DataProcessor> getProcessorsOfType(Class<? extends DataProcessor>... types) {
    List<DataProcessor> processors = new ArrayList<DataProcessor>(types.length);
    
    Class<? extends DataProcessor> processorType = null;
    
    try {
      for (Class<? extends DataProcessor> type : types) {
        processorType = type;
        if (!PROCESSOR_CACHE.containsKey(type)) PROCESSOR_CACHE.put(type, type.newInstance());
        processors.add(PROCESSOR_CACHE.get(type));
      }
    }
    catch (Exception e) {
      throw new RuntimeException("Fatal ERROR when initialising DataInterceptor: " + processorType, e);
    }
    
    return processors;
  }
  
  private ProcessorChainManager() {}

  public static void init() {}

  public static void shutdown() {
    CHAINS.clear();
    PROCESSOR_CACHE.clear();
  }
  
  public static void cleanChainCache() {
    CHAINS.clear();
  }
  
  public static ProcessorChain destroyChain(Class<? extends ProcessorChain> type) {
    return CHAINS.remove(type);
  }
  
  public static DataProcessor findProcessor(Class<? extends DataProcessor> type) {
    return PROCESSOR_CACHE.get(type);
  }

  public static DataProcessor destoryProcessor(Class<? extends DataProcessor> type) {
    return PROCESSOR_CACHE.remove(type);
  }

  /**
   * Build the {@link ProcessorChain} of the type specified.
   * <p>
   * Every {@link DataProcessor} in the chain must invoke chain.next() to make the next {@link DataProcessor} start to work. The chain will be stopped if one {@link DataProcessor} doesn't do this.
   * 
   * @param chainType
   *          the chain instance ready to run
   * @return the chain ready to run
   * @throws IllegalArgumentException
   *           if the chainType has not been registered yet, or the chain is empty (no processors)
   */
  public static ProcessorChain build(Class<? extends ProcessorChain> chainType) {
    return CHAINS.containsKey(chainType) ? CHAINS.get(chainType) : registerChain(chainType);
  }

}
