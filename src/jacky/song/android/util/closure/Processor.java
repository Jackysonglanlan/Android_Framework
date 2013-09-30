/*
 * Created on Jul 25, 2011
 *
 * TODO
 */
package jacky.song.android.util.closure;

/**
 * The closure for "Logic Process"。
 * 
 * @author Jacky.Song
 */
public interface Processor<IN, OUT> {

   OUT process(IN data);
}
