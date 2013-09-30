/*
 * Created on Aug 12, 2011
 *
 * TODO
 */
package jacky.song.android.framework.core.injector;

import java.lang.annotation.Annotation;

import android.content.Context;

interface ResourceLoader {

  Object loadResource(Context context, Annotation anno);
}
