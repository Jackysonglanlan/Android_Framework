/*
 * Created on Aug 4, 2011
 * 
 * TODO
 */
package jacky.song.demo.pagea;

import jacky.song.RefreshActivityAsyncService;
import jacky.song.android.framework.component.activity.BackgroundModule;
import jacky.song.android.framework.core.injector.anno.res.InjectIncomingIntent;
import jacky.song.android.skin.Skin;
import jacky.song.android.skin.Style;
import jacky.song.android.util.Log;
import android.app.AlertDialog;
import android.content.*;
import android.os.*;

public class RefreshPageModule extends BackgroundModule<Void, Void, Void> {
  
  private AlertDialog refreshAlert;
  
  private boolean viewRefreshed;
  
  @InjectIncomingIntent
  private Intent intent;
  
  @Override
  public void prepareView() {
    this.refreshAlert = this.buildRefreshDialog();
  }
  
  private AlertDialog buildRefreshDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setMessage("Refresh Page?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      
      @Override
      public void onClick(DialogInterface dialog, int id) {
        //        Intent in = new Intent(context, context.getClass());
        //        Bundle data = new Bundle();
        //        data.putString("reserveData", "12345");
        //        in.putExtras(data);
        //        Log.d("Refresh " + context);
        //        viewRefreshed = true;
        //        context.startActivity(in);
        sendRefreshCommand();
        
        Skin.style = (Skin.style == Style.Default) ? Style.Green : Style.Default;

        context.finish();
      }
    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
      
      @Override
      public void onClick(DialogInterface dialog, int id) {
        dialog.cancel();
      }
    });
    return builder.create();
  }
  
  @Override
  public void onNewIntent(Intent intent) {
    //    System.out.println(this.intent);
    //    System.out.println(this.getClass().getSimpleName() + " get incoming Intent with reserveData: "
    //        + intent.getExtras().get("reserveData"));
    
    if (viewRefreshed && isModuleExist(context.getClass(), 6)) {
      sendRequest(context.getClass(), 6, RefreshTimeModule.Function.Show.ordinal(), null);
      viewRefreshed = false;
    }
  }
  
  private void bindService() {
    Intent in = new Intent(context, RefreshActivityAsyncService.class);
    context.bindService(in, conn, Context.BIND_AUTO_CREATE);
  }

  @Override
  public void onStart() {
    super.onStart();
    bindService();
    startWorking((Void) null);
  }
  
  @Override
  public void onStop() {
    context.unbindService(conn);
  }
  
  @Override
  protected Void doInBackground(Void... params) {
    try {
      Thread.sleep(1000);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  @Override
  protected void onPostExecute(Void result) {
    refreshAlert.show();
  }
  

  private Messenger clientHandler = new Messenger(new Handler() {
    
    @Override
    public void handleMessage(Message msg) {
      Log.d("clientHandler thread: " + Thread.currentThread());
      Log.d("Get response... " + msg.getData().get("2"));
    }
  });
  
  private void sendRefreshCommand() {
    Message m = Message.obtain(null, RefreshActivityAsyncService.MSG_REFRESH_ACTIVITY);
    Bundle data = new Bundle(1);
    data.putString(RefreshActivityAsyncService.PARAM_REFRESH_ACTIVITY_NAME, PageA.class.getName());
    data.putLong(RefreshActivityAsyncService.PARAM_REFRESH_INTERNAL_IN_MS, 500);
    m.setData(data);
    m.replyTo = clientHandler;
    try {
      messager.send(m);
    }
    catch (RemoteException e) {}
  }
  
  private void sayHello() {
    try {
      Message info = Message.obtain();
      info.what = 1;
      info.replyTo = clientHandler;
      messager.send(info);
      Log.d("Done... Handshaking Over");
    }
    catch (RemoteException e) {}
  }

  private ServiceConnection conn = new ServiceConnection() {
    
    @Override
    public void onServiceDisconnected(ComponentName name) {
      
    }
    
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      Log.d("main thread: " + Thread.currentThread());
      messager = new Messenger(service);
      //      sayHello();
    }
  };
  
  private Messenger messager;
  
}
