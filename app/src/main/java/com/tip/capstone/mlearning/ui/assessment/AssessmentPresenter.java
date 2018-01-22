package com.tip.capstone.mlearning.ui.assessment;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.capstone.mlearning.model.Assessment;
import com.tip.capstone.mlearning.model.AssessmentChoice;
import com.tip.capstone.mlearning.model.Letter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * @author pocholomia
 * @since 22/11/2016
 */

class AssessmentPresenter extends MvpNullObjectBasePresenter<AssessmentView> {

    private static final String TAG = AssessmentPresenter.class.getSimpleName();

    /**
     * Fisher-Yates Shuffle Algorithm for both the questions and the choices for each question.
     *
     * @param assessmentList the list of quiz question to shuffle
     * @return a shuffled list of quiz question
     */
    List<Assessment> getShuffledAssessmentList(List<Assessment> assessmentList) {
        int n = assessmentList.size();
        for (int i = 0; i < n; i++) {
            // Get a random index of the array past i.
            int random = getRandomInt(i, n);
            // Swap the random element with the present element.
            Assessment randomAssessment = assessmentList.get(random);
            assessmentList.set(random, assessmentList.get(i));
            Realm realm = Realm.getDefaultInstance();
            List<AssessmentChoice> choices = realm.copyFromRealm(realm.where(AssessmentChoice.class).equalTo("questionId", randomAssessment.getId()).findAll());
            int m = choices.size();
            for (int j = 0; j < m; j++) {
                int r = getRandomInt(j, m);
                AssessmentChoice randomChoice = choices.get(r);
                choices.set(r, choices.get(j));
                choices.set(j, randomChoice);
            }
            RealmList<AssessmentChoice> choiceRealmList = new RealmList<>();
            choiceRealmList.addAll(choices);
            randomAssessment.setAssessmentchoices(choiceRealmList);


            assessmentList.set(i, randomAssessment);
            realm.close();
        }
        return assessmentList;
    }

    /**
     * @param i    index
     * @param size size of the list
     * @return random index to used
     */
    private int getRandomInt(int i, int size) {
        return i + (int) (Math.random() * (size - i));
    }

    double getAverage(int score, int items) {
        return (((double) score / (double) items) * 50.0) + 50.0;
    }

    public List<Letter> getChoiceLetters(String answer) {
        answer = answer.replaceAll("\\s+", "");
        List<Letter> letters = new ArrayList<>();
        for (int i = 0; i < answer.length(); i++) {
            Letter letter = new Letter();
            letter.setLetter(answer.charAt(i) + "");
            letter.setGenerated(false);
            letter.setGiven(true);
            letter.setSpace(false);
            letters.add(letter);
        }
        while (letters.size() % 10 != 0) {
            Random r = new Random();
            char c = (char) (r.nextInt(26) + 'a');
            Letter letter = new Letter();
            letter.setLetter(c + "");
            letter.setGenerated(false);
            letter.setGiven(true);
            letter.setSpace(false);
            letters.add(letter);
        }
        Collections.shuffle(letters);
        return letters;
    }

    public List<Letter> getAssessmentLetter(String answer) {
        List<Letter> letters = new ArrayList<>();
        for (int i = 0; i < answer.length(); i++) {
            Letter letter = new Letter();
            letter.setLetter((answer.charAt(i) + "").contentEquals(" ") ? " " : "");
            letter.setGenerated(false);
            letter.setGiven(false);
            letter.setSpace((answer.charAt(i) + "").contentEquals(" "));
            letters.add(letter);
        }
        return letters;
    }

}
