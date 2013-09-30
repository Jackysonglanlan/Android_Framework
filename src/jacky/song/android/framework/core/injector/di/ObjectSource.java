package jacky.song.android.framework.core.injector.di;

// TODO javadoc
abstract class ObjectSource<T> {
  
  BeanFactory dependencyInjectingObjectFactory;
  
  ObjectSource(BeanFactory aDependencyInjectingObjectFactory) {
    dependencyInjectingObjectFactory = aDependencyInjectingObjectFactory;
  }
  
  abstract T getObject();
  
  abstract Class<?> getObjectType();
  
}
