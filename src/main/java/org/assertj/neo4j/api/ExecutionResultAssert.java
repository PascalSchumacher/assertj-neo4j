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
package org.assertj.neo4j.api;

import org.assertj.core.api.IterableAssert;
import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.ResourceIterator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.assertj.core.util.Objects.areEqual;
import static org.assertj.neo4j.api.SearchErrorHelper.checkIndexAccess;
import static org.assertj.neo4j.api.SearchErrorHelper.checkIndexInBound;
import static org.assertj.neo4j.error.ShouldContainColumnNames.shouldContainColumnNames;
import static org.assertj.neo4j.error.ShouldContainEntries.shouldContainEntries;

/**
 * Assertions for Neo4J {@link org.neo4j.cypher.javacompat.ExecutionResult}
 * 
 * @author Florent Biville
 */
public class ExecutionResultAssert extends IterableAssert<Map<String, Object>> {

  static final String SUBSEQUENT_CALL_ERROR_MESSAGE = "\nSubsequent %s assertion calls should specify index in **increasing** order."
      + "\n  Previous call specified index: <%d>"
      + "\n  Current call specifies index: <%d>"
      + "\nCurrent call index should be larger than the previous one.";

  private final ExecutionResult actual;
  private Integer previousRowIndex = null;

  protected ExecutionResultAssert(ExecutionResult actual) {
    super(actual);
    this.actual = actual;

  }

  public ExecutionResult getActual() {
    return actual;
  }

  /**
   * Verifies that the row specified at the given index of the actual
   * {@link org.neo4j.cypher.javacompat.ExecutionResult} contains the specified column names<br/>
   * 
   * <strong>Warning: </strong>If you plan to chain this assertion, be sure that chained calls specify row indices in
   * increasing order!<br />
   * 
   * <p>
   * Example:
   * 
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * ExecutionEngine singletonExecutionEngine = new ExecutionEngine(graph);
   * // [...]
   * ExecutionResult result = singletonExecutionEngine.execute(&quot;MATCH (p:PEOPLE {firstName : 'Emil'}) RETURN p AS people&quot;);
   * assertThat(result).containsColumnNamesAtRow(0, &quot;people&quot;).containsColumnNamesAtRow(1, &quot;people&quot;);
   * 
   * </pre>
   * 
   * If no column names are specified, an {@link java.lang.IllegalArgumentException} is thrown.
   * <p>
   * 
   * @param rowIndex the position of the row to verify in the actual {@link org.neo4j.cypher.javacompat.ExecutionResult}
   * @param columnNames the column names contained in the specified row
   * @return this {@link PropertyContainerAssert} for assertions chaining
   * 
   * @throws IllegalArgumentException if <code>rowIndex</code> is strictly negative.
   * @throws IllegalArgumentException if <code>columnNames</code> is empty.
   * @throws IllegalArgumentException if <code>rowIndex</code> is smaller than the one given in the previous chained
   *           call.
   * 
   * @throws AssertionError if the actual {@link org.neo4j.cypher.javacompat.ExecutionResult} row does not contain the
   *           given column names.
   */
  public ExecutionResultAssert containsColumnNamesAtRow(int rowIndex, String... columnNames) {
    Objects.instance().assertNotNull(info, actual);

    checkIndexAccess(rowIndex, previousRowIndex, this.getClass());

    if (columnNames == null || columnNames.length == 0) {
      throw new IllegalArgumentException("There should be at least one column name to verify.");
    }

    SearchResultRow<Map<String, Object>> searchResult = search(rowIndex);
    checkIndexInBound(rowIndex, searchResult.getVisitedLines(), info, actual);

    Map<String, Object> row = searchResult.getValue();
    Set<String> actualColumnNames = row.keySet();
    if (!actualColumnNames.containsAll(asList(columnNames))) {
      throw Failures.instance().failure(info, shouldContainColumnNames(actual, rowIndex, row, columnNames));
    }

    return this;
  }

  /**
   * Verifies that the row specified at the given index of the actual
   * {@link org.neo4j.cypher.javacompat.ExecutionResult} contains the specified entries<br/>
   * 
   * <strong>Warning: </strong>If you plan to chain this assertion, be sure that chained calls specify row indices in
   * increasing order!<br />
   * 
   * <p>
   * Example:
   * 
   * <pre>
   * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
   * ExecutionEngine singletonExecutionEngine = new ExecutionEngine(graph);
   * // [...]
   * ExecutionResult result = singletonExecutionEngine.execute(&quot;MATCH (p:PEOPLE {firstName : 'Emil'}) RETURN p AS people&quot;);
   * assertThat(result).containsAtRow(0, MapEntry.entry(&quot;firstName&quot;, &quot;Emil&quot;));
   * 
   * </pre>
   * 
   * If any of the entry specified is {@code null}, an {@link IllegalArgumentException} is thrown.
   * <p>
   * 
   * @param rowIndex the position of the row to verify in the actual {@link org.neo4j.cypher.javacompat.ExecutionResult}
   * @param entries the entries contained in the specified row
   * @return this {@link PropertyContainerAssert} for assertions chaining
   * 
   * @throws IllegalArgumentException if <code>rowIndex</code> is strictly negative.
   * @throws IllegalArgumentException if <code>entries</code> is empty.
   * @throws IllegalArgumentException if <code>rowIndex</code> is smaller than the one given in the previous chained
   *           call.
   * 
   * @throws AssertionError if the actual {@link org.neo4j.cypher.javacompat.ExecutionResult} row does not contain the
   *           given column names.
   */
  public ExecutionResultAssert containsAtRow(int rowIndex, MapEntry... entries) {
    Objects.instance().assertNotNull(info, actual);

    checkIndexAccess(rowIndex, previousRowIndex, this.getClass());

    if (entries == null || entries.length == 0) {
      throw new IllegalArgumentException("There should be at least one entry to verify.");
    }

    SearchResultRow<Map<String, Object>> searchResult = search(rowIndex);
    checkIndexInBound(rowIndex, searchResult.getVisitedLines(), info, actual);

    Map<String, Object> row = searchResult.getValue();
    Collection<MapEntry> notFoundInRow = computeNotFoundInRow(row, entries);
    if (!notFoundInRow.isEmpty()) {
      throw Failures.instance().failure(info, shouldContainEntries(actual, rowIndex, row, entries, notFoundInRow));
    }

    return this;

  }

  private Collection<MapEntry> computeNotFoundInRow(Map<String, Object> row, MapEntry[] entries) {
    Collection<MapEntry> notFound = new LinkedList<>();
    for (MapEntry entry : entries) {
      if (!containsEntry(row, entry)) {
        notFound.add(entry);
      }
    }
    return notFound;
  }

  // FIXME? this is quite a duplication with Maps.containsEntry
  private boolean containsEntry(Map<String, Object> row, MapEntry entry) {
    if (entry == null) {
      throw new NullPointerException("Entries to look for should not be null");
    }

    Object entryKey = entry.key;
    return row.containsKey(entryKey) && areEqual(row.get(entryKey), entry.value);

  }

  private SearchResultRow<Map<String, Object>> search(int rowIndex) {
    ResourceIterator<Map<String, Object>> rowIterator = actual.iterator();
    int visitedRowCount = previousRowIndex == null ? 0 : 1 + previousRowIndex;
    Map<String, Object> row = null;
    while (rowIterator.hasNext() && visitedRowCount < rowIndex + 1) {
      row = rowIterator.next();
      visitedRowCount++;
    }

    previousRowIndex = rowIndex;
    return new SearchResultRow<Map<String, Object>>(visitedRowCount, row);
  }

}
