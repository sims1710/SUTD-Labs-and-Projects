import java.util.Random;

class MyFork {
    private final int index;
    private boolean isAvailable = true;

    public MyFork(int index) {
        this.index = index;
    }

    public synchronized void pickup() throws InterruptedException {
        while (!isAvailable) {
            wait();
        }

        isAvailable = false;
        notifyAll();
    }

    public synchronized void putdown() throws InterruptedException {
        while (isAvailable) {
            wait();
        }

        isAvailable = true;
        notifyAll();
    }

    public String toString() {
        if (isAvailable) {
            return "Fork " + index + " is available.";
        } else {
            return "Fork " + index + " is NOT available.";
        }
    }
}

public class DiningPhilFixed1 {
    private static int N = 5;

    public static void main(String[] args) throws Exception {
        PhilosopherFixed[] phils = new PhilosopherFixed[N];
        MyFork[] forks = new MyFork[N];

        for (int i = 0; i < N; i++) {
            forks[i] = new MyFork(i);
        }

        for (int i = 0; i < N; i++) {
            phils[i] = new PhilosopherFixed(i, forks[i], forks[(i + N - 1) % N]);
            phils[i].start();
        }
    }
}

class PhilosopherFixed extends Thread {
    private final int index;
    private final MyFork left;
    private final MyFork right;

    public PhilosopherFixed(int index, MyFork left, MyFork right) {
        this.index = index;
        this.left = left;
        this.right = right;
    }

    public void run() {
        Random randomGenerator = new Random();
        try {
            while (true) {
                Thread.sleep(randomGenerator.nextInt(100)); // not sleeping but thinking
                System.out.println("Phil " + index + " finishes thinking.");

                if (index == 0) {
                    right.pickup();
                    System.out.println("Phil " + index + " picks up right fork.");
                    left.pickup();
                    System.out.println("Phil " + index + " picks up left fork.");
                } else {
                    left.pickup();
                    System.out.println("Phil " + index + " picks up left fork.");
                    right.pickup();
                    System.out.println("Phil " + index + " picks up right fork.");
                }

                Thread.sleep(randomGenerator.nextInt(100)); // eating
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
