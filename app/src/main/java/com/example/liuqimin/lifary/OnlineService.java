package com.example.liuqimin.lifary;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by liuqi on 1/24/2016.
 * This OnlineService will create new thread in order to handle networking intense work
 *      eg. download & upload, sharing
 * As for Intent Service, we don't require simultaneous work flow handling.
 * @todo decide bindService or startService.
 *      accessing local file should be fine using startService.
 *      binService provides much more flexibility for us to operate in activity.
 */
public class OnlineService extends IntentService {

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public OnlineService(){
        super("OnlineService");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        try{
            Thread.sleep(5000);
        }catch(Exception e){
            Thread.currentThread().interrupt();
        }
    }


    /**
     * onStartCommand() must return the default implementation
     * (which is how the intent gets delivered to onHandleIntent()):
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent,flags,startId);
    }
}
