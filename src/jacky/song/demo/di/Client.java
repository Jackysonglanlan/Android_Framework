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
public class Client {

   @Inject
   FooManager fooM1;

   @Inject
   FooManager fooM2;

   @Inject
   BarManager barM1;

   @Inject
   BarManager barM2;

   @Inject(lazy = true)
   FooManager fooLazy;

   @Inject
   FooManager fooOverride;

   public void go() {
      fooM1.print();
      fooM2.print();
      barM1.showRealType(fooM1);
      barM2.showRealType(fooM2);

      fooLazy.print();
      fooOverride.print();
   }

}
