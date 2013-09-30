/*
 * Created on Jul 28, 2011
 * 
 * TODO
 */
package jacky.song;

import jacky.song.android.framework.component.service.AsyncService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;

public class RefreshActivityAsyncService extends AsyncService {
  
  public static final String PARAM_REFRESH_ACTIVITY_NAME = "activity_name";
  
  public static final String PARAM_REFRESH_INTERNAL_IN_MS = "refresh_internal";
  
  public static final int MSG_REFRESH_ACTIVITY = 1;

  private Callback task = new Callback() {
    
    @Override
    public boolean handleMessage(Message msg) {
      if (msg.what == MSG_REFRESH_ACTIVITY) {
        try {
          Bundle data = msg.getData();
          
          // if has internal, wait, the default internal is 500ms
          Thread.sleep(data.containsKey(PARAM_REFRESH_INTERNAL_IN_MS) ? data.getLong(PARAM_REFRESH_INTERNAL_IN_MS)
              : 500);
          
          Intent in = new Intent(RefreshActivityAsyncService.this, Class.forName(data
              .getString(PARAM_REFRESH_ACTIVITY_NAME)));
          in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(in);
        }
        catch (Exception e) {}
      }
      return true;
    }
  };
  
  @Override
  protected Callback asyncTask() {
    return task;
  }
  
}
