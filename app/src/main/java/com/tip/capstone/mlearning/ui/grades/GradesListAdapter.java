package com.tip.capstone.mlearning.ui.grades;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ItemGradesDifficultyBinding;
import com.tip.capstone.mlearning.helper.ResourceHelper;
import com.tip.capstone.mlearning.model.Difficulty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

public class GradesListAdapter extends RecyclerView.Adapter<GradesListAdapter.ViewHolder> {

    private final List<Difficulty> difficultyList;
    private final GradesView difficultyView;

    public GradesListAdapter(GradesView difficultyView) {
        this.difficultyView = difficultyView;
        difficultyList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemGradesDifficultyBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_grades_difficulty,
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setDifficulty(difficultyList.get(position));
        holder.binding.setView(difficultyView);
        Glide.with(holder.itemView.getContext())
                .load(ResourceHelper.getDrawableResourceId(holder.itemView.getContext(), difficultyList.get(position).getImg()))
                .into(holder.binding.imageView);
    }

    public void setDifficultyList(List<Difficulty> difficultyList) {
        this.difficultyList.clear();
        this.difficultyList.addAll(difficultyList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return difficultyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemGradesDifficultyBinding binding;

        public ViewHolder(ItemGradesDifficultyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
