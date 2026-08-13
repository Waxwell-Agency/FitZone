package com.ramzi_mike_bryan.FitZone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.models.Aliment;
import com.ramzi_mike_bryan.FitZone.R;

import java.util.List;

public class AlimentAdapter
        extends RecyclerView.Adapter<AlimentAdapter.AlimentViewHolder> {

    private final Context context;
    private List<Aliment> aliments;

    public AlimentAdapter(Context context, List<Aliment> aliments) {
        this.context = context;
        this.aliments = aliments;
    }

    @NonNull
    @Override
    public AlimentViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_aliment, parent, false);

        return new AlimentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull AlimentViewHolder holder,
            int position) {

        Aliment aliment = aliments.get(position);

        holder.texteIcone.setText(aliment.getIcone());
        holder.texteNom.setText(aliment.getNom());
        holder.texteDescription.setText(aliment.getDescription());
        holder.texteMoment.setText(aliment.getMoment());
        holder.texteCalories.setText(aliment.getCalories() + " kcal");
    }

    @Override
    public int getItemCount() {
        return aliments.size();
    }

    public void mettreAJourListe(List<Aliment> nouvelleListe) {
        aliments = nouvelleListe;
        notifyDataSetChanged();
    }

    public static class AlimentViewHolder extends RecyclerView.ViewHolder {

        TextView texteIcone;
        TextView texteNom;
        TextView texteDescription;
        TextView texteMoment;
        TextView texteCalories;

        public AlimentViewHolder(@NonNull View itemView) {
            super(itemView);

            texteIcone = itemView.findViewById(R.id.texteIconeAliment);
            texteNom = itemView.findViewById(R.id.texteNomAliment);
            texteDescription = itemView.findViewById(R.id.texteDescriptionAliment);
            texteMoment = itemView.findViewById(R.id.texteMomentAliment);
            texteCalories = itemView.findViewById(R.id.texteCaloriesAliment);
        }
    }
}
