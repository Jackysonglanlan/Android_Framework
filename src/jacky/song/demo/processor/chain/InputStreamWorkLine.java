/*
 * Created on 2011-11-1
 *
 * TODO
 */
package jacky.song.demo.processor.chain;

import jacky.song.android.pattern.command.DataProcessor;
import jacky.song.android.pattern.command.chain.DefaultChain;

public class InputStreamWorkLine extends DefaultChain {
  
  @Override
  public Class<? extends DataProcessor>[] processors() {
    return null;// intentionally null, for there's no need to do anything else
  }
  
}
