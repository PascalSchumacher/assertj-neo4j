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
 * Copyright 2013-2013 the original author or authors.
 */
package org.assertj.neo4j.api.path;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;

/**
 * Checks <code>{@link org.assertj.neo4j.api.PathAssert#hasLength(int)}</code> behavior.
 * 
 * @author Florent Biville
 */
public class PathAssert_hasLength_Test {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  private Path path = mock(Path.class);

  @Test
  public void should_pass_hasLength_if_path_has_length() {
    Node node = mock(Node.class);
    given_path_of_length(1);

    assertThat(path).hasLength(1);
  }

  @Test
  public void should_fail_hasLength_if_path_is_null() {
    expectedException.expect(AssertionError.class);
    expectedException.expectMessage("Expecting actual not to be null");

    assertThat((Path) null).hasLength(0);
  }

  @Test
  public void should_fail_hasLength_if_passed_length_is_negative() {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("The path length to compare against should be positive");

    assertThat(path).hasLength(-1);
  }

  @Test
  public void should_fail_hasLength_if_path_has_a_different_length() {
    given_path_of_length(1);

    expectedException.expect(AssertionError.class);

    assertThat(path).hasLength(2);
  }

  private void given_path_of_length(int length) {
    when(path.length()).thenReturn(length);
  }

}
