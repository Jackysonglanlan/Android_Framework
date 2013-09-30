/*
 * Created on Jul 20, 2011
 * 
 * TODO
 */
package jacky.song;

import jacky.song.android.framework.component.handler.AsyncHandler;
import jacky.song.android.util.Log;
import android.app.Activity;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.RemoteException;
import android.widget.TextView;

public class BusyMessenger {
  
  private Activity view;
  
  public void setView(Activity view) {
    this.view = view;
  }
  
  private AsyncHandler worker1 = AsyncHandler.newHandler(new Callback() {
    
    @Override
    public boolean handleMessage(Message msg) {
      if (msg.what == 2) {
        System.out.println("worker1: " + Thread.currentThread());
        System.out.println("worker1 gets arg1 from worker2: " + msg.arg1);
      }
      return false;
    }
  });
  
  private AsyncHandler worker2 = AsyncHandler.newHandler(new Callback() {
    
    @Override
    public boolean handleMessage(Message msg) {
      if (msg.what == 1) {
        System.out.println("worker2: " + Thread.currentThread());
        System.out.println("worker2 gets arg1 from main thread: " + msg.arg1);
        
        Message msgNew = Message.obtain();
        msgNew.what = 2;
        msgNew.arg1 = 200;
        try {
          System.out.println("worker2 is sending data to worker1");
          msg.replyTo.send(msgNew);
        }
        catch (RemoteException e) {
          System.out.println(e);
        }
      }
      return true;
    }
  });
  
  private AsyncHandler refreshLabel = AsyncHandler.newHandler(new Callback() {
    
    @Override
    public boolean handleMessage(Message msg) {
      try {
        Thread.sleep(500);
        Log.d("thread " + Thread.currentThread());
        view.runOnUiThread(new Runnable() {
          
          @Override
          public void run() {
            ((TextView) view.findViewById(R.id.txt)).setText(System.currentTimeMillis() + "");
          }
        });
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
      return false;
    }
  });
  
  public void startWorking(final boolean inMainThread) {
    // Message m = Message.obtain(worker2);
    // m.what = 1;
    // m.arg1 = 100;
    // m.replyTo = new Messenger(worker1);
    // m.sendToTarget();
    
    for (int i = 0; i < 100; i++) {
      Message m = refreshLabel.obtainMessage();
      m.sendToTarget();
    }
    
  }

}
