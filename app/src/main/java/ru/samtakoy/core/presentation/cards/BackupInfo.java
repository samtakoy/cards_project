package ru.samtakoy.core.presentation.cards;

import androidx.annotation.Nullable;

public class BackupInfo {


    @Nullable private String mQuestion;
    @Nullable private String mAnswer;

    public BackupInfo(){

    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    public boolean hasBackup(boolean onAnswer) {
        if(onAnswer){
            return !isAnswerEmpty();
        } else{
            return !isQuestionEmpty();
        }
    }

    public boolean isAnswerEmpty(){
        return mAnswer == null;
    }

    public boolean isQuestionEmpty(){
        return mQuestion == null;
    }

    public void setQuestionIfEmpty(String question) {
        if(isQuestionEmpty()){
            setQuestion(question);
        }
    }

    public void setAnswerIfEmpty(String answer) {
        if(isAnswerEmpty()){
            setAnswer(answer);
        }
    }

    public void resetAnswer() {
        mAnswer = null;
    }

    public void resetQuestion() {
        mQuestion = null;
    }
}
