/*
 * Created on 2011-10-31
 * 
 * TODO
 */
package jacky.song.demo.processor;

import jacky.song.android.pattern.command.RawData;
import jacky.song.android.pattern.command.DefaultProcessor;
import jacky.song.android.util.net.Response;

import java.io.File;

public class FileProcessor extends DefaultProcessor<File> {
  
  @Override
  public Class<?> inputType() {
    return Response.InputStream.type();
  }
  
  @Override
  public Class<File> outputType() {
    return File.class;
  }
  
  @Override
  protected File doBusiness(RawData data) {
    // TODO build File
    System.out.println("Meta data: " + data.getMetaData());
    System.out.println("RawData from " + this.getClass() + " " + data.getData());
    return null;
  }
  
}
