/*
 * Created on Aug 2, 2011 TODO
 */
package jacky.song.android.framework.component.activity;

import android.os.AsyncTask;

/**
 * Module that does its job in a background Thread.
 * 
 * @author Jacky.Song
 */
public abstract class BackgroundModule<Params, Progress, Result> extends Module {

   protected class BackgroundModuleWorker extends AsyncTask<Params, Progress, Result> {

      @Override
      protected Result doInBackground(Params... params) {
         return BackgroundModule.this.doInBackground(params);
      }

      @Override
      protected void onProgressUpdate(Progress... values) {
         BackgroundModule.this.onProgressUpdate(values);
      }

      @Override
      protected void onPostExecute(Result result) {
         BackgroundModule.this.onPostExecute(result);
      }

      // work around the protected restriction
      void publishPgrs(Progress... values) {
         publishProgress(values);
      }
   }

  private BackgroundModuleWorker worker = new BackgroundModuleWorker();

   @Override
   public void aquireResources() {
   }

   @Override
   public void releaseResources() {
   }

   /**
    * Same as {@link AsyncTask}'s {@code publishProgress(Object...)}
    * <p>
    * Invoke this in the {@link #doInBackground(Object...)} to publish the intermediate value during calculation.
    */
   protected void publishProgress(Progress... values) {
      worker.publishPgrs(values);
   }

   /**
    * Start background thread with data
    */
   protected void startWorking(Params... data) {
      worker.execute(data);
   }

   protected abstract Result doInBackground(Params... params);

   protected void onProgressUpdate(Progress... values) {
   }

   protected void onPostExecute(Result result) {
   }

}
