/*
 * Created on Aug 10, 2011
 *
 * TODO
 */
package jacky.song.android.framework.core.injector.anno.res;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface InjectIncomingIntent {

   /**
    * whether to reserve the previous intent value.
    */
   boolean reserveExtras() default true;
}
