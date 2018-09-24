package com.kunal.shopclaws.LoginRegister;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SmsListener extends BroadcastReceiver {
    public static int flag=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            flag=1;
        }
        else
        {
            flag=0;
        }
    }
    }
