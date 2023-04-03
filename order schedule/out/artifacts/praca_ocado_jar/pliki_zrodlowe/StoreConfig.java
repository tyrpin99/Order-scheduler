
package com.praca;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class StoreConfig {
    private List<Picker> pickers;
    private LocalTime pickingStartTime;
    private LocalTime pickingEndTime;
    private static StoreConfig instance;

    public static StoreConfig fromJson(String jsonPath) throws IOException {
        // sprawdzic czy istnieje
        if (instance == null) {
            instance = new StoreConfig();
        }

        String storeJson = Files.readString(Path.of(jsonPath));
        JSONObject storeObject = new JSONObject(storeJson);
        JSONArray pickersArray = storeObject.optJSONArray("pickers");
        LocalTime pickingStartTime = LocalTime.parse((storeObject.getString("pickingStartTime")));
        LocalTime pickingEndTime = LocalTime.parse((storeObject.getString("pickingEndTime")));
        for (int i = 0; i < pickersArray.length(); i++) {
            String pickerID = pickersArray.getString(i);
            instance.addPicker(new Picker(pickerID, pickingStartTime, pickingEndTime));
        }
        return instance;
    }
    private StoreConfig() {
            this.pickers = new ArrayList<>();
        }

        public List<Picker> getPickers() {
            return pickers;
        }

        private void addPicker(Picker picker){
            this.pickers.add(picker);
        }

    }
