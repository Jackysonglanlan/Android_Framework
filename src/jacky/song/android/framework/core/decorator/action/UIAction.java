/*
 * Created on Jul 25, 2011
 *
 * TODO
 */
package jacky.song.android.framework.core.decorator.action;


/**
 * An user performed Action.
 * 
 * @author Jacky.Song
 */
public interface UIAction {

   /**
    * The smaller, the higher, means the earlier invoked.
    * <p>
    * If two have the same priority, then the latter will replace the former.
    */
   int priority();

}
