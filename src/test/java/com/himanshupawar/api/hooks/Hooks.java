package com.himanshupawar.api.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

    private static final Logger LOG = LoggerFactory.getLogger(Hooks.class);

    @Before
    public void beforeScenario(Scenario scenario) {
        LOG.info("Starting scenario: {}", scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            LOG.warn("Scenario failed: {}", scenario.getName());
            // Attach a marker to the Allure report so failed scenarios are
            // easy to spot without reading the full log.
            Allure.addAttachment(
                    "Scenario status",
                    "text/plain",
                    "FAILED: " + scenario.getName(),
                    ".txt"
            );
        }
    }
}
