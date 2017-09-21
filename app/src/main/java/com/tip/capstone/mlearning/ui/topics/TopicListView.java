package com.tip.capstone.mlearning.ui.topics;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.capstone.mlearning.model.Topic;

@SuppressWarnings("WeakerAccess")
public interface TopicListView extends MvpView {

    void showAlert(String title, String message);

    void onTopicClicked(Topic topic);

    void onTakeAssessment();
}
