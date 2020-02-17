package com.sukaiyi.common.utils;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BrowserUtilsTest {

    @Test
    void open() throws Exception {
        BrowserUtils.open("https://www.baidu.com");
    }
}