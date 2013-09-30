/*
 * Created on 2011-9-27
 *
 * TODO
 */
package jacky.song.demo.pagea;

import jacky.song.R;
import jacky.song.android.framework.component.activity.Module;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;

public class AddNotifModule extends Module {
  
  private static final int NOTI_ID = 19172439;
  
  private NotificationManager nm;

  @Override
  public void aquireResources() {
    nm = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
  }
  
  @Override
  public void releaseResources() {
  }
  
  @Override
  public void onStart() {
    new AsyncTask<Void, Void, Void>() {
      
      @Override
      protected Void doInBackground(Void... params) {
        try {
          Thread.sleep(3000);
        }
        catch (InterruptedException e) {}
        showNotification(R.drawable.icon, "Test Noti", "Title", "Content");
        return null;
      }
    }.execute((Void) null);
  }
  
  @Override
  public void onNewIntent(Intent intent) {
    if (intent.getExtras() != null && intent.getExtras().getInt("aaa") == NOTI_ID) {
      System.out.println("aaaaaaa");
    }
  }
  
  public void showNotification(int icon, String tickertext, String title, String content) {
    Notification notification = new Notification(icon, tickertext, System.currentTimeMillis());
    notification.defaults = Notification.DEFAULT_ALL;
    //这是设置通知是否同时播放声音或振动，声音为Notification.DEFAULT_SOUND
    //振动为Notification.DEFAULT_VIBRATE;
    //Light为Notification.DEFAULT_LIGHTS
    //全部为Notification.DEFAULT_ALL
    //如果是振动或者全部，必须在AndroidManifest.xml加入振动权限
    Intent in = new Intent(context, PageA.class);
    in.putExtra("aaa", NOTI_ID);
    in.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    PendingIntent pt = PendingIntent.getActivity(context, 0, in, PendingIntent.FLAG_CANCEL_CURRENT);
    //点击通知后的动作
    notification.setLatestEventInfo(context, title, content, pt);
    nm.notify(NOTI_ID, notification);
  }

}
