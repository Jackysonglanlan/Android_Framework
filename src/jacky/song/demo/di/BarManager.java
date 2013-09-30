/*
 * Created on Aug 15, 2011
 *
 * TODO
 */
package jacky.song.demo.di;

public interface BarManager {

   void showRealType(FooManager manager);

   void setFooManager(FooManager fm);

   FooManager getFooManager();
}
