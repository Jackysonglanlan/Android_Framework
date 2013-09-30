/*
 * Created on Aug 5, 2011 TODO
 */
package jacky.song.android.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import jacky.song.android.util.io.IOUtils;

/**
 * Monitor system resources usage.
 * 
 * @author jackysong
 */
public class SystemMonitor extends AsyncTask<Void, Map<String, String>, Void> {

   public interface Monitor {

      String processName();

      void onRealTimeData(Map<String, String> info);
   }

   private Monitor monitor;

   public SystemMonitor(Monitor monitor) {
      this.monitor = monitor;
   }

   private Map<String, String> gatherInfo() {
      Map<String, String> info = new HashMap<String, String>();
      this.gatherCPURatio(info);
      this.gatherMemoryInfo(info);
      return info;
   }

   private void gatherMemoryInfo(Map<String, String> info) {
      long totalMemory = Runtime.getRuntime().totalMemory() >> 10;
      long freeMemory = Runtime.getRuntime().freeMemory() >> 10;
      long maxMemory = Runtime.getRuntime().maxMemory() >> 10;

      // Log.d("totalMemory:" + totalMemory);
      // Log.d("freeMemory:" + freeMemory);
      // Log.d("maxMemory:" + maxMemory);

      info.put("totalMemory", totalMemory + "");
      info.put("freeMemory", freeMemory + "");
      info.put("maxMemory", maxMemory + "");
   }

   private void gatherCPURatio(Map<String, String> info) {
      String cpuRatio = null;
      InputStream in = null;
      try {
         Process process = Runtime.getRuntime().exec("top -n 1");
         in = process.getInputStream();
         List<String> list = IOUtils.readLines(in);
         for (String line : list) {
            if (line.contains(monitor.processName())) {
               // line: 3978 0% S 8 135024K 22324K fg app_35 jacky.song
               cpuRatio = line.replaceAll(".*(\\d+%).*", "$1");
               break;
            }
         }
      } catch (IOException e) {
      } finally {
         IOUtils.closeQuietly(in);
      }
      info.put("cpuRatio", cpuRatio);
   }

   private boolean stop;

   private long updateInternal = 1000;

   public void setUpdateInternal(long updateInternal) {
      this.updateInternal = updateInternal;
   }

   public void stop() {
      stop = true;
   }

   public void start() {
      this.execute((Void) null);
   }

   @Override
   protected Void doInBackground(Void... params) {
      try {
         while (!stop) {
            Thread.currentThread().sleep(updateInternal);
            publishProgress(this.gatherInfo());
         }
      } catch (InterruptedException e) {
      }
      return null;
   }

   @Override
   protected void onProgressUpdate(Map<String, String>... values) {
      monitor.onRealTimeData(values[0]);
   }
}
