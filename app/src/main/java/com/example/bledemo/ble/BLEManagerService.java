package com.example.bledemo.ble;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BLEManagerService extends Service {
    public BLEManagerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
