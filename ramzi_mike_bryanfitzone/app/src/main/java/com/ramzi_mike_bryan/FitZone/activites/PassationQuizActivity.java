package com.ramzi_mike_bryan.FitZone.activites;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.api.ApiService;
import com.api.RetrofitClient;
import com.models.Quiz;
import com.ramzi_mike_bryan.FitZone.R;
import com.ramzi_mike_bryan.FitZone.dao.QuizResultDao;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassationQuizActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "FitZonePrefs";

    private View scrollViewQuiz;
    private View barreNavigationQuiz;
    private View conteneurResultat;

    private TextView texteTitreQuizPassation;
    private TextView texteProgressionQuestion;
    private TextView texteQuestion;
    private RadioGroup groupeOptions;
    private Button boutonPrecedent;
    private Button boutonSuivant;
    private TextView texteScoreResultat;
    private TextView texteMessageResultat;

    private Quiz quiz;
    private String userId;
    private int questionCourante = 0;
    private int[] reponsesChoisies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passation_quiz);

        scrollViewQuiz = findViewById(R.id.scrollViewQuiz);
        barreNavigationQuiz = findViewById(R.id.barreNavigationQuiz);
        conteneurResultat = findViewById(R.id.conteneurResultat);

        texteTitreQuizPassation = findViewById(R.id.texteTitreQuizPassation);
        texteProgressionQuestion = findViewById(R.id.texteProgressionQuestion);
        texteQuestion = findViewById(R.id.texteQuestion);
        groupeOptions = findViewById(R.id.groupeOptions);
        boutonPrecedent = findViewById(R.id.boutonPrecedent);
        boutonSuivant = findViewById(R.id.boutonSuivant);
        texteScoreResultat = findViewById(R.id.texteScoreResultat);
        texteMessageResultat = findViewById(R.id.texteMessageResultat);

        TextView boutonRetour = findViewById(R.id.boutonRetourQuiz);
        Button boutonRetourListeQuiz = findViewById(R.id.boutonRetourListeQuiz);

        boutonRetour.setOnClickListener(v -> finish());
        boutonRetourListeQuiz.setOnClickListener(v -> finish());

        userId = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getString("userId", null);

        String quizId = getIntent().getStringExtra("QUIZ_ID");

        if (userId == null || quizId == null) {

            Toast.makeText(
                    this,
                    "Quiz introuvable",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        boutonPrecedent.setOnClickListener(v -> {
            questionCourante--;
            afficherQuestion();
        });

        boutonSuivant.setOnClickListener(v -> passerALaSuite());

        chargerQuiz(quizId);
    }

    private void chargerQuiz(String quizId) {

        ApiService apiService = RetrofitClient.getApiService();

        apiService.getQuizById(quizId).enqueue(new Callback<Quiz>() {

            @Override
            public void onResponse(Call<Quiz> call, Response<Quiz> response) {

                if (!response.isSuccessful()
                        || response.body() == null
                        || response.body().getQuestions() == null
                        || response.body().getQuestions().isEmpty()) {

                    Toast.makeText(
                            PassationQuizActivity.this,
                            "Impossible de charger ce quiz",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                    return;
                }

                quiz = response.body();
                reponsesChoisies = new int[quiz.getQuestions().size()];

                for (int i = 0; i < reponsesChoisies.length; i++) {
                    reponsesChoisies[i] = -1;
                }

                texteTitreQuizPassation.setText(quiz.getTitle());
                afficherQuestion();
            }

            @Override
            public void onFailure(Call<Quiz> call, Throwable t) {

                Toast.makeText(
                        PassationQuizActivity.this,
                        "Impossible de contacter le serveur",
                        Toast.LENGTH_LONG
                ).show();

                finish();
            }
        });
    }

    private void afficherQuestion() {

        List<Quiz.Question> questions = quiz.getQuestions();
        Quiz.Question question = questions.get(questionCourante);

        texteProgressionQuestion.setText(
                "Question " + (questionCourante + 1) + "/" + questions.size()
        );

        texteQuestion.setText(question.getQuestion());

        groupeOptions.removeAllViews();

        List<String> options = question.getOptions();

        for (int i = 0; i < options.size(); i++) {

            RadioButton bouton = new RadioButton(this);
            bouton.setId(View.generateViewId());
            bouton.setText(options.get(i));
            bouton.setTextColor(0xFF17152A);
            bouton.setPadding(0, dp(10), 0, dp(10));
            bouton.setTag(i);

            groupeOptions.addView(bouton);

            if (reponsesChoisies[questionCourante] == i) {
                bouton.setChecked(true);
            }
        }

        groupeOptions.setOnCheckedChangeListener((group, checkedId) -> {

            RadioButton selectionne = group.findViewById(checkedId);

            if (selectionne != null) {
                reponsesChoisies[questionCourante] = (int) selectionne.getTag();
            }
        });

        boutonPrecedent.setVisibility(
                questionCourante == 0 ? View.INVISIBLE : View.VISIBLE
        );

        boutonSuivant.setText(
                questionCourante == questions.size() - 1 ? "Terminer" : "Suivant"
        );
    }

    private void passerALaSuite() {

        if (reponsesChoisies[questionCourante] == -1) {

            Toast.makeText(
                    this,
                    "Choisissez une réponse pour continuer",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (questionCourante < quiz.getQuestions().size() - 1) {
            questionCourante++;
            afficherQuestion();
        } else {
            terminerQuiz();
        }
    }

    private void terminerQuiz() {

        List<Quiz.Question> questions = quiz.getQuestions();
        int score = 0;

        for (int i = 0; i < questions.size(); i++) {
            if (reponsesChoisies[i] == questions.get(i).getCorrectOption()) {
                score++;
            }
        }

        QuizResultDao dao = new QuizResultDao(this);
        dao.enregistrerResultat(userId, quiz.getId(), score, questions.size());

        scrollViewQuiz.setVisibility(View.GONE);
        barreNavigationQuiz.setVisibility(View.GONE);
        conteneurResultat.setVisibility(View.VISIBLE);

        texteScoreResultat.setText(score + " / " + questions.size());

        int pourcentage = Math.round(100f * score / questions.size());

        texteMessageResultat.setText(
                "Résultat enregistré · " + pourcentage + "% de bonnes réponses"
        );
    }

    private int dp(int valeur) {
        float densite = getResources().getDisplayMetrics().density;
        return Math.round(valeur * densite);
    }
}
