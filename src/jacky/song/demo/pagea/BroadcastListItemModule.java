/*
 * Created on Jul 24, 2011 TODO
 */
package jacky.song.demo.pagea;

import jacky.song.R;
import jacky.song.android.framework.component.activity.Module;
import jacky.song.android.framework.core.decorator.action.ItemClickAction;
import android.view.View;
import android.widget.AdapterView;

public class BroadcastListItemModule extends Module {

   @Override
   public void prepareView() {
      addItemClickActionTo(R.id.list1, new ItemClickAction() {

         @Override
         public int priority() {
            return 1;
         }

         @Override
         public boolean accept(AdapterView<?> parent, View view, int position, long id) {
            return position % 2 == 0;
         }

         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            broadcastTo(PageA.class, view);
         }

      });
   }

   @Override
   public void aquireResources() {
   }

   @Override
   public void refreshView() {
   }

   @Override
   public void releaseResources() {
   }

   @Override
   public void destoryView() {
   }
}
