package com.tip.capstone.mlearning.ui.topics;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityTopicsListBinding;
import com.tip.capstone.mlearning.model.Difficulty;
import com.tip.capstone.mlearning.model.QuizGrade;
import com.tip.capstone.mlearning.model.Topic;
import com.tip.capstone.mlearning.ui.assessment.AssessmentActivity;
import com.tip.capstone.mlearning.ui.lesson.LessonActivity;

import io.realm.Realm;

public class TopicsListActivity extends MvpActivity<TopicListView, TopicListPresenter>
        implements TopicListView {

    private ActivityTopicsListBinding binding;
    private Realm realm;
    private Difficulty difficulty;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_topics_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int termId = getIntent().getIntExtra(Constant.ID, -1);
        if (termId == -1) {
            Toast.makeText(this, "No Intent Extra Found", Toast.LENGTH_SHORT).show();
            finish();
        }

        difficulty = realm.where(Difficulty.class).equalTo(Constant.ID, termId).findFirst();
        if (difficulty == null) {
            Toast.makeText(this, "No Difficulty Object Found", Toast.LENGTH_SHORT).show();
            finish();
        }

        getSupportActionBar().setTitle(difficulty.getTitle());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


    }

    @Override
    protected void onResume() {
        super.onResume();
        TopicListAdapter topicListAdapter = new TopicListAdapter(this, getMvpView());
        binding.recyclerView.setAdapter(topicListAdapter);

        topicListAdapter.setTopicList(difficulty.getTopics().sort(Topic.COL_SEQ));

        boolean isAssessmentOkayToTake = true;
        for (int i = 0; i < difficulty.getTopics().size(); i++) {
            QuizGrade quizGrade = realm.where(QuizGrade.class)
                    .equalTo("topic", difficulty.getTopics().get(i).getId())
                    .findFirst();
            if (quizGrade == null) {
                isAssessmentOkayToTake = false;
                Log.d("Quiz", "Not Taken: " + difficulty.getTopics().get(i).getName());
            } else {
                Log.d("Quiz", "Taken: " + difficulty.getTopics().get(i).getName());
            }
        }
        topicListAdapter.setAssessmentEnable(isAssessmentOkayToTake);
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
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

    @NonNull
    @Override
    public TopicListPresenter createPresenter() {
        return new TopicListPresenter();
    }

    @Override
    public void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Close", null)
                .show();
    }

    @Override
    public void onTopicClicked(Topic topic) {
        Intent intent = new Intent(this, LessonActivity.class);
        intent.putExtra(Constant.ID, topic.getId());
        startActivity(intent);
    }

    @Override
    public void onTakeAssessment() {
        Intent intent = new Intent(this, AssessmentActivity.class);
        intent.putExtra("difficulty", difficulty.getId());
        startActivity(intent);
    }
}
