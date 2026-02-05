package com.myname.mymodid.notification;

import java.util.LinkedList;
import java.util.Queue;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NotificationManager {

    private static final NotificationManager INSTANCE = new NotificationManager();

    private static final long SLIDE_IN_DURATION = 500L;
    private static final long DISPLAY_DURATION = 5000L;
    private static final long SLIDE_OUT_DURATION = 500L;
    private static final long TOTAL_DURATION = SLIDE_IN_DURATION + DISPLAY_DURATION + SLIDE_OUT_DURATION;

    private final Queue<Notification> queue = new LinkedList<>();
    private Notification current;
    private long displayStartTime;

    private NotificationManager() {}

    public static NotificationManager getInstance() {
        return INSTANCE;
    }

    public void enqueue(Notification notification) {
        if (current == null) {
            current = notification;
            displayStartTime = System.currentTimeMillis();
        } else {
            queue.add(notification);
        }
    }

    public Notification getCurrent() {
        return current;
    }

    public void update() {
        if (current == null) {
            return;
        }
        long elapsed = System.currentTimeMillis() - displayStartTime;
        if (elapsed >= TOTAL_DURATION) {
            if (!queue.isEmpty()) {
                current = queue.poll();
                displayStartTime = System.currentTimeMillis();
            } else {
                current = null;
            }
        }
    }

    /**
     * Returns a value in [0.0, 1.0] representing how far the notification
     * has slid in from the right edge.
     * 0.0 = fully hidden off-screen to the right.
     * 1.0 = fully visible at its target position.
     */
    public double getSlideProgress() {
        if (current == null) {
            return 0.0;
        }
        long elapsed = System.currentTimeMillis() - displayStartTime;

        if (elapsed < SLIDE_IN_DURATION) {
            return (double) elapsed / SLIDE_IN_DURATION;
        } else if (elapsed < SLIDE_IN_DURATION + DISPLAY_DURATION) {
            return 1.0;
        } else {
            long slideOutElapsed = elapsed - SLIDE_IN_DURATION - DISPLAY_DURATION;
            return 1.0 - ((double) slideOutElapsed / SLIDE_OUT_DURATION);
        }
    }
}
