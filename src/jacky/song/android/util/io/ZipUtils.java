package jacky.song.android.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.zip.*;

/**
 * 
 * @author Jacky.Song
 */
public class ZipUtils {
	
	/**
	 * Compress the specified data to the specified OutputStream.
	 * 
	 * @param os
	 *          the OutputStream to write the compressed data to
	 * @param data
	 *          the data about to be compressed, [ fileName -> fileData ]
	 */
  public static void compress(OutputStream os, Map<String, byte[]> data) {
    ZipOutputStream out = null;
    try {
      CheckedOutputStream cos = new CheckedOutputStream(os, new CRC32());
      out = new ZipOutputStream(cos);
      out.setLevel(5);
      for (String name : data.keySet()) {
        compress(name, data.get(name), out, "");
      }
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
    finally {
      IOUtils.closeQuietly(out);
    }
  }
  
  private static void compress(String eName, byte[] data, ZipOutputStream out, String basedir) throws IOException {
    //		System.out.println("压缩：" + basedir + eName);
    ZipEntry entry = new ZipEntry(basedir + new String(eName.getBytes(), "utf-8"));
    out.putNextEntry(entry);
    out.write(data);
    out.closeEntry();
  }
	
  /**
   * Decompress the zip data.
   * 
   * @param zipInputStream
   *          zip format data
   * @param output
   *          decompressed data
   */
  public static void decompress(InputStream zipInputStream, Map<String, byte[]> output) {
    try {
      ZipInputStream zis = new ZipInputStream(zipInputStream);
      ZipEntry entry = null;
      while ((entry = zis.getNextEntry()) != null) {
        if (!entry.isDirectory()) {
					output.put(entry.getName(), IOUtils.toByteArray(zis));
        }
      }
    }
    catch (IOException err) {}
  }

}
