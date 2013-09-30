/*
 * Created on Jul 30, 2011 TODO
 */
package jacky.song.demo.pageb;

import jacky.song.R;
import jacky.song.android.framework.component.activity.Module;
import jacky.song.android.skin.Skin;
import jacky.song.android.skin.Style;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BackButtonModule extends Module {
  
  @Override
  public void prepareView() {
    Button back = (Button) context.findViewById(R.id.Back);
    back.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        System.out.println(context.getClass().getSimpleName() + " Done");
        Intent in = context.getIntent();
        in.putExtra("secretCode", System.currentTimeMillis() + "");
        context.setResult(Activity.RESULT_OK, in);
        Skin.style = Style.Green;
        context.finish();
      }
    });
  }
  
  @Override
  public void aquireResources() {
    
  }
  
  @Override
  public void releaseResources() {}

}
