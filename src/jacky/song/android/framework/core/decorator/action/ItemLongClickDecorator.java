/*
 * Created on Jul 25, 2011
 *
 * TODO
 */
package jacky.song.android.framework.core.decorator.action;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;


public class ItemLongClickDecorator extends ActionDecorator<ItemLongClickAction> implements OnItemLongClickListener {

   @Override
   public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
      for (ItemLongClickAction action : actions.values()) {
         if (action.acceptItemLongClick(parent, view, position, id))
            action.onItemLongClick(parent, view, position, id);
      }
      return true;
   }

}
