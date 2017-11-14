package com.quizcom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AttemptQuizActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvQNumber, tvQuestion, tvBtnNext;
    RadioButton radio1, radio2, radio3, radio4;
    int qNumber = 0, answer = 0, correctAnswer = 0, noOfQuestions = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempt_quiz);
        setTitle("Answer the following question");
        init();
    }

    private void init() {
        tvQNumber = (TextView) findViewById(R.id.tv_q_attempt_number);
        tvQuestion = (TextView) findViewById(R.id.tv_question);
        tvBtnNext = (TextView) findViewById(R.id.tv_btn_nextQ);

        radio1 = (RadioButton) findViewById(R.id.rb_option1);
        radio2 = (RadioButton) findViewById(R.id.rb_option2);
        radio3 = (RadioButton) findViewById(R.id.rb_option3);
        radio4 = (RadioButton) findViewById(R.id.rb_option4);

        tvBtnNext.setOnClickListener(this);
        noOfQuestions = new MyDB(this).getTotalQuestions();

        showDataIfAvailable();
    }

    private void onNextClicked() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_options);
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Snackbar.make((View) radioGroup.getParent(), "Please choose an option.", Snackbar.LENGTH_LONG).show();
        } else {
            RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            if (answer == radioGroup.indexOfChild(radioButton) + 1) {
                correctAnswer++;
            }
            radioGroup.clearCheck();
            showDataIfAvailable();
        }
    }

    private void showDataIfAvailable() {
        if (noOfQuestions > 0 && noOfQuestions > qNumber) {
            Cursor cursor = new MyDB(this).getNextQuestion(++qNumber);
            int co = cursor.getCount();
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        String question = cursor.getString(cursor.getColumnIndexOrThrow(MyDB.COLUMN_Q));
                        answer = cursor.getInt(cursor.getColumnIndexOrThrow(MyDB.COLUMN_ANS));
                        String opt1 = cursor.getString(cursor.getColumnIndexOrThrow(MyDB.COLUMN_OPT1));
                        String opt2 = cursor.getString(cursor.getColumnIndexOrThrow(MyDB.COLUMN_OPT2));
                        String opt3 = cursor.getString(cursor.getColumnIndexOrThrow(MyDB.COLUMN_OPT3));
                        String opt4 = cursor.getString(cursor.getColumnIndexOrThrow(MyDB.COLUMN_OPT4));
                        tvQuestion.setText(question);
                        tvQNumber.setText("Question " + qNumber + " of " + noOfQuestions);
                        radio1.setText(opt1);
                        radio2.setText(opt2);
                        radio3.setText(opt3);
                        radio4.setText(opt4);
                        if (qNumber == noOfQuestions) {
                            tvBtnNext.setText("Submit");
                        }
                    }
                    while (cursor.moveToNext());
                }
            }
        } else {
            showDialog();
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quiz completed!");
        builder.setMessage("\n\t\t\tYour score is " + correctAnswer + " of total " + noOfQuestions+"\n");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNeutralButton("Try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(AttemptQuizActivity.this,AttemptQuizActivity.class));
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_btn_nextQ:
                onNextClicked();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to quit the Quiz?");
        builder.setTitle("Are you sure!");
        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AttemptQuizActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
