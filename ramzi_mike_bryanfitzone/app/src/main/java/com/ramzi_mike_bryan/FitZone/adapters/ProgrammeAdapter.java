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

import com.models.Programme;
import com.ramzi_mike_bryan.FitZone.R;
import com.ramzi_mike_bryan.FitZone.activites.DetailProgrammeActivity;

import java.util.List;

public class ProgrammeAdapter
        extends RecyclerView.Adapter<ProgrammeAdapter.ProgrammeViewHolder> {

    private final Context context;
    private List<Programme> programmes;

    public ProgrammeAdapter(Context context, List<Programme> programmes) {
        this.context = context;
        this.programmes = programmes;
    }

    @NonNull
    @Override
    public ProgrammeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_programme, parent, false);

        return new ProgrammeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ProgrammeViewHolder holder,
            int position) {

        Programme programme = programmes.get(position);

        holder.texteNom.setText(programme.getTitle());
        holder.texteCode.setText("Code : " + programme.getCode());
        holder.texteCoach.setText("Coach : " + programme.getCoach());

        holder.boutonVoir.setOnClickListener(v -> {

            Intent intent = new Intent(
                    context,
                    DetailProgrammeActivity.class
            );

            intent.putExtra("programId", programme.getId());
            intent.putExtra("code", programme.getCode());
            intent.putExtra("title", programme.getTitle());
            intent.putExtra("description", programme.getDescription());
            intent.putExtra("coach", programme.getCoach());
            intent.putExtra("session", programme.getSession());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return programmes.size();
    }

    public void mettreAJourListe(List<Programme> nouvelleListe) {
        programmes = nouvelleListe;
        notifyDataSetChanged();
    }

    public static class ProgrammeViewHolder
            extends RecyclerView.ViewHolder {

        TextView texteNom;
        TextView texteCode;
        TextView texteCoach;
        Button boutonVoir;

        public ProgrammeViewHolder(@NonNull View itemView) {
            super(itemView);

            texteNom =
                    itemView.findViewById(R.id.texteNomProgramme);

            texteCode =
                    itemView.findViewById(R.id.texteCodeProgramme);

            texteCoach =
                    itemView.findViewById(R.id.texteCoachProgramme);

            boutonVoir =
                    itemView.findViewById(R.id.boutonVoirProgramme);
        }
    }
}