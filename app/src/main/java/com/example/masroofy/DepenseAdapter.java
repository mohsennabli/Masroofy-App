package com.example.masroofy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DepenseAdapter extends RecyclerView.Adapter<DepenseAdapter.DepenseViewHolder> {

    private List<Depense> depenseList;

    public DepenseAdapter(List<Depense> depenseList) {
        this.depenseList = depenseList;
    }

    @NonNull
    @Override
    public DepenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_depense, parent, false);
        return new DepenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepenseViewHolder holder, int position) {
        Depense depense = depenseList.get(position);
        holder.txtMontant.setText(depense.getMontant() + " TND");
        holder.txtCategorie.setText(depense.getCategorie());
        holder.txtDescription.setText(depense.getDescription());
        holder.txtDate.setText(depense.getDate());
    }

    @Override
    public int getItemCount() {
        return depenseList.size();
    }

    public static class DepenseViewHolder extends RecyclerView.ViewHolder {
        TextView txtMontant, txtCategorie, txtDescription, txtDate;

        public DepenseViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMontant = itemView.findViewById(R.id.txtMontant);
            txtCategorie = itemView.findViewById(R.id.txtCategorie);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}
