package jacky.song.android.framework.core.injector.di;

import static java.lang.String.format;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import jacky.song.android.framework.core.injector.anno.Bean;
import jacky.song.android.framework.core.visitor.VisitorManager;

/**
 * Factory that creates objects with their dependencies injected. Object dependencies are required to be coded as fields (static or instance) annotated with the Dependency annotation. Supports object graph cycles through a proxy-based lazy-dependency-injection of interface types; class types require eager-dependency-injection.
 * <p>
 * Example usage:
 * 
 * <pre>
 * <code>
 *  // register implementations
 *  DIObjectFactory dependencyInjectingObjectFactory = new DIObjectFactory();
 *  dependencyInjectingObjectFactory.registerImplementationClass(Y.class, YImpl.class);
 *  dependencyInjectingObjectFactory.registerImplementationClass(X.class, XImpl.class);
 * 
 *  // obtain an instance of Y with its dependency on X pre-populated
 *  Y y1 = dependencyInjectingObjectFactory.getObject(Y.class);
 * </code>
 * </pre>
 */
public class BeanFactory {
  
  public static final BeanFactory INSTANCE = new BeanFactory();
  
  // [Bean Class -> ObjectSource]
  private Map<Class<?>, ObjectSource<?>> objectSourceCache = new HashMap<Class<?>, ObjectSource<?>>(
      (int) (VisitorManager.visitorCount() * 1.5));// visitors count + business beans count
  
  // [target override Field -> impl Class]
  private Map<Field, Class<?>> overrideImplMap = new HashMap<Field, Class<?>>();
  
  private BeanFactory() {
    Properties pro = new Properties();
    String key = null;
    String value = null;
    try {
      pro.load(BeanFactory.class.getResourceAsStream("/assets/beans.properties"));
      // sort the reg info, make sure all the "override injection" entries are
      // sorted below the "default injection"
      TreeSet<Object> set = new TreeSet<Object>(new Comparator<Object>() {
        
        @Override
        public int compare(Object o1, Object o2) {
          String s1 = (String) o1;
          String s2 = (String) o2;
          int i1 = s1.indexOf('-');
          int i2 = s2.indexOf('-');
          
          return (i1 - i2 == 0) ? -1 : i1 - i2;
        }
      });
      set.addAll(pro.keySet());
      // System.out.println(set);
      for (Object k : set) {
        // key format: foo.bar.ClassName(-fieldName)
        key = (String) k;
        value = (String) pro.get(key);
        String[] arr = key.split("-");// [0] -> className ,[1] -> fieldName
        
        Class<?> injectType = Class.forName(arr[0]);
        Class implType = Class.forName(value);
        
        // override field syntax
        if (arr.length > 1) {
          // here the injectType must be a concrete class
          // 1st, find the field user wants to override inject
          Field field = injectType.getDeclaredField(arr[1]);
          // 2nd, check if the field can be overrided
          if (canOverride(field.getType(), implType)) {
            overrideImplMap.put(field, implType);
            injectType = implType;// register the override impl as a concrete class
          }
          else continue;// can't override, skip
        }
        
        registerInjectClass(injectType, implType);
      }
    }
    catch (Exception e) {
      throw new RuntimeException("Fatal error when parsing entry: " + key + " - " + value, e);
    }
  }
  
  private boolean canOverride(Class<?> injectType, Class<?> overrideType) {
    return !objectSourceCache.containsKey(overrideType) && // it hasn't registered yet,
        objectSourceCache.containsKey(injectType)// the injectType must be exist now, for we sorted the reg info
        && // and it isn't the default inject type
        overrideType != objectSourceCache.get(injectType).getObjectType();
  }
  
  Class<?> getFieldOverrideImpl(Field field) {
    return overrideImplMap.get(field);
  }
  
  public <T, S extends T> void registerInjectClass(Class<T> injectType, Class<S> beanType) {
    Bean anno = beanType.getAnnotation(Bean.class);
    if (anno != null) {
      anno.type().registerBean(this, injectType, beanType);
    }
    else {
      throw new RuntimeException(injectType + " has no @Bean Annotation !");
    }
  }
  
  /**
   * Registers a class as the implementor of the type. A <b>new</b> instance of the class will be injected into every matching dependency.
   */
  public <T, S extends T> void registerClass(Class<T> anInterface, Class<S> anImplementationClass) {
    checkPreconditions(anInterface, anImplementationClass);
    objectSourceCache.put(anInterface, new ClassObjectSource<T, S>(this, anImplementationClass));
  }
  
  /**
   * Registers a class as the implementor of the type. A <b>single</b> instance of the class will be injected into every matching dependency.
   */
  public <T, S extends T> void registerSingletonClass(Class<T> anInterface, Class<S> anImplementationClass) {
    checkPreconditions(anInterface, anImplementationClass);
    objectSourceCache.put(anInterface, new ClassSingletonObjectSource<T, S>(this, this, anImplementationClass));
  }
  
  /**
   * Registers a single object as the implementor of the type. The object will be injected into every matching dependency.
   */
  public <T, S extends T> void registerObject(Class<T> anInterface, S anImplementationObject) {
    checkPreconditions(anInterface, anImplementationObject.getClass());
    objectSourceCache.put(anInterface, new SingletonObjectSource<T>(this, this, anImplementationObject));
  }
  
  /**
   * If the passed class was not previously registered, throws an IllegalArgumentException. Otherwise, based on the prior registrations, returns an object that is assignable to the passed type, and which has had all of its dependencies injected.
   */
  @SuppressWarnings("unchecked")
  public <T> T getObject(Class<T> aClass) {
    if (objectSourceCache.containsKey(aClass)) { return (T) objectSourceCache.get(aClass).getObject(); }
    
    throw new RuntimeException(format("No object source was registered for class %s.", aClass.getCanonicalName()));
  }
  
  @SuppressWarnings("unchecked")
  <T, S extends T> ObjectSource<S> getObjectSource(Class<T> aClass) {
    return (ObjectSource<S>) objectSourceCache.get(aClass);
  }
  
  // check class Not Already Registered and Can Instantiate
  private void checkPreconditions(Class<?> aClass, Class<?> anImplementationClass) {
    if (Modifier.isAbstract(anImplementationClass.getModifiers()))
      throw new IllegalArgumentException(anImplementationClass + " can't be instantiated !");
    if (objectSourceCache.containsKey(aClass)) { throw new IllegalArgumentException(format(
        "Unable to register for the class %s as it is already registered: %s.", aClass.getCanonicalName(),
        objectSourceCache.get(aClass).toString())); }
  }
}
