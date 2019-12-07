package com.staboss.convolutional_codes;

import com.staboss.convolutional_codes.coder.Coder;
import com.staboss.convolutional_codes.coder.NonSystematicCoder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoderTest {

    @Test
    public void encodeTest() throws Exception {
        String g1 = "1101";
        String g2 = "1011";

        Coder coder = new NonSystematicCoder(g1, g2);
        String encoded = coder.encode("10101");

        assertEquals("1110100110010111", encoded);
    }
}
