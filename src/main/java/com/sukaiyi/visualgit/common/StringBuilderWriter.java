package com.sukaiyi.visualgit.common;

import java.io.Writer;

/**
 * @author sukaiyi
 * @date 2020/01/17
 */
public class StringBuilderWriter extends Writer {

    private StringBuilder sb = null;

    public StringBuilderWriter(StringBuilder buffer) {
        this.sb = buffer;
    }

    @Override
    public void write(char[] chars, int offset, int length) {
        sb.append(chars, offset, length);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }
}
