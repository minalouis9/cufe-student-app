package com.example.minal.studentapp;

/**
 * Created by lenovoo on 23/04/2018.
 */


        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;
        import android.widget.Toast;

/**
 * Created by fabio on 24/01/2016.
 */
public class SensorBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(SensorBroadcast.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");


        context.startService(new Intent(context, SensorService.class));;
    }

}
