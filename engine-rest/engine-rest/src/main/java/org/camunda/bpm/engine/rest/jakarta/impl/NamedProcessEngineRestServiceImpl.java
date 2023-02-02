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

import static org.camunda.bpm.engine.rest.jakarta.util.EngineUtil.getProcessEngineProvider;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.camunda.bpm.engine.rest.dto.ProcessEngineDto;
import org.camunda.bpm.engine.rest.jakarta.CaseExecutionRestService;
import org.camunda.bpm.engine.rest.spi.ProcessEngineProvider;


@Path(NamedProcessEngineRestServiceImpl.PATH)
public class NamedProcessEngineRestServiceImpl extends AbstractProcessEngineRestServiceImpl {

  public static final String PATH = "/engine";

  @Override
  @Path("/{name}" + CaseExecutionRestService.PATH)
  public CaseExecutionRestService getCaseExecutionRestService(@PathParam("name") String engineName) {
    return super.getCaseExecutionRestService(engineName);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<ProcessEngineDto> getProcessEngineNames() {
    ProcessEngineProvider provider = getProcessEngineProvider();
    Set<String> engineNames = provider.getProcessEngineNames();

    List<ProcessEngineDto> results = new ArrayList<ProcessEngineDto>();
    for (String engineName : engineNames) {
      ProcessEngineDto dto = new ProcessEngineDto();
      dto.setName(engineName);
      results.add(dto);
    }

    return results;
  }

  @Override
  protected URI getRelativeEngineUri(String engineName) {
    return UriBuilder.fromResource(NamedProcessEngineRestServiceImpl.class).path("{name}").build(engineName);
  }

}
