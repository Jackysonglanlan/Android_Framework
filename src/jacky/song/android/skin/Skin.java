/*
 * Created on 2011-9-15
 * 
 * TODO
 */
package jacky.song.android.skin;

import jacky.song.R;

import java.lang.reflect.Field;

import android.util.SparseArray;

public enum Skin {
  
  layout {
    
    @Override
    protected Class<?> resClass() {
      return R.layout.class;
    }
  },
  
  color {
    
    @Override
    protected Class<?> resClass() {
      return R.color.class;
    }
  },
  
  drawable {
    
    @Override
    protected Class<?> resClass() {
      return R.drawable.class;
    }
  },
  
  ;

  private Skin() {
    prepareDataTable(resClass(), dataTable);
    //    System.out.println(dataTable);
  }
  
  protected abstract Class<?> resClass();

  private final SparseArray<String> dataTable = new SparseArray<String>(100);
  
  public static Style style = Style.Default;// TODO may change

  private void prepareDataTable(Class<?> res, SparseArray<String> dataTable) {
    try {
      for (Field f : res.getDeclaredFields()) {
        dataTable.put(f.getInt(res), f.getName().toLowerCase());
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private static int switchId(int maskId, SparseArray<String> dataTable) {
    String origName = dataTable.get(maskId);
    
    // change name if necessary
    
    if (origName != null && origName.startsWith(style.prefix())) return maskId;
    
    String targetName = style.prefix().toLowerCase() + style.separator() + origName;

    //    System.out.println(targetName);

    for (int i = 0; i < dataTable.size(); i++) {
      if (dataTable.valueAt(i).equals(targetName)) return dataTable.keyAt(i);
    }

    // if no corresponding skin, return the original one
    return maskId;
  }
  
  public int load(int maskId) {
    return switchId(maskId, dataTable);
  }

}
