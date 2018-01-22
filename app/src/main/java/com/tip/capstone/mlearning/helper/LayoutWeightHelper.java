package com.tip.capstone.mlearning.helper;

import android.widget.LinearLayout;

/**
 * Created by Jansen on 1/12/2018.
 */

public class LayoutWeightHelper {

    public static LinearLayout.LayoutParams ChangeProgress(float weight){
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                weight
        );
    }
}
