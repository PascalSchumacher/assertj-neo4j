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
package org.assertj.neo4j.api;

import static org.assertj.neo4j.error.ShouldHaveEndNode.shouldHaveEndNode;
import static org.assertj.neo4j.error.ShouldHaveLastRelationship.shouldHaveLastRelationship;
import static org.assertj.neo4j.error.ShouldHaveStartNode.shouldHaveStartNode;

import org.assertj.core.api.IterableAssert;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;

/**
 * Assertions for Neo4J {@link org.neo4j.graphdb.Path}
 * 
 * @author Florent Biville
 */
public class PathAssert extends IterableAssert<PropertyContainer> {

  protected PathAssert(Path actual) {
    super(actual);
  }

  public Path getActual() {
    return (Path) actual;
  }

  /**
   * Verifies that the actual {@link org.neo4j.graphdb.Path} starts with the given node<br/>
   * <p>
   * Example:
   * 
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * // [...]
   * Relationship love = homerNode.createRelationshipTo(doughnutNode, DynamicRelationshipType.withName(&quot;LOVES&quot;));
   * // PathExpander bellyExpander = [...]
   * Path veryShortPath = GraphAlgoFactory.shortestPath(bellyExpander, 2).findSinglePath(homerNode, doughnutNode);
   * 
   * assertThat(veryShortPath).hasStart(homerNode);
   * </pre>
   * 
   * If the <code>node</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   * 
   * @param node the node from which the actual {@link org.neo4j.graphdb.Path} may start from
   * @return this {@link NodeAssert} for assertions chaining
   * 
   * @throws IllegalArgumentException if <code>node</code> is {@code null}.
   * @throws AssertionError if the actual {@link org.neo4j.graphdb.Path} does not start with the given node
   */
  public PathAssert hasStart(Node node) {
    Objects.instance().assertNotNull(info, actual);

    Path actualPath = getActual();
    Node actualStart = actualPath.startNode();
    if (actualStart == null) {
      throw new IllegalStateException("The actual start node should not be null");
    }

    if (node == null) {
      throw new IllegalArgumentException("The start node to look for should not be null");
    }

    if (!actualStart.equals(node)) {
      throw Failures.instance().failure(info, shouldHaveStartNode(actualPath, node));
    }
    return this;
  }

  /**
   * Verifies that the actual {@link org.neo4j.graphdb.Path} ends with the given node<br/>
   * <p>
   * Example:
   * 
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * // [...]
   * Relationship love = homerNode.createRelationshipTo(doughnutNode, DynamicRelationshipType.withName(&quot;LOVES&quot;));
   * // PathExpander bellyExpander = [...]
   * Path veryShortPath = GraphAlgoFactory.shortestPath(bellyExpander, 2).findSinglePath(homerNode, doughnutNode);
   * 
   * assertThat(veryShortPath).hasEnd(doughnutNode);
   * </pre>
   * 
   * If the <code>node</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   * 
   * @param node the node from which the actual {@link org.neo4j.graphdb.Path} may end to
   * @return this {@link NodeAssert} for assertions chaining
   * 
   * @throws IllegalArgumentException if <code>node</code> is {@code null}.
   * @throws AssertionError if the actual {@link org.neo4j.graphdb.Path} does not end with the given node
   */
  public PathAssert hasEnd(Node node) {
    Objects.instance().assertNotNull(info, actual);

    Path actualPath = getActual();
    Node actualEnd = actualPath.endNode();
    if (actualEnd == null) {
      throw new IllegalStateException("The actual end node should not be null");
    }

    if (node == null) {
      throw new IllegalArgumentException("The end node to look for should not be null");
    }

    if (!actualEnd.equals(node)) {
      throw Failures.instance().failure(info, shouldHaveEndNode(actualPath, node));
    }
    return this;
  }

  /**
   * Verifies that the given relationship is the last one of the actual {@link org.neo4j.graphdb.Path}<br/>
   * <p>
   * Example:
   * 
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * // [...]
   * Relationship love = homerNode.createRelationshipTo(doughnutNode, DynamicRelationshipType.withName(&quot;LOVES&quot;));
   * // PathExpander bellyExpander = [...]
   * Path veryShortPath = GraphAlgoFactory.shortestPath(bellyExpander, 2).findSinglePath(homerNode, doughnutNode);
   * 
   * assertThat(veryShortPath).hasLastRelationship(love);
   * </pre>
   * 
   * If the <code>node</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   * 
   * @param relationship the relationship from which the actual {@link org.neo4j.graphdb.Path} may contain last
   * @return this {@link NodeAssert} for assertions chaining
   * 
   * @throws IllegalArgumentException if <code>relationship</code> is {@code null}.
   * @throws AssertionError if the actual {@link org.neo4j.graphdb.Path} does not contain this relationship last
   */
  public PathAssert hasLastRelationship(Relationship relationship) {
    Objects.instance().assertNotNull(info, actual);

    Path actualPath = getActual();
    Relationship actualLastRelationship = actualPath.lastRelationship();
    if (actualLastRelationship == null) {
      throw new IllegalStateException("The actual last relationship should not be null");
    }

    if (relationship == null) {
      throw new IllegalArgumentException("The last relationship to look for should not be null");
    }

    if (!actualLastRelationship.equals(relationship)) {
      throw Failures.instance().failure(info, shouldHaveLastRelationship(actualPath, relationship));
    }
    return this;
  }

}