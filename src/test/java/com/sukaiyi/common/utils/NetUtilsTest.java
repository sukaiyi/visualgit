package com.sukaiyi.common.utils;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NetUtilsTest {

    @Test
    void choosePort() {
        int port = NetUtils.choosePort(80, 81, 82);
        assertTrue(port > 0);
    }
}