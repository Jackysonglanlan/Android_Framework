/*
 * Created on Aug 15, 2011
 * 
 * TODO
 */
package jacky.song.demo.pagea;

import jacky.song.android.framework.component.activity.Module;
import jacky.song.android.framework.core.injector.anno.Inject;
import jacky.song.demo.di.BarManager;
import jacky.song.demo.di.Client;
import jacky.song.demo.di.FooManager;

public class DITestModule extends Module {
  
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
  
  @Override
  public void aquireResources() {
    System.out.println("----- test DI ----");
    fooM1.print();
    fooM2.print();
    barM1.showRealType(fooM1);
    barM2.showRealType(fooM2);
    System.out.println("barM1: " + barM1);
    System.out.println("barM2: " + barM2);
    fooLazy.print();
    fooOverride.print();
    System.out.println("----- test DI done ----");
  }
  
  @Override
  public void releaseResources() {}

}
