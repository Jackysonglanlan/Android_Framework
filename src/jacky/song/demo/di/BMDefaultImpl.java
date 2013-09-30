/*
 * Created on Aug 15, 2011
 *
 * TODO
 */
package jacky.song.demo.di;

import jacky.song.android.framework.core.injector.anno.Bean;
import jacky.song.android.framework.core.injector.anno.Inject;
import jacky.song.android.framework.core.injector.di.BeanScope;

@Bean(type = BeanScope.Prototype)
public class BMDefaultImpl implements BarManager {

   @Inject
   private FooManager fm;

   @Override
   public void showRealType(FooManager manager) {
      System.out.println(manager.getClass());
   }

   @Override
   public FooManager getFooManager() {
      return fm;
   }

   @Override
   public void setFooManager(FooManager fm) {
      this.fm = fm;
   }
}
