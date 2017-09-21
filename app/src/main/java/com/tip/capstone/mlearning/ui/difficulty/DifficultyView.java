package com.tip.capstone.mlearning.ui.difficulty;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.capstone.mlearning.model.Difficulty;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

@SuppressWarnings("WeakerAccess")
public interface DifficultyView extends MvpView {

    void onTermClicked(Difficulty difficulty);

}
