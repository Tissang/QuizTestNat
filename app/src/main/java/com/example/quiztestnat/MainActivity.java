package com.example.quiztestnat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView totQuestTextView;
    TextView questionTextView;
    Button ansA, ansB, ansC, ansD;
    Button nextBtn;
    ProgressBar check;
    int questionNumber = 0;
    ArrayList<String> question = new ArrayList<String>();
    ArrayList<String> trueAnswers = new ArrayList<String>();
    ArrayList<List<String>> answers = new ArrayList<List<String>>();

    int score = 0;
    int totalQuestion = 0;
    int currentQuestionIndex = 0;
    String selectedAnswer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        question.clear();
        trueAnswers.clear();
        answers.clear();


        try {
            XmlPullParser parser = getResources().getXml(R.xml.quest);

            while (parser.getEventType()!= XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG  && parser.getName().equals("question")) {
                    question.add(parser.getAttributeValue(0));

                    String[] sanswers = new String[] {parser.getAttributeValue(1), parser.getAttributeValue(2),
                            parser.getAttributeValue(3), parser.getAttributeValue(4)};
                    List<String> list = Arrays.asList(sanswers);

                    trueAnswers.add(sanswers[0]);
                    Collections.shuffle(list);
                    answers.add(list);
                }
                parser.next();
            }
        }

        catch (Throwable t) {
            Toast.makeText(this, "Ошибка при загрузке XML-документа: " + t.toString(), Toast.LENGTH_SHORT).show();
        }

        totQuestTextView = findViewById(R.id.total_question);
        questionTextView = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_A);
        ansB = findViewById(R.id.ans_B);
        ansC = findViewById(R.id.ans_C);
        ansD = findViewById(R.id.ans_D);
        nextBtn = findViewById(R.id.next_btn);
        check = findViewById(R.id.progressbar);


        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

        totalQuestion = question.size();

        totQuestTextView.setText("Количество вопросов : " + totalQuestion);

        questionTextView.setText(question.get(currentQuestionIndex));
        ansA.setText(answers.get(currentQuestionIndex).get(0));
        ansB.setText(answers.get(currentQuestionIndex).get(1));
        ansC.setText(answers.get(currentQuestionIndex).get(2));
        ansD.setText(answers.get(currentQuestionIndex).get(3));

        loadNewQuestion();
    }

    @Override
    public void onClick(View view) {


        ansA.setBackgroundColor(Color.DKGRAY);
        ansB.setBackgroundColor(Color.DKGRAY);
        ansC.setBackgroundColor(Color.DKGRAY);
        ansD.setBackgroundColor(Color.DKGRAY);

        Button clickedButton = (Button) view;
        if (clickedButton.getId() == R.id.next_btn) {
            if (selectedAnswer.equals(trueAnswers.get(currentQuestionIndex))) {
                score++;
            }
            currentQuestionIndex++;
            loadNewQuestion();


        } else {
            selectedAnswer = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.BLACK);

        }
    }
    void loadNewQuestion() {
        float progress = currentQuestionIndex;
        progress = progress / 7 * 100;
        check.setProgress(Math.round(progress));

        if (currentQuestionIndex == totalQuestion) {
            finishQuiz();
            return;
        }

        questionTextView.setText(question.get(currentQuestionIndex));
        ansA.setText(answers.get(currentQuestionIndex).get(0));
        ansB.setText(answers.get(currentQuestionIndex).get(1));
        ansC.setText(answers.get(currentQuestionIndex).get(2));
        ansD.setText(answers.get(currentQuestionIndex).get(3));


    }

    void finishQuiz() {
        String passStatus = "";
        if (score > totalQuestion * 0.70) {
            passStatus = "Пройдено";
        } else {
            passStatus = "Провалено";
        }

        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage("Ваш счет " + score + " из " + totalQuestion)
                .setPositiveButton("Заново", (dialogInterface, i) -> restartQuiz())
                .setCancelable(false)
                .show();

         }

        void restartQuiz() {
        score = 0;
        currentQuestionIndex = 0;
        loadNewQuestion();
        }
    }
