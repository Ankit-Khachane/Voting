package com.jforce.voting;

import android.app.Application;

import com.backendless.Backendless;
import com.blankj.utilcode.util.Utils;
import com.jforce.voting.api.ServerConfig;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.setUrl(ServerConfig.SERVER_URL);
        Backendless.initApp(this, ServerConfig.APPLICATION_ID, ServerConfig.API_KEY);
        Utils.init(this);
    }
}
