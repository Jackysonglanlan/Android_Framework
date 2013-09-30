/*
 * Created on Jul 28, 2011
 * 
 * TODO
 */
package jacky.song.android.framework.component.handler;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class AsyncHandler extends Handler {
  
  public static AsyncHandler newHandler(final Callback callback) {
    HandlerThread worker = new HandlerThread(AsyncHandler.class.getName());
    
    worker.start();
    
    return new AsyncHandler(worker.getLooper(), callback);
    /*
    final AsyncHandler[] holder = new AsyncHandler[1];
    
    WORKERS.submit(new Runnable() {
      
      @Override
      public void run() {
        Looper.prepare();
        Looper looper = Looper.myLooper();
        holder[0] = new AsyncHandler(looper, callback);
        Looper.loop();
      }
    });
    
    while (holder[0] == null)
      // waiting until AsyncHandler is successfully initialized
      ;
    return holder[0];
    */

  }
  
  private Looper looper;
  
  private Callback callback;
  
  private AsyncHandler(Looper looper, Callback callback) {
    super(looper);
    this.looper = looper;
    this.callback = callback;
  }
  
  /**
   * Stop the Looper of this Handler and reclaim the resources.
   */
  public void stop() {
    looper.quit();// release underlining thread
  }
  
  @Override
  public final void handleMessage(Message msg) {
    callback.handleMessage(msg);
  }

}
