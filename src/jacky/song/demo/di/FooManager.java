/*
 * Created on Aug 15, 2011
 *
 * TODO
 */
package jacky.song.demo.di;


public interface FooManager {

   void print();

   void setBarManager(BarManager bm);

   BarManager getBarManager();
}
