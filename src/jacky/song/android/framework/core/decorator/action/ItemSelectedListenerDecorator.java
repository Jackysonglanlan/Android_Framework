/*
 * Created on Jul 24, 2011
 *
 * TODO
 */
package jacky.song.android.framework.core.decorator.action;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;


/**
 * Decorate a regular {@link OnItemSelectedListener}, apply Composite Pattern.
 * 
 * @author Jacky.Song
 */
public class ItemSelectedListenerDecorator extends ActionDecorator<ItemSelectedAction> implements
      OnItemSelectedListener {


   @Override
   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      for (ItemSelectedAction action : actions.values()) {
         if (action.acceptOnItemSelected(parent, view, position, id))
            action.onItemSelected(parent, view, position, id);
      }
   }

   @Override
   public void onNothingSelected(AdapterView<?> parent) {
      for (ItemSelectedAction action : actions.values()) {
         if (action.acceptOnNothingSelected(parent))
            action.onNothingSelected(parent);
      }
   }

}
