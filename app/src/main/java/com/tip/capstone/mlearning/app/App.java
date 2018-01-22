package com.tip.capstone.mlearning.app;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.helper.StringHelper;
import com.tip.capstone.mlearning.model.Assessment;
import com.tip.capstone.mlearning.model.AssessmentChoice;
import com.tip.capstone.mlearning.model.Choice;
import com.tip.capstone.mlearning.model.Difficulty;
import com.tip.capstone.mlearning.model.Glossary;
import com.tip.capstone.mlearning.model.Lesson;
import com.tip.capstone.mlearning.model.LessonDetail;
import com.tip.capstone.mlearning.model.Question;
import com.tip.capstone.mlearning.model.School;
import com.tip.capstone.mlearning.model.Topic;
import com.tip.capstone.mlearning.model.Tutorial;

import io.realm.Realm;
import io.realm.RealmConfiguration;

;

/**
 * @author pocholomia
 * @since 18/11/2016
 * Initialization and Configuration for the Application
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new EntypoModule());
        // initialize realm configuration
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        // setting up data for realm
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                // delete all data excluding grades
                realm.delete(Difficulty.class);
                realm.delete(Topic.class);
                realm.delete(Lesson.class);
                realm.delete(LessonDetail.class);
                realm.delete(Question.class);
                realm.delete(Choice.class);
                realm.delete(Assessment.class);
                realm.delete(AssessmentChoice.class);
                realm.delete(Glossary.class);
                realm.delete(Tutorial.class);
                realm.delete(School.class);

                // add data from raw files
                realm.createAllFromJson(Difficulty.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.difficulty));
                realm.createAllFromJson(Topic.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.topic));
                realm.createAllFromJson(Lesson.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.lesson));
                realm.createAllFromJson(LessonDetail.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.lesson_details));
                realm.createAllFromJson(School.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.school));
                realm.createAllFromJson(Question.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.questions));
                realm.createAllFromJson(Choice.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.choices));

                realm.createAllFromJson(Assessment.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.assessment));
                realm.createAllFromJson(AssessmentChoice.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.assessment_choices));
                realm.createAllFromJson(Glossary.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.glossary));
                realm.createAllFromJson(Tutorial.class, StringHelper.readRawTextFile(getApplicationContext(), R.raw.tutorials));

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                realm.close();
            }
        });

    }
}
