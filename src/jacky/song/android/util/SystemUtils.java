/*
 * Created on Aug 27, 2011
 *
 * TODO
 */
package jacky.song.android.util;

import java.io.IOException;


public class SystemUtils {

   public static void exec(String cmd) {
      try {
         Runtime.getRuntime().exec(cmd);
      } catch (IOException e) {
         e.printStackTrace();
         throw new RuntimeException("Can't exec " + cmd, e);
      }
   }

}
