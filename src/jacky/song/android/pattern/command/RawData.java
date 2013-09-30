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
 * Created on 2011-10-31
 * 
 * TODO
 */
package jacky.song.android.pattern.command;

import java.util.Map;

/**
 * Represents the raw materials(data) a {@link ProcessorChain} needed.
 * <p>
 * <i>By the end of the chain, the data should become the final product the chain produces.</i>
 * 
 * @author Jacky.Song
 */
public class RawData {
  
  private Object data;

  private Map<String, Object> metaData;
  
  public Object getData() {
    return this.data;
  }
  
  public void setData(Object data) {
    this.data = data;
  }
  
  /**
   * The meta data which describe the raw data.
   */
  public Map<String, Object> getMetaData() {
    return this.metaData;
  }

  public void setMetaData(Map<String, Object> metaData) {
    this.metaData = metaData;
  }

}
