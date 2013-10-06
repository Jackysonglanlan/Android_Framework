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

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Sync {
	
	protected Semaphore semaphore = new Semaphore(0);
	
	protected boolean waitAtMost(int seconds) throws Exception {
		return semaphore.tryAcquire(seconds, TimeUnit.SECONDS);
	}
	
	protected void finishTest() {
		semaphore.release();
	}

}
