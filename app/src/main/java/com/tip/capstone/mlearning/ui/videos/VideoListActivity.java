package com.tip.capstone.mlearning.ui.videos;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityVideoListBinding;
import com.tip.capstone.mlearning.model.Video;
import com.tip.capstone.mlearning.ui.videos.play.VideoPlayActivity;

import java.util.List;

public class VideoListActivity extends MvpViewStateActivity<VideoListView, VideoListPresenter>
        implements VideoListView {

    private VideoListAdapter adapter;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ActivityVideoListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_video_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // assumes theme has toolbar
        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.bg_gradient));

        adapter = new VideoListAdapter(getMvpView());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        //binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.recyclerView.setAdapter(adapter);

        presenter.loadVideoList(null);
    }

    @NonNull
    @Override
    public VideoListPresenter createPresenter() {
        return new VideoListPresenter();
    }

    @NonNull
    @Override
    public ViewState<VideoListView> createViewState() {
        setRetainInstance(true);
        return new VideoListViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_video_list, menu);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.loadVideoList(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onVideoClicked(Video video) {
        Intent intent = new Intent(this, VideoPlayActivity.class);
        intent.putExtra(Constant.RES_ID, video.getResourceId());
        intent.putExtra("videoTitle", video.getTitle());
        startActivity(intent);
    }

    @Override
    public void setVideoList(List<Video> videoList) {
        adapter.setVideoList(videoList);
    }
}