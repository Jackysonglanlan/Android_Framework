package jacky.song.android.framework.core.injector.di;

import static java.lang.String.format;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import jacky.song.android.framework.core.injector.anno.Inject;

/**
 * Given a DependencyInjectingObjectFactory, injects dependencies into a class.
 */
public class DependencyInjector {
  
  private BeanFactory dependencyInjectingObjectFactory;
  
  public DependencyInjector(BeanFactory aDependencyInjectingObjectFactory) {
    dependencyInjectingObjectFactory = aDependencyInjectingObjectFactory;
  }
  
  @SuppressWarnings("unchecked")
  public <T> void injectDependenciesForClassHierarchy(T anObject) {
    // the reflection API requires that each class in the hierarchy be considered
    // start with the lowest class in the hierarchy
    Class<?> interfaceOfObject = anObject.getClass();
    
    do {
      // inject the dependencies for this class
      injectDependenciesForSingleClass(anObject, (Class<T>) interfaceOfObject);
      
      // move on up the class hierarchy...
      interfaceOfObject = interfaceOfObject.getSuperclass();
      
      // until the top is reached
    } while (interfaceOfObject != null);
  }
  
  private <T, S extends T> void injectDependenciesForSingleClass(S anObject, Class<T> aClass) {
    // for each
    Field targetField = null;
    try {
      for (Field field : aClass.getDeclaredFields()) {
        field.setAccessible(true);
        
        // if the field is annotated, and it has not already been set (possibly through injection)...
        if (field.isAnnotationPresent(Inject.class) && field.get(anObject) == null) {
          targetField = field;
          
          Class<?> classOfDependency = dependencyInjectingObjectFactory.getFieldOverrideImpl(field);
          
          // if it's null, means no override, that's a default impl
          if (classOfDependency == null) {
            classOfDependency = field.getType();
          }
          
          final Object injectedValue;
          
          Inject anno = field.getAnnotation(Inject.class);
          if (anno.lazy() && classOfDependency.isInterface()) {
            // use Proxy to implement lazy loading, but performance is a considerable issue
            injectedValue = createProxy(classOfDependency, anObject, field);
          }
          // not lazy, or the dependency class isn't an interface, so we can't use Proxy
          else {
            injectedValue = dependencyInjectingObjectFactory.getObject(classOfDependency);
          }
          
          // inject
          field.set(anObject, injectedValue);
        }
      }// for end
    }
    catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException("Unable to access field " + targetField, e);
    }
  }
  
  @SuppressWarnings("unchecked")
  private <T> T createProxy(final Class<T> aClass, final Object obj, final Field field) {
    // TODO optimize this to replace the proxy once the object is lazily created
    return (T) Proxy.newProxyInstance(BeanFactory.class.getClassLoader(), new Class[] { aClass },
        new InvocationHandler() {
          
          @Override
          public Object invoke(Object aProxy, Method aMethod, Object[] anArrayOfArguments) throws Throwable {
            final ObjectSource<T> objectSource = dependencyInjectingObjectFactory.getObjectSource(aClass);
            if (objectSource == null) { throw new RuntimeException(format(
                "No source was registered for the dependecy of type %s.", aClass.getName())); }
            
            T realImpl = objectSource.getObject();
            
            // once one of the methods is invoked on the Proxy, the realImpl takes part in, replace the Proxy
            field.set(obj, realImpl);
            // so, there's no reflection invocation anymore, no performance issue.
            // here, the bean has no reference to this Proxy, it refers directly to the "real" object, therefore
            // this Proxy is eligible for reclaimation.
            
            // but for the 1st invocation, we have to return result, so this is the only time we use
            // reflection to invoke method.
            return aMethod.invoke(realImpl, anArrayOfArguments);
          }
        });
  }

}
