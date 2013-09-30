/*
 * Created on 2011-11-8
 * 
 * TODO
 */
package jacky.song.android.pattern.command;

import jacky.song.android.pattern.command.chain.ProcessorChain;

/**
 * Works as a filter in the {@link ProcessorChain},
 * 
 * @author Jacky.Song
 */
public abstract class FilterProcessor<T> extends DefaultProcessor<T> {
  
  //////// Filter implies that it can't change the type of the data flow /////////

  //////// In another word, it outputType must be the same as its inputType /////////
  
  @Override
  public abstract Class<T> inputType();

  @Override
  public final Class<T> outputType() {
    return inputType();// same inputType as the outputType
  }

  //////// Again, it implies that the filter CAN NOT stop the chain, i.e. the method below should always return true /////////
  
  // subclass can't override this to return false
  @Override
  protected final boolean keepChainRunningAfterFinished(T result) {
    return true;
  }

  @Override
  protected final T doBusiness(RawData data) {
    return this.filter(data);
  }
  
  /**
   * Filter the chain's process.
   */
  protected abstract T filter(RawData data);

}
