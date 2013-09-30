/*
 * Created on Aug 12, 2011 TODO
 */
package jacky.song.android.framework.core.injector;

import jacky.song.android.framework.core.injector.anno.res.*;

import java.lang.annotation.Annotation;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * The Preference you want to inject in.
 * 
 * @author Jacky.Song
 */
public enum Preferences implements ResourceLoader {
  
  SharedPrefInt {
    
    @Override
    protected boolean accept(Class<? extends Annotation> type) {
      return type == SharedPrefInt.class;
    }
    
    @Override
    protected Object loadResource(Annotation anno, SharedPreferences sp) {
      SharedPrefInt pref = (SharedPrefInt) anno;
      return sp.getInt(pref.key(), pref.defaut());
    }
  },
  
  SharedPrefFloat {
    
    @Override
    protected boolean accept(Class<? extends Annotation> type) {
      return type == SharedPrefFloat.class;
    }
    
    @Override
    protected Object loadResource(Annotation anno, SharedPreferences sp) {
      SharedPrefFloat pref = (SharedPrefFloat) anno;
      return sp.getFloat(pref.key(), pref.defaut());
    }
  },
  
  SharedPrefBoolean {
    
    @Override
    protected boolean accept(Class<? extends Annotation> type) {
      return type == SharedPrefBoolean.class;
    }
    
    @Override
    protected Object loadResource(Annotation anno, SharedPreferences sp) {
      SharedPrefBoolean pref = (SharedPrefBoolean) anno;
      return sp.getBoolean(pref.key(), pref.defaut());
    }
  },
  
  SharedPrefLong {
    
    @Override
    protected boolean accept(Class<? extends Annotation> type) {
      return type == SharedPrefLong.class;
    }
    
    @Override
    protected Object loadResource(Annotation anno, SharedPreferences sp) {
      SharedPrefLong pref = (SharedPrefLong) anno;
      return sp.getLong(pref.key(), pref.defaut());
    }
  },
  
  SharedPrefString {
    
    @Override
    protected boolean accept(Class<? extends Annotation> type) {
      return type == SharedPrefString.class;
    }
    
    @Override
    protected Object loadResource(Annotation anno, SharedPreferences sp) {
      SharedPrefString pref = (SharedPrefString) anno;
      return sp.getString(pref.key(), pref.defaut());
    }
  },
  
  ;
  
  protected abstract boolean accept(Class<? extends Annotation> type);
  
  protected abstract Object loadResource(Annotation anno, SharedPreferences sp);
  
  @Override
  public Object loadResource(Context context, Annotation anno) {
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    return this.loadResource(anno, sp);
  }
  
  static Preferences findLoader(Annotation anno) {
    for (Preferences pref : values())
      if (pref.accept(anno.annotationType())) return pref;
    return null;
  }

}
