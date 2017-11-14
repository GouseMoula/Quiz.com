package com.quizcom;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvBtnCreate, tvBtnAttempt;
    LinearLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent = (LinearLayout) findViewById(R.id.main_active);
        tvBtnCreate = (TextView) findViewById(R.id.tv_btn_create_quiz);
        tvBtnAttempt = (TextView) findViewById(R.id.tv_btn_attempt_quiz);
        tvBtnCreate.setOnClickListener(this);
        tvBtnAttempt.setOnClickListener(this);
    }

    private void attemptQuiz() {
        int qNumber = new MyDB(MainActivity.this).getTotalQuestions();
        if (qNumber > 0) {
            startActivity(new Intent(MainActivity.this, AttemptQuizActivity.class));
        } else {
            Snackbar.make(parent, "No questions available.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void createQuiz() {
        int qNumber = new MyDB(MainActivity.this).getTotalQuestions();
        if (qNumber < 5) {
            startActivity(new Intent(MainActivity.this, CreateQuizActivity.class));
        } else {
            Snackbar.make(parent, "Quiz already created.", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_btn_attempt_quiz:
                attemptQuiz();
                break;
            case R.id.tv_btn_create_quiz:
                createQuiz();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_delete_quiz) {
            deleteQuiz();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteQuiz() {
        if (new MyDB(this).deleteQuiz()) {
            Snackbar.make(parent, "Quiz deleted successfully.", Snackbar.LENGTH_SHORT).show();
        }
        else {
            Snackbar.make(parent, "No data to delete.", Snackbar.LENGTH_SHORT).show();
        }

    }
}
