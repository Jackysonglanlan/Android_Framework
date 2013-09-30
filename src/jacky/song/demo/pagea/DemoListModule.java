/*
 * Created on Jul 23, 2011 TODO
 */
package jacky.song.demo.pagea;

import jacky.song.R;
import jacky.song.android.framework.component.activity.Module;
import jacky.song.android.util.Cache;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DemoListModule extends Module {
  
  public enum OperationType {
    Add, Remove, Replace;
  }
  
  private class TestAdapter extends BaseAdapter {
    
    private ArrayList<String> mData = new ArrayList<String>();
    
    private LayoutInflater mInflater;
    
    public TestAdapter() {
      mInflater = LayoutInflater.from(context);
    }
    
    public void cleanData() {
      mData.clear();
    }
    
    public void addItem(final String item) {
      mData.add(item);
      notifyDataSetChanged();
    }
    
    @Override
    public int getViewTypeCount() {
      return 3;
    }
    
    @Override
    public int getItemViewType(int position) {
      return 0;
    }
    
    @Override
    public int getCount() {
      return mData.size();
    }
    
    @Override
    public String getItem(int position) {
      return mData.get(position);
    }
    
    @Override
    public long getItemId(int position) {
      return position;
    }
    
    private Cache<View> cache = new Cache<View>(7);
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      // System.out.println("getView " + position + " " + convertView + " type = " + type);
      TextView textView = null;
      if (!cache.contains(position)) {
        convertView = mInflater.inflate(R.layout.item1, null);
        convertView.setTag(R.id.text, convertView.findViewById(R.id.text));
        cache.cache(position, convertView);
      }
      convertView = cache.get(position);
      textView = (TextView) convertView.getTag(R.id.text);
      textView.setText(mData.get(position));
      return convertView;
    }
    
  }
  
  private TestAdapter adapter;
  
  @Override
  public Object onReceiveRequest(int type, Object data) {
    if (type == OperationType.Add.ordinal()) {
      adapter.addItem((String) data);
      refreshAdapterView(R.id.list1);
    }
    return null;
  }
  
  @Override
  public void prepareView() {
    ListView list = (ListView) context.findViewById(R.id.list1);
    adapter = new TestAdapter();
    for (int i = 0; i < 5; i++) {
      adapter.addItem("list item" + i);
    }
    list.setAdapter(adapter);
  }
  
  @Override
  public void aquireResources() {}
  
  @Override
  public void releaseResources() {}
  
}
