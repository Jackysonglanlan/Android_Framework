/*
 * Created on Aug 15, 2011 TODO
 */
package jacky.song.android.framework.core.injector.di;

public enum BeanScope {

   Singleton {

      @Override
      public <T, S extends T> void registerBean(BeanFactory factory, Class<T> injectType, Class<S> implType) {
         factory.registerSingletonClass(injectType, implType);
      }
   },

   Prototype {

      @Override
      public <T, S extends T> void registerBean(BeanFactory factory, Class<T> injectType, Class<S> implType) {
         factory.registerClass(injectType, implType);
      }
   },

   ;

   public abstract <T, S extends T> void registerBean(BeanFactory factory, Class<T> injectType, Class<S> implType);
}
