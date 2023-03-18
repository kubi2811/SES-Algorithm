package Test;

public interface IBuffer {
    void put(IMessage message);
    IMessage take();
}
