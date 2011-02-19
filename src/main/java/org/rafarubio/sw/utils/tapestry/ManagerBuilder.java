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
package org.rafarubio.sw.utils.tapestry;

import org.apache.tapestry5.ioc.ServiceBuilder;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.openrdf.elmo.ElmoManager;
import org.rafarubio.sw.utils.tapestry.elmo.ElmoManagerFactoryProvider;
import org.rafarubio.sw.utils.tapestry.elmo.ElmoManagerService;

/**
 * {@link ServiceBuilder} for {@link ElmoManager} autobuilding.
 * 
 * @author rafa
 * @see ElmoManagerService
 */
public class ManagerBuilder implements ServiceBuilder<ElmoManager> {

	public ElmoManager buildService(ServiceResources resources)
	{
		PerthreadManager threadManager = resources.getService(PerthreadManager.class);
		ElmoManagerFactoryProvider provider = resources.getService(ElmoManagerFactoryProvider.class);
		
		String id = resources.getServiceId();
		
		ElmoManagerService srv = new ElmoManagerService(provider.getFactory(id));
		threadManager.addThreadCleanupListener(srv);
		
		return srv.getManager();
	}

}
