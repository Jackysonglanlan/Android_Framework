/*
 * Created on Aug 15, 2011
 *
 * TODO
 */
package jacky.song.demo.di;

import jacky.song.android.framework.core.injector.anno.Bean;
import jacky.song.android.framework.core.injector.anno.Inject;
import jacky.song.android.framework.core.injector.di.BeanScope;

@Bean(type = BeanScope.Singleton)
public class FMDefaultImpl implements FooManager {

   @Inject
   private BarManager bm;

   @Override
   public void print() {
      System.out.println("FooManagerImpl");
   }

   @Override
   public void setBarManager(BarManager bm) {
      this.bm = bm;
   }

   @Override
   public BarManager getBarManager() {
      return bm;
   }
}
