package com.himanshupawar.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Centralized environment configuration.
 *
 * Resolution order (highest priority first):
 *   1. JVM system property (-Dbase.url=...)
 *   2. Environment variable (BASE_URL)
 *   3. src/test/resources/config.local.properties (git-ignored; local-only overrides, e.g. a real API key)
 *   4. src/test/resources/config.properties (committed defaults)
 *
 * No absolute/hardcoded filesystem paths anywhere — this is loaded from the
 * classpath, so it works identically on any machine and in CI.
 *
 * The API key specifically: never pass it as a -D on the command line (it
 * ends up in shell history and process listings). Use config.local.properties
 * locally, and the RQRES_API_KEY repository secret in CI.
 */
public final class Config {

    private static final Properties DEFAULT_PROPERTIES = loadProperties("config.properties");
    private static final Properties LOCAL_PROPERTIES = loadProperties("config.local.properties");

    private Config() {
    }

    private static Properties loadProperties(String resourceName) {
        Properties props = new Properties();
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + resourceName + " from classpath", e);
        }
        return props;
    }

    public static String baseUrl() {
        return resolve("base.url", "BASE_URL");
    }

    public static int requestTimeoutSeconds() {
        String value = resolve("request.timeout.seconds", "REQUEST_TIMEOUT_SECONDS");
        return value != null ? Integer.parseInt(value) : 10;
    }

    /**
     * The ReqRes API key, or null if none is configured (anonymous tier).
     * Anonymous requests are capped at 40/day per IP — set this to avoid
     * hitting that limit, especially in CI where the cap is shared across runs.
     */
    public static String apiKey() {
        return resolve("api.key", "RQRES_API_KEY");
    }

    private static String resolve(String propertyKey, String envVarName) {
        String systemProp = System.getProperty(propertyKey);
        if (systemProp != null && !systemProp.isBlank()) {
            return systemProp;
        }
        String envVar = System.getenv(envVarName);
        if (envVar != null && !envVar.isBlank()) {
            return envVar;
        }
        String local = LOCAL_PROPERTIES.getProperty(propertyKey);
        if (local != null && !local.isBlank()) {
            return local;
        }
        return DEFAULT_PROPERTIES.getProperty(propertyKey);
    }
}
