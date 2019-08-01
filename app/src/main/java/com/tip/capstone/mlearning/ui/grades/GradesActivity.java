package com.tip.capstone.mlearning.ui.grades;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityGradesBinding;
import com.tip.capstone.mlearning.model.Difficulty;
import com.tip.capstone.mlearning.ui.grades.detail.GradesDetailActivity;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

@SuppressLint("ConstantCondition")
public class GradesActivity extends MvpActivity<GradesView, GradesPresenter> implements GradesView {
    ActivityGradesBinding binding;

    private Realm realm;
    private RealmResults<Difficulty> difficultyRealmResults;
    private GradesListAdapter termListAdapter;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_grades);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Grades");
        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.bg_gradient));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        termListAdapter = new GradesListAdapter(getMvpView());
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
        difficultyRealmResults.removeAllChangeListeners();
        realm.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public GradesPresenter createPresenter() {
        return new GradesPresenter();
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
        Intent intent = new Intent(this, GradesDetailActivity.class);
        intent.putExtra(Constant.ID, difficulty.getId());
        startActivity(intent);
    }


}

