package com.tip.capstone.mlearning.ui.topics;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityTopicsListBinding;
import com.tip.capstone.mlearning.model.Difficulty;
import com.tip.capstone.mlearning.model.Topic;
import com.tip.capstone.mlearning.ui.assessment.AssessmentActivity;
import com.tip.capstone.mlearning.ui.lesson.LessonActivity;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class TopicsListActivity extends MvpActivity<TopicListView, TopicListPresenter>
        implements TopicListView {

    private ActivityTopicsListBinding binding;
    private Realm realm;
    private Difficulty difficulty;
    private List<Topic> topicList;
    private TopicListAdapter topicListAdapter;

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

        getSupportActionBar().setTitle("Topics");
        topicList = realm.copyFromRealm(realm.where(Topic.class).findAll());
        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_gradient));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        topicListAdapter = new TopicListAdapter(this, getMvpView());
        binding.recyclerView.setAdapter(topicListAdapter);

        topicListAdapter.setTopicList(topicList);

    }


    @Override
    protected void onResume() {
        super.onResume();
        /*topicListAdapter = new TopicListAdapter(this, getMvpView());
        binding.recyclerView.setAdapter(topicListAdapter);

        topicListAdapter.setTopicList(topicList);

        boolean isAssessmentOkayToTake = true;
        for (int i = 0; i < topicList.size(); i++) {
            QuizGrade quizGrade = realm.where(QuizGrade.class)
                    .equalTo("topic", topicList.get(i).getId())
                    .findFirst();
            if (quizGrade == null) {
                isAssessmentOkayToTake = false;
                Log.d("Quiz", "Not Taken: " + topicList.get(i).getName());
            } else {
                Log.d("Quiz", "Taken: " + topicList.get(i).getName());
            }
        }
        topicListAdapter.setAssessmentEnable(isAssessmentOkayToTake);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
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
                presenter.loadTopicList(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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

    @Override
    public void setTopics(RealmResults<Topic> sort) {
        topicListAdapter.setTopicList(sort);
    }
}
