/*
 * Created on Jul 16, 2011 TODO
 */
package jacky.song.android.util;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

public class Log {

   public static String TAG = "TEST";

   private static Logger log = LoggerFactory.getLogger();

   public static void setLog(Logger log) {
      Log.log = log;
   }

   public static void d(Object obj, Throwable e) {
      log.debug(obj, e);
   }

   public static void d(Object obj) {
      log.debug(obj);
   }

   public static void i(Object obj, Throwable e) {
      log.info(obj, e);
   }

   public static void i(Object obj) {
      log.info(obj);
   }

   public static void e(Object obj, Throwable e) {
      log.error(obj, e);
   }

   public static void e(Object obj) {
      log.error(obj);
   }

   public static void fatal(Object obj, Throwable e) {
      log.fatal(obj, e);
   }

   public static void fatal(Object obj) {
      log.fatal(obj);
   }


   public static void ad(Object obj, Throwable e) {
      android.util.Log.d(TAG, obj == null ? "null" : obj.toString(), e);
   }

   public static void ad(Object obj) {
      Log.ad(obj, null);
   }

}
