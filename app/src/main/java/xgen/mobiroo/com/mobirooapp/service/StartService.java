package xgen.mobiroo.com.mobirooapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class StartService extends Service {
    int i = 0;
    int max = 1000;
    public static int sum = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startCalculate();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startCalculate() {
        while (i < max){
            sum = sum + i;
            i++;
        }
        Intent i = new Intent();
        i.setAction("serviceAction");
        i.putExtra("calculation_result", sum);
        sendBroadcast(i);
    }
}

