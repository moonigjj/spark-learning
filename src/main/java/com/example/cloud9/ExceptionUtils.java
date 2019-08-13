/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.cloud9;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 *
 * @author tangyue
 * @version $Id: ExceptionUtils.java, v 0.1 2019-08-13 14:09 tangyue Exp $$
 */
public final class ExceptionUtils {

    public static String getStackTrace(Exception e) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(bytes);
        e.printStackTrace(stream);

        return new String(bytes.toByteArray());
    }
}
