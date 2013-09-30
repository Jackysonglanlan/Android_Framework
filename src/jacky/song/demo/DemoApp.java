/*
 * Created on 2011-10-10
 * 
 * TODO
 */
package jacky.song.demo;

import jacky.song.R;
import jacky.song.android.pattern.command.chain.ProcessorChainManager;
import jacky.song.android.util.closure.Closure;
import jacky.song.android.util.net.HttpConnector;
import jacky.song.android.util.net.HttpMethod;
import jacky.song.demo.domain.User;
import jacky.song.demo.domain.UserItem;
import jacky.song.demo.processor.chain.FileWorkLine;

import java.sql.Timestamp;

import org.apache.http.HttpResponse;
import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.Database;
import org.kroz.activerecord.DatabaseBuilder;

import android.app.Application;

public class DemoApp extends Application {
  
  static {
    HttpConnector.init();

    DatabaseBuilder builder = new DatabaseBuilder(Const.DATABASE_NAME);
    builder.addClass(User.class);
    builder.addClass(UserItem.class);
    Database.setBuilder(builder);
    
    ProcessorChainManager.build(FileWorkLine.class);
    //    System.out.println(ProcessorChainManager.findProcessor(JsonProcessor.class));
    //    System.out.println(ProcessorChainManager.findProcessor(FileProcessor.class));
  }

  @Override
  public void onCreate() {
    super.onCreate();

    ActiveRecordBase db = null;
    try {
      db = ActiveRecordBase.open(this, Const.DATABASE_NAME, Const.DATABASE_VERSION);
      User user = db.newEntity(User.class);
      user.setFirstName("sss");
      user.setLastName("ccc");
      user.setNewInt(1111);
      user.setBirthday(new Timestamp(System.currentTimeMillis()));
      
      for (int i = 0; i < 3; i++) {
        UserItem item = db.newEntity(UserItem.class);
        System.out.println(user.getID());
        item.setPrice("$" + i);
        item.setStatus("status " + i);
        user.addItem(item);
      }
      
      user.save();
      System.out.println(db.findAll(User.class));
      System.out.println(db.findAll(UserItem.class).size());
      db.delete(User.class, "_id > ?", "5");
      db.delete(UserItem.class, "USER_ID > ?", "5");

      HttpConnector.asyncConnect(HttpMethod.GET, getResources().getString(R.string.baidu), null, FileWorkLine.class,
          new Closure<HttpResponse>() {
            
            @Override
            public void doWith(HttpResponse data) {
              System.out.println(Thread.currentThread() + " error code: " + data.getStatusLine().getReasonPhrase());
            }
          });

    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (db != null) db.close();
    }
  }
  
  @Override
  public void onTerminate() {
    super.onTerminate();
    HttpConnector.shutdown();
  }

}
