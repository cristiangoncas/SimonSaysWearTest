package com.cristiangoncas.wearexample.controller;

import com.cristiangoncas.wearexample.R;
import com.cristiangoncas.wearexample.config.Constants;
import com.cristiangoncas.wearexample.model.SequenceModel;

import java.util.ArrayList;
import java.util.List;

public class SequenceController {

    private int currentLevel;

    private final static List<Integer> arrayBtn = new ArrayList<>();

    static {
        arrayBtn.add(0, R.id.yellow);
        arrayBtn.add(1, R.id.red);
        arrayBtn.add(2, R.id.green);
        arrayBtn.add(3, R.id.blue);
    }

    public SequenceController() {
        currentLevel = 1;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void upLevel() {
        if (currentLevel < Constants.MAX_LEVEL)
            currentLevel++;
    }

    public void downLevel() {
        if (currentLevel > Constants.MIN_LEVEL)
            currentLevel--;
    }

    private int generateRandomBtn() {
        return (int) (Math.random() * (3 + 1));
    }

    public SequenceModel generateSequence() {
        SequenceModel sequence = new SequenceModel();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < (currentLevel + 3); i++) {
            list.add(i, arrayBtn.get(generateRandomBtn()));
        }
        sequence.setBtnSequence(list);
        return sequence;
    }
}