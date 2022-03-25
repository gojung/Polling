package com.polling.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtils {
    /**
     * Random 6 digit number
     */
    public static String generateAuthNo4() {
        return RandomStringUtils.randomNumeric(6);
    }
}
