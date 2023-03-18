package Test;

public class MessageSender implements Runnable {
    private int senderId;
    private int receiverId;
    private int threadId;
    private IBuffer buffer;
    private ISender sender;

    public MessageSender(int senderId, int receiverId, int threadId, IBuffer buffer, ISender sender) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.threadId = threadId;
        this.buffer = buffer;
        this.sender = sender;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep((long) (Math.random() * 1000));
                IMessage message = new Message("message " + threadId, "process " + senderId, "process " + receiverId, System.currentTimeMillis());
                buffer.put(message);
                sender.send(message);
                System.out.println("Thread " + threadId + " sent message to process " + receiverId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}