/*
 * Copyright (c) 2010, Rafa Rubio All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution. 
 * - Neither the name of the openrdf.org nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
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
