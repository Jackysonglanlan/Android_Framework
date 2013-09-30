/*
 * Created on Jul 12, 2011 TODO
 */
package jacky.song.android.framework.component.activity;

import com.google.code.microlog4android.config.PropertyConfigurator;

import jacky.song.android.framework.core.injector.ResourceInjector;
import jacky.song.android.framework.core.visitor.Visitor;
import jacky.song.android.framework.core.visitor.VisitorManager;
import jacky.song.android.skin.Skin;
import jacky.song.android.skin.SkinableResources;
import jacky.song.android.util.closure.Closure;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;

/**
 * Introduce Visitor Pattern into Activity.
 * <p>
 * Core component of the framework.
 * <p>
 * <i> Every Acitivity who want to use {@link Visitor} to modulize its UI element must extends this class. </i>
 * <p style="font-style:italic">
 * You can bind one or more {@link Visitor} to an Activity, or you can share one {@link Visitor} with several Activities.<br>
 * The relationship between Visitors and Activities is <b>N to N</b>. <br>
 * <br>
 * Usage:<br>
 * <ol style="color:green">
 * <li>Create a file named visitors.properties in the assets dir.</li>
 * <li>Create a key-value pair:
 * <ul>
 * <li>key is the fully quanlified class name of the Acitivity you want to modulize</li>
 * <li>value is the fully quanlified class name of the Visitor you created.</li>
 * </ul>
 * </li>
 * <li>Add the priority value to the end of the Activity's class name to configure the priority.</li>
 * </ol>
 * <p>
 * Example:
 * 
 * <pre>
 * <code>
 * foo.bar.TestActivity-1 = foo.bar.FooVisitor1
 * foo.bar.TestActivity-4 = foo.bar.FooVisitor2
 * foo.bar.TestActivity-2 = foo.bar.FooVisitor3
 * </code>
 * </pre>
 * 
 * @author Jacky.Song
 */
public abstract class BaseActivity extends Activity {
  
  abstract protected int defaultLayout();
  
  public BaseActivity() {
    // // apply Visitor Pattern /////
    VisitorManager.visit(this);
  }
  
  protected LayoutInflater inflater;
  
  protected Resources resources;
  
  private static boolean logConfigured;
  
  private void configLogs() {
    if (!logConfigured) {
      PropertyConfigurator.getConfigurator(this).configure();
      logConfigured = true;
    }
  }

  @Override
  public Resources getResources() {
    Resources orig = super.getResources();// beware it's super
    if (resources == null)
      resources = new SkinableResources(orig.getAssets(), orig.getDisplayMetrics(), orig.getConfiguration());
    return resources;
  }
  
  @Override
  public void setContentView(int layoutResID) {
    super.setContentView(Skin.layout.load(layoutResID));
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    configLogs();
    setContentView(defaultLayout());
    inflater = LayoutInflater.from(this);
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        // inject resources
        ResourceInjector.injectResourcesToVisitor(BaseActivity.this, data);
        data.setInflater(inflater);
        data.setResources(resources);
        data.onCreate(savedInstanceState);
        data.prepareView();
      }
    }, true);
  }
  

  @Override
  protected void onRestart() {
    super.onRestart();
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        data.onRestart();
      }
    }, true);
  }
  
  @Override
  protected void onStart() {
    super.onStart();
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        // inject Intent
        Intent in = getIntent();
        ResourceInjector.injectIntent(data, in, in);
        data.aquireResources();
        data.onStart();
      }
    }, true);
  }
  
  @Override
  protected void onRestoreInstanceState(final Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        data.onRestoreInstanceState(savedInstanceState);
      }
    }, true);
  }
  
  @Override
  protected void onNewIntent(final Intent intent) {
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        // inject intent for reuse case
        ResourceInjector.injectIntent(data, intent, getIntent());
        setIntent(intent);// update the return value of getIntent()
        data.onNewIntent(intent);
      }
    }, true);
    
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        data.onResume();
        data.refreshView();
      }
    }, true);
  }
  
  @Override
  protected void onPause() {
    super.onPause();
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        data.onPause();
        data.saveCrucialData();
      }
    }, false);
  }
  
  @Override
  protected void onSaveInstanceState(final Bundle outState) {
    super.onSaveInstanceState(outState);
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        data.onSaveInstanceState(outState);
      }
    }, true);
  }
  
  @Override
  protected void onStop() {
    super.onStop();
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        data.releaseResources();
        data.onStop();
      }
    }, false);
  }
  
  @Override
  protected void onDestroy() {
    super.onDestroy();
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        data.onDestroy();
        data.destoryView();
        // clean relevant cache in Injector
        ResourceInjector.cleanCacheOnDestory(data);
      }
    }, false);
    
    // Destory all relevant visitors
    VisitorManager.unregisterVisitors(this);
  }
  
  // ///////// Take over Menu controll //////////////
  
  @Override
  protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor visitor) {
        visitor.onActivityResult(requestCode, resultCode, data);
      }
    }, true);
    
  }
  
  @Override
  public void onBackPressed() {
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        data.onBackPressed();
      }
    }, true);
  }
  
  // ///////// Take over Menu controll //////////////
  
  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        data.onCreateOptionsMenu(menu);
      }
    }, true);
    
    return true; // TODO what if Visitor want to return false?
  }
  
  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        data.onOptionsItemSelected(item);
      }
    }, true);
    return true;
  }
  
  @Override
  public void onOptionsMenuClosed(final Menu menu) {
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        data.onOptionsMenuClosed(menu);
      }
    }, true);
  }
  
  @Override
  public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        data.onCreateContextMenu(menu, v, menuInfo);
      }
    }, true);
  }
  
  @Override
  public boolean onContextItemSelected(final MenuItem item) {
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        data.onContextItemSelected(item);
      }
    }, true);
    return true;
  }
  
  @Override
  public void onContextMenuClosed(final Menu menu) {
    VisitorManager.delegateToVisitors(this.getClass(), new Closure<Visitor>() {
      
      @Override
      public void doWith(Visitor data) {
        data.onContextMenuClosed(menu);
      }
    }, true);
  }

}
