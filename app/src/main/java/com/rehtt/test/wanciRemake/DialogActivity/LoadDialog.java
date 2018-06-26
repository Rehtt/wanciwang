package com.rehtt.test.wanciRemake.DialogActivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.rehtt.test.wanciRemake.R;

//加载框
public class LoadDialog extends Dialog {
    public LoadDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.load_dialog_layout);

        LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter=new IntentFilter();
        LocalReceiver receiver =new LocalReceiver();
        intentFilter.addAction("LoadDone");
        localBroadcastManager.registerReceiver(receiver,intentFilter);


    }



class LocalReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(final Context context, Intent intent) {
        LoadDialog.this.dismiss();
    }
}

}
