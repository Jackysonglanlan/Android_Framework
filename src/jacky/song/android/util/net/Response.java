/*
 * Created on 2011-10-31
 * 
 * TODO
 */
package jacky.song.android.util.net;

import jacky.song.android.pattern.command.RawData;
import jacky.song.android.pattern.command.chain.ProcessorChain;
import jacky.song.android.pattern.command.chain.ProcessorChainManager;
import jacky.song.android.util.closure.Closure;
import jacky.song.android.util.closure.Processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * Responsible for processing the response from server.
 * 
 * @author Jacky.Song
 */
public enum Response {
  
  String {
    
    @Override
    public Class<?> type() {
      return String.class;
    }
    
    @Override
    protected Object parseResponse(HttpResponse response) throws Exception {
      return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
    }
    
  },
  
  InputStream {
    
    @Override
    public Class<?> type() {
      return java.io.InputStream.class;
    }
    
    @Override
    protected Object parseResponse(HttpResponse response) throws Exception {
      return response.getEntity().getContent();
    }
    
  },
  
  ;
  
  public abstract Class<?> type();
  
  protected abstract Object parseResponse(HttpResponse response) throws Exception;
  
  private static Object parseResponse(Class<?> parsedDataType, HttpResponse response) {
    try {
      for (Response p : values())
        if (p.type() == parsedDataType) return p.parseResponse(response);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void handleResponse(HttpResponse response, Class<? extends ProcessorChain> chainType,
      Closure<HttpResponse> errorCallback) {
    if (response.getStatusLine().getStatusCode() != 200) {
      if (errorCallback != null) errorCallback.doWith(response);
      return;
    }
    
    RawDataGenerator generator = new RawDataGenerator();
    generator.response = response;
    generator.chainType = chainType;

    ProcessorChain chain = ProcessorChainManager.build(chainType);
    chain.start(generator);
  }

  private static class RawDataGenerator implements Processor<Class<?>, RawData> {
    
    Class<? extends ProcessorChain> chainType;
    
    HttpResponse response;
    
    @Override
    public RawData process(Class<?> type) {
      RawData rawData = new RawData();
      // get response data
      Object data = parseResponse(type, response);
      if (data == null)
        throw new IllegalArgumentException(chainType
            + "'s 1st DataProcessor's inputType() returns wrong type, ONLY String or InputStream is allowed.");
      
      rawData.setData(data);// the data here is the raw material to feed the chain

      // parse headers
      Header[] headers = response.getAllHeaders();
      Map<String, Object> metaData = new HashMap<String, Object>(headers.length);
      for (Header header : headers) {
        metaData.put(header.getName(), header.getValue());
      }
      rawData.setMetaData(metaData);
      return rawData;
    }
  }

}
