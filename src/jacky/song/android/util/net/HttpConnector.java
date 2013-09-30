/*
 * Copyright (C) 2010 Jacky Song.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

/*
 * Created on 2011-10-29
 * 
 * TODO
 */
package jacky.song.android.util.net;

import jacky.song.android.pattern.command.chain.ProcessorChain;
import jacky.song.android.pattern.command.chain.ProcessorChainManager;
import jacky.song.android.util.closure.Closure;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;

/**
 * Used for connecting the internet via HTTP protocal.
 * 
 * @author Jacky.Song
 */
public class HttpConnector {
  
  private static HttpClient createClient() {
    HttpParams params = new BasicHttpParams();
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
    HttpProtocolParams.setUseExpectContinue(params, true);
    
    ConnManagerParams.setTimeout(params, 1000);
    
    HttpConnectionParams.setConnectionTimeout(params, 2000);
    HttpConnectionParams.setSoTimeout(params, 4000);
    
    SchemeRegistry schReg = new SchemeRegistry();
    schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
    
    ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
    
    return new DefaultHttpClient(conMgr, params);
  }
  
  private static HttpClient client;
  
  public static void init() {
    client = createClient();
    ProcessorChainManager.init();
  }
  
  public static void cleanWorkLine() {
    ProcessorChainManager.cleanChainCache();
  }

  public static void shutdown() {
    if (client != null) client.getConnectionManager().shutdown();
    ProcessorChainManager.shutdown();
  }
  
  private static void checkProperInitialized() {
    if (client == null) throw new IllegalStateException("You must initialize the HttpConnector first !");
  }
  
  /**
   * Connects to an url synchronisely, and returns the response.
   * 
   * @param method
   * @param url
   * @param params
   * @param chainType
   *          The ProcessorChain you want to use to deal with the response data
   * @param errorCallback
   *          Callback when the server response code isn't 200
   * @return The response data in the type you specified, or null if the Connector can't convert the raw data to this type, or error happens.
   * @throws IllegalStateException
   *           if the init() method hasn't been called
   */
  public static void syncConnect(HttpMethod method, String url, Map<String, Object> params,
      Class<? extends ProcessorChain> chainType, Closure<HttpResponse> errorCallback) {
    checkProperInitialized();
    
    HttpResponse resp = method.connect(client, url, params);
    
    if (resp == null) // no response returned
      errorCallback.doWith(new BasicHttpResponse(HttpVersion.HTTP_1_1, 0, "Can't get response from server"));
    else Response.handleResponse(resp, chainType, errorCallback);
  }
  
  /**
   * Connects to an url asynchronisely, and invokes the respHandler when it's done.
   * 
   * @param method
   * @param url
   * @param params
   * @param chainType
   *          The ProcessorChain you want to use to deal with the response data
   * @param errorCallback
   *          Callback when the server response code isn't 200
   * @throws IllegalStateException
   *           if the init() method hasn't been called
   */
  public static void asyncConnect(final HttpMethod method, final String url, final Map<String, Object> params,
      final Class<? extends ProcessorChain> chainType, final Closure<HttpResponse> errorCallback) {
    checkProperInitialized();
    new AsyncTask<Void, Void, HttpResponse>() {
      
      @Override
      protected HttpResponse doInBackground(Void... p) {
        final HttpResponse[] response = new HttpResponse[1];
        syncConnect(method, url, params, chainType, new Closure<HttpResponse>() {
          
          @Override
          public void doWith(HttpResponse data) {
            response[0] = data;
          }
        });
        
        // (response[0] == null) means no error happened, so return the response

        // The reason why return the response rather than invoke the callback directly is because this method is running in
        // a separate thread, not in main thread, if the user want to update the UI in the error callback, they have to do
        // a little more work.
        return response[0] == null ? null : response[0];
      }
      
      @Override
      protected void onPostExecute(HttpResponse result) {
        if (result != null) // error
          errorCallback.doWith(result);// call error callback, now it's called in main thread, user can update UI
      }
    }.execute((Void) null);
  }
  
}
