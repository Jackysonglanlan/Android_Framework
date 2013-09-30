/*
 * Created on 2011-9-16 TODO
 */
package jacky.song.android.skin;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

public class SkinableResources extends Resources {
  
  public SkinableResources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
    super(assets, metrics, config);
  }
  
  @Override
  public Drawable getDrawable(int id) throws NotFoundException {
    return super.getDrawable(Skin.drawable.load(id));
  }
  
  @Override
  public int getColor(int id) throws NotFoundException {
    return super.getColor(Skin.color.load(id));
  }

  @Override
  public XmlResourceParser getLayout(int id) throws NotFoundException {
    return super.getLayout(Skin.layout.load(id));
  }

}
