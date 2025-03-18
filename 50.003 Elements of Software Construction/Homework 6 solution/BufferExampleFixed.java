import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class BufferExampleFixed {
    public static void main(String[] args) throws Exception {
        BlockingQueue<Object> buffer = new ArrayBlockingQueue<>(10);
        MyProducer prod = new MyProducer(buffer);
        prod.start();
        MyConsumer cons = new MyConsumer(buffer);
        cons.start();
    }
}

class MyProducer extends Thread {
    private BlockingQueue<Object> buffer;

    public MyProducer(BlockingQueue<Object> buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000));
            buffer.put(new Object());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyConsumer extends Thread {
    private BlockingQueue<Object> buffer;

    public MyConsumer(BlockingQueue<Object> buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000));
            buffer.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

