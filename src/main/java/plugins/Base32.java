package plugins;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Base32 implements Plugin {
    private final String fileExtension = ".b32";

    @Override
    public byte[] convert(byte[] data){
        return encode(data).getBytes();
    }
    @Override
    public byte[] revert(byte[] data) {
        return decode(data);
    }

    @Override
    public String getFileExtension() {
        return fileExtension;
    }

    private String encode(byte[] data){
        char[] tbl = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7'
        };
        StringBuilder str = new StringBuilder();
        int offset = 0;

        for (int i = 0; i < data.length; i += 5) {
            long buf = (((long)(data[i] & 0xFF)) << 32) & 0x00_00_00_FF__FF_FF_FF_FFL;

            for (int j = 1; j < 5; j++) {
                if (i + j < data.length) {
                    buf |= (((long)(data[i + j] & 0xFF)) << (8 * (4 - j)));
                } else {
                    offset++;
                }
            }

            for (int j = 0; j < 8 - offset; j++) {
                int c = (int) ((buf & 0x00_00_00_F8__00_00_00_00L) >> 35);
                str.append(tbl[c]);
                buf <<= 5;
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
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, -1, -1, -1, -1,
                -1,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
                15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
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
            long buf = 0;
            if (tbl[bytes[i]] == -1) {
                // skip unknown characters
                i++;
                continue;
            }
            int offset = 0;

            for (int j = 0; j < 8; j++) {
                if (i + j < bytes.length && tbl[bytes[i + j]] != -1) {
                    buf = buf | (((long)(tbl[bytes[i + j] & 0xFF])) << (5 * (7 - j)));
                    offset++;
                }

            }

            for (int j = 0; j < 5 + (offset - 8); j++) {
                int c = (int) ((buf & 0x00_00_00_FF__00_00_00_00L) >> 32);
                buffer.write((char) c);
                buf <<= 8;
            }
            i += 8;
        }
        return buffer.toByteArray();
    }

}
