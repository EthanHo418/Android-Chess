package com.example.android60;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
import com.example.android60.chess.model.BackendTester;
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void adHoc() {
        System.out.println("REAL OUTPUT STARTS HERE");
        BackendTester.run();
        Assert.assertTrue(true);
    }
}