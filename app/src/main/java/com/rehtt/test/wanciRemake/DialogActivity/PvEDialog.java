package com.rehtt.test.wanciRemake.DialogActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;

import com.rehtt.test.wanciRemake.Activity.PvEActivity;
import com.rehtt.test.wanciRemake.R;
import com.rehtt.test.wanciRemake.Tools.Data;
import com.rehtt.test.wanciRemake.Tools.Dip;
import com.rehtt.test.wanciRemake.Tools.SetFullScreen;


public class PvEDialog extends Dialog {


    public PvEDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.pve_dialog);
        setCanceledOnTouchOutside(false);

    }


    Dip dip = new Dip();
    Data data = new Data();

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            dip.setContext(getContext());
            float x = event.getX();
            float y = event.getY();
            if (x > dip.px(21) && x < dip.px(164) && y > dip.px(90) && y < dip.px(180)) {
                data.setPvEGrade(1);
            } else if (x > dip.px(168) && x < dip.px(312) && y > dip.px(90) && y < dip.px(180)) {
                data.setPvEGrade(2);
            } else if (x > dip.px(21) && x < dip.px(164) && y > dip.px(185) && y < dip.px(275)) {
                data.setPvEGrade(3);
            } else if (x > dip.px(168) && x < dip.px(312) && y > dip.px(185) && y < dip.px(275)) {
            }
//            Intent i =new Intent();
//            i.setClass(getContext(), PvEActivity.class);
//            getContext().startActivity(i);
            getContext().startActivity(new Intent().setClass(getContext(), PvEActivity.class));
        }
        return super.onTouchEvent(event);
    }
}
