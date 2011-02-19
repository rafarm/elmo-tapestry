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

import org.openrdf.elmo.ElmoModule;
import org.openrdf.repository.Repository;

/**
 * Defines a combination of {@link ElmoModule} and Repository to create an
 * {@link ElmoManagerFactory} with. It's used to configure {@link ElmoContextProvider}
 * service through contribution.
 * 
 * @author rafa
 *
 */
public class ElmoContext {
	private final ElmoModule module;
	private final Repository repository;
	
	public ElmoContext(
			ElmoModule mod,
			Repository rep )
	{
		module = mod;
		repository = rep;
	}

	public ElmoModule getModule()
	{
		return module;
	}

	public Repository getRepository()
	{
		return repository;
	}
}
