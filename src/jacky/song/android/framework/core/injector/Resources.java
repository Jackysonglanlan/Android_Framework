/*
 * Created on Aug 10, 2011 TODO
 */
package jacky.song.android.framework.core.injector;

import java.io.IOException;
import java.lang.annotation.Annotation;

import jacky.song.android.framework.core.injector.anno.res.InjectAssets;
import jacky.song.android.framework.core.injector.anno.res.InjectResource;

import android.app.Activity;
import android.content.Context;

/**
 * The resource you want to InjectResource in.
 * 
 * @author Jacky.Song
 */
public enum Resources implements ResourceLoader {
  
  // //// ----------- Assets ----------- //////
  
  Assets {
    
    @Override
    public Object loadResource(Context context, Annotation anno) {
      InjectAssets asset = (InjectAssets) anno;
      try {
        return context.getAssets().open(asset.value());
      }
      catch (IOException e) {
        throw new RuntimeException("Can't InjectResource asset: " + asset.value(), e);
      }
    }
  },
  
  // //// ----------- Resources ----------- //////
  
  View {
    
    @Override
    public Object loadResource(Context context, Annotation anno) {
      if (!(context instanceof Activity)) return null;
      InjectResource view = (InjectResource) anno;
      return ((Activity) context).findViewById(view.id());
    }
  },
  
  String {
    
    @Override
    public Object loadResource(Context context, Annotation anno) {
      InjectResource rsc = (InjectResource) anno;
      return context.getResources().getString(rsc.id());
    }
  },
  
  Boolean {
    
    @Override
    public Object loadResource(Context context, Annotation anno) {
      InjectResource rsc = (InjectResource) anno;
      return context.getResources().getBoolean(rsc.id());
    }
  },
  
  Color {
    
    @Override
    public Object loadResource(Context context, Annotation anno) {
      InjectResource rsc = (InjectResource) anno;
      return context.getResources().getColor(rsc.id());
    }
  },
  
  Dimension {
    
    @Override
    public Object loadResource(Context context, Annotation anno) {
      InjectResource rsc = (InjectResource) anno;
      return context.getResources().getDimension(rsc.id());
    }
  },
  
  Animation {
    
    @Override
    public Object loadResource(Context context, Annotation anno) {
      InjectResource rsc = (InjectResource) anno;
      return context.getResources().getAnimation(rsc.id());
    }
  },
  
  ColorStateList {
    
    @Override
    public Object loadResource(Context context, Annotation anno) {
      InjectResource rsc = (InjectResource) anno;
      return context.getResources().getColorStateList(rsc.id());
    }
  },
  
  Drawable {
    
    @Override
    public Object loadResource(Context context, Annotation anno) {
      InjectResource rsc = (InjectResource) anno;
      return context.getResources().getDrawable(rsc.id());
    }
  },
  
  Integer {
    
    @Override
    public Object loadResource(Context context, Annotation anno) {
      InjectResource rsc = (InjectResource) anno;
      return context.getResources().getInteger(rsc.id());
    }
  },
  
  IntArray {
    
    @Override
    public Object loadResource(Context context, Annotation anno) {
      InjectResource rsc = (InjectResource) anno;
      return context.getResources().getIntArray(rsc.id());
    }
  },
  
  Layout {
    
    @Override
    public Object loadResource(Context context, Annotation anno) {
      InjectResource rsc = (InjectResource) anno;
      return context.getResources().getLayout(rsc.id());
    }
  },
  
  StringArray {
    
    @Override
    public Object loadResource(Context context, Annotation anno) {
      InjectResource rsc = (InjectResource) anno;
      return context.getResources().getStringArray(rsc.id());
    }
  },
  
  Xml {
    
    @Override
    public Object loadResource(Context context, Annotation anno) {
      InjectResource rsc = (InjectResource) anno;
      return context.getResources().getXml(rsc.id());
    }
  },
  
  ;
}
