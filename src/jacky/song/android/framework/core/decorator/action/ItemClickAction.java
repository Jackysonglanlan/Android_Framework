/*
 * Created on Jul 24, 2011 TODO
 */
package jacky.song.android.framework.core.decorator.action;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public interface ItemClickAction extends UIAction, OnItemClickListener {

   /**
    * Are you interested in the action happened on this view?
    * 
    * @param parent
    * @param view
    * @param position
    * @param id
    * @return true if you are, then your Action will be invoked, false if you aren't
    */
   boolean accept(AdapterView<?> parent, View view, int position, long id);

}
