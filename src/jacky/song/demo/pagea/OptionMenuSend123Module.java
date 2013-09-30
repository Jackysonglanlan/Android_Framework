/*
 * Created on Jul 22, 2011 TODO
 */
package jacky.song.demo.pagea;

import jacky.song.R;
import jacky.song.android.framework.component.activity.Module;
import jacky.song.android.framework.core.decorator.adapter.DefaultViewInterceptor;
import jacky.song.demo.pagea.DemoListModule.OperationType;
import jacky.song.demo.pageb.PageB;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OptionMenuSend123Module extends Module {
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.item2) {
      Intent in = new Intent(context, PageB.class);
      in.putExtra("msg", "123");
      context.startActivityForResult(in, 1);
    }
    return true;
  }
  
  @Override
  public void onActivityResult(int requestCode, int resultCode, final Intent data) {
    if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
      String txt = "secretCode:" + data.getCharSequenceExtra("secretCode");
      sendRequest(PageA.class, 2, OperationType.Add.ordinal(), txt);
    }
  }
  
  @Override
  public void prepareView() {
    
    addItemViewToAdapterView(R.id.list1, new DefaultViewInterceptor() {
      
      @Override
      public int priority() {
        return 2;
      }
      
      @Override
      public boolean acceptView(BaseAdapter adapter, int position) {
        String content = (String) adapter.getItem(position);
        return content.startsWith("secret");
      }
      
      @Override
      protected View createItemView(int position, ViewGroup parent) {
        View convertView = inflater.inflate(R.layout.item3, null);
        convertView.setTag(R.id.textInItem3, convertView.findViewById(R.id.textInItem3));
        return convertView;
      }
      
      @Override
      protected void refreshItemView(int position, Object data, View convertView, ViewGroup parent) {
        TextView txt = (TextView) convertView.getTag(R.id.textInItem3);
        txt.setText(data.toString());
      }
    });
  }
  
  @Override
  public void aquireResources() {}
  
  @Override
  public void releaseResources() {}
  
}
