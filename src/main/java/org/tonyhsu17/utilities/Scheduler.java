package org.tonyhsu17.utilities;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



/**
 * Provides running a method on a separate thread for an definitive number of times.
 * 
 * @author Tony Hsu
 *
 */
public class Scheduler extends Thread implements Logger {
    /**
     * Timer to fire off event
     */
    private Timer timer;
    int count;
    int maxCount;

    /**
     * Initializes stop watch with a predetermined seconds
     * 
     * @param seconds Number of seconds to start from
     * @param delegate Controller to handle events
     */
    public Scheduler(double seconds, Runnable function) {
        timer = new Timer((int)(seconds * 1000), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                function.run();
                count++;
                if(count >= maxCount) {
                    stopThread();
                }
            }
        });
    }

    public void runNTimes(int count) {
        maxCount = count;
        timer.start();
    }

    public void stopThread() {
        timer.stop();
        interrupt();
    }


    @Override
    public void run() {
        count = 0;
        timer.start();
    }
}
