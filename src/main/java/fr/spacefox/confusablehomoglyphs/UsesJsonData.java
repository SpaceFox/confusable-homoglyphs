package fr.spacefox.confusablehomoglyphs;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author SpaceFox
 * @version 1.0.1
 */
abstract class UsesJsonData<T> {

    private static final Logger LOGGER = Logger.getLogger(UsesJsonData.class.getName());

    private boolean initialized = false;
    private final Type realDataType;

    UsesJsonData(Type realDataType) {
        this.realDataType = realDataType;
    }

    boolean loadJson() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream resourceStream = classLoader.getResourceAsStream(getInternalResourceName());
        return loadJson(resourceStream);
    }

    abstract String getInternalResourceName();

    boolean loadJson(String jsonFileName) {
        boolean initialized = false;
        try (FileInputStream stream = new FileInputStream(new File(jsonFileName))) {
            initialized = loadJson(stream);
        } catch (FileNotFoundException e) {
            LOGGER.severe("No " + jsonFileName + " data file found. Please regenerate it from CLI or download it.");
        } catch (IOException | JsonSyntaxException e) {
            LOGGER.log(Level.SEVERE, "Unable to load the " + jsonFileName + " data file due to " + e, e);
            LOGGER.severe("Please regenerate it from CLI or download it.");
        }
        return initialized;
    }

    private boolean loadJson(InputStream stream) {
        try (Reader reader = new InputStreamReader(stream)) {
            setData(new GsonBuilder()
                    .create()
                    .fromJson(reader, realDataType));
            initialized = true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to load data due to " + e, e);
        }
        return initialized;
    }

    abstract void setData(T fromJson);

    void assertInitialization() {
        if (!initialized) {
            throw new IllegalStateException("Call .loadJson() method to load data before using Categories object.");
        }
    }
}
