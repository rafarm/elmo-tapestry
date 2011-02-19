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

import java.util.Map;

import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.openrdf.elmo.ElmoManager;
import org.rafarubio.sw.utils.tapestry.elmo.ElmoContext;
import org.rafarubio.sw.utils.tapestry.elmo.ElmoManagerFactoryProvider;

/**
 * Elmo Tapestry integration module.
 * Provides autobuilded {@link ElmoManager}s configured through {@link ElmoContextProvider}
 * (mapped {@link ElmoManagerFactory}es) service contributions.
 * <p>
 * Exemple:
 * </p>
 * <pre>
 * {@code
 * public class AppModule {
 *   public static void bind(ServiceBinder binder) {
 *     ManagerBuilder builder = new ManagerBuilder();
 *     binder.bind(ElmoManager.class, builder).withId("DefaultContext").scope(ScopeConstants.PERTHREAD);
 *     binder.bind(ElmoManager.class, builder).withId("OtherContext").scope(ScopeConstants.PERTHREAD);
 *   }
 *   
 *   public void contributeElmoContextProvider(MappedConfiguration<String,ElmoContext> contrib) {
 *     ElmoModule module = new ElmoModule();
 *     module.addConcept(Person.class);
 *     
 *     Repository repository = new SailRepository( new MemoryStore() );
 *     try {
 *       repository.initialize();
 *     } catch (RepositoryException e) {
 *       e.printStackTrace();
 *     }
 *     
 *     ElmoContext context = new ElmoContext(module,repository);
 *     contrib.add("DefaultContext", context);
 *     
 *     repository = new SailRepository( new MemoryStore() );
 *     try {
 *       repository.initialize();
 *     } catch (RepositoryException e) {
 *       e.printStackTrace();
 *     }
 *     
 *     context = new ElmoContext(module,repository);
 *     contrib.add("OtherContext", context);
 *   }
 * }
 * </pre>
 * @author rafa
 *
 */
public class ElmoTapestryModule {
	
	public static ElmoManagerFactoryProvider buildElmoContextProvider(
			RegistryShutdownHub hub,
			Map<String,ElmoContext> config )
	{
		ElmoManagerFactoryProvider srv = new ElmoManagerFactoryProvider(config);
		hub.addRegistryShutdownListener(srv);
		
		return srv;
	}
}
