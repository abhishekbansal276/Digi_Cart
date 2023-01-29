package com.example.project_one;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

public class NetworlChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!Common.isConnectedToInternet(context)){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("No Internet Connection!")
                    .setCancelable(false)
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(context,"Retrying...",
                                    Toast.LENGTH_SHORT).show();
                            onReceive(context, intent);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Letter Formats");
            alert.show();
        }
    }
}
