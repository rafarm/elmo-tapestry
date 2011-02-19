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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry5.ioc.services.RegistryShutdownListener;
import org.apache.tapestry5.ioc.util.UnknownValueException;
import org.openrdf.elmo.ElmoManagerFactory;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;

/**
 * Registry shutdown aware {@link ElmoManagerFactory} provider service.
 * Maintains a contributed mapping of {@link ElmoContext}s (combination of
 * {@link ElmoModule} and {@link Repository}).
 * 
 * Factories aren't instantiated until they're needed and are automatically
 * closed (closing corresponding repository, also) at registry shutdown.
 *
 * @author rafa
 *
 */
public class ElmoManagerFactoryProvider implements RegistryShutdownListener {

	private final Map<String, ElmoManagerFactory> factories;
	private final Map<String, ElmoContext> config;
	private final Set<Repository> repositories;
	
	public ElmoManagerFactoryProvider( Map<String, ElmoContext> cfg )
	{
		config = cfg;
		factories = new HashMap<String, ElmoManagerFactory>();
		repositories = new HashSet<Repository>();
	}
	
	/**
	 * Returns the {@link ElmoManagerFactory} identified by id.
	 * 
	 * @param id The factory identifier
	 * @return ElmoManagerFactory
	 */
	public ElmoManagerFactory getFactory( String id )
	{
		ElmoManagerFactory factory = factories.get(id);
		if (factory==null) {
			ElmoContext cfg = config.get(id);
			if (cfg==null) {
				throw new UnknownValueException(String.format("No Elmo configuration found for %s.", id), null);
			}
			
			Repository repo = cfg.getRepository();
			repositories.add(repo);
			
			factory = new SesameManagerFactory( cfg.getModule(), repo );
			factories.put(id, factory);
		}
		
		return factory;
	}

	public void registryDidShutdown()
	{
		for(ElmoManagerFactory factory : factories.values())
			factory.close();
		
		for(Repository repo : repositories){
			try {
				repo.shutDown();
			} catch (RepositoryException e) {
				// TODO Decide what to do with this eventuality
				e.printStackTrace();
			}
		}
	}
}
