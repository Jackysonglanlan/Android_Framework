/*
 * Created on Jul 22, 2011 TODO
 */
package jacky.song.demo.pagea;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import jacky.song.R;
import jacky.song.android.framework.component.activity.Module;
import jacky.song.android.framework.core.decorator.adapter.DefaultViewInterceptor;
import jacky.song.demo.pagea.DemoListModule.OperationType;

public class OptionMenuAAAModule extends Module {
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.item1) {
      // ListView list = (ListView) context.findViewById(R.id.list1);
      // View v1 = list.getChildAt(1);
      // View target = v1.findViewById(R.id.text);
      // int x = target.getLeft();
      // TranslateAnimation anim = new TranslateAnimation(x, x, target.getTop(), -target.getHeight());
      // anim.setDuration(3000);
      // v1.startAnimation(anim);
      
      sendRequest(context.getClass(), 2, OperationType.Add.ordinal(), item.toString());
    }
    return true;
  }
  
  @Override
  public void prepareView() {
    addItemViewToAdapterView(R.id.list1, new DefaultViewInterceptor() {
      
      @Override
      public int priority() {
        return 1;
      }
      
      @Override
      public boolean acceptView(BaseAdapter adapter, int position) {
        return adapter.getItem(position).equals("aaa");
      }
      
      @Override
      protected View createItemView(int position, ViewGroup parent) {
        View convertView = inflater.inflate(R.layout.item2, null);
        convertView.setTag(R.id.textInItem2, convertView.findViewById(R.id.textInItem2));
        return convertView;
      }

      @Override
      protected void refreshItemView(int position, Object data, View convertView, ViewGroup parent) {
        //        System.out.println(position + " " + convertView);
        TextView textView = (TextView) convertView.getTag(R.id.textInItem2);
        textView.setText(data.toString());
      }
      
    });
  }
  
  @Override
  public void aquireResources() {}
  
  @Override
  public void releaseResources() {}
  
}
