package nsu.titov.messages;

import nsu.titov.converters.ByteIntConverter;
import nsu.titov.logic.MessageHandler;
import nsu.titov.network.MessageReader;

import java.nio.channels.SelectionKey;

/**
 * This class represents a message default message structure of BitTorrent protocol
 * All child classes represents different types of messages such as choke, bitfield etc and
 * should implement default constructor with 1 parameter:
 * int (represent total message size)
 * <p>
 * <tr/>Input payload for message constructor
 * <tr/>-----------------------------------
 * <tr/>| Message ID | Payload |
 * <tr/>-----------------------------------
 *
 * <p>
 * <tr/>{@code MESSAGE DOES NOT ALLOCATE MEMORY FOR INPUT PAYLOAD!!!}
 * <tr/>{@code BUT ALLOCATES MEMORY OUTPUT BYTES}
 */


abstract public class Message {
    protected int finalDataSize = 5;
    protected int payloadSize = 0;
    protected MessageId id;
    byte[] payload;

    public MessageId getId() {
        return id;
    }

    public Message() {

    }

    public byte[] getBytes() {
        byte[] bytes = new byte[finalDataSize];
        byte[] size = ByteIntConverter.intToByte(finalDataSize);

        bytes[0] = size[0];
        bytes[1] = size[1];
        bytes[2] = size[2];
        bytes[3] = size[3];

        bytes[4] = MessageId.idToByte(this.id);

        generateBytes(bytes);
        return bytes;
    }

    abstract void generateBytes(byte[] bytes);

    abstract public void handle(MessageHandler handler, MessageReader reader, SelectionKey key);
}
