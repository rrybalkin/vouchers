package com.rrybalkin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Messages {

    private static final String BASE_NAME = "messages/Messages";
    private static final Messages INSTANCE = new Messages();
    private final ResourceBundle messagesBundle;

    private Messages() {
        this.messagesBundle = ResourceBundle.getBundle(BASE_NAME, Configuration.getInstance().appLocale, new UTF8Control());
        if (messagesBundle == null) {
            throw new IllegalStateException("Messages bundle not found.");
        }
    }

    public static String get(String messageKey) {
        return INSTANCE.messagesBundle.getString(messageKey);
    }

    public static String get(String messageKey, Object... args) {
        return MessageFormat.format(INSTANCE.messagesBundle.getString(messageKey), args);
    }

    /**
     * A custom ResourceBundle control for UTF8 encoding.
     */
    private static class UTF8Control extends ResourceBundle.Control {
        public ResourceBundle newBundle
                (String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException
        {
            // The below is a copy of the default implementation.
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    // Only this line is changed to make it to read properties files as UTF-8.
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }
}
