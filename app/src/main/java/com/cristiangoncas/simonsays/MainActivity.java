package com.cristiangoncas.simonsays;

import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.CircularButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cristiangoncas.simonsays.config.Constants;
import com.cristiangoncas.simonsays.controller.SequenceController;
import com.cristiangoncas.simonsays.model.SequenceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends WearableActivity implements View.OnClickListener {

    private Button yellow, red, green, blue, retry;
    private TextView level, msg;
    private LinearLayout controls, retryMsg;

    private final Handler handler = new Handler();

    private SequenceController sequenceController;

    private boolean capturingUserSequence = false;
    private List<Integer> currentSequence = new ArrayList<>();
    private List<Integer> userSequence = new ArrayList<>();

    final static Map<String, Integer> lightColors = new HashMap<>();

    static {
        lightColors.put("yellow", R.drawable.rounded_yellow_light);
        lightColors.put("red", R.drawable.rounded_red_light);
        lightColors.put("green", R.drawable.rounded_green_light);
        lightColors.put("blue", R.drawable.rounded_blue_light);
    }

    final static Map<String, Integer> brightColors = new HashMap<>();

    static {
        brightColors.put("yellow", R.drawable.rounded_yellow);
        brightColors.put("red", R.drawable.rounded_red);
        brightColors.put("green", R.drawable.rounded_green);
        brightColors.put("blue", R.drawable.rounded_blue);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sequenceController = new SequenceController();

        controls = (LinearLayout) findViewById(R.id.controls);
        retryMsg = (LinearLayout) findViewById(R.id.retry);
        yellow = (Button) findViewById(R.id.yellow);
        yellow.setOnClickListener(this);
        red = (Button) findViewById(R.id.red);
        red.setOnClickListener(this);
        green = (Button) findViewById(R.id.green);
        green.setOnClickListener(this);
        blue = (Button) findViewById(R.id.blue);
        blue.setOnClickListener(this);
        retry = (Button) findViewById(R.id.retry_btn);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableControls();
                userSequence = new ArrayList<>();
                retryMsg.setVisibility(View.GONE);
            }
        });
        CircularButton decrease = (CircularButton) findViewById(R.id.decrease_level);
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentLevel = sequenceController.getCurrentLevel();
                if (currentLevel > Constants.MIN_LEVEL) {
                    sequenceController.downLevel();
                    currentLevel = sequenceController.getCurrentLevel();
                    level.setText(String.valueOf(currentLevel));
                }
            }
        });
        CircularButton increase = (CircularButton) findViewById(R.id.increase_level);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentLevel = sequenceController.getCurrentLevel();
                if (currentLevel < Constants.MAX_LEVEL) {
                    sequenceController.upLevel();
                    currentLevel = sequenceController.getCurrentLevel();
                    level.setText(String.valueOf(currentLevel));
                }
            }
        });
        level = (TextView) findViewById(R.id.level);
        level.setText(String.valueOf(sequenceController.getCurrentLevel()));
        msg = (TextView) findViewById(R.id.msg);
        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controls.setVisibility(View.GONE);
                executeSequence();
            }
        });
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.yellow:
                changeColor(view, getBrightColor("yellow"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeColor(view, getLightColor("yellow"));
                    }
                }, 200);
                break;
            case R.id.red:
                changeColor(view, getBrightColor("red"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeColor(view, getLightColor("red"));
                    }
                }, 200);
                break;
            case R.id.green:
                changeColor(view, getBrightColor("green"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeColor(view, getLightColor("green"));
                    }
                }, 200);
                break;
            case R.id.blue:
                changeColor(view, getBrightColor("blue"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeColor(view, getLightColor("blue"));
                    }
                }, 200);
                break;
        }

        if (!capturingUserSequence) {
            // TODO: Failed
        } else if (userSequence.size() < currentSequence.size()) {
            if (view.getId() == currentSequence.get(userSequence.size())) {
                retryMsg.setVisibility(View.GONE);
                userSequence.add(view.getId());
            } else {
                disableColorButtons();
                msg.setText("Wrong!");
                msg.setTextColor(getResources().getColor(R.color.red));
                retryMsg.setVisibility(View.VISIBLE);
                capturingUserSequence = false;
            }
        }

        if (userSequence.size() == currentSequence.size()) {
            disableColorButtons();
            msg.setText("Congratz!");
            msg.setTextColor(getResources().getColor(R.color.green));
            retryMsg.setVisibility(View.VISIBLE);
        }
    }

    private void executeSequence() {
        final SequenceModel sequence = sequenceController.generateSequence();
        currentSequence = sequence.getBtnSequence();
        final int currentLevel = sequenceController.getCurrentLevel();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int brightTime = 400;
                if (currentLevel > 45) {
                    brightTime = 40;
                } else if (currentLevel > 40) {
                    brightTime = 60;
                } else if (currentLevel > 35) {
                    brightTime = 80;
                } else if (currentLevel > 24) {
                    brightTime = 100;
                } else if (currentLevel > 23) {
                    brightTime = 150;
                } else if (currentLevel > 22) {
                    brightTime = 200;
                } else if (currentLevel > 21) {
                    brightTime = 250;
                } else if (currentLevel > 20) {
                    brightTime = 250;
                } else if (currentLevel > 15) {
                    brightTime = 290;
                } else if (currentLevel > 10) {
                    brightTime = 350;
                } else if (currentLevel > 5) {
                    brightTime = 450;
                }

                boolean isFirst = true;
                try {
                    for (int id : currentSequence) {
                        if (!isFirst) {
                            Thread.sleep(brightTime);
                        } else {
                            isFirst = false;
                        }
                        switch (id) {
                            case R.id.yellow:
                                changeColor(yellow, getBrightColor("yellow"));
                                Log.i("Wear", "Yellow");
                                Thread.sleep(brightTime);
                                break;
                            case R.id.red:
                                changeColor(red, getBrightColor("red"));
                                Log.i("Wear", "Red");
                                Thread.sleep(brightTime);
                                break;
                            case R.id.green:
                                changeColor(green, getBrightColor("green"));
                                Log.i("Wear", "Green");
                                Thread.sleep(brightTime);
                                break;
                            case R.id.blue:
                                changeColor(blue, getBrightColor("blue"));
                                Log.i("Wear", "Blue");
                                Thread.sleep(brightTime);
                                break;
                        }
                        changeColor(yellow, getLightColor("yellow"));
                        changeColor(red, getLightColor("red"));
                        changeColor(green, getLightColor("green"));
                        changeColor(blue, getLightColor("blue"));
                    }
                    enableColorButtons();
                } catch (InterruptedException e) {
                    // Ignoring exception
                }
            }
        }).start();
    }

    private void changeColor(final View button, final int drawable) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                button.setBackground(getDrawable(drawable));
            }
        });
    }

    private void enableControls() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                controls.setVisibility(View.VISIBLE);
            }
        });
    }

    private int getLightColor(String color) {
        return lightColors.get(color);
    }

    private int getBrightColor(String color) {
        return brightColors.get(color);
    }

    private void enableColorButtons() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                capturingUserSequence = true;
                yellow.setEnabled(true);
                red.setEnabled(true);
                green.setEnabled(true);
                blue.setEnabled(true);
            }
        });
    }

    private void disableColorButtons() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                yellow.setEnabled(false);
                red.setEnabled(false);
                green.setEnabled(false);
                blue.setEnabled(false);
            }
        });
    }
}