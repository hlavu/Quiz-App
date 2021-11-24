package com.example.quiz_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragment;
    ProgressBar progressBar;
    int numOfQuestion;
    int numOfCorrect;
    QuestionBank questionBank;
    String[] questions;
    String[] answers;
    String[] colors;
    Button trueBtn;
    Button falseBtn;
    int index;
    int tempQuestionNo;
    InternalStorageManager internalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trueBtn = findViewById(R.id.truebtn);
        falseBtn = findViewById(R.id.falsebtn);
        progressBar = findViewById(R.id.progressBar);

        internalStorage = new InternalStorageManager();
        questions = getResources().getStringArray(R.array.questions);
        answers = getResources().getStringArray(R.array.answers);
        colors = getResources().getStringArray(R.array.colors);

        if (savedInstanceState != null) {
            questionBank = new QuestionBank();
            questionBank.setArrayList(savedInstanceState.getParcelableArrayList("questionBank"));
            index = savedInstanceState.getInt("currentIdx");
            tempQuestionNo = savedInstanceState.getInt("tempQuestionNo");
            numOfCorrect = savedInstanceState.getInt("numOfCorrect");
        } else {
            questionBank = new QuestionBank(questions, answers, colors, questions.length);
            index = 0;
            numOfCorrect = 0;
            tempQuestionNo = 0;
        }

        newAttempt(questionBank);

        trueBtn.setOnClickListener(view -> checkResult(trueBtn.getText().toString()));
        falseBtn.setOnClickListener(view -> checkResult(falseBtn.getText().toString()));
    }

    // receives new number of questions and saves to a temp value
    void setQuestionNo(int questionNo){
        tempQuestionNo = questionNo;
    }

    // prepares for the next attempt
    void resetData(int numOfQuestion){
        if(numOfQuestion == 0){
            numOfQuestion = questions.length;
        }
        questionBank = new QuestionBank(questions, answers, colors, numOfQuestion);
        index = 0;
        numOfCorrect = 0;
    }

    // displays new attempt
    void newAttempt(QuestionBank questionBank){
        progressBar.setProgress(index);
        numOfQuestion = questionBank.getNumOfQuestion();
        progressBar.setMax(numOfQuestion);
        fragment = getSupportFragmentManager();
        displayNewFragment();
    }

    // creates menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.quiz_menu, menu);
        return true;
    }

    // sets onClickListener for each option in the menu
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.getArg: {
                ArrayList<String> results = internalStorage.getFromInternalPrivateFile(MainActivity.this);

                if(results.size() > 0){
                    int sum = 0;
                    for(int i=0; i<results.size(); i++){
                        sum += Integer.parseInt(results.get(i));
                    }

                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage(getString(R.string.results, sum, results.size()));
                    dialog.setCancelable(true);

                    dialog.setPositiveButton(
                            R.string.saveBtn,
                            (dialog1, id) -> dialog1.cancel());

                    AlertDialog resultAlert = dialog.create();
                    resultAlert.show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.noHistory , Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.selectQuestionNo: {
                String[] numbers = getResources().getStringArray(R.array.numQ);
                AlertDialog.Builder dialogQ = new AlertDialog.Builder(this);
                dialogQ.setTitle(R.string.questionNo)
                        .setItems(numbers, (dialog, which) -> {
                            setQuestionNo(Integer.parseInt(numbers[which]));
                            resetData(tempQuestionNo);
                            newAttempt(questionBank);
                        });
                dialogQ.setCancelable(true);
                AlertDialog resultAlert = dialogQ.create();
                resultAlert.show();
                break;
            }
            case R.id.reset: {
                internalStorage.resetTheStorage(MainActivity.this);
                break;
            }
        }
        return true;
    }

    // remove current fragment in the fragment frame and add the new one
    public void displayNewFragment() {
        FragmentProcess fragmentObj = (FragmentProcess) fragment.findFragmentById(R.id.add_replace_area);

        Bundle bundle = new Bundle();
        bundle.putParcelable("question", questionBank.getQuestionBank().get(index));

        if (fragmentObj != null) {
            fragment.beginTransaction().remove(fragmentObj).commit();
        }
        fragment.beginTransaction().add(R.id.add_replace_area, FragmentProcess.class, bundle).commit(); // add
    }


    // checks result, increases numOfCorrect if the answer is correct
    // checks numOfQuestion left, if there is : changes progress bar and calls displayNewFragment()
    // else shows Result Dialog
    void checkResult(String correctAnswer){
        if(questionBank.getQuestionBank().get(index).getAnswer().equals(correctAnswer)){
            Toast.makeText(getApplicationContext(), R.string.correct, Toast.LENGTH_SHORT).show();
            numOfCorrect++;
        }else {
            Toast.makeText(getApplicationContext(), R.string.incorrect, Toast.LENGTH_SHORT).show();
        }

        if(index < numOfQuestion-1){
            index++;
            displayNewFragment();
            progressBar.setProgress(index);
        }else {
            progressBar.setProgress(index+1);
            showAlertDialog();
        }
    }

    public void showAlertDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.dialogTitle);
        dialog.setMessage(getString(R.string.message, numOfCorrect, numOfQuestion));
        dialog.setCancelable(true);

        dialog.setPositiveButton(
                R.string.saveBtn,
                (dialog1, id) -> {
                    internalStorage.saveNewResultInInternalPrivateFile(MainActivity.this, numOfCorrect);
                    dialog1.cancel();
                    resetData(tempQuestionNo);
                    newAttempt(questionBank);
                });

        dialog.setNegativeButton(
                R.string.ignoreBtn,
                (dialog2, id) -> {
                    dialog2.cancel();
                    resetData(tempQuestionNo);
                    newAttempt(questionBank);
                });

        AlertDialog resultAlert = dialog.create();
        resultAlert.show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("questionBank", questionBank.getQuestionBank());
        outState.putInt("currentIdx", index);
        outState.putInt("numOfCorrect", numOfCorrect);
        outState.putInt("tempQuestionNo", tempQuestionNo);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        questionBank.setArrayList(savedInstanceState.getParcelableArrayList("questionBank"));
        index = savedInstanceState.getInt("currentIdx");
        tempQuestionNo = savedInstanceState.getInt("tempQuestionNo");
        numOfCorrect = savedInstanceState.getInt("numOfCorrect");
        progressBar.setMax(questionBank.getNumOfQuestion());
        progressBar.setProgress(index);
    }
}