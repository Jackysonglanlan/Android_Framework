/*
 * Created on Jul 22, 2011 TODO
 */
package jacky.song.demo.pagea;

import java.util.ArrayList;
import java.util.List;

import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import jacky.song.R;
import jacky.song.android.framework.component.activity.Module;
import jacky.song.android.framework.core.injector.Resources;
import jacky.song.android.framework.core.injector.anno.res.InjectResource;
import jacky.song.demo.pagea.DemoListModule.OperationType;

public class DemoSpinnerModule extends Module {
  
  private List<String> data;
  
  @InjectResource(type = Resources.String, id = R.string.app_name)
  private String appName;
  
  private boolean first = true;
  
  @Override
  public boolean onContextItemSelected(MenuItem item) {
    if (item.toString().equals("record")) {

    }
    else if (item.toString().equals("stop")) {
    }
    else if (item.toString().equals("cancel")) {
    }
    else if (item.toString().equals("play")) {
    }
    return true;
  }
  
  @InjectResource(type = Resources.View, id = R.id.s1)
  private Spinner s1;
  
  @Override
  public void prepareView() {
    System.out.println(appName);
    data = new ArrayList<String>();
    
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,
        data) {
      
      @Override
      public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = super.getDropDownView(position, convertView, parent);
        v.setBackgroundColor(context.getResources().getColor(position % 2 == 0 ? R.color.blue : R.color.green));
        return v;
      }
    };
    for (int i = 0; i < 3; i++) {
      adapter.add("item" + i);
    }
    
    s1.setAdapter(adapter);
    s1.setPrompt("aaa");
    s1.setOnItemSelectedListener(new OnItemSelectedListener() {
      
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!first && isModuleExist(PageA.class, 2)) {
          ((TextView) context.findViewById(R.id.txt)).setText(data.get(position));
          sendRequest(PageA.class, 2, OperationType.Add.ordinal(), data.get(position));
        }
        else first = false;
      }
      
      @Override
      public void onNothingSelected(AdapterView<?> parent) {}
    });
    
    s1.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
      
      @Override
      public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        MenuInflater inflater = context.getMenuInflater();
        inflater.inflate(R.menu.test_context_menu, menu);
      }
    });
  }
  
  @Override
  public void aquireResources() {}
  
  @Override
  public void releaseResources() {}

   @Override
   public void onPause() {
      System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");
   }
   
   @Override
   public void onStop() {
      System.out.println("bbbbbbbbbbbbbbbbb");
   }
   
   @Override
   public void onDestroy() {
      System.out.println("ccccccccccccccc");
   }
}
