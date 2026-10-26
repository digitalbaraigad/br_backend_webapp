package com.baraigad.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/** Loads notification text as UTF-8 so Marathi can remain readable in properties files. */
public class NotificationPropertiesEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final String CLASSPATH_FILE = "notifications.properties";
    private static final String EXTERNAL_FILE = "/etc/baraigad/notifications.properties";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        addIfPresent(environment, new ClassPathResource(CLASSPATH_FILE), "baraigadNotificationDefaults");
        // Add the server file last so it has priority over the packaged default file.
        addIfPresent(environment, new FileSystemResource(EXTERNAL_FILE), "baraigadNotificationOverrides");
    }

    private void addIfPresent(ConfigurableEnvironment environment, Resource resource, String name) {
        if (!resource.exists()) {
            return;
        }
        Properties properties = new Properties();
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load notification configuration from " + resource, exception);
        }
        environment.getPropertySources().addFirst(new PropertiesPropertySource(name, properties));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 20;
    }
}
