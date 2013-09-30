/*
 * Created on Jul 24, 2011 TODO
 */
package jacky.song.android.framework.core.decorator.action;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public interface ItemSelectedAction extends OnItemSelectedListener, UIAction {

   /**
    * Are you interested in the action?
    * 
    * @param parent
    * @return true if you are, then your Action will be invoked, false if you aren't
    */
   boolean acceptOnNothingSelected(AdapterView<?> parent);

   /**
    * Are you interested in the action?
    * 
    * @param parent
    * @param view
    * @param position
    * @param id
    * @return true if you are, then your Action will be invoked, false if you aren't
    */
   boolean acceptOnItemSelected(AdapterView<?> parent, View view, int position, long id);

}
