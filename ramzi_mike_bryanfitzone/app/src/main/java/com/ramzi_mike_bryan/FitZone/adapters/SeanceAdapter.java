package com.ramzi_mike_bryan.FitZone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.models.Seance;
import com.ramzi_mike_bryan.FitZone.R;

import java.util.List;

public class SeanceAdapter
        extends RecyclerView.Adapter<SeanceAdapter.SeanceViewHolder> {

    private final List<Seance> seances;

    public SeanceAdapter(List<Seance> seances) {
        this.seances = seances;
    }

    @NonNull
    @Override
    public SeanceViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_seance, parent, false);

        return new SeanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull SeanceViewHolder holder,
            int position) {

        Seance seance = seances.get(position);

        holder.titre.setText(seance.getTitle());
        holder.date.setText("Date limite : " + seance.getDueDate());
        holder.statut.setText(seance.getStatus());
        holder.description.setText(seance.getDescription());
        holder.instructions.setText(
                "Instructions : " + seance.getInstructions()
        );
    }

    @Override
    public int getItemCount() {
        return seances.size();
    }

    public static class SeanceViewHolder
            extends RecyclerView.ViewHolder {

        TextView titre;
        TextView date;
        TextView statut;
        TextView description;
        TextView instructions;

        public SeanceViewHolder(@NonNull View itemView) {
            super(itemView);

            titre = itemView.findViewById(
                    R.id.texteTitreSeance
            );

            date = itemView.findViewById(
                    R.id.texteDateSeance
            );

            statut = itemView.findViewById(
                    R.id.texteStatutSeance
            );

            description = itemView.findViewById(
                    R.id.texteDescriptionSeance
            );

            instructions = itemView.findViewById(
                    R.id.texteInstructionsSeance
            );
        }
    }
}