package com.hcl.sa.utils;

import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class SuperClass {

    private volatile  static SuperClass obj;
    private JsonElement jsonElement = null;
    public static DataStore specStore = DataStoreFactory.getSpecDataStore();

    //private Logger logger = LogManager.getLogger(com.hcl.bigfix.utils.SuperClass.class);

    private SuperClass() {
    }

    public static SuperClass getInstance() {
        if (obj == null){
            // To make thread safe
            synchronized (SuperClass.class) {
                // check again as multiple threads
                // can reach above step
                if (obj == null)
                    obj = new SuperClass();
            }
        }
        return obj;
    }

    public JsonElement getJsonParser(String filePath) {
        try {
            JsonParser  jsonParser = null;
            jsonParser = new JsonParser();
            jsonElement = jsonParser.parse(new FileReader(filePath));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return jsonElement;
    }
}