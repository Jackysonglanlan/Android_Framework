/*
 * Created on Jul 22, 2011 TODO
 */
package jacky.song.android.framework.core.visitor;

import jacky.song.android.framework.core.injector.di.BeanFactory;
import jacky.song.android.util.Log;
import jacky.song.android.util.closure.Closure;

import java.io.IOException;
import java.util.*;

import android.content.Context;
import android.util.SparseArray;

/**
 * Central manager of Visitor.
 * <p>
 * Core component of this framework.
 * 
 * @author Jacky.Song
 */
public final class VisitorManager {
  
  // [ Context.class -> [priority -> Visitor.class] ]
  private static final Map<Class<? extends Context>, SparseArray<Class<? extends Visitor>>> VISITOR_CLASS_MAP;
  
  // [ Context.class -> Visitor ]
  private static final Map<Class<? extends Context>, SparseArray<Visitor>> VISITOR_CACHE;
  
  // the refine factor, controls the size of VISITOR_CACHE in order to precisely use memory
  // 1.25 means 1/4 larger than the actual need. These extra room is for the dynamic loaded Visitors.
  private static final float SPARSE_ARRAY_LOAD_FACTOR = 1.25f;

  static {
    // read properties file
    Properties pro = new Properties();
    try {
      pro.load(VisitorManager.class.getResourceAsStream("/assets/modules.properties"));
    }
    catch (IOException e) {
      Log.ad("Fatal error !!! Can't find modules.properties file in assets folder", e);
      throw new RuntimeException("Fatal error !!! Can't find modules.properties file in assets folder", e);
    }

    Set<String> keys = new TreeSet<String>(new Comparator<String>() {// sort by priority

          @Override
          public int compare(String key1, String key2) {
            return Integer.parseInt(key1.split("-")[1]) - Integer.parseInt(key2.split("-")[1]);
          }
        });
    
    keys.addAll((Set) pro.keySet());
    
    // init data structure with proper size
    VISITOR_CLASS_MAP = new HashMap<Class<? extends Context>, SparseArray<Class<? extends Visitor>>>(
        (int) (keys.size() * SPARSE_ARRAY_LOAD_FACTOR)); // make extra room
    VISITOR_CACHE = new HashMap<Class<? extends Context>, SparseArray<Visitor>>(VISITOR_CLASS_MAP.size());

    loadVisitors(pro, keys);
  }
  
  // /////// initialization /////////
  
  private static void preconditionCheck(Class<?> targetClass, Class<?> visitorClass) {
    if (!Context.class.isAssignableFrom(targetClass))
      throw new IllegalArgumentException(targetClass + " does not inherit Context");
    if (!Visitor.class.isAssignableFrom(visitorClass))
      throw new IllegalArgumentException(visitorClass + " does not implement Visitor");
  }
  
  private static void loadVisitors(Properties pro, Set<String> keys) {
    if (VisitorManager.isVisitorsInitialized()) return;// init only once, no performance issue.
    try {
      for (String key : keys) {
        // key format: foo.bar.MyActivity-1
        String[] arr = key.split("-");
        String priority = arr[1];
        String targetClassName = arr[0];
        // value format: foo.bar.MyVisitor
        String visistorClassName = pro.getProperty(key);
        
        // init and register to VisitorLocator
        
        Class<?> targetClass = Class.forName(targetClassName);
        Class<?> visitorClass = Class.forName(visistorClassName);
        
        preconditionCheck(targetClass, visitorClass);
        
        // thanks to the TreeSet, it gurantees that the class will be added in priority order
        VisitorManager.registerVisitorClass((Class) targetClass, (Class) visitorClass, Integer.parseInt(priority));
        
        // register for inject dependency, visitors should be prototype, or they'll never be reclaimed by GC
        BeanFactory.INSTANCE.registerClass(visitorClass, (Class) visitorClass);
      }
      VisitorManager.initFinished();
    }
    catch (Exception e) {
      Log.ad("Load visitors failed", e);
      throw new RuntimeException("Fatal error !!! Can't initialize VisitorLocator", e);
    }
    // Log.ad("Load visitors: " + VISITOR_CLASS_MAP);
  }
  
  // /////// usage /////////
  
  /**
   * Apply Visitor Pattern.
   */
  public static void visit(Context context) {
    SparseArray<Visitor> visitors = VISITOR_CACHE.get(context.getClass());
    if (visitors != null) {
      for (int i = 0; i < visitors.size(); i++) {
        visitors.valueAt(i).accept(context);
      }
    }
    else if (VISITOR_CLASS_MAP.containsKey(context.getClass())) {
      SparseArray<Class<? extends Visitor>> visitorClasses = VISITOR_CLASS_MAP.get(context.getClass());
      visitors = new SparseArray<Visitor>(visitorClasses.size()); // same size as class cache
      try {
        // init Visitors
        for (int i = 0; i < visitorClasses.size(); i++) {
          Class<? extends Visitor> vType = visitorClasses.valueAt(i);
          
          //          Visitor visitor =  vType.newInstance();

          // dependency injection
          Visitor visitor = BeanFactory.INSTANCE.getObject(vType);
          
          // pass reference
          visitor.accept(context);

          visitors.append(visitorClasses.keyAt(i), visitor);// same structure as the one in VISITOR_CLASS_MAP
        }
        // cache
        VISITOR_CACHE.put(context.getClass(), visitors);
        // System.out.println("VisitorLocator cache added: " + readableCache);
      }
      catch (Exception e) {
        String msg = "Apply Visitor Pattern Error on " + context.getClass() + " :\n" + e;
        throw new RuntimeException(msg, e);
      }
    }
  }
  
  /**
   * Delegate actions to the relevant Visitors of the specified Context.
   * 
   * @param context
   *          Context who wants to delegate its actions
   * @param logic
   *          the delegate action
   * @param priorityByASC
   *          if true, the delegate order will be 'ASC', otherwise is 'DESC'
   */
  public static void delegateToVisitors(Class<? extends Context> context, Closure<Visitor> logic, boolean priorityByASC) {
    SparseArray<Visitor> visitors = VISITOR_CACHE.get(context);
    if (visitors != null) {
      int size = visitors.size();
      for (int i = 0; i < size; i++) {
        Visitor visitor = visitors.valueAt(priorityByASC ? i : size - i - 1);
        logic.doWith(visitor);
      }
    }
  }
  
  // ////// Visitor Management ////////
  
  /**
   * Register a Visitor class to associate with an Context class.
   * 
   * @param clazz
   *          the Context's class
   * @param visitor
   *          the Visitor class which is about to associate
   * @param priority
   *          the smaller, the earlier a Visitor can be informed when the associated Context's lifecycle changes.
   */
  private static void registerVisitorClass(Class<? extends Context> clazz, Class<? extends Visitor> visitor,
      int priority) {
    if (!VISITOR_CLASS_MAP.containsKey(clazz)) {
      VISITOR_CLASS_MAP.put(clazz, new SparseArray<Class<? extends Visitor>>(8));
      // the num. of visitors every Context have is by estimate
    }
    // priority as key, we can use append here, for we sorted
    VISITOR_CLASS_MAP.get(clazz).append(priority, visitor);
  }
  
  // ////////// Dynamic loading / unloading Visitors
  
  /**
   * Load a Visitor class to associate with an Context class.
   * 
   * @param clazz
   *          the Context's class
   * @param visitor
   *          the Visitor class which is about to associate
   * @param priority
   *          the smaller, the earlier a Visitor can be informed when the associated Context's lifecycle changes.
   */
  public static void loadVisitor(Class<? extends Context> clazz, Class<? extends Visitor> visitor, int priority) {
    registerVisitorClass(clazz, visitor, priority);
  }
  
  /**
   * Unload Visitor of a given Context.
   * 
   * @param clazz
   *          the Context you want to unload functions
   * @param priority
   *          priority of the Visitor you want to unload
   */
  public static void unloadVisitor(Class<? extends Context> clazz, int priority) {
    if (VISITOR_CLASS_MAP.containsKey(clazz)) {
      VISITOR_CLASS_MAP.get(clazz).delete(priority);
    }
  }
  
  /**
   * Destory all Visitors which associate with the given Context class, so GC can reclaim the memeory.
   * <p>
   * This method is usually invoked in Context's {@code onDestory()} method.
   * 
   * @param clazz
   *          The destoried Context's class
   */
  public static void unregisterVisitors(Context context) {
    VISITOR_CACHE.remove(context.getClass());// destory cache data, reclaim memory. It's better than invoke clear()
    // System.out.println("VisitorLocator cache removed: " + readableCache);
  }
  
  // ///// Communication /////////
  
  /**
   * Send request to a Visitor with specified type and data.
   * 
   * @param type
   *          the Request type
   * @param data
   *          data along with the request.
   * @return the response the invoker wants to get from this Visitor after request, if any.
   * @throws IllegalArgumentException
   *           if there's no such a Visitor
   */
  public static final Object sendRequest(Class<? extends Context> context, int priority, int type, Object data) {
    Visitor visitor = locateVisitor(context, priority);
    if (visitor == null)
      throw new IllegalArgumentException("No such a Visitor: [" + context.getName() + "-" + priority + "]");
    return visitor.onReceiveRequest(type, data);
  }
  
  /**
   * Broadcost data to all the Modules in the specified Context.
   * 
   * @param context
   *          all the Modules in this Context will receive the data you published
   * @param data
   *          the data you want to publish
   */
  public final static void broadcastTo(Class<? extends Context> context, final Object data) {
    delegateToVisitors(context, new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor visitor) {
        visitor.onNotify(data);
      }
    }, true);
  }
  
  // ///// Lifecycle /////////
  
  private static boolean visitorsInitialized = false;
  
  private static void initFinished() {
    visitorsInitialized = true;
  }
  
  public static boolean isVisitorsInitialized() {
    return visitorsInitialized;
  }
  
  // ///// Visit /////////
  
  /**
   * Locate the Visitor of the specified Context.
   * 
   * @return null if can't find the relevant Visitor
   */
  public static Visitor locateVisitor(Class<? extends Context> context, int priority) {
    if (VISITOR_CACHE.containsKey(context)) return VISITOR_CACHE.get(context).get(priority);
    return null;
  }
  
  public static int visitorCount() {
    return VISITOR_CLASS_MAP.size();
  }

  public static Map<Class<? extends Context>, SparseArray<Class<? extends Visitor>>> getVisitorClassMap() {
    return Collections.unmodifiableMap(VISITOR_CLASS_MAP);
  }
  
  public static Map<Class<? extends Context>, SparseArray<Visitor>> getVisitorCache() {
    return Collections.unmodifiableMap(VISITOR_CACHE);
  }

}
