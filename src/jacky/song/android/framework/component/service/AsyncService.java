/*
 * Created on Jul 28, 2011 TODO
 */
package jacky.song.android.framework.component.service;

import jacky.song.android.framework.component.handler.AsyncHandler;
import android.app.Service;
import android.content.Intent;
import android.os.Handler.Callback;
import android.os.*;

/**
 * A service who will do its task in a background Thread rather than main Thread.
 * 
 * @author Jacky.Song
 */
public abstract class AsyncService extends Service {
  
  private AsyncHandler handler;
  
  @Override
  public void onCreate() {
    handler = AsyncHandler.newHandler(asyncTask());
    service = new Messenger(handler);
  }
  
  protected String name() {
    return this.getClass().getSimpleName();
  }
  
  @Override
  public void onDestroy() {
    handler.stop();
    super.onDestroy();
  }

  /**
   * handle the incoming intent.
   */
  protected void handleIntent(Intent intent) {}
  
  /**
   * @return The {@code Handler.Callback} you want to bind with this Service
   */
  protected abstract Callback asyncTask();
  
  private Messenger service;
  
  @Override
  public final IBinder onBind(Intent intent) {
    handleIntent(intent);
    return service.getBinder();
  }
  
  private Handler mainHandler = new Handler() {
    
    @Override
    public void handleMessage(Message msg) {
      handleDataInMainThread(msg);
    }
  };
  
  protected void sendMessageToMainThread(int what, int arg1, int arg2, Bundle data) {
    Message msg = mainHandler.obtainMessage(what, arg1, arg2);
    msg.setData(data);
    msg.sendToTarget();
  }
  
  protected void handleDataInMainThread(Message message) {}

}
