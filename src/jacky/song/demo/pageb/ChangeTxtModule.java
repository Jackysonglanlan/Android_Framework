/*
 * Created on Jul 30, 2011 TODO
 */
package jacky.song.demo.pageb;

import android.content.Intent;
import android.widget.TextView;
import jacky.song.R;
import jacky.song.android.framework.component.activity.Module;
import jacky.song.android.framework.core.injector.*;
import jacky.song.android.framework.core.injector.anno.res.InjectIncomingIntent;
import jacky.song.android.framework.core.injector.anno.res.InjectResource;

public class ChangeTxtModule extends Module {

   @InjectResource(type = Resources.View, id = R.id.page2_txt)
   private TextView txt;

   @InjectIncomingIntent
   private Intent intent;

   @Override
   public void aquireResources() {
   }

   @Override
   public void refreshView() {
      txt.setText("ChangeTxtModule got data: " + intent.getCharSequenceExtra("msg"));
   }

   @Override
   public void releaseResources() {
   }

}
