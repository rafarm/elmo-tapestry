/*
   Copyright (c) 2010, Rafa Rubio All rights reserved.
 
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
      
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.rafarubio.sw.utils.tapestry.elmo;

import org.apache.tapestry5.ioc.services.ThreadCleanupListener;
import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.ElmoManagerFactory;


/**
 * Builds a per-thread {@link ElmoManager}.
 * 
 * @author rafa
 *
 */
public class ElmoManagerService implements ThreadCleanupListener {
	
	private final ElmoManager manager;
	
	public ElmoManagerService(
			ElmoManagerFactory factory
			)
	{
		manager = factory.createElmoManager();
	}
	
	/**
	 * @return ElmoManager
	 */
	public ElmoManager getManager(){
		return manager;
	}

	public void threadDidCleanup() {
		manager.close();
	}

}
