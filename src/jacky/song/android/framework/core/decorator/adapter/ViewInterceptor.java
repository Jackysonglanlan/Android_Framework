/*
 * Created on Jul 24, 2011
 * 
 * TODO
 */
package jacky.song.android.framework.core.decorator.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Responsible for intercepting the item view of a {@link android.widget.ListView} and changing/modifying it if necessary.
 * 
 * @author Jacky.Song
 */
public interface ViewInterceptor {
  
  /**
   * Is the view in this position the one you are interested in?
   * 
   * @param adapter
   *          the target Adapter
   * @param position
   *          current position
   * @return true if it is, false if it isn't
   */
  boolean acceptView(BaseAdapter adapter, int position);
  
  /**
   * Intercept the view you are interested in, you can change it, modify it here as you wish.
   * 
   * @return the modified View
   */
  View getView(int position, Object data, View convertView, ViewGroup parent);
  
  /**
   * @return the type code of the interested position
   */
  int viewTypeCode();

  /**
   * The priority of this Interceptor. The lower the earlier the interceptor will be iterated.
   */
  int priority();

}
