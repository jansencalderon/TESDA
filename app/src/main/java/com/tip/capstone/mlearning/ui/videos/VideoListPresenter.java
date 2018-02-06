package com.tip.capstone.mlearning.ui.videos;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 29/11/2016
 */

public class VideoListPresenter extends MvpNullObjectBasePresenter<VideoListView> {

    private static final int[] VIDEO_RES_ID = {
            R.raw.topic1,
            R.raw.topic2,
            R.raw.topic3,
            R.raw.topic4,
            R.raw.topic5_1,
            R.raw.topic5_2,
            R.raw.topic6,
            R.raw.topic7_1,
            R.raw.topic7_2
    };
    private static final String[] VIDEO_NAME_WITHOUT_EXTENSION = {
            "topic1",
            "topic2",
            "topic3",
            "topic4",
            "topic5_1",
            "topic5_2",
            "topic6",
            "topic7_1",
            "topic7_2"
    };
    private static final String[] VIDEO_TITLE = {
            "Basic Health and Safety Procedure",
            "Basic Tools Required for Electronic PCB Repairs",
            "Calculation and Mensturation",
            "Prepare and Interpret Technical Drawings",
            "Computer Operations 1",
            "Computer Operations 2",
            "Two Way Switching",
            "Air Conditioner Assembly",
            "Air Conditioner Disassembly"
    };

    public void loadVideoList(String query) {
        List<Video> videoList = new ArrayList<>();
        for (int i = 0; i < VIDEO_RES_ID.length; i++) {
            int resId = VIDEO_RES_ID[i];
            String nameWithoutExtension = i < VIDEO_NAME_WITHOUT_EXTENSION.length ?
                    VIDEO_NAME_WITHOUT_EXTENSION[i] : "";
            String videoTitle = i < VIDEO_TITLE.length ?
                    VIDEO_TITLE[i] : "";
            Video video = new Video(resId, nameWithoutExtension, videoTitle);
            if (query != null && !query.isEmpty() && videoTitle.toUpperCase().contains(query.toUpperCase())) {
                videoList.add(video);
            } else if (query == null || query.isEmpty()) {
                videoList.add(video);
            }

        }
        getView().setVideoList(videoList);
    }
}