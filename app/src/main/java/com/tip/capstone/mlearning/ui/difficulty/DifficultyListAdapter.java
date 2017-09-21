package com.tip.capstone.mlearning.ui.difficulty;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ItemDifficultyBinding;
import com.tip.capstone.mlearning.model.Difficulty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

public class DifficultyListAdapter extends RecyclerView.Adapter<DifficultyListAdapter.ViewHolder> {

    private final List<Difficulty> difficultyList;
    private final DifficultyView difficultyView;

    public DifficultyListAdapter(DifficultyView difficultyView) {
        this.difficultyView = difficultyView;
        difficultyList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDifficultyBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_difficulty,
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setDifficulty(difficultyList.get(position));
        holder.binding.setView(difficultyView);
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
        private final ItemDifficultyBinding binding;

        public ViewHolder(ItemDifficultyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
