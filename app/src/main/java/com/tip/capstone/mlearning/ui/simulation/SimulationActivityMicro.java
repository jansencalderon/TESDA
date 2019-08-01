package com.tip.capstone.mlearning.ui.simulation;

import android.content.ClipData;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ActivitySimulationMicrowaveBinding;
import com.tip.capstone.mlearning.ui.adapter.ActivityLogAdapter;

import java.util.ArrayList;
import java.util.List;

public class SimulationActivityMicro extends AppCompatActivity
        implements View.OnDragListener, View.OnTouchListener {

    private ActivitySimulationMicrowaveBinding binding;
    private List<String> listActivityLog = new ArrayList<>();
    private ActivityLogAdapter activityLogAdapter;

    /* public static final int[] SIMULATION_DRAWABLES = {
             R.drawable.good_vga,
             R.drawable.screwdriverp,
             R.drawable.ramddr2,
             R.drawable.psupply,
             R.drawable.ramddr3,
             R.drawable.psupply2,
             R.drawable.dvi,
             R.drawable.brush,
             R.drawable.light,
             R.drawable.powercord,
             R.drawable.cases};
 */
    private int score = 0;
    boolean step1 = true, step2 = false, step3 = false, step4 = false, step5 = false,
            step6 = false, step7 = false, step8 = false, step9 = false, step10 = false,
            stepmonitorOn = false;

    private Drawable enterShape;
    private Drawable normalShape;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_simulation_microwave);

        enterShape = ContextCompat.getDrawable(this, R.drawable.shape_droptarget);
        normalShape = ContextCompat.getDrawable(this, R.drawable.shape);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView[] imageViews = {
                binding.capacitor,
                binding.drill,
                binding.fuse,
                binding.hammer,
                binding.longNose,
                binding.screwdriverPhillip};

        binding.layoutToolbox.setOnDragListener(this);
        binding.monitor.setOnDragListener(this);
        binding.layoutProblem.setOnDragListener(this);
        binding.activityLogRecyclerView.setOnDragListener(this);
        binding.progressLayout.setOnDragListener(this);

        for (int i = 0; i < imageViews.length; i++) {
         /*   Glide.with(this)
                    .load(SIMULATION_DRAWABLES[i])
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .priority(Priority.IMMEDIATE)
                    .dontAnimate()
                    .into(imageViews[i]);*/

            imageViews[i].setOnTouchListener(this);
        }

     /*   final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_instruction);
        dialog.setCancelable(false);
        Button start = (Button) dialog.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();*/
        //hide
        binding.hint.setText("The microware is not powered, it seems the fuse is blown off.");
        binding.monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (step1) {
                    addToLog("Microwave turned on, seems faulty.");
                    addToLog("Progress 20%");
                    binding.progress.setProgress(50f);
                    binding.monitor.setImageDrawable(ContextCompat.getDrawable(SimulationActivityMicro.this, R.drawable.micro_main_problem));
                    binding.hint.setText("Open the side panel of the microwave.");
                    MediaPlayer mp = MediaPlayer.create(SimulationActivityMicro.this, R.raw.ting);
                    mp.start();

                    step1 = false;
                    step2 = true;
                }

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.activityLogRecyclerView.setLayoutManager(layoutManager);


        addToLog("Troubleshooting starts.");

    }

    public void addToLog(String toBeAdded) {
        listActivityLog.add(" - " + toBeAdded);
        Log.d("ACTIVITY LOG : ",
                listActivityLog.size() + 1 + " " + toBeAdded);
        activityLogAdapter = new ActivityLogAdapter(listActivityLog);
        binding.activityLogRecyclerView.setAdapter(activityLogAdapter);
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


    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        ImageView imageView = (ImageView) dragEvent.getLocalState();
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                if (view.getId() == R.id.layout_problem) {
                    enterShape(view);
                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                normalShape(view);
                break;
            case DragEvent.ACTION_DROP:
                addToLog("Dropped " + imageView.getContentDescription() + " to " + view.getContentDescription());
                if (step2) {
                    if ((view.getId() == R.id.monitor) && (imageView.getId() == R.id.screwdriverPhillip)) {
                        binding.monitor.setImageDrawable(ContextCompat.getDrawable(SimulationActivityMicro.this, R.drawable.micro_interior));
                        binding.progress.setProgress(40f);
                        addToLog("Progress 40%");
                        MediaPlayer mp = MediaPlayer.create(SimulationActivityMicro.this, R.raw.ting);
                        mp.start();
                        step2 = false;
                        step3 = true;
                        binding.hint.setText("Find the faulty component.");
                        addToLog("Side Panel of the microwave is removed using a screw driver.");
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        MediaPlayer mp = MediaPlayer.create(SimulationActivityMicro.this, R.raw.engk);
                        if (view.getId() != R.id.layout_toolbox) {
                            mp.start();
                        }
                    }
                } else if (step3) {
                    if ((view.getId() == R.id.monitor) && (imageView.getId() == R.id.long_nose)) {
                        addToLog("Faulty fuse has been removed, replace it with a new one.");
                        binding.monitor.setImageDrawable(ContextCompat.getDrawable(SimulationActivityMicro.this, R.drawable.micro_interior));
                        binding.progress.setProgress(78f);
                        addToLog("Progress 78%");
                        MediaPlayer mp = MediaPlayer.create(SimulationActivityMicro.this, R.raw.ting);
                        mp.start();
                        step3 = false;
                        step4 = true;
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        MediaPlayer mp = MediaPlayer.create(SimulationActivityMicro.this, R.raw.engk);
                        if (view.getId() != R.id.layout_toolbox) {
                            mp.start();
                        }
                    }
                } else if (step4) {
                    if ((view.getId() == R.id.monitor) && (imageView.getId() == R.id.fuse)) {
                        binding.progress.setProgress(100f);
                        addToLog("New fuse is placed, troubleshooting is complete.");
                        addToLog("Progress 100%");
                        addToLog("Congratulations ");
                        binding.hint.setText("Congratulations! You fixed it.");
                        final AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(SimulationActivityMicro.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(SimulationActivityMicro.this);
                        }
                        builder.setTitle("Congratulations!")
                                .setMessage("You have successfully solved the problem!")
                                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                        binding.monitor.setImageDrawable(ContextCompat.getDrawable(SimulationActivityMicro.this, R.drawable.micro_problem));
                        MediaPlayer mp = MediaPlayer.create(SimulationActivityMicro.this, R.raw.ting);
                        mp.start();
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        MediaPlayer mp = MediaPlayer.create(SimulationActivityMicro.this, R.raw.engk);
                        if (view.getId() != R.id.layout_toolbox) {
                            mp.start();
                        }
                    }
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    MediaPlayer mp = MediaPlayer.create(SimulationActivityMicro.this, R.raw.engk);
                    if (view.getId() != R.id.layout_toolbox) {
                        mp.start();
                    }
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                normalShape(view);
                /*if(isFinish()){
                    final Dialog dialog = new Dialog(this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_congrats_1);
                    dialog.setCancelable(false);
                    Button start = (Button) dialog.findViewById(R.id.start);
                    start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(SimulationActivityVGA.this,ReviseSimulationActivity2.class));
                            finish();
                        }
                    });
                    dialog.show();
                }*/
                break;
        }
        return true;
    }

    private void enterShape(View view) {
        switch (view.getId()) {
            case R.id.layout_problem:
                view.setBackground(enterShape);
                break;
            case R.id.layout_toolbox:
                //view.setBackground(enterShape);
                break;

        }
    }

    private void normalShape(View view) {
        switch (view.getId()) {
            case R.id.layout_problem:
                view.setBackground(normalShape);
                break;
            case R.id.layout_toolbox:
                //view.setBackground(normalShape);
                break;

        }
    }
/*
    private boolean isEnterable(View view, ImageView imageView) {
         switch (view.getId()) {
             case R.id.layout_top_left:
                 return true;
             case R.id.layout_acids:
                 return asd(PANEL_ACIDS, (int) imageView.getId());
             case R.id.layout_alcohol:
                 return asd(PANEL_ALCOHOL, (int) imageView.getId());
             case R.id.layout_ketones:
                 return asd(PANEL_KETONES, (int) imageView.getId());
             default:
                 return false;
         }
     }*/

    private boolean asd(int[] panels, int id) {
        for (int i = 0; i < panels.length; i++) {
            if (id == panels[i]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData clipData = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(clipData, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }


    /*private boolean isFinish() {
        for (int id : PANEL_ACIDS) {
            if (binding.layoutAcids.findViewById(id) == null) {
                return false;
            }
        }
        for (int id : PANEL_ALCOHOL) {
            if (binding.layoutAlcohol.findViewById(id) == null) {
                return false;
            }
        }
        for (int id : PANEL_KETONES) {
            if (binding.layoutKetones.findViewById(id) == null) {
                return false;
            }
        }
        return true;
    }*/

}
