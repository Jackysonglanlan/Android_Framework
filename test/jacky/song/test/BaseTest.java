package jacky.song.test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;

public abstract class BaseTest<T extends Activity> extends ActivityUnitTestCase<T> {

	public BaseTest(Class<T> activityClass) {
		super(activityClass);
	}

	protected Semaphore semaphore;
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();

    semaphore = new Semaphore(0);
		startActivity(new Intent(getInstrumentation().getTargetContext(), this.getActivityClass()), null, null);
    this.before();
  }

	protected abstract Class<T> getActivityClass();

  @Override
  protected void tearDown() throws Exception {
    this.after();
    super.tearDown();
  }
  
  protected void before() throws Exception{
    
  }
  
  protected void after() throws Exception{
    
  }
  
	protected Context getContext() {
		return this.getActivity().getApplicationContext();
	}

  protected void waitAtMost(int seconds) throws Exception{
    assertTrue(semaphore.tryAcquire(seconds, TimeUnit.SECONDS));
  }
  
  protected void finishTest() {
    semaphore.release();
  }
  
}
