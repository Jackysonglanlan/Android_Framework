/*
 * Created on Jul 25, 2011
 *
 * TODO
 */
package jacky.song.android.framework.core.decorator.action;

import java.util.SortedMap;
import java.util.TreeMap;

public abstract class ActionDecorator<T extends UIAction> {

   protected SortedMap<Integer, T> actions = new TreeMap<Integer, T>();// by estimate

   public void addAction(T action) {
      actions.put(action.priority(), action);
   }

}
