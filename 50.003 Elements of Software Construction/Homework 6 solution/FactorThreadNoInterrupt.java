import java.math.BigInteger;

public class FactorThreadNoInterrupt {
    public static final int numberOfThreads = 4;
    public static void main(String[] args) throws Exception { 
       BigInteger n = new BigInteger("1127451830576035879");
        FactorNoInterrupt[] factors = new FactorNoInterrupt[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            factors[i] = new FactorNoInterrupt(n, i + 2, numberOfThreads);
            factors[i].start();
        }
        for (int i = 0; i < numberOfThreads; i++) {
            factors[i].join();
        }
    }

public static BigInteger factorMultiThread(BigInteger input) {
        BigInteger result = null;
        return result;
    }
}

class FactorNoInterrupt extends Thread {
    BigInteger n;
    BigInteger init;
    BigInteger stepSize;
    BigInteger result;

    public FactorNoInterrupt(BigInteger n, int init, int stepSize) {
        this.n = n;
        this.init = BigInteger.valueOf(init);
        this.stepSize = BigInteger.valueOf(stepSize);
    }

    public void run() {
        BigInteger zero = new BigInteger("0");

        while (init.compareTo(n) < 0) {
            if (n.remainder(init).compareTo(zero) == 0) {
                System.out.println("Got it: " + init);
                break;
            }

            init = init.add(stepSize);
        }
    }
}