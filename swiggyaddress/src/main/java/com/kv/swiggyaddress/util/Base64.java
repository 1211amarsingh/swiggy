package com.kv.swiggyaddress.util;

import java.nio.charset.StandardCharsets;

public class Base64 {

    public static String encode(String text) {
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        return android.util.Base64.encodeToString(data, android.util.Base64.DEFAULT);
    }

    public static String decode(String base64) {
        byte[] data1 = android.util.Base64.decode(base64, android.util.Base64.DEFAULT);
        return new String(data1, StandardCharsets.UTF_8);
    }
}
