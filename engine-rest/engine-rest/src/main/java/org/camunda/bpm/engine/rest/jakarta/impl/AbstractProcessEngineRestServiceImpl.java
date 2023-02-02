/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.rest.jakarta.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.Providers;
import java.net.URI;
import org.camunda.bpm.engine.rest.jakarta.CaseExecutionRestService;
import org.camunda.bpm.engine.rest.jakarta.util.ProvidersUtil;

/**
 * <p>Abstract process engine resource that provides instantiations of all REST resources.</p>
 *
 * <p>Subclasses can add JAX-RS to methods as required annotations. For example, if only
 * the process definition resource should be exposed, it is sufficient to add JAX-RS annotations to that
 * resource. The <code>engineName</code> parameter of all the provided methods may be <code>null</code>
 * to instantiate a resource for the default engine.</p>
 *
 * @author Thorben Lindhauer
 */
public abstract class AbstractProcessEngineRestServiceImpl {

  @Context
  protected Providers providers;

  public CaseExecutionRestService getCaseExecutionRestService(String engineName) {
    String rootResourcePath = getRelativeEngineUri(engineName).toASCIIString();
    CaseExecutionRestServiceImpl subResource = new CaseExecutionRestServiceImpl(engineName, getObjectMapper());
    subResource.setRelativeRootResourceUri(rootResourcePath);
    return subResource;
  }

  protected abstract URI getRelativeEngineUri(String engineName);

  protected ObjectMapper getObjectMapper() {
    return ProvidersUtil
        .resolveFromContext(providers, ObjectMapper.class, MediaType.APPLICATION_JSON_TYPE, this.getClass());
  }

}
