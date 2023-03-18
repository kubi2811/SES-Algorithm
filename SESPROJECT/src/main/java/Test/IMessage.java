package Test;

public interface IMessage {
    String getContent();
    void setContent(String content);
    String getSender();
    void setSender(String sender);
    String getReceiver();
    void setReceiver(String receiver);
    long getTimestamp();
    void setTimestamp(long timestamp);
}
