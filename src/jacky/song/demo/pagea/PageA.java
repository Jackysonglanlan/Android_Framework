package jacky.song.demo.pagea;

import jacky.song.R;
import jacky.song.android.framework.component.activity.BaseActivity;
import android.view.Menu;
import android.view.MenuInflater;

public class PageA extends BaseActivity {

  @Override
  protected int defaultLayout() {
    return R.layout.main;
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = this.getMenuInflater();
    inflater.inflate(R.menu.test_options_menu, menu);
    return true;
  }
  
  /*
  private void writeFileToSDCard(byte[] data, String name) {
     FileOutputStream os = null;
     try {
        os = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/" + name);
        IOUtils.copy(new ByteArrayInputStream(data), os);
     } catch (Exception e) {
        Log.d("", e);
     } finally {
        IOUtils.closeQuietly(os);
     }
  }

  private Bitmap getPicFromSDCard(String path) {
     // 图片路径
     path = Environment.getExternalStorageDirectory().toString() + "/" + path;
     return BitmapFactory.decodeFile(path, null);
  }
  */
}
