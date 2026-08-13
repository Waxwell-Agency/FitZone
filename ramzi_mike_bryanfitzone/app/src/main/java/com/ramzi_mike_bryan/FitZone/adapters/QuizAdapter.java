package com.ramzi_mike_bryan.FitZone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.models.Quiz;
import com.ramzi_mike_bryan.FitZone.R;
import com.ramzi_mike_bryan.FitZone.activites.PassationQuizActivity;
import com.ramzi_mike_bryan.FitZone.dao.QuizResult;

import java.util.List;
import java.util.Map;

public class QuizAdapter
        extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private final Context context;
    private List<Quiz> quiz;
    private final Map<String, String> titresProgrammes;
    private final Map<String, QuizResult> resultats;

    public QuizAdapter(
            Context context,
            List<Quiz> quiz,
            Map<String, String> titresProgrammes,
            Map<String, QuizResult> resultats) {

        this.context = context;
        this.quiz = quiz;
        this.titresProgrammes = titresProgrammes;
        this.resultats = resultats;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_quiz, parent, false);

        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull QuizViewHolder holder,
            int position) {

        Quiz unQuiz = quiz.get(position);

        int nbQuestions = unQuiz.getQuestions() == null
                ? 0
                : unQuiz.getQuestions().size();

        String titreProgramme = titresProgrammes.get(unQuiz.getProgramId());

        holder.texteTitre.setText(unQuiz.getTitle());

        holder.texteProgramme.setText(
                "Programme : " + (titreProgramme != null ? titreProgramme : "—")
        );

        holder.texteNbQuestions.setText(
                nbQuestions == 1 ? "1 question" : nbQuestions + " questions"
        );

        QuizResult resultat = resultats.get(unQuiz.getId());

        if (resultat != null) {

            holder.texteStatut.setText("Terminé");
            holder.texteStatut.setBackgroundResource(R.drawable.bg_statut_actif);

            holder.boutonCommencer.setText(
                    "Revoir · " + resultat.getScore() + "/" + resultat.getTotal()
            );

        } else {

            holder.texteStatut.setText("À faire");
            holder.texteStatut.setBackgroundResource(R.drawable.bg_statut_a_faire);
            holder.boutonCommencer.setText("Commencer");
        }

        holder.boutonCommencer.setOnClickListener(v -> {

            Intent intent = new Intent(context, PassationQuizActivity.class);
            intent.putExtra("QUIZ_ID", unQuiz.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return quiz.size();
    }

    public void mettreAJourListe(List<Quiz> nouvelleListe) {
        quiz = nouvelleListe;
        notifyDataSetChanged();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {

        TextView texteTitre;
        TextView texteProgramme;
        TextView texteNbQuestions;
        TextView texteStatut;
        Button boutonCommencer;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);

            texteTitre = itemView.findViewById(R.id.texteTitreQuiz);
            texteProgramme = itemView.findViewById(R.id.texteProgrammeQuiz);
            texteNbQuestions = itemView.findViewById(R.id.texteNbQuestionsQuiz);
            texteStatut = itemView.findViewById(R.id.texteStatutQuiz);
            boutonCommencer = itemView.findViewById(R.id.boutonCommencerQuiz);
        }
    }
}
