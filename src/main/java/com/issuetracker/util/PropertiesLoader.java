package com.issuetracker.util;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class PropertiesLoader {
    
    private Properties properties;
    
    public PropertiesLoader(String fileName) {
        loadProperties(fileName);
    }
    
    private void loadProperties(String fileName) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            properties = new Properties();
            if (input == null) {
                throw new RuntimeException("Unable to find " + fileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from " + fileName, e);
        }
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public Properties getAllProperties() {
        return properties;
    }
}