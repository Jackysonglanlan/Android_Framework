/*
 * Created on Jul 24, 2011 TODO
 */
package jacky.song.android.framework.core.decorator.adapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

/**
 * Decorate {@link BaseAdapter}, apply Responsibility Chain Pattern.
 * 
 * @author Jacky.Song
 */
public class BaseAdapterDecorator extends BaseAdapter {
  
  private BaseAdapter target;
  
  private SparseArray<ViewInterceptor> chain = new SparseArray<ViewInterceptor>(4);// by estimate
  
  /**
   * Construct by a target BaseAdapter.
   * 
   * @param adapter
   *          the Adapter be decorated.
   */
  public BaseAdapterDecorator(BaseAdapter adapter) {
    target = adapter;
  }
  
  /**
   * Add the ViewInterceptor to intercept and change the Adapter's item view.
   */
  public void addItemViewInterceptor(ViewInterceptor interceptor) {
    chain.put(interceptor.priority(), interceptor);
  }
  
  @Override
  public int getViewTypeCount() {
    // this value must be get from target itself, for this method will only be called
    // when when the adapter is set on the the AdapterView, at which time the chain is empty.
    return target.getViewTypeCount();
  }
  
  @Override
  public int getItemViewType(int position) {
    for (int i = 0; i < chain.size(); i++) {
      ViewInterceptor interceptor = chain.valueAt(i);
      if (interceptor.acceptView(target, position)) {
        int viewType = interceptor.viewTypeCode();
        // if return IGNORE_ITEM_VIEW_TYPE, use index value instead, because the orig adapter has a "default" item view,
        // and its view type code is 0, so we have to start at 1
        return viewType == Adapter.IGNORE_ITEM_VIEW_TYPE ? i + 1 : viewType;
      }
    }
    return 0; // default type code
    //    return target.getItemViewType(position);
  }
  
  @Override
  public int getCount() {
    return target.getCount();
  }
  
  @Override
  public Object getItem(int position) {
    return target.getItem(position);
  }
  
  @Override
  public long getItemId(int position) {
    return target.getItemId(position);
  }
  
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View preView = null;
    View currView = null;
    // ///// Apply Pattern ///////
    for (int i = 0; i < chain.size(); i++) {
      ViewInterceptor interceptor = chain.valueAt(i);
      if (interceptor.acceptView(target, position)) {
        Object data = target.getItem(position);
        if (preView == null) {
          // first node in the chain, pass convertView
          preView = interceptor.getView(position, data, convertView, parent);
        }
        else {
          // the rest nodes, pass view down along the chain
          currView = interceptor.getView(position, data, preView, parent);
          if (currView != null) {// if the previous one handled it
            preView = currView;// ready to pass it to the next
            currView = null;// clean
          }
          else {
            // if someone end the chain by returning null, it's done
            break;
          }
        }
      }
    }
    // preView == null means NO ViewInterceptor interested in this position's
    // view, so we return the default one
    
    return preView == null ? target.getView(position, convertView, parent) : preView;
  }

}
