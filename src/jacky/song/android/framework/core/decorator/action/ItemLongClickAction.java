/*
 * Created on Jul 25, 2011
 *
 * TODO
 */
package jacky.song.android.framework.core.decorator.action;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;


public interface ItemLongClickAction extends UIAction, OnItemLongClickListener {

   boolean acceptItemLongClick(AdapterView<?> parent, View view, int position, long id);
}
