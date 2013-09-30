/*
 * Created on Aug 3, 2011
 *
 * TODO
 */
package jacky.song.demo.pagea;

import jacky.song.R;
import jacky.song.android.framework.component.activity.BackgroundModule;
import jacky.song.android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

public class RefreshTimeModule extends BackgroundModule<Void, Long, Void> {

   public enum Function {
      Show;
   }

   private TextView timeLabel;

   private AlertDialog timingDialog;

   @Override
   public void prepareView() {
      timeLabel = (TextView) context.findViewById(R.id.txt);
      timingDialog = this.buildStartDialog(context);
   }

   private AlertDialog buildStartDialog(Context context) {
      AlertDialog.Builder builder = new AlertDialog.Builder(context);
      builder.setMessage("Start Timing?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

         @Override
         public void onClick(DialogInterface dialog, int id) {
            startWorking((Void) null);
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
   public void destoryView() {
   }

   @Override
   protected Void doInBackground(Void... params) {
      for (int i = 0; i < 60 * 1; i++) {
         Log.d(Thread.currentThread());
         try {
            Thread.currentThread().sleep(1000);
         } catch (InterruptedException e) {
         }
         publishProgress(System.currentTimeMillis());
      }
      return null;
   }

   @Override
   protected void onProgressUpdate(Long... values) {
      timeLabel.setText("now: " + new SimpleDateFormat("HH:mm:ss").format(new Date(values[0])) + "");
   }

   @Override
   public Object onReceiveRequest(int type, Object data) {
      if (type == Function.Show.ordinal()) {
         timingDialog.show();
      }
      return null;
   }

}
