package com.example.quiz_assignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class QuestionBank {

    ArrayList<Question> questionBank;
    int numOfQuestion;

    public ArrayList<Question> getQuestionBank() {
        return questionBank;
    }

    public int getNumOfQuestion() {
        return numOfQuestion;
    }

    public  QuestionBank(){
        questionBank = null;
        numOfQuestion = 0;
    }

    public QuestionBank(String[] questions, String[] answers, String[] colors, int numOfQuestion){
        ArrayList<Question> prepareQuestion = new ArrayList<>();
        questionBank = new ArrayList<>();
        this.numOfQuestion = numOfQuestion;

        // shuffles colors
        List<String> shuffleColor = Arrays.asList(colors);
        Collections.shuffle(shuffleColor);

        for(int i=0; i < questions.length; i++){
            prepareQuestion.add(new Question(questions[i], answers[i], shuffleColor.get(i)));
        }
        // shuffle question bank
        Collections.shuffle(prepareQuestion);

        for(int i=0; i<numOfQuestion; i++){
            questionBank.add(prepareQuestion.get(i));
        }
    }

    public void setArrayList(ArrayList<Question> questionBank){
        this.questionBank = questionBank;
        this.numOfQuestion = questionBank.size();
    }
}
