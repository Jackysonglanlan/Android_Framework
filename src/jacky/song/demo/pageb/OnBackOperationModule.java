/*
 * Created on Jul 30, 2011 TODO
 */
package jacky.song.demo.pageb;

import jacky.song.android.framework.component.activity.Module;

public class OnBackOperationModule extends Module {

   @Override
   public void onBackPressed() {
      context.finish();
   }

   @Override
   public void prepareView() {
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
