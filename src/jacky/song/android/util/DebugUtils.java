/*
 * Created on 2011-11-11
 * 
 * TODO
 */
package jacky.song.android.util;

import java.text.DecimalFormat;

import android.os.Debug;

public class DebugUtils {
  
  private static final String DEBUG = "DEBUG";

  public static void logHeap(Class<?> clazz) {
    Double allocated = new Double(Debug.getNativeHeapAllocatedSize()) / new Double((1048576));
    Double available = new Double(Debug.getNativeHeapSize()) / 1048576.0;
    Double free = new Double(Debug.getNativeHeapFreeSize()) / 1048576.0;
    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    df.setMinimumFractionDigits(2);
    
    android.util.Log.d(DEBUG, "debug. =================================");
    android.util.Log.d(DEBUG, "debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available)
        + "MB (" + df.format(free) + "MB free) in [" + clazz.getSimpleName() + "]");
    android.util.Log.d(
        DEBUG,
        "debug.memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory() / 1048576)) + "MB of "
            + df.format(new Double(Runtime.getRuntime().maxMemory() / 1048576)) + "MB ("
            + df.format(new Double(Runtime.getRuntime().freeMemory() / 1048576)) + "MB free)");
    System.gc();
    System.gc();
    
    // don't need to add the following lines, it's just an app specific handling in my app
    //    if (allocated >= (new Double(Runtime.getRuntime().maxMemory()) / new Double((1048576)) - MEMORY_BUFFER_LIMIT_FOR_RESTART)) {
    //      android.os.Process.killProcess(android.os.Process.myPid());
    //    }
  }

}
