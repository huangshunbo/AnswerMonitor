package com.android.answermonitor.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_answer")
public class AnswerBean {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "num")
    private int num;
    @DatabaseField(columnName = "question",index = true)
    private String question;
    @DatabaseField(columnName = "answer")
    private String answer;

    public AnswerBean() {
    }

    public AnswerBean(int num, String question, String answer) {
        this.num = num;
        this.question = question;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
