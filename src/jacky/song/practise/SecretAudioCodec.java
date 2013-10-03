/*
 * Created on Aug 27, 2011 TODO
 */
package jacky.song.practise;

import jacky.song.android.util.closure.Closure;

import java.io.File;

import android.content.Context;

public class SecretAudioCodec {

   /**
    * The same as AudioCodec.{@link #recode(int, int, int, File, int, Closure, Closure)}.
    * <p>
    * Just one difference: this method will keep the recording file in a secret place where user can't see, which means
    * the recording can only be played back by this application.
    * 
    * @param audioSource source
    * @param outFormat file format you want to save as
    * @param audioEncoder record format
    * @param context
    * @param outFileName the recording file name
    * @param maxDuration the max duration of this record, when time exceed, timeExceedHandler will be invoked.
    * @param onVolumnHandler invoked every 0.2s, passed in the volumn mount
    * @param onRecordingHandler when the recording starts, this callback will be invoked every 1 sec, passed in the
    *           current time duration
    * @param onFinishHandler invoked when user manually stops recording or the recording time exceed maxDuration, send
    *           back the recording file.
    * @return AudioRecorder which you can stop it manually
    */
   public static AudioRecorder recode(int audioSource, int outFormat, int audioEncoder, Context context,
         String outFileName, int maxDuration, Closure<Integer> onVolumnHandler, Closure<Integer> onRecordingHandler,
         final Closure<File> onFinishHandler) {
      final File recordingFile = getSecretFile(context, outFileName);

      AudioRecorder ar = AudioRecorder.INSTANCE;
      ar.setAudioSource(audioSource);
      ar.setOutFormat(outFormat);
      ar.setAudioEncoder(audioEncoder);
      ar.setMaxDuration(maxDuration);
      ar.setRecordingFile(recordingFile);
      ar.recode(onVolumnHandler, onRecordingHandler, new Closure<Void>() {

         @Override
         public void doWith(Void data) {
            onFinishHandler.doWith(recordingFile);
         }
      });
      return ar;
   }

   /**
    * <b> This method can only be used to decode the recording which recorded by
    * {@link #recode(int, int, int, Context, String, int, Closure, Closure)}. </b>
    * 
    * @param context
    * @param secretAudioFileName the secret recording file name
    * @param onPlayingHandler callback when the player is on playing, <i>passed in the accumulated time:
    *           notifyInterval*invocationCount</i>
    * @param notifyInterval time interval between the onPlayingHandler is invoked every time
    * @param completeHandler
    */
   public static AudioPlayer decode(Context context, final String secretAudioFileName,
         Closure<Integer> onPlayingHandler, int notifyInterval, Closure<Void> completeHandler) {
      AudioPlayer ap = AudioPlayer.INSTANCE;
      ap.setOnPlayingHandler(onPlayingHandler, notifyInterval);
      ap.setCompleteHandler(completeHandler);
      ap.prepare(getSecretFile(context, secretAudioFileName));
      return ap;
   }

   private static File getSecretFile(Context ctx, String fileName) {
      File talkFilesDir = ctx.getDir("push_to_talk", Context.MODE_PRIVATE);
      return new File(talkFilesDir.getAbsolutePath() + "/" + fileName);
   }
}
