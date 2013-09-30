/*
 * Created on Jul 16, 2011
 * 
 * TODO
 */
package jacky.song;

import jacky.song.android.util.Log;
import android.app.Service;
import android.content.Intent;
import android.os.*;

@Deprecated
public class TestService extends Service {
  
  private Messenger service;
  
  class ServiceHandler extends Handler {
    
    public ServiceHandler(Looper looper) {
      super(looper);
    }
    
    private void printHi() {
      System.out.println("hi");
    }
    
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == 1) {
        Log.d("service thread: " + Thread.currentThread());
        
        printHi();
        try {
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
    }
  }
  
  private void startWorker() {
    HandlerThread worker = new HandlerThread("Test");
    worker.start();
    
    while (worker.getLooper() == null)
      ;
    
    ServiceHandler handler = new ServiceHandler(worker.getLooper());
    service = new Messenger(handler);
  }
  
  @Override
  public IBinder onBind(Intent intent) {
    startWorker();
    return service.getBinder();
  }

}
