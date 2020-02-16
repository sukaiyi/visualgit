package com.sukaiyi.visualgit.utils;

import com.sukaiyi.common.utils.MD5Utils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MD5UtilsTest {

    @Test
    void testCalc(){
        assertEquals("827ccb0eea8a706c4c34a16891f84e7b".toUpperCase(), MD5Utils.calc("12345"));
        assertEquals("e10adc3949ba59abbe56e057f20f883e".toUpperCase(), MD5Utils.calc("123456"));
        assertEquals("b10a8db164e0754105b7a99be72e3fe5".toUpperCase(), MD5Utils.calc("Hello World"));
    }
}