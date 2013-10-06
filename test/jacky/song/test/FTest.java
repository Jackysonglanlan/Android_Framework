/*
 * Copyright (C) 2010 Jacky Song.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Created on 2013-10-3
 *
 * TODO
 */
package jacky.song.test;

import roboguice.RoboGuice;
import roboguice.inject.RoboInjector;
import android.test.suitebuilder.annotation.SmallTest;
import jacky.song.FooActivity;
import jacky.song.bean.CompletionClosure;
import jacky.song.bean.DBSource;
import jacky.song.bean.ServerSource;

public class FTest extends BaseTest<FooActivity> {
	
	public FTest() {
		super(FooActivity.class);
	}

	@Override
	protected Class<FooActivity> getActivityClass() {
		return FooActivity.class;
	}
	
	public void testActiveAndroid() throws Exception {
		DBSource db = new DBSource();
		db.saveCateWithItems();
	}
	
	public void estInject() throws Exception {

		FooActivity foo = getActivity();
		
		System.out.println(foo.getHello());
		
		RoboInjector injector = RoboGuice.getInjector(getContext());

		ServerSource serverSource = injector.getInstance(ServerSource.class);

		serverSource.asyncGet("http://www.baidu.com", new CompletionClosure() {
			
			@Override
			public void onCompleted(Exception e, String result) {
				System.out.println(result);
				finishTest();
			}
		});

		waitAtMost(10);
	}

}
