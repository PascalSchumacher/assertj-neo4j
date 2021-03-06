/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright 2013-2014 the original author or authors.
 */
package org.assertj.neo4j.api.propertycontainer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.PropertyContainer;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Checks <code>{@link org.assertj.neo4j.api.PropertyContainerAssert#doesNotHavePropertyKey(String)}</code> behavior.
 * 
 * @author Florent Biville
 */
public class PropertyContainerAssert_doesNotHavePropertyKey_Test {

  @Rule
  public ExpectedException expectedException = none();

  private PropertyContainer propertyContainer = mock(PropertyContainer.class);

  @Test
  public void should_pass_if_property_container_does_NOT_have_key() {
    assertThat(propertyContainer).doesNotHavePropertyKey("key");
  }

  @Test
  public void should_fail_if_property_container_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((PropertyContainer) null).doesNotHavePropertyKey("key");
  }

  @Test
  public void should_fail_if_given_property_key_is_null() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The key to look for should not be null");

    assertThat(propertyContainer).doesNotHavePropertyKey(null);
  }

  @Test
  public void should_fail_if_property_container_has_key() {
    expectedException.expect(AssertionError.class);

    given_a_property_container_with_key("E♯+++");

    assertThat(propertyContainer).doesNotHavePropertyKey("E♯+++");
  }

  private void given_a_property_container_with_key(String nodeKey) {
    when(propertyContainer.hasProperty(nodeKey)).thenReturn(true);
  }

}
