package com.cristiangoncas.wearexample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends WearableActivity implements View.OnClickListener {

    Button yellow, red, green, blue, decrease, increase, start;
    TextView level;

    private final int BRIGHT_VALUE = 1000;
    private final int INTERVAL_VALUE = 1500;

    private Handler handler = new Handler();

    int levelNumber = 1;

    static Map<String, Integer> lightColors = new HashMap<>();

    static {
        lightColors.put("yellow", R.color.yellow_light);
        lightColors.put("red", R.color.red_light);
        lightColors.put("green", R.color.green_light);
        lightColors.put("blue", R.color.blue_light);
    }

    static Map<String, Integer> brightColors = new HashMap<>();

    static {
        brightColors.put("yellow", R.color.yellow);
        brightColors.put("red", R.color.red);
        brightColors.put("green", R.color.green);
        brightColors.put("blue", R.color.blue);
    }

    static List<Integer> sequence1 = new ArrayList<>();

    static {
        sequence1.add(0, R.id.yellow);
        sequence1.add(1, R.id.blue);
        sequence1.add(2, R.id.green);
        sequence1.add(3, R.id.red);
        sequence1.add(4, R.id.red);
        sequence1.add(5, R.id.blue);
        sequence1.add(6, R.id.yellow);
        sequence1.add(7, R.id.green);
        sequence1.add(8, R.id.yellow);
        sequence1.add(9, R.id.blue);
        sequence1.add(10, R.id.red);
        sequence1.add(11, R.id.green);
        sequence1.add(12, R.id.yellow);
        sequence1.add(13, R.id.red);
        sequence1.add(14, R.id.green);
        sequence1.add(15, R.id.yellow);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setAmbientEnabled();

        yellow = (Button) findViewById(R.id.yellow);
        yellow.setOnClickListener(this);
        red = (Button) findViewById(R.id.red);
        red.setOnClickListener(this);
        green = (Button) findViewById(R.id.green);
        green.setOnClickListener(this);
        blue = (Button) findViewById(R.id.blue);
        blue.setOnClickListener(this);
        decrease = (Button) findViewById(R.id.decrease_level);
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (levelNumber > 1) {
                    levelNumber--;
                    level.setText(String.valueOf(levelNumber));
                }
                if (levelNumber <= 1) {
                    decrease.setEnabled(false);
                }
                if (levelNumber < 100) {
                    increase.setEnabled(true);
                }
            }
        });
        increase = (Button) findViewById(R.id.increase_level);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (levelNumber < 10) {
                    levelNumber++;
                    level.setText(String.valueOf(levelNumber));
                }
                if (levelNumber >= 10) {
                    increase.setEnabled(false);
                }
                if (levelNumber > 1) {
                    decrease.setEnabled(true);
                }
            }
        });
        level = (TextView) findViewById(R.id.level);
        level.setText(String.valueOf(levelNumber));
        start = (Button) findViewById(R.id.color);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                decrease.setEnabled(false);
                increase.setEnabled(false);
                executeSequence();
            }
        });
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
    }

    @Override
    public void onClick(View view) {
    }

    private void executeSequence() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int brightTime = BRIGHT_VALUE / levelNumber;
                int betweenColorTime = INTERVAL_VALUE / levelNumber;
                try {
                    for (int id : sequence1) {
                        switch (id) {
                            case R.id.yellow:
                                changeColor(yellow, getBrightColor("yellow"));
                                Thread.sleep(brightTime);
                                changeColor(yellow, getLightColor("yellow"));
                                break;
                            case R.id.red:
                                changeColor(red, getBrightColor("red"));
                                Thread.sleep(brightTime);
                                changeColor(red, getLightColor("red"));
                                break;
                            case R.id.green:
                                changeColor(green, getBrightColor("green"));
                                Thread.sleep(brightTime);
                                changeColor(green, getLightColor("green"));
                                break;
                            case R.id.blue:
                                changeColor(blue, getBrightColor("blue"));
                                Thread.sleep(brightTime);
                                changeColor(blue, getLightColor("blue"));
                                break;
                        }
                        Thread.sleep(betweenColorTime);
                    }
                } catch (InterruptedException e) {

                }
            }
        }).start();
        start.setEnabled(true);
        decrease.setEnabled(true);
        increase.setEnabled(true);
    }

    public void changeColor(final View button, final int color) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), color));
            }
        });
    }

    private int getLightColor(String color) {
        return lightColors.get(color);
    }

    private int getBrightColor(String color) {
        return brightColors.get(color);
    }
}