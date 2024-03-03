package com.thombsonadam.antigrief.state;

import com.thombsonadam.antigrief.Mod;

import java.util.ArrayList;
import java.util.List;

public class State {
    private boolean isClam;
    private boolean isTotemDisappearedToKick = false;
    private long lastClamState = 0;
    private long lastUnclamState = 0;
    private boolean totemState;
    private boolean inResetState = false;
    private final List<Trigger> triggers = new ArrayList<>();
    private final List<List<Trigger>> previousTriggers = new ArrayList<>();
    private long cachedDelay;
    public boolean playersInDangerZone = false;

    public long calcDelay() {
        long l = calcDelay0();
        if (isClam && fromLastUnClam() < 5) {
            l = cachedDelay;
        }

        isTotemDisappearedToKick = l < 150;

        return cachedDelay = l;
    }

    public long calcDelay0() {
        if (triggers.isEmpty()) {
            return playersInDangerZone ? 500 : 1000;
        }

        long min = playersInDangerZone ? 600 : 800;
        for (Trigger trigger : triggers) {
            double distance = trigger.getDistanceToClient();
            long lm = 1000;
            if (distance <= 7) {
                lm = 10;
            } else if (distance <= 17) {
                lm = 100;
            } else if (distance <= 27) {
                lm = 500;
            }
            min = Math.min(min, lm);
        }
        return min;
    }

    public void updateTotemState(boolean s) {
        if (!s && totemState) {
            onTotemDisappeared();
        }
        totemState = s;
    }

    private void onTotemDisappeared() {
        Mod.LOGGER.warn("TOTEM USED!");
        if (isTotemDisappearedToKick) {
            Mod.kick(this);
        }
    }

    public boolean isParkTask() {
        return Mod.isDisabled();
    }

    public void resetStart() {
        inResetState = true;
        if (!triggers.isEmpty()) {
            if (previousTriggers.size() > 6) {
                previousTriggers.remove(0);
            }
            previousTriggers.add(new ArrayList<>(triggers));
        }
        triggers.clear();
        playersInDangerZone = false;
    }

    public void resetEnd() {
        inResetState = false;
        isClam = triggers.isEmpty();
        if (isClam) {
            lastClamState = System.currentTimeMillis();
        } else {
            lastUnclamState = System.currentTimeMillis();
        }

        Mod.debugLine = new StringBuilder(debug());
    }

    public void addTrigger(Trigger trigger) {
        triggers.add(trigger);
    }

    public long fromLastClam() {
        return (System.currentTimeMillis() - lastClamState) / 1000;
    }

    public long fromLastUnClam() {
        return (System.currentTimeMillis() - lastUnclamState) / 1000;
    }

    public String debug() {
        return String.format("delay=%s; flc=%s; flUnC=%s; trrgrs=%s", cachedDelay, fromLastClam(), fromLastUnClam(), triggers.size());
    }

    public String kickAddiction() {
        StringBuilder sb = new StringBuilder("§a");

        int i = 0;
        if (!previousTriggers.isEmpty()) {
            for (Trigger trigger : previousTriggers.get(previousTriggers.size() - 1)) {
                sb.append("\n * ").append(trigger.getDescription());
                i++;
                if (i > 7) {
                    break;
                }
            }
        }


        if (i > 7) {
            sb.append("\n.....");
        }

        return sb.toString();
    }
}
