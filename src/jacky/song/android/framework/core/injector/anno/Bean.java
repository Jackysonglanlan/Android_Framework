package jacky.song.android.framework.core.injector.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jacky.song.android.framework.core.injector.di.BeanScope;

/**
 * Use this annotation to indicate field upon which a class is dependent. The dependency injecting framework will inject
 * a value into the field, based on the class or object registered for the type of the field.
 * 
 * The annotation works on any field (regardless of access, and regardless of being static or not).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Bean {

   BeanScope type() default BeanScope.Singleton;
}
