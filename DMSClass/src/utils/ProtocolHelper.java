package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import enums.Bool;

public final class ProtocolHelper {
    public static byte[] serialization(final Serializable obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(obj);
                return baos.toByteArray();
            }
        }
    }

    public static Serializable deserialization(final byte[] bytes) throws ClassNotFoundException, IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                final Object obj = ois.readObject();
                return (Serializable) obj;
            }
        }
    }

    static byte[] shortToByte(short input){
        byte[] result = new byte[2];
        result[0] = (byte) ((input >> 8) & 0xFF);
        result[1] = (byte) (input & 0xFF);
        return result;
    }
    static short bytesToShort(byte[] input){
        return (short) (input[0] << 8 | (input[1] & 0xFF));
    }
    static short bytesToShort(byte a, byte b){
        return (short) (a << 8 | (b & 0xFF));
    }

    static List<byte[]> splitBySize(final byte[] bytes, final int chunk_size) {
        final List<byte[]> result = new ArrayList<>();
        for (int i = 0; i < bytes.length; i += chunk_size) {
            result.add(Arrays.copyOfRange(bytes, i, Math.min(bytes.length, i + chunk_size)));
        }
        return result;
    }

    static List<Protocol> split(final Protocol protocol, final int size_to_split) throws IOException {
        List<Protocol> result = new ArrayList<>();
        byte[] tmp = protocol.getBody();
        List<byte[]> body_chunks = splitBySize(tmp, size_to_split - Protocol.HEADER_LENGTH);

        int body_chunks_size = body_chunks.size();
        short seq = 0;
        Bool isLast = Bool.get(false);

        for (; seq < body_chunks_size; ++seq) {
            if (seq + 1 == body_chunks_size)
                isLast = Bool.get(true);
            result.add(new Protocol.Builder(protocol.type, protocol.direction, protocol.code1, protocol.code2)
                    .body(body_chunks.get(seq)).sequence(seq, isLast).build());
        }

        return result;
    }

    // 테스트 안됨!
    static Protocol merge(final byte[] packet) throws IOException, Exception {
        List<Protocol> tmp = new ArrayList<>();

        int chunk_size = 0;
        for(int cursor = 0; cursor < packet.length; cursor += chunk_size){
            chunk_size = bytesToShort(packet[cursor], packet[cursor + 1]);
            tmp.add(new Protocol.Builder(Arrays.copyOfRange(packet, cursor, chunk_size)).build());
        }

        return merge(tmp);
    }

    static Protocol merge(List<Protocol> protocols) throws IOException {
        protocols.sort(null);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (Protocol protocol : protocols) {
            output.write(protocol.getBody());
        }

        return new Protocol.Builder(protocols.get(0).type, protocols.get(0).direction, protocols.get(0).code1, protocols.get(0).code2).body(output.toByteArray()).build();
    }
}