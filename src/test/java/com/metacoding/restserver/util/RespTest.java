package com.metacoding.restserver.util;

import com.metacoding.restserver._core.util.Resp;
import org.junit.jupiter.api.Test;

public class RespTest {

    @Test
    public void test() {
        String data = "data";
        Resp resp = new Resp(true, "성공", data);
    }
}
