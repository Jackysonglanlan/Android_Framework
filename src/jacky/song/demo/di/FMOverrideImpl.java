/*
 * Created on Aug 15, 2011
 *
 * TODO
 */
package jacky.song.demo.di;

import jacky.song.android.framework.core.injector.anno.Bean;

@Bean
public class FMOverrideImpl implements FooManager {

   @Override
   public void print() {
      System.out.println("Another FooManagerImpl");
   }

   @Override
   public void setBarManager(BarManager bm) {
   }

   @Override
   public BarManager getBarManager() {
      return null;
   }
}
