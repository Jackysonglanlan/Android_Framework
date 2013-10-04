/*
 * Created on Aug 29, 2011 TODO
 */
package jacky.song.demo.pagea;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import jacky.song.R;
import jacky.song.android.framework.component.activity.Module;
import jacky.song.android.framework.core.injector.Resources;
import jacky.song.android.framework.core.injector.anno.res.InjectResource;
import jacky.song.practise.AudioRecorder;

public class PushToTalkModule extends Module {
  
  @InjectResource(type = Resources.View, id = R.id.txt)
  private TextView txt;
  
  @InjectResource(type = Resources.View, id = R.id.recordingTime)
  private TextView recordingTime;
  
  @InjectResource(type = Resources.View, id = R.id.recordingVolumn)
  private TextView recordingVolumn;
  
  private AudioRecorder ar;
  
  private long touchTime;
  
  private static final long VALVE_HOLD_TIME = 500;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    // txt相当于录音按钮，下面照写
    txt.setLongClickable(true);
    txt.setOnTouchListener(new OnTouchListener() {
      
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        // 这个 if 就是按下去的时候调用
        if (action == MotionEvent.ACTION_DOWN) {
          touchTime = System.currentTimeMillis();
          
          // 为了处理用户刚点击一下就放开，所以用异步任务监控touch的时间
          new AsyncTask<Void, Void, Void>() {
            
            @Override
            protected Void doInBackground(Void... params) {
              try {
                Thread.sleep(VALVE_HOLD_TIME);// 至少要touch这么长
              }
              catch (InterruptedException e) {}
              return null;
            }
            
            @Override
            protected void onPostExecute(Void result) {
              System.out.println("touch time: " + touchTime);
              
              if (isUserPressedLongEnough()) onTouchDown();
            }
          }.execute((Void) null);
        }
        // 这个if就是用户放开的时候
        else if (action == MotionEvent.ACTION_UP) {
          touchTime = System.currentTimeMillis() - touchTime;
          
          if (isUserPressedLongEnough()) onTouchUp();
        }
        return false;
      }
    });
  }
  
  private boolean isUserPressedLongEnough() {
    return touchTime > VALVE_HOLD_TIME;
  }
  
  private void onTouchDown() {
    System.out.println("down");
    
    recordingTime.setVisibility(View.VISIBLE);
    recordingVolumn.setVisibility(View.VISIBLE);
    recordingTime.setText("");
    recordingVolumn.setText("");
    
    // 下面开始录音
  }
  
  private void onTouchUp() {
    System.out.println("up");
    if (ar != null) ar.stop();// 停止录音，
  }
  
  @Override
  public void aquireResources() {}
  
  @Override
  public void releaseResources() {}

}
