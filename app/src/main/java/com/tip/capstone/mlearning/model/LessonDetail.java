package com.tip.capstone.mlearning.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

public class LessonDetail extends RealmObject {

    public static final String COL_SEQ = "sequence";
    @PrimaryKey
    private int id;
    private int learningObjectiveId;
    private String body;
    private String body_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getBody() {
        return body;
    }


    public void setBody(String body) {
        this.body = body;
    }

    public static String getColSeq() {
        return COL_SEQ;
    }

    public String getBody_type() {
        return body_type;
    }

    public void setBody_type(String body_type) {
        this.body_type = body_type;
    }

    public int getLearningObjectiveId() {
        return learningObjectiveId;
    }

    public void setLearningObjectiveId(int learningObjectiveId) {
        this.learningObjectiveId = learningObjectiveId;
    }
}
