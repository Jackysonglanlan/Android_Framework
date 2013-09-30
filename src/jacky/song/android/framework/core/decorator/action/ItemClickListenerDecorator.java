/*
 * Created on Jul 24, 2011
 *
 * TODO
 */
package jacky.song.android.framework.core.decorator.action;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Decorate a regular {@link OnItemClickListener}, apply Composite Pattern.
 * 
 * @author Jacky.Song
 */
public class ItemClickListenerDecorator extends ActionDecorator<ItemClickAction> implements OnItemClickListener {

   @Override
   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      for (ItemClickAction action : actions.values()) {
         if (action.accept(parent, view, position, id))
            action.onItemClick(parent, view, position, id);
      }
   }
   
}
