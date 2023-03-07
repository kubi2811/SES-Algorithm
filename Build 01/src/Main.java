import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    private static final int NUM_PROCESSES = 7;
    private static final int NUM_MESSAGES = 150;

    public static void main(String[] args) {
        // Khởi tạo các BlockingQueue để chứa tin nhắn
        BlockingQueue<String>[] processQueues = new ArrayBlockingQueue[NUM_PROCESSES];
        for (int i = 0; i < NUM_PROCESSES; i++) {
            processQueues[i] = new ArrayBlockingQueue<>(NUM_MESSAGES);
        }

        // Tạo các tiến trình
        Thread[] processes = new Thread[NUM_PROCESSES];
        for (int i = 0; i < NUM_PROCESSES; i++) {
            final int processId = i;
            processes[i] = new Thread(() -> createProcess(processId, processQueues));
            processes[i].start();
        }

        // Đợi cho tất cả các tiến trình kết thúc
        for (Thread process : processes) {
            try {
                process.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Hàm gửi tin nhắn
    private static void sendMessage(int processId, String message, BlockingQueue<String>[] processQueues) {
        // Ngẫu nhiên thời gian để gửi tin nhắn
        double sleepTime = new Random().nextDouble();
        try {
            Thread.sleep((long) (sleepTime * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Gửi tin nhắn tới các tiến trình khác
        for (int i = 0; i < NUM_PROCESSES; i++) {
            if (i != processId) {
                try {
                    processQueues[i].put(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Hàm nhận tin nhắn
    private static void receiveMessage(int processId, BlockingQueue<String>[] processQueues) {
        while (true) {
            try {
                // Lấy tin nhắn từ queue
                String message = processQueues[processId].take();

                // Xử lý tin nhắn
                System.out.println("Process " + processId + " received message: " + message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Hàm tạo tiến trình
    private static void createProcess(int processId, BlockingQueue<String>[] processQueues) {
        // Tạo tiến trình nhận tin nhắn
        Thread receiver = new Thread(() -> receiveMessage(processId, processQueues));
        receiver.start();

        // Gửi các tin nhắn tới các tiến trình khác
        for (int i = 0; i < NUM_MESSAGES; i++) {
            String message = "message " + (i + 1);
            sendMessage(processId, message, processQueues);
        }
    }
}



