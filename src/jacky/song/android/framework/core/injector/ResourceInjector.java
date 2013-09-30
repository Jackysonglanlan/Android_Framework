/*
 * Created on Aug 10, 2011 TODO
 */
package jacky.song.android.framework.core.injector;

import jacky.song.android.framework.core.injector.anno.res.*;
import jacky.song.android.framework.core.visitor.Visitor;
import jacky.song.android.framework.core.visitor.VisitorManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;

/**
 * Used to decouple the UI related codes from Module by applying <b>Dependency Injection</b> idea.
 * 
 * @author Jacky.Song
 */
public class ResourceInjector {
  
  private static ResourceLoader findResourceLoader(Annotation anno) {
    // Asserts, special treatment
    if (anno.annotationType() == InjectAssets.class) return Resources.Assets;
    
    // Preference
    ResourceLoader loader = Preferences.findLoader(anno);
    if (loader != null) return loader;
    
    // Resource
    InjectResource inject = (InjectResource) anno;
    return inject.type();
  }
  
  // // ----------- Usage -------- //////

  public static void injectResourcesToVisitor(Context context, Visitor visitor) {
    if (!VISITOR_ANNO_FIELDS_CACHE.containsKey(visitor.getClass())) return;// no annotated field, skip
      
    Set<Field> annotatedFields = VISITOR_ANNO_FIELDS_CACHE.get(visitor.getClass());
    try {
      // //// start injecting ///////
      for (Field field : annotatedFields) {
        // load
        Object value = ANNO_FIELD_VALUE_CACHE.get(field);
        if (value == null) {
          Annotation anno = field.getAnnotations()[0];// can only be one
          value = findResourceLoader(anno).loadResource(context, anno);
        }
        // inject
        field.set(visitor, value);
        // cache
        ANNO_FIELD_VALUE_CACHE.put(field, value);
      }
    }
    catch (Exception e) {
      throw new RuntimeException("Fatal Error when inject Visitor: " + visitor.getClass(), e);
    }
    // System.out.println(INTENT_FIELD_CACHE);
  }
  
  /**
   * Inject Intent to the annotated field.
   */
  public static void injectIntent(Visitor visitor, Intent newIntent, Intent oldIntent) {
    if (!INTENT_FIELD_CACHE.containsKey(visitor.getClass())) return;
    
    Field intentField = INTENT_FIELD_CACHE.get(visitor.getClass());
    // System.out.println(oldIntent == newIntent);
    try {
      // reserve if necessary
      if ((oldIntent != newIntent) && intentField.getAnnotation(InjectIncomingIntent.class).reserveExtras()) {
        Bundle newData = newIntent.getExtras();
        newData.putAll(oldIntent.getExtras());
        newIntent.putExtras(newData);
      }
      // inject
      intentField.set(visitor, newIntent);
    }
    catch (Exception e) {
      throw new RuntimeException("Can't inject Intent field: " + intentField.getName() + " in " + visitor.getClass(), e);
    }
  }
  
  public static void cleanCacheOnDestory(Visitor visitor) {
    if (VISITOR_ANNO_FIELDS_CACHE.containsKey(visitor.getClass()))
      for (Field field : VISITOR_ANNO_FIELDS_CACHE.get(visitor.getClass())) {
        ANNO_FIELD_VALUE_CACHE.remove(field);// clean all the values to reclaim memory
      }
  }
  
  // // ----------- Prepare -------- //////
  
  // //// all the Reflection Object cached below shouldn't consume much memory, for these objects have already
  // //// been there when JVM started. It always stay in the heap whether or not you refer it.
  // //// And it can never be recycled, it stays in the "Permanent Area".
  
  private static final Set<Class<? extends Annotation>> ANNOTATION_TYPES = new HashSet<Class<? extends Annotation>>(
      Arrays.asList(InjectIncomingIntent.class, InjectAssets.class, InjectResource.class, SharedPrefBoolean.class,
          SharedPrefFloat.class, SharedPrefInt.class, SharedPrefLong.class, SharedPrefString.class));
  
  private static final Map<Class<? extends Visitor>, Set<Field>> VISITOR_ANNO_FIELDS_CACHE = new HashMap<Class<? extends Visitor>, Set<Field>>(
      (int) (VisitorManager.visitorCount() * 0.8));// 80%, by estimate, not all Visitors need Resource Injection
  
  // A Visitor only has one Intent field
  private static final Map<Class<? extends Visitor>, Field> INTENT_FIELD_CACHE = new HashMap<Class<? extends Visitor>, Field>(
      VisitorManager.visitorCount() / 2);// only half of the Visitors need processing Intent, by estimate
  
  // this cache contains Runtime objects which may suppose to be recycled, so it should be cleaned when there's no need.
  private static final Map<Field, Object> ANNO_FIELD_VALUE_CACHE = new HashMap<Field, Object>(
      VISITOR_ANNO_FIELDS_CACHE.size());
  
  private static void collectAnnotations(Class<? extends Visitor> vType) {
    Field[] fields = vType.getDeclaredFields();
    for (Field field : fields) {
      // precheck
      Annotation anno = detectAnno(field);
      
      if (anno == null) continue;// skip, can't handle
        
      // pass, we can handle
      field.setAccessible(true);
      
      if (anno.annotationType() == InjectIncomingIntent.class)
        cacheIntentField(vType, field);// intent field to INTENT_FIELD_CACHE
      else cacheAnnoField(vType, field);// others to ANNO_FIELD_CACHE
    }
  }
  
  // return the annotation we are about to handle, or null if we can't handle.
  // throws RuntimeException if there is something wrong
  private static Annotation detectAnno(Field field) {
    // find Annotation
    Annotation[] annos = field.getAnnotations();
    if (annos.length > 1) throw new RuntimeException("Only one Annotation is allowed !");
    if (annos.length == 1 && ANNOTATION_TYPES.contains(annos[0].annotationType())) return annos[0];
    return null;
  }
  
  private static void cacheAnnoField(Class<? extends Visitor> vType, Field field) {
    Set<Field> set = VISITOR_ANNO_FIELDS_CACHE.get(vType);
    // lazy loading cache
    if (set == null) {
      set = new HashSet<Field>(5);// every Visitor has 5 fields that need to be injected, by estimate
      VISITOR_ANNO_FIELDS_CACHE.put(vType, set);
    }
    set.add(field);
  }
  
  private static void cacheIntentField(Class<? extends Visitor> vType, Field field) {
    if (!INTENT_FIELD_CACHE.containsKey(vType)) INTENT_FIELD_CACHE.put(vType, field);
  }
  
  static {
    Map<Class<? extends Context>, SparseArray<Class<? extends Visitor>>> map = VisitorManager.getVisitorClassMap();
    for (Class<? extends Context> activityType : map.keySet()) {
      SparseArray<Class<? extends Visitor>> vTypes = map.get(activityType);
      for (int i = 0; i < vTypes.size(); i++) {
        Class<? extends Visitor> vType = vTypes.valueAt(i);
        collectAnnotations(vType);
      }
    }
    // System.out.println(VISITOR_ANNO_FIELDS_CACHE);
    // System.out.println(INTENT_FIELD_CACHE);
  }

}
