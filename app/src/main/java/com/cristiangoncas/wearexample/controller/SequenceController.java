package com.cristiangoncas.wearexample.controller;

import com.cristiangoncas.wearexample.R;

import java.util.ArrayList;
import java.util.List;

public class SequenceController {

    private final static int Min_LEVEL = 1;
    private final static int MAX_LEVEL = 100;

    private int currentLevel;

    static List<Integer> arrayBtn = new ArrayList<>();

    static {
        arrayBtn.add(0, R.id.yellow);
        arrayBtn.add(1, R.id.red);
        arrayBtn.add(2, R.id.green);
        arrayBtn.add(3, R.id.blue);
    }

    public SequenceController() {
        currentLevel = 1;
    }

    public void upLevel() {
        currentLevel++;
    }

    public void downLevel() {
        currentLevel--;
    }

    private int generateRandomBtn() {
        return (int) (Math.random() * (3 + 1));
    }

    private void generateSequence() {

    }

}