package com.rapid7.appspider;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nbugash on 19/08/15.
 */
public class ScanEngineGroup extends Base {
    public final static String GETALLENGINEGROUPS = "/EngineGroup/GetAllEngineGroups";
    public final static String GETENGINEGROUPSFORCLIENT = "/EngineGroup/GetEngineGroupsForClient";

    /**
     * @param restUrl
     * @param authToken
     * @return HashMap with engine_id as the key and engine_name as the value
     */
    public static Map<String,String> getAllEngineGroups(String restUrl, String authToken) {
        String apiCall = restUrl + GETALLENGINEGROUPS;
        Object response = get(apiCall, authToken);
        if (response.getClass().equals(JSONObject.class)) {
           return scanEngineGroup((JSONObject)response);
        }
        return null;
    }

    /**
     * @param restUrl
     * @param authToken
     * @return HashMap with engine_id as the key and engine_name as the value
     */
    public static Map<String,String> getEngineGroupsForClient(String restUrl, String authToken) {
        String apiCall = restUrl + GETENGINEGROUPSFORCLIENT;
        Object response = get(apiCall,authToken);
        if (response.getClass().equals(JSONObject.class)){
            return scanEngineGroup((JSONObject)response);
        }
        return null;
    }

    /**
     * @param restUrl
     * @param authToken
     * @return String[] of all the engine groups for a specific client
     */
    public static String[] getEngineNamesGroupsForClient(String restUrl, String authToken) {
        Map<String,String> response = getEngineGroupsForClient(restUrl, authToken);
        if(response.getClass().equals(HashMap.class)) {
            ArrayList<String> list = new ArrayList<String>();
            for(Map.Entry<String,String> entry: response.entrySet()) {
                list.add(entry.getKey());
            }
            String[] engineNames = new String[list.size()];
            return list.toArray(engineNames);
        }
        return null;
    }

    public static String getEngineGroupIdFromName(String restUrl, String authToken, String engineGroupName) {
        Map<String,String> engines = getAllEngineGroups(restUrl,authToken);
        return engines.get(engineGroupName);
    }

    /**
     * Helper method for getEngineGroupsForClient and getAllEngineGroups
     * @param jsonObject
     * @return HashMap with key being the name of the engine group and the value being the engine group id
     * <b>Assumption: 1-to-1 relationship between the engine group name and engine group id</b>
     */
    private static Map<String, String> scanEngineGroup(JSONObject jsonObject) {
        Map<String,String> engines = new HashMap<String, String>();
        JSONArray engineGroups = jsonObject.getJSONArray("EngineGroups");
        for (int i = 0; i < engineGroups.length(); i++ ) {
            String engine_id = engineGroups.getJSONObject(i).getString("Id");
            String engine_name = engineGroups.getJSONObject(i).getString("Name");
            engines.put(engine_name, engine_id);
        }
        return engines;
    }
}
