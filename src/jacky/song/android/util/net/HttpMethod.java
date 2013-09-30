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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public enum HttpMethod {
  
  GET {
    
    @Override
    protected HttpUriRequest buildRequest(String url, Map<String, Object> params) {

      String paramList = null;
      if (params != null && !params.isEmpty()) {
        List<NameValuePair> list = new ArrayList<NameValuePair>(params.size());
        for (String key : params.keySet()) {
          list.add(new BasicNameValuePair(key, params.get(key).toString()));
        }
        paramList = "?" + URLEncodedUtils.format(list, HTTP.UTF_8);
      }
      
      HttpGet get = new HttpGet(paramList == null ? url : url + paramList);
      return get;
    }
  },
  
  POST {
    
    @Override
    protected HttpUriRequest buildRequest(String url, Map<String, Object> params) {
      HttpPost post = new HttpPost(url);
      
      if (params != null && !params.isEmpty()) {
        List<NameValuePair> paramList = new ArrayList<NameValuePair>(params.size());
        for (String key : params.keySet()) {
          paramList.add(new BasicNameValuePair(key, params.get(key).toString()));
        }
        
        try {
          HttpEntity reqEntity = new UrlEncodedFormEntity(paramList, HTTP.UTF_8);
          post.setEntity(reqEntity);
        }
        catch (Exception e) {
          throw new RuntimeException("Error while connecting [ " + url + " ]: " + e);
        }
      }
      post.setHeader("ContentType", "text/xml;Charset=UTF-8");
      return post;
    }
  },
  
  ;
  
  protected abstract HttpUriRequest buildRequest(String url, Map<String, Object> params);
  
  HttpResponse connect(HttpClient client, String url, Map<String, Object> params) {
    try {
      return client.execute(buildRequest(url, params));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
