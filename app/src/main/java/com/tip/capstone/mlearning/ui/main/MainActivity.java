package com.tip.capstone.mlearning.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ActivityMainBinding;
import com.tip.capstone.mlearning.ui.difficulty.DifficultyActivity;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setView(getMvpView());
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void onStudyClicked() {
        startActivity(new Intent(this, DifficultyActivity.class));
    }
/*
    @Override
    public void onAssessmentClicked() {
        startActivity(new Intent(this, AssessmentActivity.class));
    }

    @Override
    public void onGradesClicked() {
        startActivity(new Intent(this, GradesActivity.class));
    }

    @Override
    public void onVideosClicked() {
        startActivity(new Intent(this, VideoListActivity.class));
    }

    @Override
    public void onSimulationClicked() {
        startActivity(new Intent(this, SimulationMenuActivity.class));
    }

    @Override
    public void onGlossaryClicked() {
        startActivity(new Intent(this, GlossaryActivity.class));
    }*/
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                startActivity(new Intent(MainActivity.this, TutorialActivity.class));
                return true;
            case R.id.action_about:
                Dialog dialog = new Dialog(this);
                dialog.setTitle("About");
                dialog.setContentView(R.layout.dialog_about);
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
