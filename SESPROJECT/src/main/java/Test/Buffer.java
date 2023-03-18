package Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Buffer implements IBuffer {
    private BlockingQueue<IMessage> queue;

    public Buffer() {
        this.queue = new LinkedBlockingQueue<>();
    }

    public void put(IMessage message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IMessage take() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}