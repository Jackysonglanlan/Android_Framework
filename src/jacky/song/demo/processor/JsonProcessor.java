/*
 * Created on 2011-10-31
 * 
 * TODO
 */
package jacky.song.demo.processor;

import jacky.song.android.pattern.command.RawData;
import jacky.song.android.pattern.command.DefaultProcessor;
import jacky.song.android.util.net.Response;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class JsonProcessor extends DefaultProcessor<InputStream> {
  
  @Override
  public Class<?> inputType() {
    return Response.String.type();
  }
  
  @Override
  public Class<InputStream> outputType() {
    return InputStream.class;
  }
  
  @Override
  protected InputStream doBusiness(RawData data) {
    System.out.println(data.getData() + " in JsonProcessor with thread: " + Thread.currentThread());
    ByteArrayInputStream is = new ByteArrayInputStream(data.getData().toString().getBytes());
    return is;
  }

}
