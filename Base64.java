package plugins;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Base64  implements Plugin{
    private final String fileExtension = ".b64";

    private String encode(byte[] data) {
        char[] tbl = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
                'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
        };

        StringBuilder str = new StringBuilder();
        int offset = 0;
        for (int i = 0; i < data.length; i += 3) {

            int buf = ((data[i] & 0xFF) << 16) & 0xFFFFFF;
            if (i + 1 < data.length) {
                buf |= (data[i + 1] & 0xFF) << 8;
            } else {
                offset++;
            }
            if (i + 2 < data.length) {
                buf |= (data[i + 2] & 0xFF);
            } else {
                offset++;
            }

            for (int j = 0; j < 4 - offset; j++) {
                int c = (buf & 0x00FC0000) >> 18;
                str.append(tbl[c]);
                buf <<= 6;
            }
        }
        for (int j = 0; j < offset; j++) {
            str.append("=");
        }

        return str.toString();
    }

    private byte[] decode(byte[] bytes) {
        int[] tbl = {
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
                52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
                -1,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
                15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
                -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
                41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int i = 0; i < bytes.length; ) {
            int buf = 0;
            if (tbl[bytes[i]] == -1) {
                // skip unknown characters
                i++;
                continue;
            } else {
                buf = (tbl[bytes[i]] & 0xFF) << 18;
            }

            int num = 0;
            if (i + 1 < bytes.length && tbl[bytes[i+1]] != -1) {
                buf = buf | ((tbl[bytes[i+1]] & 0xFF) << 12);
                num++;
            }
            if (i + 2 < bytes.length && tbl[bytes[i+2]] != -1) {
                buf = buf | ((tbl[bytes[i+2]] & 0xFF) << 6);
                num++;
            }
            if (i + 3 < bytes.length && tbl[bytes[i+3]] != -1) {
                buf = buf | (tbl[bytes[i+3]] & 0xFF);
                num++;
            }

            while (num > 0) {
                int c = (buf & 0x00FF0000) >> 16;
                buffer.write((char)c);
                buf <<= 8;
                num--;
            }
            i += 4;
        }
        return buffer.toByteArray();
    }

    @Override
    public void save(byte[] data, String fileNameWithoutExtension) throws IOException {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileNameWithoutExtension + fileExtension))){
            bufferedWriter.write(encode(data));
            bufferedWriter.flush();
        }
    }

    @Override
    public byte[] load(String fileNameWithExtension) throws IllegalArgumentException, IOException {
        if (!fileNameWithExtension.substring(fileNameWithExtension.lastIndexOf(".")).equals(fileExtension)){
            throw new IllegalArgumentException("Wrong fileExtension");
        }
        return decode(Files.readAllBytes(Paths.get(fileNameWithExtension)));
    }
}
