package com.tip.capstone.mlearning.ui.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ItemUserAnswerBinding;
import com.tip.capstone.mlearning.model.UserAnswer;
import com.tip.capstone.mlearning.ui.views.SummaryView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 22/11/2016
 * Adapter for the summary list RecyclerView
 */

public class SummaryListAdapter extends RecyclerView.Adapter<SummaryListAdapter.ViewHolder> {

    private final List<UserAnswer> userAnswerList;
    private SummaryView view;

    /**
     * Constructor and init the list
     */
    public SummaryListAdapter(SummaryView view) {
        this.view = view;
        userAnswerList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemUserAnswerBinding itemUserAnswerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_user_answer,
                parent,
                false);
        return new ViewHolder(itemUserAnswerBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserAnswer userAnswer = userAnswerList.get(position);
        holder.itemUserAnswerBinding.setAnswer(userAnswer);
        String strItemNum = (position + 1) + ".)"; // add 1 as index starts w/ 0
        holder.itemUserAnswerBinding.txtItemNum.setText(strItemNum);
        // load the images if the choice type is image

        holder.itemUserAnswerBinding.itemUserAnswer.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.getContext(),
                        userAnswer.isCorrect() ? R.color.green_light : R.color.red_light));
        holder.itemUserAnswerBinding.seeReference.setVisibility(userAnswer.isCorrect() ? View.GONE : View.VISIBLE);
        holder.itemUserAnswerBinding.setView(view);
    }

    @Override
    public int getItemCount() {
        return userAnswerList.size();
    }

    /**
     * @param userAnswerList list of user answers to display
     */
    public void setUserAnswerList(List<UserAnswer> userAnswerList) {
        this.userAnswerList.clear();
        this.userAnswerList.addAll(userAnswerList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserAnswerBinding itemUserAnswerBinding;

        ViewHolder(ItemUserAnswerBinding itemUserAnswerBinding) {
            super(itemUserAnswerBinding.getRoot());
            this.itemUserAnswerBinding = itemUserAnswerBinding;
        }
    }
}
