package jacky.song.android.framework.core.injector.di;

// TODO javadoc
class SingletonObjectSource<T> extends ObjectSource<T> {

   private final BeanFactory dependencyInjectingObjectFactory;

   private T singleObject;

   private boolean dependenciesInjected;

   SingletonObjectSource(BeanFactory dependencyInjectingObjectFactory,
         BeanFactory aDependencyInjectingObjectFactory, T aSingleObject) {
      super(aDependencyInjectingObjectFactory);
      this.dependencyInjectingObjectFactory = dependencyInjectingObjectFactory;
      singleObject = aSingleObject;
   }

   @Override
   Class<?> getObjectType() {
      return singleObject.getClass();
   }

   @Override
   public T getObject() {
      if (!dependenciesInjected) {
         new DependencyInjector(dependencyInjectingObjectFactory).injectDependenciesForClassHierarchy(singleObject);
         dependenciesInjected = true;
      }
      return singleObject;
   }

   @Override
   public String toString() {
      // TODO Auto-generated method stub
      return super.toString();
   }
}