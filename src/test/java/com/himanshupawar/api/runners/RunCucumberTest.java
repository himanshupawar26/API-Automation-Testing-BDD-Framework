package com.himanshupawar.api.runners;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Entry point for running the full suite via `mvn test`.
 *
 * Glue path, Allure registration, and report output paths are all configured
 * in src/test/resources/junit-platform.properties (the documented mechanism
 * for allure-cucumber7-jvm under the JUnit Platform Suite engine) rather than
 * here, to keep a single source of truth.
 *
 * Tag-scoped runs use Maven's standard property, e.g.:
 *   mvn test -Dcucumber.filter.tags="@smoke"
 *   mvn test -Dcucumber.filter.tags="@regression and not @negative"
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class RunCucumberTest {
}
