/*
 * Created on 2011-9-15
 *
 * TODO
 */
package jacky.song.demo.pagea;

import jacky.song.R;
import jacky.song.android.framework.component.activity.Module;
import android.os.Bundle;


public class DemoSkinModule extends Module {
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    System.out.println(context.getResources().getColor(R.color.black));
  }
  
  @Override
  public void aquireResources() {
  }
  
  @Override
  public void releaseResources() {
  }
  
}
