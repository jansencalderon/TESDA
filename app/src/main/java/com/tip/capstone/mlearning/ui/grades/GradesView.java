package com.tip.capstone.mlearning.ui.grades;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.capstone.mlearning.model.Difficulty;

/**
 * Created by Sen on 2/21/2017.
 */

public interface GradesView extends MvpView {

    void onTermClicked(Difficulty difficulty);
}
