//package solutions;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilFixed2 {
    private static int N = 5;

    public static void main(String[] args) throws Exception {
        PhilosopherFixed2[] phils = new PhilosopherFixed2[N];
        MyFork2[] forks = new MyFork2[N];

        for (int i = 0; i < N; i++) {
            forks[i] = new MyFork2(i);
        }

        for (int i = 0; i < N; i++) {
            phils[i] = new PhilosopherFixed2(i, forks[i], forks[(i + N - 1) % N]);
            phils[i].start();
        }
    }
}

class PhilosopherFixed2 extends Thread {
    private final int index;
    private final MyFork2 left;
    private final MyFork2 right;

    public PhilosopherFixed2(int index, MyFork2 left, MyFork2 right) {
        this.index = index;
        this.left = left;
        this.right = right;
    }

    public void run() {
        Random randomGenerator = new Random();
        try {
            while (true) {
                Thread.sleep(randomGenerator.nextInt(100)); //not sleeping but thinking
                System.out.println("Phil " + index + " finishes thinking.");
                if (!left.pickup()) {
                    break;
                }

                System.out.println("Phil " + index + " picks up left fork.");

                if (!right.pickup()) {
                    left.putdown();
                    break;
                }

                System.out.println("Phil " + index + " picks up right fork.");
                Thread.sleep(randomGenerator.nextInt(100)); //eating
                System.out.println("Phil " + index + " finishes eating.");
                left.putdown();
                System.out.println("Phil " + index + " puts down left fork.");
                right.putdown();
                System.out.println("Phil " + index + " puts down right fork.");
            }
        } catch (InterruptedException e) {
            System.out.println("Don't disturb me while I am sleeping, well, thinking.");
        }
    }
}

class MyFork2 {
    private final int index;
    private boolean isAvailable = true;
    private final ReentrantLock reentrantLock = new ReentrantLock();
    Random r = new Random();

    public MyFork2(int index) {
        this.index = index;
    }

    public boolean pickup() throws InterruptedException {
        return reentrantLock.tryLock(r.nextInt(1000), TimeUnit.MILLISECONDS);
    }

    public void putdown() throws InterruptedException {
        reentrantLock.unlock();
    }
}