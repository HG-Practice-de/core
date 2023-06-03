package de.hg_practice.core.util.timer;

public abstract class AbstractTimer implements Timer {

    private boolean running = false;

    @Override
    public final boolean start() {
        if (isStopped()) {
            doStart();
            running = true;
            return true;
        } else return false;
    }

    @Override
    public final boolean stop() {
        if (isRunning()) {
            doStop();
            running = false;
            return true;
        } else return false;
    }

    protected abstract void doStart();

    protected abstract void doStop();

    @Override
    public boolean isRunning() {
        return running;
    }
}
