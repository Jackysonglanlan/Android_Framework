/*
 * Created on Jul 28, 2011
 * 
 * TODO
 */
package jacky.song.practise;

import jacky.song.android.framework.component.service.AsyncService;
import jacky.song.android.util.Log;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;

public class EchoAsyncService extends AsyncService {
  
  private Callback task = new Callback() {
    
    @Override
    public boolean handleMessage(Message msg) {
      if (msg.what == 1) {
        Log.d("service thread: " + Thread.currentThread());
        
        try {
          Log.d("Hi ...");
          Log.d("I send you something...");
          Message m = Message.obtain();
          Bundle b = new Bundle();
          b.putInt("2", 2);
          m.setData(b);
          // Thread.sleep(3000);
          msg.replyTo.send(m);
        }
        catch (Exception e) {
          Log.d("", e);
        }
      }
      return true;
    }
  };
  
  @Override
  protected Callback asyncTask() {
    return task;
  }
  
}
