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
package org.camunda.bpm.identity.impl.ldap;

import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.identity.UserQuery;
import org.camunda.bpm.engine.impl.test.ResourceProcessEngineTestCase;

import java.util.List;

public class LdapUserLargeQueryTest extends ResourceProcessEngineTestCase {

  LdapTestEnvironment ldapTestEnvironment;

  public LdapUserLargeQueryTest() {
    // pageSize = 3 in this configuration
    super("camunda.ldap.pages.cfg.xml");
  }

  protected void setUp() throws Exception {
    ldapTestEnvironment = new LdapTestEnvironment();
    // Attention, stay under 80, there is a limitation in the query on 100
    ldapTestEnvironment.init(80, 5, 5);
    super.setUp();
  }

  protected void tearDown() throws Exception {
    if (ldapTestEnvironment != null) {
      ldapTestEnvironment.shutdown();
      ldapTestEnvironment = null;
    }
    super.tearDown();
  }

  public void testAllUsersQuery() {
    List<User> listUsers = identityService.createUserQuery().list();

    // In this group, we expect more than a page size
    assertEquals(ldapTestEnvironment.getTotalNumberOfUsersCreated(), listUsers.size());
  }

  public void testPagesAllUsersQuery() {
    List<User> listUsers = identityService.createUserQuery().list();

    assertEquals(ldapTestEnvironment.getTotalNumberOfUsersCreated(), listUsers.size());

    // ask 3 pages
    for (int firstResult = 0; firstResult < 10; firstResult += 4) {
      List<User> listPages = identityService.createUserQuery().listPage(firstResult, 5);
      for (int i = 0; i < listPages.size(); i++) {
        assertEquals(listUsers.get(firstResult + i).getId(), listPages.get(i).getId());
        assertEquals(listUsers.get(firstResult + i).getLastName(), listPages.get(i).getLastName());
      }

    }
  }

  public void testQueryPaging() {
    UserQuery query = identityService.createUserQuery();

    assertEquals(92, query.listPage(0, Integer.MAX_VALUE).size());

    // Verifying the un-paged results
    assertEquals(92, query.count());
    assertEquals(92, query.list().size());

    // Verifying paged results
    assertEquals(2, query.listPage(0, 2).size());
    assertEquals(2, query.listPage(2, 2).size());
    assertEquals(3, query.listPage(4, 3).size());
    assertEquals(1, query.listPage(91, 3).size());
    assertEquals(1, query.listPage(91, 1).size());

    // Verifying odd usages
    assertEquals(0, query.listPage(-1, -1).size());
    assertEquals(0, query.listPage(92, 2).size()); // 92 is the last index with a result
    assertEquals(92, query.listPage(0, 93).size()); // there are only 92 groups
  }

}
