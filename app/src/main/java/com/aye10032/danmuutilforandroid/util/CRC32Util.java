package com.aye10032.danmuutilforandroid.util;

public class CRC32Util {

    private static long CRCPOLYNOMIAL = 0xEDB88320L;
    private long[] crctable = new long[256];

    public CRC32Util() {
        for (int i = 0; i < 256; i++) {
            long crcreg = i;

            for (int x = 0; x < 8; x++) {
                if ((crcreg & 1) != 0) {
                    crcreg = CRCPOLYNOMIAL ^ (crcreg >> 1);
                } else {
                    crcreg = crcreg >> 1;
                }
            }
            crctable[i] = crcreg;
        }
    }

    public String solve(String string) {
        int[] index = new int[]{0, 0, 0, 0};
        int i = 0;

        String[] deepCheckData = new String[2];

        long ht = Long.parseLong(string, 16) ^ 0xffffffffL;

        for (i = 3; i > -1; i--) {
            index[3 - i] = getCrcIndex(ht >> (i * 8));
            long snum = crctable[index[3 - i]];
            ht ^= snum >> ((3 - i) * 8);
        }
        for (i = 0; i < 100000000; i++) {
            int lastindex = crc32LastIndex(i + "");
            if (lastindex == index[3]) {
                deepCheckData = deepCheck(i + "", index);
                if (deepCheckData.length == 2) {
                    break;
                }
            }
        }
        if (i == 100000000) {
            return "-1";
        }

        return i + "" + deepCheckData[1];
    }

    private int getCrcIndex(long t) {
        for (int i = 0; i < 256; i++) {
            if (crctable[i] >> 24 == t) {
                return i;
            }
        }
        return -1;
    }

    private int crc32LastIndex(String string) {
        long crcstart = 0xFFFFFFFFL;
        long index = 0;
        for (int i = 0; i < string.length(); i++) {
            index = ((long) (crcstart ^ (long) (string.charAt(i))) & 255L);
            crcstart = (crcstart >> 8) ^ crctable[(int) index];
        }

        return (int) index;
    }

    private long crc32(String string) {
        long crcstart = 0xFFFFFFFFL;
        long index = 0;
        for (int i = 0; i < string.length(); i++) {
            index = ((long) (crcstart ^ (long) (string.charAt(i))) & 255L);
            crcstart = (crcstart >> 8) ^ crctable[(int) index];
        }

        return crcstart;
    }

    private String[] deepCheck(String i, int[] index) {
        String string = "";
        int ct = 0x00;
        long hashcode = crc32(i);

        long tc = hashcode & 0xff ^ index[2];
        if (!(tc <= 57 && tc >= 48)) {
            return new String[]{"0"};
        }
        string += (tc - 48);
        hashcode = crctable[index[2]] ^ (hashcode >> 8);
        tc = hashcode & 0xff ^ index[1];

        if (!(tc <= 57 && tc >= 48)) {
            return new String[]{"0"};
        }
        string += (tc - 48);
        hashcode = crctable[index[1]] ^ (hashcode >> 8);
        tc = hashcode & 0xff ^ index[0];

        if (!(tc <= 57 && tc >= 48)) {
            return new String[]{"0"};
        }
        string += (tc - 48);
        hashcode = crctable[index[1]] ^ (hashcode >> 8);

        return new String[]{"1", string};
    }

}
