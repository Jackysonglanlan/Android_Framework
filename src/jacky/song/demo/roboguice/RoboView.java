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
 * Created on 2013-10-6
 *
 * TODO
 */
package jacky.song.demo.roboguice;

import android.os.Bundle;
import jacky.song.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;

public class RoboView extends RoboActivity {
	@InjectResource(R.string.hello)
	private String hello;
	
	public String getHello() {
		return this.hello;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.green_main);
	}
	
}
