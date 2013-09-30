/*
 * Created on 2011-11-1
 * 
 * TODO
 */
package jacky.song.demo.processor.chain;

import jacky.song.android.pattern.command.DataProcessor;
import jacky.song.android.pattern.command.chain.DefaultChain;
import jacky.song.demo.processor.FileProcessor;
import jacky.song.demo.processor.JsonProcessor;

public class FileWorkLine extends DefaultChain {
  
  @Override
  public Class<? extends DataProcessor>[] processors() {
    return buildChain(JsonProcessor.class, FileProcessor.class);
  }
  
}
