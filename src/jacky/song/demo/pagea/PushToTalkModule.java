/*
 * Created on Aug 29, 2011 TODO
 */
package jacky.song.demo.pagea;

import jacky.song.R;
import jacky.song.android.framework.component.activity.Module;
import jacky.song.android.framework.core.injector.Resources;
import jacky.song.android.framework.core.injector.anno.res.InjectResource;
import jacky.song.android.util.closure.Closure;
import jacky.song.practise.AudioPlayer;
import jacky.song.practise.AudioRecorder;
import jacky.song.practise.SecretAudioCodec;

import java.io.File;

import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

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
    ar = SecretAudioCodec.recode(MediaRecorder.AudioSource.MIC, // 照写
        MediaRecorder.OutputFormat.THREE_GPP,// 照写
        MediaRecorder.AudioEncoder.AMR_NB,// 照写
        context,// 你的Context对象
        System.currentTimeMillis() + AudioPlayer.RECORDING_FORMAT,// 录音的文件名，这里是用当前时间做文件名
        10, // 最大录音长度
        // 这个回调就是音量，从0到32000，每0.2s调一次
        new Closure<Integer>() {
          
          @Override
          public void doWith(Integer data) {
            recordingVolumn.setText("Volumn: " + data);// data就是音量
          }
        },
        // 这个回调是录音时间，从1开始，一直到最长时间
        new Closure<Integer>() {
          
          @Override
          public void doWith(Integer data) {
            recordingTime.setText("Recording time: " + data + "s");// data就是已经录了多久，单位秒
          }
        },
        // 这个回调是录音完毕
        new Closure<File>() {
          
          // 不管是用户主动结束(就是下面的onTouchUp())，还是最大时间到了，都会调用
          
          @Override
          public void doWith(File data) {// 这个data就是录好音的文件
            recordingVolumn.setText("Recording DONE");
            
            // 录音完毕以后马上回放
            AudioPlayer player = SecretAudioCodec.decode(context, // Context对象
                data.getName(), // 录音文件名
                // 这个回调在播放过程中调用
                new Closure<Integer>() {

                  @Override
                  public void doWith(Integer data) {// data = 通知时间间隔 * 调用次数
                    recordingTime.setText("Playing: " + data + "ms");
                  }
                }, 1000,// 通知时间间隔，ms
                // 这个回调在放完以后调用
                new Closure<Void>() {

                  @Override
                  public void doWith(Void data) {// 这个data没用，不管
                    recordingTime.setVisibility(View.GONE);
                    recordingVolumn.setVisibility(View.GONE);
                    System.out.println("play end");
                  }
                });
            
            player.play();
            // player.stop(); // 如果用户主动停止播放，就这样
            
          }
        });
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
