package com.tip.capstone.mlearning.ui.assessment;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ActivityAssessmentBinding;
import com.tip.capstone.mlearning.databinding.DialogQuizSummaryBinding;
import com.tip.capstone.mlearning.databinding.DialogViewReferenceBinding;
import com.tip.capstone.mlearning.helper.ResourceHelper;
import com.tip.capstone.mlearning.model.Assessment;
import com.tip.capstone.mlearning.model.AssessmentChoice;
import com.tip.capstone.mlearning.model.AssessmentGrade;
import com.tip.capstone.mlearning.model.LessonDetail;
import com.tip.capstone.mlearning.model.Letter;
import com.tip.capstone.mlearning.model.UserAnswer;
import com.tip.capstone.mlearning.ui.adapter.LetterListAdapter;
import com.tip.capstone.mlearning.ui.adapter.SummaryListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author pocholomia
 * @since 21/11/2016
 */
public class AssessmentActivity extends MvpViewStateActivity<AssessmentView, AssessmentPresenter>
        implements AssessmentView {

    private static final String TAG = AssessmentActivity.class.getSimpleName();

    private int type;

    private Realm realm;
    private RealmResults<Assessment> assessmentRealmResults;

    private ActivityAssessmentBinding binding;

    private List<UserAnswer> userAnswerList;
    private List<Assessment> assessmentList;

    private AssessmentChoiceListAdapter choiceAdapter;

    private LetterListAdapter adapterLetterAnswer;
    private LetterListAdapter adapterLetterChoice;
    private List<List<Letter>> lettersList;

    @SuppressWarnings("ConstantConditions") // assumes that the theme has toolbar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance(); // init realm
        // retrieving data from Realm DB
        type = getIntent().getIntExtra("type", -1);
        if (type == -1) {
            Toast.makeText(getApplicationContext(), "No Difficulty ID Found", Toast.LENGTH_SHORT).show();
            finish();
        }
        assessmentRealmResults = realm.where(Assessment.class).equalTo("type", type).findAll();
        if (assessmentRealmResults.size() <= 0) {
            // quit if no data
            Toast.makeText(getApplicationContext(), "No Assessment Data Found", Toast.LENGTH_SHORT).show();
            finish();
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_assessment);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // TODO: 24/11/2016 setup the toolbar title on manifest instead here
        getSupportActionBar().setTitle("Assessment");
        // setup RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        // setup adapter
        choiceAdapter = new AssessmentChoiceListAdapter();
        binding.recyclerView.setAdapter(choiceAdapter);

        // recyclerview for identification answer
        adapterLetterAnswer = new LetterListAdapter(getMvpView(), false);
        binding.recyclerViewAnswer.setLayoutManager(new GridLayoutManager(this, 10));
        binding.recyclerViewAnswer.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewAnswer.setAdapter(adapterLetterAnswer);

        // setup bind data
        String strNumItems = "Number of Items: " + assessmentRealmResults.size();
        binding.txtNumItems.setText(strNumItems);
        binding.setView(getMvpView());
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
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit?")
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AssessmentActivity.this.finish();
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        realm.close(); // close realm
        super.onDestroy();
    }

    @NonNull
    @Override
    public AssessmentPresenter createPresenter() {
        return new AssessmentPresenter();
    }

    @NonNull
    @Override
    public ViewState<AssessmentView> createViewState() {
        return new AssessmentViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        /*// alert user if already taken the assessment.json
        AssessmentGrade assessmentGrade = realm.where(AssessmentGrade.class).findFirst();
        if (assessmentGrade != null) {
            // already taken the assessment.json
            new AlertDialog.Builder(this)
                    .setTitle("Retake Assessment?")
                    .setMessage("If Submitted, it will overwrite previous grade!")
                    .setCancelable(false)
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GuessActivity.this.finish();
                        }
                    })
                    .show();
        }*/
        // setup data
        ((AssessmentViewState) getViewState()).setCounter(0);

        lettersList = new ArrayList<>();
        userAnswerList = new ArrayList<>();
        assessmentList = presenter.getShuffledAssessmentList(realm.copyFromRealm(assessmentRealmResults));

        for (int i = 0; i < assessmentList.size(); i++) lettersList.add(new ArrayList<Letter>());

        ((AssessmentViewState) getViewState()).setLetterList(lettersList);
        ((AssessmentViewState) getViewState()).setUserAnswerList(userAnswerList);
        ((AssessmentViewState) getViewState()).setAssessmentList(assessmentList);

        onSetQuestion(assessmentList.get(((AssessmentViewState) getViewState()).getCounter()));
    }

    /**
     * Setup the next Question
     *
     * @param assessment the assessment.json question to display
     */
    private void onSetQuestion(Assessment assessment) {
        int counter = ((AssessmentViewState) getViewState()).getCounter();
        String strNumItems = "Number of Items: " + (counter + 1) + "/" + assessmentRealmResults.size();
        binding.txtNumItems.setText(strNumItems);
        UserAnswer userAnswer = null;
        if (userAnswerList.size() > counter)
            userAnswer = userAnswerList.get(counter);
        Log.d(TAG, "onSetQuestion: user answer: " + (userAnswer == null ? "null" : userAnswer.getUserAnswer()));
        if (assessment.getQuestion_type().equals(Constant.QUESTION_TYPE_TEXT)) {
            binding.txtQuestion.setText((counter + 1) + ". " + assessment.getQuestion());
            binding.imageQuestion.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.recyclerViewAnswer.setVisibility(View.GONE);
            binding.recyclerViewLetterChoices.setVisibility(View.GONE);
            List<AssessmentChoice> choices = realm.where(AssessmentChoice.class).equalTo("questionId", assessment.getId()).findAll();
            //  Toast.makeText(this,"Size "+ choices.size(), Toast.LENGTH_SHORT).show();
            choiceAdapter.setChoiceList(choices);
            if (userAnswer != null)
                choiceAdapter.setAnswer(userAnswer.getUserAnswer());
        } else if (assessment.getQuestion_type().equals(Constant.QUESTION_TYPE_IMAGE)) {
            Glide.with(this)
                    .load(ResourceHelper.getDrawableResourceId(this, assessment.getQuestion()))
                    .fitCenter()
                    .into(binding.imageQuestion);
            binding.txtQuestion.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.recyclerViewAnswer.setVisibility(View.GONE);
            binding.recyclerViewLetterChoices.setVisibility(View.GONE);
            List<AssessmentChoice> choices = realm.where(AssessmentChoice.class).equalTo("questionId", assessment.getId()).findAll();
            choiceAdapter.setChoiceList(choices);
            if (userAnswer != null)
                choiceAdapter.setAnswer(userAnswer.getUserAnswer());

            /*binding.recyclerView.setVisibility(View.GONE);
            binding.recyclerViewAnswer.setVisibility(View.VISIBLE);
            binding.recyclerViewLetterChoices.setVisibility(View.VISIBLE);
            adapterLetterAnswer.setLetters(presenter.getAssessmentLetter(assessment.getAnswer()));

            Log.d(TAG, "onSetQuestion: answer: " + assessment.getAnswer());

            if (lettersList.get(counter).size() <= 0) {
                lettersList.set(counter, presenter.getChoiceLetters(assessment.getAnswer()));
                ((AssessmentViewState) getViewState()).setLetterList(lettersList);
            }

            adapterLetterChoice.setLetters(lettersList.get(counter));
            Log.d(TAG, "onSetQuestion: choices: " + adapterLetterChoice.getAnswer());

            if (userAnswer != null)
                for (int i = 0; i < userAnswer.getUserAnswer().length(); i++) {
                    String s = userAnswer.getUserAnswer().charAt(i) + "";
                    int emptyIndex = adapterLetterAnswer.getEmptyIndex();
                    if (emptyIndex != -1) adapterLetterAnswer.addLetter(s, emptyIndex);
                }
            Log.d(TAG, "onSetQuestion: ident answer: " + adapterLetterAnswer.getAnswer());*/
        }
    }

    @Override
    public void onPrevious() {
        if (((AssessmentViewState) getViewState()).getCounter() <= 0) {
            // TODO: 24/11/2016 this implementation can be changed by disabling button if counter is 0
            new AlertDialog.Builder(this)
                    .setTitle("Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AssessmentActivity.this.finish();
                        }
                    })
                    .setNegativeButton("CANCEL", null)
                    .show();
        } else {
            setUserAnswer(false);
            ((AssessmentViewState) getViewState()).decrementCounter();
            onSetQuestion(assessmentList.get(((AssessmentViewState) getViewState()).getCounter()));
        }
    }

    private String setUserAnswer(boolean hasReturn) {
        Assessment assessment = assessmentList.get(((AssessmentViewState) getViewState()).getCounter());
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuestionId(assessment.getId());
        userAnswer.setCorrectAnswer(assessment.getAnswer());
        userAnswer.setLessonDetailId(assessment.getLesson_detail());

        AssessmentChoice choice = choiceAdapter.getSelectedChoice();
        if (choice == null && hasReturn) {
            return "Select Answer";
        }
        userAnswer.setUserAnswer(choice != null ? choice.getBody() : "");
        userAnswer.setChoiceType(choice != null ? choice.getChoice_type() : 0);

        /*if (assessment.getQuestion_type() == Constant.QUESTION_TYPE_MULTIPLE) {
            AssessmentChoice choice = choiceAdapter.getSelectedChoice();
            if (choice == null && hasReturn) {
                return "Select Answer";
            }
            userAnswer.setUserAnswer(choice != null ? choice.getBody() : "");
            userAnswer.setChoiceType(choice != null ? choice.getChoice_type() : 0);
        } else if (assessment.getQuestion_type() == Constant.QUESTION_TYPE_IDENTIFICATION) {
            if (hasReturn) {
                if (adapterLetterAnswer.getAnswer().isEmpty()) {
                    return "Enter Answer";
                }
                if (!adapterLetterAnswer.completeAnswer()) {
                    return "Answer incomplete";
                }
            }
            userAnswer.setUserAnswer(adapterLetterAnswer.getAnswer());
            userAnswer.setChoiceType(Constant.Q_TYPE_TEXT);
        }*/
        if (userAnswerList.size() > ((AssessmentViewState) getViewState()).getCounter())
            userAnswerList.set(((AssessmentViewState) getViewState()).getCounter(), userAnswer);
        else
            userAnswerList.add(userAnswer);
        ((AssessmentViewState) getViewState()).setUserAnswerList(userAnswerList);
        return null;
    }

    @Override
    public void onNext() {
        String message = setUserAnswer(true);
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            return;
        }

        if (((AssessmentViewState) getViewState()).getCounter() < assessmentList.size() - 1) {
            ((AssessmentViewState) getViewState()).incrementCounter();
            onSetQuestion(assessmentList.get(((AssessmentViewState) getViewState()).getCounter()));
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Submit?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // todo: compute score and show answer summary
                            showSummary();
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            userAnswerList.remove(((AssessmentViewState) getViewState()).getCounter());
                            ((AssessmentViewState) getViewState()).setUserAnswerList(userAnswerList);
                            AssessmentActivity.this.onSetQuestion(assessmentList.get(((AssessmentViewState) getViewState()).getCounter()));
                        }
                    })
                    .show();

        }
    }

    @Override
    public void restoreData(int counter, List<Assessment> assessmentList, List<UserAnswer> userAnswerList, List<List<Letter>> lettersList) {
        this.assessmentList = assessmentList;
        this.userAnswerList = userAnswerList;
        this.lettersList = lettersList;
        onSetQuestion(assessmentList.get(counter));
    }

    @Override
    public void onLetterClicked(int position, boolean choice, Letter letter) {
        String strLetter = letter.getLetter();
        Log.d(TAG, "onLetterClicked: choice: " + choice + " ||letter: " + strLetter);
        if (choice) {
            int emptyIndex = adapterLetterAnswer.getEmptyIndex();
            if (emptyIndex != -1) {
                adapterLetterChoice.removeLetter(position);
                adapterLetterAnswer.addLetter(strLetter, emptyIndex);
            }
        } else {
            int emptyIndex = adapterLetterChoice.getEmptyIndex();
            if (emptyIndex != -1) {
                adapterLetterAnswer.removeLetter(position);
                adapterLetterChoice.addLetter(strLetter, emptyIndex);
            }
        }
    }

    /**
     * Saves the Result of the Assessment
     * Displays the Summary Dialog
     */
    private void showSummary() {
        DialogQuizSummaryBinding dialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_quiz_summary,
                null,
                false);
        int score = 0;
        int items = userAnswerList.size();
        for (UserAnswer userAnswer : userAnswerList) {
            if (userAnswer.isCorrect()) score++;
        }
        dialogBinding.txtRawScore.setText(score + "/" + items);
        String ave = Math.round(presenter.getAverage(score, items)) + "%";
        dialogBinding.txtAverage.setText(ave);

        dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialogBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        SummaryListAdapter summaryListAdapter = new SummaryListAdapter(getMvpView());
        summaryListAdapter.setUserAnswerList(userAnswerList);
        dialogBinding.recyclerView.setAdapter(summaryListAdapter);

        Number maxCount = realm
                .where(AssessmentGrade.class)
                .equalTo("type", type)
                .max("count");

        Number maxId = realm.where(AssessmentGrade.class).max("id");

        final AssessmentGrade assessmentGrade = new AssessmentGrade();
        assessmentGrade.setId(maxId == null ? 1 : maxId.intValue() + 1);
        assessmentGrade.setType(type);
        assessmentGrade.setRawScore(score);
        assessmentGrade.setItemCount(items);
        assessmentGrade.setDateUpdated(new Date().getTime());
        assessmentGrade.setCount(maxCount == null ? 1 : maxCount.intValue() + 1);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(assessmentGrade);
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("Summary")
                .setView(dialogBinding.getRoot())
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AssessmentActivity.this.finish();
                    }
                })
                .show();
    }

    @Override
    public void onViewReference(int lessonDetailId) {
        DialogViewReferenceBinding dialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_view_reference,
                null,
                false);
        LessonDetail lessonDetail = realm.where(LessonDetail.class).equalTo("id", lessonDetailId).findFirst();
        dialogBinding.setLessonDetail(lessonDetail);
        if (lessonDetail.getBody_type().equals(Constant.DETAIL_TYPE_TEXT)) {
            dialogBinding.txtBody.setText(lessonDetail.getBody());
        } else {
            Glide.with(this)
                    .load(ResourceHelper.getDrawableResourceId(this,
                            lessonDetail.getBody()))
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(dialogBinding.imageLessonDetail);
        }


        new AlertDialog.Builder(this)
                .setView(dialogBinding.getRoot())
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AssessmentActivity.this.finish();
                    }
                })
                .show();
    }
}
