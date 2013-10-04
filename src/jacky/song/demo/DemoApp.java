/*
 * Created on 2011-10-10
 * 
 * TODO
 */
package jacky.song.demo;

import com.activeandroid.ActiveAndroid;

import android.app.Application;

public class DemoApp extends Application {
  
  static {}

  @Override
  public void onCreate() {
    super.onCreate();
    ActiveAndroid.initialize(this);
  }
  @Override
  public void onTerminate() {
    super.onTerminate();
    ActiveAndroid.dispose();
  }
}
