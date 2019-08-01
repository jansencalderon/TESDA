package com.tip.capstone.mlearning.ui.lesson.detail;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.app.Constant;
import com.tip.capstone.mlearning.databinding.ItemLessonDetailHeaderBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonDetailImageBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonDetailLinkBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonDetailTextBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonDetailVideoBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonHeaderBinding;
import com.tip.capstone.mlearning.databinding.ItemLessonQuizButtonBinding;
import com.tip.capstone.mlearning.helper.ResourceHelper;
import com.tip.capstone.mlearning.model.Lesson;
import com.tip.capstone.mlearning.model.LessonDetail;
import com.tip.capstone.mlearning.utils.DeveloperKey;

import java.util.ArrayList;
import java.util.List;


class LessonDetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_HEADER = 0;
    private static final int VIEW_TEXT = 1;
    private static final int VIEW_IMAGE = 2;
    private static final int VIEW_QUIZ = 3;
    private static final int VIEW_VIDEO = 5;
    private static final int VIEW_DETAIL_HEADER = 4;
    private static final int VIEW_LINK = 6;
    private static final String TAG = LessonDetailListAdapter.class.getSimpleName();

    private final Lesson lesson;
    private final List<LessonDetail> lessonDetails;
    private final LessonDetailListView lessonDetailListView;
    private boolean isLastPage;
    private String query;
    private int lessonDetailRef;

    /**
     * Constructor
     *
     * @param lesson          lesson of the list (for header purposes)
     * @param view            for listener of items
     * @param lessonDetailRef
     */
    LessonDetailListAdapter(Lesson lesson, LessonDetailListView view, boolean isLastPage,
                            String query, int lessonDetailRef) {
        this.lesson = lesson;
        this.lessonDetailListView = view;
        this.isLastPage = isLastPage;
        this.query = query;
        this.lessonDetailRef = lessonDetailRef;
        lessonDetails = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (position == 0 && lesson != null) {
                return VIEW_HEADER;
            } else if (isLastPage && position == (getItemCount() - 1)) {
                return VIEW_QUIZ;
            } else {
                // decrement index for list if has lesson
                int index = lesson != null ? position - 1 : position;
                switch (lessonDetails.get(index).getBody_type()) {
                    case Constant.DETAIL_TYPE_TEXT:
                        return VIEW_TEXT;
                    case Constant.DETAIL_TYPE_IMAGE:
                        return VIEW_IMAGE;
                    case Constant.DETAIL_TYPE_DETAIL_HEADER:
                        return VIEW_DETAIL_HEADER;
                    case Constant.DETAIL_TYPE_VIDEO:
                        return VIEW_VIDEO;
                    case Constant.DETAIL_TYPE_LINK:
                        return VIEW_LINK;

                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            lessonDetailListView.showAlert(
                    lesson.getTitle());
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_HEADER:
                ItemLessonHeaderBinding itemLessonHeaderBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_header,
                        parent,
                        false);
                return new LessonHeaderViewHolder(itemLessonHeaderBinding);
            case VIEW_TEXT:
                ItemLessonDetailTextBinding itemLessonDetailTextBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_detail_text,
                        parent,
                        false);
                return new LessonDetailTextViewHolder(itemLessonDetailTextBinding);
            case VIEW_IMAGE:
                ItemLessonDetailImageBinding itemLessonDetailImageBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_detail_image,
                        parent,
                        false);
                return new LessonDetailImageViewHolder(itemLessonDetailImageBinding);
            case VIEW_QUIZ:
                ItemLessonQuizButtonBinding itemLessonQuizButtonBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_quiz_button,
                        parent,
                        false);
                return new LessonQuizButtonHolder(itemLessonQuizButtonBinding);
            case VIEW_DETAIL_HEADER:
                ItemLessonDetailHeaderBinding itemLessonDetailHeaderBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_detail_header,
                        parent,
                        false);
                return new LessonDetailHeaderViewHolder(itemLessonDetailHeaderBinding);
            case VIEW_VIDEO:
                ItemLessonDetailVideoBinding itemLessonDetailVideoBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_detail_video,
                        parent,
                        false);
                return new LessonDetailVideoViewHolder(itemLessonDetailVideoBinding);
            case VIEW_LINK:
                ItemLessonDetailLinkBinding itemLessonDetailLinkBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_lesson_detail_link,
                        parent,
                        false);
                return new LessonDetailLinkViewHolder(itemLessonDetailLinkBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case VIEW_HEADER:
                LessonHeaderViewHolder lessonHeaderViewHolder = (LessonHeaderViewHolder) holder;
                lessonHeaderViewHolder.itemLessonHeaderBinding.setLesson(lesson);
                break;
            case VIEW_TEXT:
                final LessonDetail lessonDetail = lessonDetails.get(lesson != null ? position - 1 : position);
                final LessonDetailTextViewHolder lessonDetailTextViewHolder = (LessonDetailTextViewHolder) holder;
                lessonDetailTextViewHolder.itemLessonDetailTextBinding
                        .setLessonDetail(lessonDetail);
                lessonDetailTextViewHolder.itemLessonDetailTextBinding.setView(lessonDetailListView);
                lessonDetailTextViewHolder.itemLessonDetailTextBinding.layoutLessonDetailText
                        .setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                                lessonDetail.getId() == lessonDetailRef ?
                                        R.color.yellow : R.color.white));
                //use a loop to change text color
                String text = lessonDetail.getBody();
                if (query != null && !query.isEmpty() &&
                        text.toUpperCase().contains(query.toUpperCase())) {
                    Log.d(TAG, "onBindViewHolder: id:" + lessonDetail.getId());
                    Log.d(TAG, "onBindViewHolder: query: " + query);
                    Spannable WordtoSpan = new SpannableString(text);
                    int startIndex = text.toUpperCase().indexOf(query.toUpperCase());
                    int stopIndex = startIndex + query.length();
                    WordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), startIndex, stopIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    WordtoSpan.setSpan(new BackgroundColorSpan(Color.YELLOW), startIndex, stopIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    lessonDetailTextViewHolder.itemLessonDetailTextBinding.txtBody.setText(WordtoSpan);
                } else {
                    Log.d(TAG, "onBindViewHolder: query: " + query);
                }

                lessonDetailTextViewHolder.itemLessonDetailTextBinding.txtBody
                        .setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                lessonDetailListView.onDetailClick(lessonDetail);
                                return true;
                            }
                        });

                //TODO: Enable TextOnTouch
                /*lessonDetailTextViewHolder.itemLessonDetailTextBinding.txtBody
                        .setOnTouchListener(new View.OnTouchListener() {

                            final float STEP = 200;
                            float mRatio = 1.0f;
                            int mBaseDist;
                            float mBaseRatio;
                            float fontsize = 13;

                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                if (motionEvent.getPointerCount() == 2) {
                                    int action = motionEvent.getAction();
                                    int pureaction = action & MotionEvent.ACTION_MASK;
                                    if (pureaction == MotionEvent.ACTION_POINTER_DOWN) {
                                        mBaseDist = getDistance(motionEvent);
                                        mBaseRatio = mRatio;
                                    } else {
                                        float delta = (getDistance(motionEvent) - mBaseDist) / STEP;
                                        float multi = (float) Math.pow(2, delta);
                                        mRatio = Math.min(1024.0f, Math.max(0.1f, mBaseRatio * multi));
                                        lessonDetailTextViewHolder.itemLessonDetailTextBinding.txtBody.setTextSize(mRatio + 13);
                                    }
                                }
                                return false;
                            }

                            int getDistance(MotionEvent event) {
                                int dx = (int) (event.getX(0) - event.getX(1));
                                int dy = (int) (event.getY(0) - event.getY(1));
                                return (int) (Math.sqrt(dx * dx + dy * dy));
                            }
                        });*/
                lessonDetailTextViewHolder.itemLessonDetailTextBinding.txtBody.setLongClickable(true);
                break;
            case VIEW_IMAGE:
                LessonDetailImageViewHolder lessonDetailImageViewHolder = (LessonDetailImageViewHolder) holder;
                LessonDetail detail = lessonDetails.get(lesson != null ? position - 1 : position);
                lessonDetailImageViewHolder.itemLessonDetailImageBinding
                        .setLessonDetail(detail);
                lessonDetailImageViewHolder.itemLessonDetailImageBinding.setView(lessonDetailListView);

                if (detail.getBody().contains(".gif")) {
                    GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(lessonDetailImageViewHolder.itemLessonDetailImageBinding.imageLessonDetail);
                    Glide.with(holder.itemView.getContext())
                            .load(ResourceHelper.getDrawableResourceId(holder.itemView.getContext(), detail.getBody()))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(imageViewTarget);
                    //lessonDetailListView.showAlert(detail.getBody()+detail.getBody_type());
                } else {
                    Glide.with(holder.itemView.getContext())
                            .load(ResourceHelper.getDrawableResourceId(holder.itemView.getContext(),
                                    detail.getBody()))
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            //.placeholder(R.drawable.ic_photo)
                            //.error(R.drawable.ic_photo)
                            .into(lessonDetailImageViewHolder.itemLessonDetailImageBinding.imageLessonDetail);
                }
                break;
            case VIEW_QUIZ:
                LessonQuizButtonHolder lessonQuizButtonHolder = (LessonQuizButtonHolder) holder;
                lessonQuizButtonHolder.itemLessonQuizButtonBinding.setView(lessonDetailListView);
                break;
            case VIEW_DETAIL_HEADER:
                LessonDetailHeaderViewHolder lessonDetailHeaderViewHolder = (LessonDetailHeaderViewHolder) holder;
                lessonDetailHeaderViewHolder.itemLessonDetailHeaderBinding.setDetail(lessonDetails.get(lesson != null ? position - 1 : position));
                break;
            case VIEW_LINK:
                final LessonDetailLinkViewHolder lessonDetailLinkViewHolder = (LessonDetailLinkViewHolder) holder;
                lessonDetailLinkViewHolder.itemLessonDetailLinkBinding.setView(lessonDetailListView);
                lessonDetailLinkViewHolder.itemLessonDetailLinkBinding.setLessonDetail(lessonDetails.get(lesson != null ? position - 1 : position));
                break;
            case VIEW_VIDEO:
                final LessonDetailVideoViewHolder lessonDetailVideoViewHolder = (LessonDetailVideoViewHolder) holder;
                lessonDetailVideoViewHolder.itemLessonDetailVideoBinding.setLessonDetail(lessonDetails.get(lesson != null ? position - 1 : position));
                final YouTubeThumbnailView youTubeThumbnailView = lessonDetailVideoViewHolder.itemLessonDetailVideoBinding.thumbnail;
                final String videoId = lessonDetails.get(lesson != null ? position - 1 : position).getBody();
                youTubeThumbnailView.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                        youTubeThumbnailLoader.setVideo(videoId);
                        youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                            @Override
                            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                                youTubeThumbnailLoader.release();
                            }

                            @Override
                            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                            }
                        });
                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });

                lessonDetailVideoViewHolder.itemLessonDetailVideoBinding.layoutLessonDetailText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                        try {
                            lessonDetailVideoViewHolder.itemView.getContext().startActivity(appIntent);
                        } catch (ActivityNotFoundException ex) {
                            lessonDetailVideoViewHolder.itemView.getContext().startActivity(webIntent);
                        }
                    }
                });
                break;

        }
    }

    @Override
    public int getItemCount() {
        int size = lessonDetails.size();
        if (lesson != null) size++;
        if (isLastPage) size++;
        return size;
    }

    /**
     * @param lessonDetails list of lesson details to display
     */
    void setLessonDetails(List<LessonDetail> lessonDetails) {
        this.lessonDetails.clear();
        this.lessonDetails.addAll(lessonDetails);
        notifyDataSetChanged();
    }

    private class LessonHeaderViewHolder extends RecyclerView.ViewHolder {
        private final ItemLessonHeaderBinding itemLessonHeaderBinding;

        LessonHeaderViewHolder(ItemLessonHeaderBinding itemLessonHeaderBinding) {
            super(itemLessonHeaderBinding.getRoot());
            this.itemLessonHeaderBinding = itemLessonHeaderBinding;
        }
    }

    private class LessonDetailTextViewHolder extends RecyclerView.ViewHolder {
        private final ItemLessonDetailTextBinding itemLessonDetailTextBinding;

        LessonDetailTextViewHolder(ItemLessonDetailTextBinding itemLessonDetailTextBinding) {
            super(itemLessonDetailTextBinding.getRoot());
            this.itemLessonDetailTextBinding = itemLessonDetailTextBinding;
        }
    }

    private class LessonDetailImageViewHolder extends RecyclerView.ViewHolder {
        private final ItemLessonDetailImageBinding itemLessonDetailImageBinding;

        LessonDetailImageViewHolder(ItemLessonDetailImageBinding itemLessonDetailImageBinding) {
            super(itemLessonDetailImageBinding.getRoot());
            this.itemLessonDetailImageBinding = itemLessonDetailImageBinding;
        }
    }

    private class LessonQuizButtonHolder extends RecyclerView.ViewHolder {
        private final ItemLessonQuizButtonBinding itemLessonQuizButtonBinding;

        LessonQuizButtonHolder(ItemLessonQuizButtonBinding itemLessonQuizButtonBinding) {
            super(itemLessonQuizButtonBinding.getRoot());
            this.itemLessonQuizButtonBinding = itemLessonQuizButtonBinding;
        }
    }

    private class LessonDetailHeaderViewHolder extends RecyclerView.ViewHolder {
        private final ItemLessonDetailHeaderBinding itemLessonDetailHeaderBinding;

        LessonDetailHeaderViewHolder(ItemLessonDetailHeaderBinding itemLessonDetailHeaderBinding) {
            super(itemLessonDetailHeaderBinding.getRoot());
            this.itemLessonDetailHeaderBinding = itemLessonDetailHeaderBinding;
        }
    }

    private class LessonDetailVideoViewHolder extends RecyclerView.ViewHolder {
        private final ItemLessonDetailVideoBinding itemLessonDetailVideoBinding;

        LessonDetailVideoViewHolder(ItemLessonDetailVideoBinding itemLessonDetailVideoBinding) {
            super(itemLessonDetailVideoBinding.getRoot());
            this.itemLessonDetailVideoBinding = itemLessonDetailVideoBinding;
        }
    }

    private class LessonDetailLinkViewHolder extends RecyclerView.ViewHolder {
        private final ItemLessonDetailLinkBinding itemLessonDetailLinkBinding;

        LessonDetailLinkViewHolder(ItemLessonDetailLinkBinding itemLessonDetailLinkBinding) {
            super(itemLessonDetailLinkBinding.getRoot());
            this.itemLessonDetailLinkBinding = itemLessonDetailLinkBinding;
        }
    }
}