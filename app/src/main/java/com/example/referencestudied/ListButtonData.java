package com.example.referencestudied;

import java.util.concurrent.Callable;

public class ListButtonData {
    private final String text;
    private final Callable<Void> action;

    public ListButtonData(String text, Callable<Void> action) {
        this.text = text;
        this.action = action;
    }

    public String getText() {
        return text;
    }

    public Callable<Void> getAction() {
        return action;
    }
}
