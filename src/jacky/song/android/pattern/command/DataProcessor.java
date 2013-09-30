/*
 * Created on 2011-10-31
 *
 * TODO
 */
package jacky.song.android.pattern.command;

import jacky.song.android.pattern.command.chain.ProcessorChain;
import jacky.song.android.pattern.command.chain.ProcessorChainManager;

/**
 * Processor responsible for processing the {@link RawData} and return the processed data.
 * 
 * @author Jacky.Song
 */
public interface DataProcessor {
  
  /**
   * The type of the input data this processor needs.
   */
  Class<?> inputType();
  
  /**
   * The type of the output data this processor produced.
   * <p>
   * The {@link ProcessorChainManager} will perform "Fail-Fast" check in every step when the chain is running:
   * <p>
   * <i>If the output data of this processor can't be cast to the next one's input type, {@link ProcessorChainManager} will throw a RuntimeException.</i>
   */
  Class<?> outputType();

  /**
   * Handle the {@link RawData}.
   * <p>
   * <b>The processor is responsible for keeping the chain running, i.e. it must invoke {@code chain.next()} to make the next processor start working.</b>
   * <p>
   * <b>The product of the processor should be placed into the {@link RawData} by invoking {@code data.setData()}, then the product will be passed to the next processor to process.</b>
   * <p>
   * If the processor doesn't invoke {@code chain.next()}, the chain will be stopped.
   * 
   * @param rawData
   *          the data to process
   * @param chain
   *          current "WorkLine" this processor belongs to
   */
  void process(RawData data, ProcessorChain chain);

}
