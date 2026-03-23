package io.allitov.progressbar;

import java.util.concurrent.ThreadLocalRandom;

public class MultiThreadProgress {
    private static final int NUM_THREADS = 5;
    private static final int PROGRESS_LENGTH = 20;
    private static final int MAX_STEP_DELAY = 500;

    static void main() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        long[] startTimes = new long[NUM_THREADS];

        System.out.println("Начало многопоточного расчёта:");

        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadIndex = i;
            startTimes[i] = System.currentTimeMillis();

            threads[i] = new Thread(() -> {
                for (int step = 0; step <= PROGRESS_LENGTH; step++) {
                    updateDisplay(threadIndex, step, false, 0);

                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextInt(100, MAX_STEP_DELAY));
                    } catch (InterruptedException _) {
                        Thread.currentThread().interrupt();
                    }
                }

                long duration = System.currentTimeMillis() - startTimes[threadIndex];
                updateDisplay(threadIndex, PROGRESS_LENGTH, true, duration);
            });

            System.out.println();
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.printf("\033[%dB", NUM_THREADS);
        System.out.println("\nРасчёт завершён.");
    }

    private static synchronized void updateDisplay(int index, int step, boolean finished, long duration) {
        int linesToMoveUp = NUM_THREADS - index;

        System.out.printf("\033[%dA", linesToMoveUp);

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < PROGRESS_LENGTH; i++) {
            if (i < step) bar.append("=");
            else bar.append(" ");
        }
        bar.append("]");

        long threadId = Thread.currentThread().threadId();

        if (finished) {
            System.out.printf("\rПоток %d (ID: %d): %s Завершено! Время: %d мс%n",
                    index + 1, threadId, bar, duration);
        } else {
            System.out.printf("\rПоток %d (ID: %d): %s %d%%",
                    index + 1, threadId, bar, (step * 100 / PROGRESS_LENGTH));
        }

        System.out.printf("\033[%dB", linesToMoveUp - 1);
    }
}