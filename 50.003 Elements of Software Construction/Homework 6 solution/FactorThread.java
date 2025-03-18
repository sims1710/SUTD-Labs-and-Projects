import java.math.BigInteger;

class Factor extends Thread {
    BigInteger n;
    BigInteger init;
    BigInteger stepSize;
    BigInteger result;

    public Factor(BigInteger n, int init, int stepSize) {
        this.n = n;
        this.init = BigInteger.valueOf(init);
        this.stepSize = BigInteger.valueOf(stepSize);
    }

    public void run() {
        BigInteger zero = new BigInteger("0");

        while (init.compareTo(n) < 0) {
            if (isInterrupted()) {
                break;
            }

            if (n.remainder(init).compareTo(zero) == 0) {
                result = init;
                FactorThread.found = true;
                break;
            }

            init = init.add(stepSize);
        }
    }

    public BigInteger getResult() {
        return result;
    }
}

public class FactorThread {
    public static final int numberOfThreads = 4;
    public volatile static boolean found = false;

    public static void main(String[] args) throws Exception {
        // http://en.wikipedia.org/wiki/Fermat_number
        // BigInteger n = new BigInteger("4294967297");
        BigInteger n = new BigInteger("1127451830576035879");

        long startTime = System.currentTimeMillis();
        BigInteger result = factorMultiThread(n);
        assert (result != null);
        long duration = System.currentTimeMillis() - startTime;
        BigInteger theother = n.divide(result);
        System.out.println("Factors are: " + result + ", " + theother);
        int seconds = (int) (duration / 1000) % 60;
        int minutes = (int) ((duration / (1000 * 60)) % 60);
        int hours = (int) ((duration / (1000 * 60 * 60)) % 24);
        System.out.print("Time used: " + hours + " hours " + minutes + " minutes " + seconds + " seconds.");
    }

    // Precondition: n is a semi-prime number.
    // Postcondition: the returned value is a prime factor of n;
    public static BigInteger factorMultiThread(BigInteger input) {
        Factor[] factors = new Factor[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            factors[i] = new Factor(input, 2 + i, numberOfThreads);
            factors[i].start();
        }

        while (!found) {
        }

        BigInteger result = null;

        for (int i = 0; i < numberOfThreads; i++) {
            if (factors[i].getResult() != null) {
                result = factors[i].getResult();
            } else {
                factors[i].interrupt();
            }
        }
        return result;
    }
}