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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
