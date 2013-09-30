/*
 * Created on Jul 22, 2011 TODO
 */
package jacky.song.android.framework.component.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import jacky.song.android.framework.core.decorator.action.*;
import jacky.song.android.framework.core.decorator.adapter.BaseAdapterDecorator;
import jacky.song.android.framework.core.decorator.adapter.ViewInterceptor;
import jacky.song.android.framework.core.visitor.Visitor;
import jacky.song.android.framework.core.visitor.VisitorManager;
import jacky.song.android.util.Log;

/**
 * Convenience Class for implementing {@link Visitor}.
 * <p>
 * Developers should extend this class, rather than implement the {@link Visitor} directly.
 * <p>
 * It also provide some convenience methods to better modulize the various Listeners,<br>
 * and add "Communication among Visitors" capability.
 * 
 * @author Jacky.Song
 */
public abstract class Module implements Visitor {
  
  protected Activity context;
  
  protected LayoutInflater inflater;
  
  protected Resources resources;
  
  @Override
  public void setInflater(LayoutInflater inflater) {
    this.inflater = inflater;
  }
  
  @Override
  public void setResources(Resources resources) {
    this.resources = resources;
  }

  @Override
  public final void accept(Context ctx) {
    this.context = (Activity) ctx;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.d(this.getClass().getSimpleName() + " onCreate...");
  }
  
  @Override
  public void onRestart() {
    Log.d(this.getClass().getSimpleName() + " onRestart...");
  }
  
  @Override
  public void onStart() {
    Log.d(this.getClass().getSimpleName() + " onStart...");
  }
  
  @Override
  public void onRestoreInstanceState(Bundle savedInstanceState) {
    Log.d(this.getClass().getSimpleName() + " onRestoreInstanceState...bundle: " + savedInstanceState);
  }
  
  @Override
  public void onNewIntent(Intent intent) {
    Log.d(this.getClass().getSimpleName() + " onNewIntent... intent: " + intent);
  }
  
  @Override
  public void onResume() {
    Log.d(this.getClass().getSimpleName() + " onResume...");
  }
  
  @Override
  public void onPause() {
    Log.d(this.getClass().getSimpleName() + " onPause...");
  }
  
  @Override
  public void onSaveInstanceState(Bundle outState) {
    Log.d(this.getClass().getSimpleName() + " onSaveInstanceState... bundle: " + outState);
  }
  
  @Override
  public void onStop() {
    Log.d(this.getClass().getSimpleName() + " onStop...");
  }
  
  @Override
  public void onDestroy() {
    Log.d(this.getClass().getSimpleName() + " onDestory...");
  }
  
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.d(this.getClass().getSimpleName() + " onActivityResult... requestCode:" + requestCode + " resultCode:"
        + resultCode + " intent:" + data);
  }
  
  // //////// Menu ////////
  
  @Override
  public void onBackPressed() {}
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return false;
  }
  
  @Override
  public void onOptionsMenuClosed(Menu menu) {}
  
  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {}
  
  @Override
  public boolean onContextItemSelected(MenuItem item) {
    return false;
  }
  
  @Override
  public void onContextMenuClosed(Menu menu) {}
  
  // //////// Visitor Communication ////////
  
  protected boolean isModuleExist(Class<? extends Activity> activity, int priority) {
    return VisitorManager.locateVisitor(activity, priority) != null;
  }

  // -- 1 to 1 -- //
  
  @Override
  public Object onReceiveRequest(int type, Object data) {
    Log.d(getClass().getSimpleName() + " onReceiveRequest() with DATA: " + data);
    return null;
  }
  
  /**
   * Send request with specified type and data.
   * 
   * @param type
   *          the Request type
   * @param data
   *          data along with the request.
   * @return the response the invoker wants to get from this Visitor after request, if any.
   */
  public final Object sendRequest(Class<? extends Activity> activity, int priority, int type, Object data) {
    return VisitorManager.sendRequest(activity, priority, type, data);
  }
  
  // -- 1 to N -- //
  
  @Override
  public void onNotify(Object data) {
    Log.d(getClass().getSimpleName() + " onNotify() get DATA: " + data);
  }
  
  private List<Visitor> observers;
  
  public final void addObserver(Visitor observer) {
    // lazy loading, no memory waste
    if (observers == null) {
      observers = new ArrayList<Visitor>(3);
    }
    observers.add(observer);
  }
  
  /**
   * Publish data to all your observers
   */
  public final void informObservers(Object data) {
    if (observers != null) {
      for (Visitor observer : observers) {
        observer.onNotify(data);
      }
    }
  }
  
  /**
   * Broadcast data to all the Modules in the specified activity.
   * 
   * @param activity
   *          all the Modules in this activity will receive the data you broadcast
   * @param data
   *          the data you want to publish
   */
  public final void broadcastTo(Class<? extends Activity> activity, Object data) {
    VisitorManager.broadcastTo(activity, data);
  }
  
  // //////// View relevent ///////
  
  @Override
  public void prepareView() {}
  
  @Override
  public void refreshView() {}
  
  @Override
  public void saveCrucialData() {}
  
  @Override
  public void destoryView() {}
  
  // //////// handle AdapterView ///////
  
  // -- BaseAdapter --//
  
  /**
   * Intercept the {@link AdapterView} of the specified UI element, the modify logic is defined in {@code ViewInterceptor}.
   * 
   * @param refId
   *          the reference id of the target UI element. <i>This element must have an AdapterView</i>.
   * @param interceptor
   *          the interceptor which intercept the view before it shows.
   */
  protected final void addItemViewToAdapterView(int refId, ViewInterceptor interceptor) {
    AdapterView<BaseAdapter> view = (AdapterView) context.findViewById(refId);
    
    BaseAdapter adapter = view.getAdapter();
    if (adapter == null || adapter.getClass() != BaseAdapterDecorator.class) {
      adapter = new BaseAdapterDecorator(adapter);
      view.setAdapter(adapter);
    }
    
    // now the adapter is hacked. we can add interceptors and start to work
    ((BaseAdapterDecorator) view.getAdapter()).addItemViewInterceptor(interceptor);
  }
  
  /**
   * Use this method to refresh the {@link AdapterView} after you modified the underlining Adapter.
   * 
   * @param refId
   *          the resouceId of the {@link AdapterView} you want to refresh
   */
  protected final void refreshAdapterView(int refId) {
    AdapterView<BaseAdapter> view = (AdapterView) context.findViewById(refId);
    view.getAdapter().notifyDataSetChanged();
  }
  
  // -- OnItemClickListener --//
  
  /**
   * Add {@link ItemClickAction} to an AdapterView and handle {@link #onItemClick()} event you are interested in.
   * 
   * @param adapterViewId
   *          the resource id of the target AdapterView
   * @param action
   *          the action you are willing to take
   */
  protected final void addItemClickActionTo(int adapterViewId, ItemClickAction action) {
    AdapterView<BaseAdapter> view = (AdapterView) context.findViewById(adapterViewId);

    OnItemClickListener listener = view.getOnItemClickListener();
    if (listener == null || listener.getClass() != ItemClickListenerDecorator.class) {
      listener = new ItemClickListenerDecorator();
      view.setOnItemClickListener(listener);
    }

    // now the listener is hacked
    ((ItemClickListenerDecorator) view.getOnItemClickListener()).addAction(action);
  }
  
  // -- OnItemSelectedListener --//
  
  /**
   * Add {@link ItemSelectedAction} to an AdapterView and handle {@link #onItemSelected()} event you are interested in.
   * 
   * @param adapterViewId
   *          the resource id of the target AdapterView
   * @param action
   *          the action you are willing to take
   */
  protected final void addItemSelectedActionTo(int adapterViewId, ItemSelectedAction action) {
    AdapterView<BaseAdapter> view = (AdapterView) context.findViewById(adapterViewId);
    
    OnItemSelectedListener listener = view.getOnItemSelectedListener();
    if (listener == null || listener.getClass() != ItemSelectedListenerDecorator.class) {
      listener = new ItemSelectedListenerDecorator();
      view.setOnItemSelectedListener(listener);
    }

    ((ItemSelectedListenerDecorator) view.getOnItemSelectedListener()).addAction(action);
  }
  
  // -- OnItemLongClickListener --//
  
  protected final void addItemLongClickActionTo(int adapterViewId, ItemLongClickAction action) {
    AdapterView<BaseAdapter> view = (AdapterView) context.findViewById(adapterViewId);
    
    OnItemLongClickListener listener = view.getOnItemLongClickListener();
    if (listener == null || listener.getClass() != ItemLongClickDecorator.class) {
      listener = new ItemLongClickDecorator();
      view.setOnItemLongClickListener(listener);
    }

    ((ItemLongClickDecorator) view.getOnItemSelectedListener()).addAction(action);
  }

}
