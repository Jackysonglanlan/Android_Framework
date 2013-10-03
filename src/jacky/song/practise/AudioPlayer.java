/*
 * Created on Sep 1, 2011 TODO
 */
package jacky.song.practise;

import jacky.song.android.util.SystemUtils;
import jacky.song.android.util.closure.Closure;

import java.io.File;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.AsyncTask;

public class AudioPlayer {

   public static final AudioPlayer INSTANCE = new AudioPlayer();

   public static final String RECORDING_FORMAT = ".amr";

   private MediaPlayer mp;

   private boolean isPrepared;
   private boolean isPlaying;

   private Closure<Void> completeHandler;
   private Closure<Integer> onPlayingHandler;

   private int notifyInterval;

   private AsyncTask<Void, Integer, Void> watcher;

   private AudioPlayer() {
   }

   /**
    * @param completeHandler callback when finished playing, despite of whether it stops normally, or is stopped
    *           manually
    */
   public void setCompleteHandler(Closure<Void> completeHandler) {
      this.completeHandler = completeHandler;
   }

   /**
    * Set callback for notification while playing.
    * 
    * @param onPlayingHandler callback when the player is on playing, <i>passed in the accumulated time:
    *           notifyInterval*invocationCount</i>
    * @param notifyInterval time interval between the onPlayingHandler is invoked every time
    */
   public void setOnPlayingHandler(Closure<Integer> onPlayingHandler, int notifyInterval) {
      this.onPlayingHandler = onPlayingHandler;
      this.notifyInterval = notifyInterval;
   }

   private void checkPrepared() {
      if (!isPrepared)
         throw new RuntimeException("AudioPlayer hasn't prepared, call prepare() first !!");
   }

   public long getDuration() {
      this.checkPrepared();
      return mp.getDuration();
   }

   public void play() {
      this.checkPrepared();
      mp.start();
      isPlaying = true;
      if (onPlayingHandler != null) {
         watcher = new AsyncTask<Void, Integer, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
               int count = 0;
               try {
                  while (isPlaying) {
                     Thread.sleep(notifyInterval);
                     publishProgress( (count++) * notifyInterval);
                  }
               } catch (InterruptedException e) {
                  return null;
               }
               return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
               if (isPlaying)
                  onPlayingHandler.doWith(values[0]);
            }
         };
         watcher.execute((Void) null);
      }
   }

   public void stop() {
      if (mp.isPlaying())
         mp.stop();
   }

   /**
    * @param audioFile audio to play
    */
   public void prepare(final File audioFile) {
      // grant file access permission
      SystemUtils.exec("chmod 666 " + audioFile.getAbsolutePath());

      mp = new MediaPlayer();

      try {
         mp.setDataSource(audioFile.getAbsolutePath());
         mp.prepare();// block until finish
         isPrepared = true;
         mp.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
               // try again
               // mp.reset();
               // try {
               // mp.setDataSource(audioFile.getAbsolutePath());
               // mp.prepare();
               // mp.start();
               // mp.setOnErrorListener(null);// only try once
               // } catch (Exception e) {
               // }

               return true;
            }
         });
         mp.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
               arg0.release();
               isPlaying = false;
               watcher.cancel(true);
               watcher = null;

               // recover access permission
               SystemUtils.exec("chmod 600 " + audioFile.getAbsolutePath());

               if (completeHandler != null)
                  completeHandler.doWith(null);
            }
         });
      } catch (Exception e) {
      }
   }

}
