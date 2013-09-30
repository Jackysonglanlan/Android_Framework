/*
 * Created on 2011-10-12
 *
 * TODO
 */
package jacky.song;

import java.util.HashMap;
import java.util.Map;

public class SparseArray<T> {
  
  private Map<Integer, T> map;
  
  private Map<Integer, Integer> keyIndex;
  
  private int cursor;
  
  public SparseArray(int ca) {
    map = new HashMap<Integer, T>(ca);
    keyIndex = new HashMap<Integer, Integer>(map.size());
  }
  
  public SparseArray() {
    map = new HashMap<Integer, T>();
    keyIndex = new HashMap<Integer, Integer>();
  }
  
  public int size() {
    return map.size();
  }
  
  public void append(int key, T value) {
    map.put(key, value);
    keyIndex.put(key, cursor++);
  }
  
  public void put(int key, T value) {
    append(key, value);
  }
  
  public void delete(int key) {
    map.remove(key);
  }

  public T valueAt(int index) {
    return map.get(keyAt(index));
  }
  
  public int keyAt(int index) {
    for (Integer key : keyIndex.keySet())
      if (keyIndex.get(key).intValue() == index) return key;
    return -1;
  }
  
  public T get(int key) {
    return map.get(key);
  }
}
