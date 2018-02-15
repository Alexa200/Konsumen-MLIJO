package com.mlijo.aryaym.konsumen_mlijo.Base;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by buivu on 28/10/2016.
 */
public class DeviceToken {
    private static final String TAG = "DeviceToken";
    private static DeviceToken instance;

    //constructor
    private DeviceToken() {

    }

    public static DeviceToken getInstance() {
        if (instance == null) {
            instance = new DeviceToken();
        }
        return instance;
    }

    public void addDeviceToken(FirebaseFirestore mFirestore, String token) {
        Map<String, Object> myMap = new HashMap<>();
        myMap.put(Constants.DEVICE_TOKEN, token);
        mFirestore.collection("User").document("Konsumen").collection(BaseActivity.getUid()).document(Constants.DETAIL_KONSUMEN).update(myMap);
    }
}
