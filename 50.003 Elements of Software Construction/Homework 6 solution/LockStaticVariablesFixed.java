public class LockStaticVariablesFixed {
    public static int saving = 5000;
    public static int cash = 0;

    public static Object lock = new Object();

    public static void main(String args[]) {
        int numberOfThreads = 10000;
        WD[] threads = new WD[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new WD();
            threads[i].start();
        }
        try {
            for (int i = 0; i < numberOfThreads; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            System.out.println("Some thread is not finished.");
        }
        System.out.println("The result is: " + LockStaticVariablesFixed.cash);
    }
}

class WD extends Thread {
    public void run() {
        synchronized (LockStaticVariablesFixed.lock) {
            if (LockStaticVariablesFixed.saving >= 1000) {
                System.out.println("I am doing something.");
                LockStaticVariablesFixed.saving = LockStaticVariablesFixed.saving - 1000;
                LockStaticVariablesFixed.cash = LockStaticVariablesFixed.cash + 1000;
            }
        }
    }
}
