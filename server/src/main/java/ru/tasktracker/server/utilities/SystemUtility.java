package ru.tasktracker.server.utilities;

public class SystemUtility {
    public static String getEnvironmentVariable(String key) {
        return System.getProperty(key);
    }

    public static String getEnvironmentVariableOrDefault(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value == null) {
            System.setProperty(key, defaultValue);

            return defaultValue;
        }

        return value;
    }
}
