package com.quizcom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CreateQuizActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvQNumber, tvBtnSubmit, tvBtnAddOption;
    EditText etQuestion, etAnswer;
    LinearLayout optionsContainer, ll_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);
        setTitle(getString(R.string.create_quiz_title));
        init();
    }

    private void init() {
        tvQNumber = (TextView) findViewById(R.id.tv_q_number_create);
        tvBtnSubmit = (TextView) findViewById(R.id.tv_btn_submit_question);
        tvBtnAddOption = (TextView) findViewById(R.id.tv_add_option);
        etQuestion = (EditText) findViewById(R.id.et_create_question);
        etAnswer = (EditText) findViewById(R.id.et_create_answer);
        optionsContainer = (LinearLayout) findViewById(R.id.options_container);
        ll_answer = (LinearLayout) findViewById(R.id.ll_answer);
        tvBtnSubmit.setOnClickListener(this);
        tvBtnAddOption.setOnClickListener(this);
        int qNumber = new MyDB(this).getTotalQuestions();
        if (qNumber < 5 || qNumber >= 0) {
            tvQNumber.setText("Question " + (1 + qNumber) + " of 5");
            ll_answer.setVisibility(View.GONE);
            tvBtnSubmit.setVisibility(View.GONE);

        } else {
            Snackbar.make((View) optionsContainer.getParent(), "Quiz already created.", Snackbar.LENGTH_SHORT).show();
            finish();
        }
    }

    private void addOptions() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        int currentChildCount = optionsContainer.getChildCount();
        if (currentChildCount < 4) {
            View view = inflater.inflate(R.layout.optins_layout, optionsContainer, false);
            TextView textView = view.findViewById(R.id.tv_opt_num);
            if (textView != null) {
                textView.setText((currentChildCount + 1) + "  : ");
            }
            optionsContainer.addView(view);
            view.findViewById(R.id.et_option).requestFocus();
        }
        currentChildCount = optionsContainer.getChildCount();
        if (currentChildCount >= 4) {
            tvBtnAddOption.setVisibility(View.GONE);
            ll_answer.setVisibility(View.VISIBLE);
            tvBtnSubmit.setVisibility(View.VISIBLE);
        }
    }

    private void submitQuestion() {
        int qNumber = new MyDB(this).getTotalQuestions();
        String question = etQuestion.getText().toString();
        String answer = etAnswer.getText().toString();
        if (question == null || question.isEmpty() || answer == null || answer.isEmpty()) {
            Snackbar.make((View) optionsContainer.getParent(), "Enter all inputs", Snackbar.LENGTH_SHORT).show();
        } else {
            String[] options = validOptions();
            if (options != null) {
                int trueOption = Integer.parseInt(answer);
                if (optionsContainer.getChildCount() == 4 && trueOption <= 4 && trueOption >= 1) {
                    if (new MyDB(this).addNewQuestion((qNumber + 1), question, trueOption, options[0], options[1], options[2], options[3])) {
                        qNumber = new MyDB(this).getTotalQuestions();
                        resetViews();
                        if (qNumber >= 5) {
                            showCompleteDialog();
                            tvBtnAddOption.setVisibility(View.GONE);
                            View view= (View) etQuestion.getParent();
                            view.setVisibility(View.GONE);
                            tvBtnSubmit.setVisibility(View.GONE);
                        } else {
                            Snackbar.make((View) optionsContainer.getParent(), "Question saved ", Snackbar.LENGTH_SHORT).show();
                            tvQNumber.setText("Question " + (1 + qNumber) + " of 5");
                        }
                    }
                } else {
                    Snackbar.make((View) optionsContainer.getParent(), "Enter option between 1-4", Snackbar.LENGTH_SHORT).show();
                }
            }
        }
}

    private String[] validOptions() {
        String[] opt = new String[4];
        for (int i = 0; i < optionsContainer.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) optionsContainer.getChildAt(i);
            EditText editText = linearLayout.findViewById(R.id.et_option);
            editText.setError(null);
            if (editText.getText().toString() == null || editText.getText().toString().isEmpty()) {
                editText.setError("Enter option here");
                editText.requestFocus();
                return null;
            } else {
                opt[i] = editText.getText().toString();
            }
        }
        return opt;
    }

    private void resetViews() {
        optionsContainer.removeAllViews();
        etAnswer.setText("");
        etQuestion.setText("");
        tvBtnAddOption.setVisibility(View.VISIBLE);
        ll_answer.setVisibility(View.GONE);
    }

    private void showCompleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Quiz created successfully");
        builder.setMessage("\nDo you want to attempt quiz?"+"\n");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(CreateQuizActivity.this, AttemptQuizActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_btn_submit_question:
                submitQuestion();
                break;
            case R.id.tv_add_option:
                addOptions();
                break;
            default:
                break;
        }
    }
//    @Override
//    public void onBackPressed() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Do you want to quit the Quiz?");
//        builder.setTitle("Are you sure!");
//        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                CreateQuizActivity.super.onBackPressed();
//            }
//        });
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.create().show();
//    }
}
