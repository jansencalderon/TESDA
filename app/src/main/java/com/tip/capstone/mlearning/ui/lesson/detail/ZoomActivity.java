package com.tip.capstone.mlearning.ui.lesson.detail;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ActivityZoomBinding;
import com.tip.capstone.mlearning.helper.ResourceHelper;
import com.tip.capstone.mlearning.helper.TouchImageView;

public class ZoomActivity extends AppCompatActivity {

    ActivityZoomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_zoom);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TouchImageView touchImageView = (TouchImageView) findViewById(R.id.imageView);
        String pic = getIntent().getStringExtra("pic");
        // Bitmap icon = BitmapFactory.decodeResource(this.getResources(), ResourceHelper.getDrawableResourceId(this, pic));
        Glide.with(this)
                .load(ResourceHelper.getDrawableResourceId(this, pic))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_photo)
                .error(R.drawable.ic_photo)
                .into(touchImageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
