/*
 * Created on Jul 22, 2011 TODO
 */
package jacky.song.android.framework.core.visitor;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;

/**
 * Function module, responsible for implementing the required functions and rendering the corresponding UI element.
 * 
 * @author jackysong
 */
public interface Visitor {
  
  /**
   * Accept the Activity this Visitor is interested in.
   */
  void accept(Context activity);
  
  void setInflater(LayoutInflater inflater);
  
  void setResources(Resources resources);

  // //// Take over Activity's lifecycle callbacks ////////
  
  void onCreate(Bundle savedInstanceState);
  
  void onRestart();
  
  void onStart();
  
  void onRestoreInstanceState(Bundle savedInstanceState);
  
  void onNewIntent(Intent intent);
  
  void onResume();
  
  void onPause();
  
  void onSaveInstanceState(Bundle outState);
  
  void onStop();
  
  void onDestroy();
  
  // //// Other operation ////////
  
  void onActivityResult(int requestCode, int resultCode, Intent data);
  
  void onBackPressed();
  
  // //// Take over Activity's Menu operation ////////
  
  // // options ////
  
  boolean onCreateOptionsMenu(Menu menu);
  
  boolean onOptionsItemSelected(MenuItem item);
  
  void onOptionsMenuClosed(Menu menu);
  
  // // context ////
  
  void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo);
  
  boolean onContextItemSelected(MenuItem item);
  
  void onContextMenuClosed(Menu menu);
  
  // //// Visitor Cooperation ////////
  
  /**
   * Callback when other Visitors send you an request.
   * <p>
   * Used in <b><i>1 to 1 Communication among Visitors</i></b> scenario.
   * 
   * @param type
   *          request type
   * @param data
   *          data along with the request
   * @return the response of the request, if any
   */
  Object onReceiveRequest(int type, Object data);
  
  /**
   * Callback when someone send a message publicly.
   * <p>
   * Used in <b><i>1 to N Communication among Visitors</i></b> scenario.
   * 
   * @param data
   *          the data sent publicly
   */
  void onNotify(Object data);
  
  // //// Mandantory methods ////////
  
  void prepareView();
  
  void aquireResources();
  
  void refreshView();
  
  void saveCrucialData();
  
  void releaseResources();
  
  void destoryView();

}
