/*
 * Created on 2011-11-7
 * 
 * TODO
 */
package jacky.song.android.pattern.command;

import jacky.song.android.pattern.command.chain.ProcessorChain;

public abstract class DefaultProcessor<Result> implements DataProcessor {
  
  @Override
  public abstract Class<Result> outputType();

  @Override
  public void process(RawData data, ProcessorChain chain) {
    Result result = this.doBusiness(data);
    chain.getRawData().setData(result);
    if (keepChainRunningAfterFinished(result)) chain.moveDelay(nextOffset(), delayOfMovingNext());
  }
  
  /**
   * Return the position of the next processor (relative to this processor) which should start running after this processor is done.
   * <p>
   * If the return value is neigative, then the chain will run backward.
   * <p>
   * <em>Default value is 1, means the one right after this processor. So it's the same as invoke {@code chain.next()}</em>
   */
  protected int nextOffset() {
    return 1;
  }
  
  /**
   * Return the time delay (in millisecond) before moving to the next processor.
   */
  protected long delayOfMovingNext() {
    return 0;
  }
  
  /**
   * Wether the processor should keep the chain running <b>after</b> its job is done? In another word, whether the next {@link DataProcessor} should be activated.
   * <p>
   * Default behavior returns true.
   * 
   * @param result
   *          the result from {@link #doInBackground(RawData)} method
   * @return true if the chain should keep running, false if the chain should be stopped.
   */
  protected boolean keepChainRunningAfterFinished(Result result) {
    return true;
  }
  
  /**
   * Perform the business logic of this processor.
   * 
   * @param data
   *          raw data
   * @return
   */
  protected abstract Result doBusiness(RawData data);
  
}
