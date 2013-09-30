/*
 * Created on 2011-9-15
 * 
 * TODO
 */
package jacky.song.android.skin;

public enum Style {
  
  Default {
    
    @Override
    public String prefix() {
      return "";
    }
    
    @Override
    protected String separator() {
      return "";
    }
  },
  
  Green {
  
  },
  
  Blue {
  
  },
  
  ;
  
  protected String prefix() {
    return this.name();
  }
  
  protected String separator() {
    return "_";
  }
}
