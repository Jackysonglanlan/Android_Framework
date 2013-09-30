/*
 * Created on Jul 26, 2011
 *
 * TODO
 */
package jacky.song.android.util;

import android.util.SparseArray;

public class Cache<T> {

   private SparseArray<T> cache;

   public Cache() {
      cache = new SparseArray<T>();
   }

   public Cache(int initialCapacity) {
      cache = new SparseArray<T>(initialCapacity);
   }

   /**
    * Cache and return the cached value.
    */
   public T cache(int key, T value) {
      T v = cache.get(key);
      if (v == null)
         cache.put(key, value);
      return cache.get(key);
   }

   public void clear() {
      cache.clear();
   }

   public T get(int key) {
      return cache.get(key);
   }

   public boolean contains(int key) {
      return get(key) != null;
   }

   public boolean containsValue(T value) {
      return cache.indexOfValue(value) > 0;
   }

}
