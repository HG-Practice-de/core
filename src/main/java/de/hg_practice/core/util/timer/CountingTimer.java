package de.hg_practice.core.util.timer;

public interface CountingTimer extends Timer {

    void beforeCount();
    void setCounter(long counter);
    long getCounter();
}
