package com.tip.capstone.mlearning.ui.topics;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.capstone.mlearning.model.Topic;

import io.realm.Case;
import io.realm.Realm;
import io.realm.Sort;

import static android.content.ContentValues.TAG;

/**
 * @author pocholomia
 * @since 18/11/2016
 */
public class TopicListPresenter extends MvpNullObjectBasePresenter<TopicListView> {
    public void refreshTopics(final String topicsJson) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Topic.class);
                realm.createOrUpdateAllFromJson(Topic.class, topicsJson);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
                Log.d(TAG, "onSuccess: Realm Set Topic List");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                realm.close();
                getView().showAlert("Database Error", "Error on Saving Topic List");
            }
        });
    }

    public void loadTopicList(String query) {
        final Realm realm = Realm.getDefaultInstance();
        if (query.equals(""))
            getView().setTopics(realm.where(Topic.class).findAll());
        else
            getView().setTopics(realm.where(Topic.class).contains("title", query, Case.INSENSITIVE).findAll().sort("title", Sort.DESCENDING));

        realm.close();
    }
}
