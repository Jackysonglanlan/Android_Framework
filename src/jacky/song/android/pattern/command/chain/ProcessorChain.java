/*
 * Created on 2011-11-1
 * 
 * TODO
 */
package jacky.song.android.pattern.command.chain;

import java.util.List;

import jacky.song.android.pattern.command.DataProcessor;
import jacky.song.android.pattern.command.RawData;
import jacky.song.android.util.closure.Processor;

/**
 * Works like a work line, combined with seviral {@link DataProcessor}s together to do a specific task.
 * <p>
 * Each {@link DataProcessor} is an independent work unit, has its own input and output data.
 * <p>
 * A chain is a collection of these {@link DataProcessor}s, makes them work like a work line to do a specific task together.<br>
 * Every {@link DataProcessor}'s output is the input of the one runs right after it, if the type can't match, the {@link ProcessorChain} will "Fail-Fast".
 * 
 * @author Jacky.Song
 */
public interface ProcessorChain {
  
  /**
   * The "working procedure" this chain needs in order to do its job.
   */
  Class<? extends DataProcessor>[] processors();

  void setProcessors(List<DataProcessor> processors);

  RawData getRawData();

  /**
   * Start running the chain, the 1st {@link DataProcessor} in the chain will start working immediately.
   * 
   * @param rawDataProvider
   *          provide raw data based on the input type of the 1st processor of the chain, then the data will be passed back to it
   * @throws IllegalStateException
   *           if there's no {@link DataProcessor}s in the chain
   */
  void start(Processor<Class<?>, RawData> rawDataProvider);

  /**
   * Make the chain start running the next processor, if there's no other processors left, nothing happenes.
   */
  void next();
  
  /**
   * Jump to a {@link DataProcessor} in the chain and run it after the specified delay in millisecond.
   * 
   * @param offset
   *          index offset of the target processor, neigative means the chain will run backward.
   * @param intervalInMS
   *          delay in millisecond, if the value <=0, then the next processor will start immediately
   * @throws IllegalArgumentException
   *           if the specified index is out of the chain's range
   */
  void moveDelay(int offset, long intervalInMS);

}
