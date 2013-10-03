/*
 * Created on Aug 27, 2011 TODO
 */
package jacky.song.practise;

import jacky.song.android.util.closure.Closure;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import android.media.MediaRecorder;
import android.os.AsyncTask;

public class AudioRecorder {
  
  private MediaRecorder recorder;
  
  private File recordingFile;
  
  private int maxDuration;
  
  private int audioSamplingRate = 8000;
  
  private int audioEncodingBitRate = 44100 / 2;
  
  private int audioSource;
  
  private int outFormat;
  
  private int audioEncoder;
  
  public static final AudioRecorder INSTANCE = new AudioRecorder();
  
  private AudioRecorder() {
    this.recorder = new MediaRecorder();
  }
  
  public void setAudioSource(int audioSource) {
    this.audioSource = audioSource;
  }
  
  public void setAudioEncoder(int audioEncoder) {
    this.audioEncoder = audioEncoder;
  }
  
  /**
   * @param outFormat
   *          file format you want to save as
   */
  public void setOutFormat(int outFormat) {
    this.outFormat = outFormat;
  }
  
  public void setAudioEncodingBitRate(int audioEncodingBitRate) {
    this.audioEncodingBitRate = audioEncodingBitRate;
  }
  
  public void setAudioSamplingRate(int audioSamplingRate) {
    this.audioSamplingRate = audioSamplingRate;
  }
  
  /**
   * @param maxDuration
   *          the max duration of this record, when time exceed, timeExceedHandler will be invoked.
   */
  public void setMaxDuration(int maxDuration) {
    this.maxDuration = maxDuration;
  }
  
  /**
   * @param recordingFile
   *          the file you want to save the recording
   */
  public void setRecordingFile(File recordingFile) {
    this.recordingFile = recordingFile;
  }
  
  /**
   * Cancel the recording process, notice that the recording file will be deleted after cancellation
   */
  public void cancel() {
    this.stop();
    recordingFile.delete();// no need the recording
    // System.out.println("delete: " + res);
  }
  
  public void stop() {
    if (isRecording.get()) {
      isRecording.set(false);
      try {
        recorder.stop();// stop recording
        // recorder.release();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  public void release() {
    recorder.release();
  }
  
  private AsyncTask<Void, Integer, Void> task;
  
  private AtomicBoolean isRecording = new AtomicBoolean(false);
  
  private static final int VOLUMN_SAMPLE_FREQ = 250;// frequency of sample the sound volumn
  
  /**
   * Record an audio and save it to the specified recording file.
   * 
   * @param onVolumnHandler
   *          invoked every 0.2s, passed in the sound volumn, pass null if it's not needed
   * @param onRecordingHandler
   *          when the recording starts, this callback will be invoked every 1s, passed in the current time duration, pass null if it's not needed
   * @param onFinishHandler
   *          invoked when user manually stops recording or, the recording time exceed maxDuration
   */
  public void recode(final Closure<Integer> onVolumnHandler, final Closure<Integer> onRecordingHandler,
      final Closure<Void> onFinishHandler) {
    if (isRecording.get()) return;
    
    recorder.reset();
    
    recorder.setAudioSource(audioSource);
    recorder.setOutputFormat(outFormat);
    recorder.setAudioEncoder(audioEncoder);
    recorder.setOutputFile(recordingFile.getAbsolutePath());
    
    recorder.setAudioSamplingRate(audioSamplingRate);
    recorder.setAudioEncodingBitRate(audioEncodingBitRate);
    
    try {
      recorder.prepare();
    }
    catch (Exception e) {}
    
    recorder.start(); // start recording
    isRecording.set(true);
    
    task = new AsyncTask<Void, Integer, Void>() {
      
      int timePassedInSec = 0;
      
      @Override
      protected Void doInBackground(Void... params) {
        
        int sampleCount = 0;
        while (timePassedInSec <= maxDuration && isRecording.get()) {
          try {
            Thread.sleep(VOLUMN_SAMPLE_FREQ);
          }
          catch (InterruptedException e) {}
          publishProgress(++sampleCount);
        }
        return null;
      }
      
      @Override
      protected void onProgressUpdate(Integer... values) {
        int sampleCount = values[0];
        
        if (isRecording.get() && onVolumnHandler != null) onVolumnHandler.doWith(recorder.getMaxAmplitude());// notify client
          
        // every 1 sec
        if (isRecording.get() && sampleCount % (1000 / VOLUMN_SAMPLE_FREQ) == 0 && onRecordingHandler != null)
          onRecordingHandler.doWith(++timePassedInSec);// notify client
      }
      
      @Override
      protected void onPostExecute(Void result) {
        // time exceed
        stop();
        onFinishHandler.doWith(null);// notify client
      }
    };
    
    task.execute((Void) null);
  }

}
