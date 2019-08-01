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

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tip.capstone.mlearning.R;
import com.tip.capstone.mlearning.databinding.ActivitySimulationSysBinding;
import com.tip.capstone.mlearning.ui.adapter.ActivityLogAdapter;

import java.util.ArrayList;
import java.util.List;

public class SimulationActivitySys extends AppCompatActivity
        implements View.OnDragListener, View.OnTouchListener {

    private ActivitySimulationSysBinding binding;
    private List<String> listActivityLog = new ArrayList<>();
    private ActivityLogAdapter activityLogAdapter;

    public static final int[] SIMULATION_DRAWABLES = {
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_simulation_sys);

        enterShape = ContextCompat.getDrawable(this, R.drawable.shape_droptarget);
        normalShape = ContextCompat.getDrawable(this, R.drawable.shape);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView[] imageViews = {
                binding.vgaGood,
                binding.screwdriverPhillip,
                binding.ramddr2,
                binding.psupply,
                binding.ramddr3,
                binding.psupply2,
                binding.dvi,
                binding.brush,
                binding.light,
                binding.powerCord,
                binding.cases};

        binding.layoutToolbox.setOnDragListener(this);
        binding.monitor.setOnDragListener(this);
        binding.layoutProblem.setOnDragListener(this);
        binding.activityLogRecyclerView.setOnDragListener(this);
        binding.progressLayout.setOnDragListener(this);

        for (int i = 0; i < imageViews.length; i++) {
            Glide.with(this)
                    .load(SIMULATION_DRAWABLES[i])
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .priority(Priority.IMMEDIATE)
                    .dontAnimate()
                    .into(imageViews[i]);

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
        binding.monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (step4) {
                    addToLog("Motherboard");
                    addToLog("Progress 50%");
                    binding.progress.setProgress(50f);
                    binding.monitor.setImageDrawable(ContextCompat.getDrawable(SimulationActivitySys.this, R.drawable.mobo));
                    binding.hint.setText("Place some memory and clean the MOBO then put back the case");
                    MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.ting);
                    mp.start();

                    step4 = false;
                    step5 = true;
                }

                if (step9) {
                    addToLog("System unit tapped");
                    addToLog("Progress 90%");
                    binding.progress.setProgress(90f);
                    binding.monitor.setImageDrawable(ContextCompat.getDrawable(SimulationActivitySys.this, R.drawable.system_unit));
                    binding.hint.setText("Turn on the system unit");

                    MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.ting);
                    mp.start();

                    step9 = false;
                    step10 = true;
                }

                if (step10) {
                    binding.progress.setProgress(100f);
                    addToLog("Progress 100%");
                    addToLog("Congratulations ");
                    binding.hint.setText("Congratulations! You fixed it.");
                    final AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(SimulationActivitySys.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(SimulationActivitySys.this);
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

                    binding.monitor.setImageDrawable(ContextCompat.getDrawable(SimulationActivitySys.this, R.drawable.system_unit_complete));
                    MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.ting);
                    mp.start();
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.activityLogRecyclerView.setLayoutManager(layoutManager);


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
                    /*if (!step1 && step2) {
                        if ((view.getId() == R.id.monitor) && (imageView.getId() == R.id.vgaGood)) {
                            step1 = false;
                            binding.progress.setProgress(100f);
                            addToLog("Progress 100%");
                            addToLog("Congratulations ");
                            binding.hint.setText("Congratulations! You fixed it.");
                            final AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(this);
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

                            binding.monitor.setImageDrawable(ContextCompat.getDrawable(SimulationActivitySys.this, R.drawable.monitor_good));
                            MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.ting);
                            mp.start();
                        } else {
                            imageView.setVisibility(View.VISIBLE);
                            MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.engk);
                            if (view.getId() != R.id.layout_toolbox) {
                                mp.start();
                            }
                        }
                    }*/

                //step 5 Drag ung brush, Drag ung 	ram (dagdag progress)


                //step 1 Screwdriver para mapunta s image 2
                if (step1) {
                    if ((view.getId() == R.id.monitor) && (imageView.getId() == R.id.screwdriverPhillip)) {
                        binding.monitor.setImageDrawable(ContextCompat.getDrawable(SimulationActivitySys.this, R.drawable.sysunit2));
                        binding.progress.setProgress(10f);
                        addToLog("Progress 10%");
                        MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.ting);
                        mp.start();
                        step2 = true;
                        step1 = false;
                        imageView.setVisibility(View.VISIBLE);
                        binding.hint.setText("Put a Power Supply");
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.engk);
                        if (view.getId() != R.id.layout_toolbox) {
                            mp.start();
                        }
                    }
                }
                //step 2 Drag ng power supply then punta image 3
                else if (step2) {
                    if ((view.getId() == R.id.monitor) && (imageView.getId() == R.id.psupply)) {
                        binding.monitor.setImageDrawable(ContextCompat.getDrawable(SimulationActivitySys.this, R.drawable.sysunit1));
                        binding.progress.setProgress(20f);
                        addToLog("Progress 20%");
                        MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.ting);
                        mp.start();
                        step2 = false;
                        step3 = true;
                        binding.hint.setText("Remove the Screws to get the Motherboard");
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.engk);
                        if (view.getId() != R.id.layout_toolbox) {
                            mp.start();
                        }
                    }
                }
                //step 3 Screwdriver ulit pero walang mangyayare dadagdag lng ung progress
                else if (step3) {
                    if ((view.getId() == R.id.monitor) && (imageView.getId() == R.id.screwdriverPhillip)) {
                        binding.monitor.setImageDrawable(ContextCompat.getDrawable(SimulationActivitySys.this, R.drawable.sysunit1));
                        binding.progress.setProgress(40f);
                        addToLog("Progress 40%");
                        MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.ting);
                        mp.start();
                        step3 = false;
                        step4 = true;
                        binding.hint.setText("Tap the system unit to open MOBO");
                        imageView.setVisibility(View.VISIBLE);
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.engk);
                        if (view.getId() != R.id.layout_toolbox) {
                            mp.start();
                        }
                    }
                }
                //step 5 drag yung brush
                else if (step5) {
                    if ((view.getId() == R.id.monitor) && (imageView.getId() == R.id.brush)) {
                        //  binding.monitor.setImageDrawable(ContextCompat.getDrawable(SimulationActivitySys.this, R.drawable.mo));
                        binding.progress.setProgress(60f);
                        addToLog("Progress 60%");
                        MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.ting);
                        mp.start();
                        step5 = false;
                        step6 = true;
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.engk);
                        if (view.getId() != R.id.layout_toolbox) {
                            mp.start();
                        }
                    }
                }

                //step 6 drag yung brush
                else if (step6) {
                    if ((view.getId() == R.id.monitor) && (imageView.getId() == R.id.ramddr3)) {
                        //  binding.monitor.setImageDrawable(ContextCompat.getDrawable(SimulationActivitySys.this, R.drawable.mo));
                        binding.progress.setProgress(70f);
                        addToLog("Progress 70%");
                        MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.ting);
                        mp.start();
                        step6 = false;
                        step7 = true;
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.engk);
                        if (view.getId() != R.id.layout_toolbox) {
                            mp.start();
                        }
                    }
                } else if (step7) {
                    if ((view.getId() == R.id.monitor) && (imageView.getId() == R.id.cases)) {
                        binding.monitor.setImageDrawable(ContextCompat.getDrawable(SimulationActivitySys.this, R.drawable.sysunit1));
                        binding.progress.setProgress(75f);
                        addToLog("Progress 75%");
                        MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.ting);
                        mp.start();
                        step7 = false;
                        step8 = true;
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.engk);
                        if (view.getId() != R.id.layout_toolbox) {
                            mp.start();
                        }
                    }
                } else if (step8) {
                    if ((view.getId() == R.id.monitor) && (imageView.getId() == R.id.screwdriverPhillip)) {
                        binding.monitor.setImageDrawable(ContextCompat.getDrawable(SimulationActivitySys.this, R.drawable.sysunit1));
                        binding.progress.setProgress(80f);
                        addToLog("Progress 80%");
                        MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.ting);
                        mp.start();
                        step8 = false;
                        step9 = true;
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.engk);
                        if (view.getId() != R.id.layout_toolbox) {
                            mp.start();
                        }
                    }
                }else {
                    imageView.setVisibility(View.VISIBLE);
                    MediaPlayer mp = MediaPlayer.create(SimulationActivitySys.this, R.raw.engk);
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
