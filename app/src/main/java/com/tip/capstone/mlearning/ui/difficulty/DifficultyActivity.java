package com.tip.capstone.mlearning.ui.difficulty;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityDifficultyBinding;
import com.tip.capstone.mlearning.model.Difficulty;
import com.tip.capstone.mlearning.ui.topics.TopicsListActivity;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class DifficultyActivity extends MvpActivity<DifficultyView, DifficultyPresenter> implements DifficultyView {

    private Realm realm;
    private RealmResults<Difficulty> difficultyRealmResults;
    private DifficultyListAdapter termListAdapter;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        ActivityDifficultyBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_difficulty);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        termListAdapter = new DifficultyListAdapter(getMvpView());
        binding.recyclerView.setAdapter(termListAdapter);

        difficultyRealmResults = realm.where(Difficulty.class).findAllSortedAsync(Difficulty.COL_SEQ);
        difficultyRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Difficulty>>() {
            @Override
            public void onChange(RealmResults<Difficulty> element) {
                termListAdapter.setDifficultyList(realm.copyFromRealm(difficultyRealmResults));
            }
        });
    }

    @Override
    protected void onDestroy() {
        difficultyRealmResults.removeChangeListeners();
        realm.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public DifficultyPresenter createPresenter() {
        return new DifficultyPresenter();
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
    public void onTermClicked(Difficulty difficulty) {
        Intent intent = new Intent(this, TopicsListActivity.class);
        intent.putExtra(Constant.ID, difficulty.getId());
        startActivity(intent);
    }

}
