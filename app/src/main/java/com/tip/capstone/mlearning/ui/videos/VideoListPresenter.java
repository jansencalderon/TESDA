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
            R.raw.v1,
            R.raw.v2,
            R.raw.v3,
            R.raw.v4,
            R.raw.v5,
            R.raw.v6,
            R.raw.v7,
            R.raw.v8,
            R.raw.v9,
            R.raw.v10
    };
    private static final String[] VIDEO_NAME_WITHOUT_EXTENSION = {
            "v1",
            "v2",
            "v3",
            "v4",
            "v5",
            "v6",
            "v7",
            "v8",
            "v9",
            "v10",
            "v11"
    };
    private static final String[] VIDEO_TITLE = {
            "OHS",
            "Safety concerns when working on Electronics",
            "Basic Electronic Components",
            "How To Identify Electronic Components",
            "Electronics Tools",
            "How to Solder",
            "How to Desolder",
            "How to make circuit board",
            "Power Supply",
            "How to Read a Resistor"
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