package com.teenvan.deliver.applications;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by navneet on 14/08/15.
 */
public class DeliverApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "RuVVC29Ir3IHLh1Hw5Ozyb1Hd4qdNCAAqgk4FIkk",
                "WX45GGBtCqhBIvxHfkDltyo5hG3yO2TMvAcAPWdL");
    }
}
