package io.pulkit.ubicomp.maori;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Responsible for syncing local models with the server.
 */
public class ModelSynchronizer {

    private static final String TAG = "maori:ModelSynchronizer:";
    // TODO: Remove hard coded server address
    private static final String hostName = "10.0.0.5:9979";
    private AllModels allModels;

    public ModelSynchronizer(Context context) {
        allModels = new AllModels(context);
    }

    public void synchronize() {
        List<ModelInfo> clientModelInfoList = allModels.getModelInfo();
        Log.d(TAG, "Client model size : " + clientModelInfoList.size());
        HashMap<ModelInfo, ModelInfo> serverModelInfoMap = fetchModelInfoMap();
        Log.d(TAG, "Server model size : " + serverModelInfoMap.keySet().size());

        for (ModelInfo clientModelInfo : clientModelInfoList) {
            ModelInfo serverModelInfo = serverModelInfoMap.get(clientModelInfo);

            if (serverModelInfo == null) {
                allModels.delete(clientModelInfo.getName(), clientModelInfo.getVersion());
            }

            if (!clientModelInfo.equals(serverModelInfo)) {
                allModels.updateActiveState(serverModelInfo.getName(), serverModelInfo.getVersion(),
                        serverModelInfo.isActive());
            }

            serverModelInfoMap.remove(serverModelInfo);
        }

        Log.d(TAG, "Downloading models (size) : " + serverModelInfoMap.keySet().size());
        for (ModelInfo newModelInfo : serverModelInfoMap.keySet()) {
            fetchAndInsertModel(newModelInfo);
        }
    }

    private void fetchAndInsertModel(ModelInfo newModelInfo) {
        Log.d(TAG, "Downloading model " + newModelInfo);

        byte[] modelBites = new HttpWrapper().fetchResultBytes(String.format(
                "http://%s/maori-server/model?name=%s&version=%s",
                hostName, newModelInfo.getName(), newModelInfo.getVersion()));

        allModels.add(new Model(newModelInfo.getName(), newModelInfo.getVersion(), newModelInfo.isActive(), modelBites));
    }

    private HashMap<ModelInfo, ModelInfo> fetchModelInfoMap() {
        Log.d(TAG, "Fetching Model Info");

        // TODO: Remove hard coded device id
        String url = String.format("http://%s/maori-server/model/info?deviceId=", hostName, "device1");
        String jsonResult = new HttpWrapper().fetchResultString(url);

        HashMap<ModelInfo, ModelInfo> modelInfoMap = new HashMap<ModelInfo, ModelInfo>();
        try {
            JSONObject resultObject = new JSONObject(jsonResult);

            JSONArray resultArray = resultObject.getJSONArray("result");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject modelInfoObject = resultArray.getJSONObject(i);
                ModelInfo modelInfo = new ModelInfo(modelInfoObject.getString("name"),
                        modelInfoObject.getString("version"), modelInfoObject.getBoolean("active"));
                modelInfoMap.put(modelInfo, modelInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return modelInfoMap;
    }

}
