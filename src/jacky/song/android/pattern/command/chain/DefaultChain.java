/*
 * Created on 2011-11-1
 * 
 * TODO
 */
package jacky.song.android.pattern.command.chain;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jacky.song.android.pattern.command.DataProcessor;
import jacky.song.android.pattern.command.RawData;
import jacky.song.android.util.closure.Processor;

/**
 * Default implementation of {@link ProcessorChain}.
 * <p>
 * <i>Every chain implementation should extends this class, rather than implements the {@link ProcessorChain} directly.</i>
 * 
 * @author Jacky.Song
 */
public abstract class DefaultChain implements ProcessorChain {
  
  protected Class<? extends DataProcessor>[] buildChain(Class<? extends DataProcessor>... types) {
    return types;
  }
  
  private List<DataProcessor> processors;
  
  @Override
  public final void setProcessors(List<DataProcessor> processors) {
    this.processors = processors;
  }

  private RawData rawData;
  
  @Override
  public final RawData getRawData() {
    return this.rawData;
  }

  private void runCurrentProcessor() {
    DataProcessor processor = processors.get(currRunningCursor);
    processor.process(rawData, this);
  }

  private int currRunningCursor;

  @Override
  public void start(Processor<Class<?>, RawData> rawDataProvider) {
    if(processors==null || processors.isEmpty())
      throw new IllegalStateException("There's no DataProcessors in the chain ["+this.getClass()+"]");
    
    // get the raw material of the chain
    rawData = rawDataProvider.process(processors.get(0).inputType()); // pass the input type of the chain, i.e. Input type of the 1st processor

    currRunningCursor = 0;
    move(0);
  }
  
  @Override
  public final void next() {
    // apply Chain of Responsibility pattern
    move(1);
  }
  
  // move to the processor and run it
  private void move(int offset) {
    int targetIndex = currRunningCursor + offset;
    
    // end of the chain
    // TODO quietly stop? or throw an Exception
    if (targetIndex == processors.size()) {
      currRunningCursor = targetIndex;
      return;
    }
      
    if (targetIndex < 0 || targetIndex > processors.size())
      throw new IllegalArgumentException("DataProcessor [" + processors.get(currRunningCursor).getClass()
          + "] jumps out of range in [" + getClass() + "]: target index is " + targetIndex);

    currRunningCursor = targetIndex;
    runCurrentProcessor();
  }

  private static ScheduledExecutorService delayWorker;
  
  @Override
  public void moveDelay(final int offset, long intervalInMS) {
    if (intervalInMS <= 0) {
      move(offset);// immediately move
      return;
    }

    // lazy
    if (delayWorker == null) delayWorker = Executors.newSingleThreadScheduledExecutor();

    delayWorker.schedule(new Runnable() {
      
      @Override
      public void run() {
        move(offset);
      }
    }, intervalInMS, TimeUnit.MILLISECONDS);// delay move
  }

}
