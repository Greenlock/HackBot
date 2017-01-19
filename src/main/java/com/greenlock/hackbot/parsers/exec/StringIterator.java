package com.greenlock.hackbot.parsers.exec;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LukeSmalley on 1/7/2017.
 */
public abstract class StringIterator<T> {

    protected T result;
    private String specimen;
    private int index = 0;
    private List<Runnable> states = new ArrayList<>();

    protected StringIterator(String specimen) {
        this.specimen = specimen;
    }


    protected char current() {
        return specimen.charAt(index);
    }

    protected boolean currentEquals(char value) {
        return specimen.charAt(index) == value;
    }

    protected boolean currentBegins(String value) {
        return specimen.substring(index, Math.min(index + value.length(), specimen.length())).startsWith(value);
    }


    protected boolean canAdvance() {
        return index + 1 < specimen.length();
    }

    protected boolean canAdvance(int count) {
        return index + count < specimen.length();
    }

    protected void advance() {
        index++;
    }

    protected void advance(int count) {
        index += count;
    }

    protected boolean canRewind() {
        return index - 1 >= 0;
    }

    protected boolean canRewind(int count) {
        return index - count >= 0;
    }

    protected void rewind() {
        index--;
    }

    protected void rewind(int count) {
        index -= count;
    }


    protected void enter(Runnable state) {
        states.add(0, state);
    }

    protected void leave() {
        states.remove(0);
    }


    public abstract void onFinish();

    public T run() {
        for (index = 0; index < specimen.length(); index++) {
            states.get(0).run();
        }
        onFinish();
        return result;
    }
}
