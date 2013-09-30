/*
 * Created on 2011-9-25
 * 
 * TODO
 */
package jacky.song.android.framework.core.decorator.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

/**
 * Default implementation of {@link ViewInterceptor}.
 * <p>
 * All {@link ViewInterceptor}s should extends this class, other than implement a new one.
 * 
 * @author Jacky.Song
 */
public abstract class DefaultViewInterceptor implements ViewInterceptor {
  
  @Override
  public View getView(int position, Object data, View convertView, ViewGroup parent) {
    if (convertView == null) convertView = createItemView(position, parent);
    refreshItemView(position, data, convertView, parent);
    return convertView;
  }
  
  /**
   * override this method to return a type code, default value is {@link android.widget.Adapter.IGNORE_ITEM_VIEW_TYPE}
   */
  @Override
  public int viewTypeCode() {
    return Adapter.IGNORE_ITEM_VIEW_TYPE;
  }
  
  /**
   * Create the item view.
   */
  protected abstract View createItemView(int position, ViewGroup parent);

  /**
   * Fill data (refresh) into item view of the specified position.
   * 
   */
  protected abstract void refreshItemView(int position, Object data, View convertView, ViewGroup parent);
  
}
