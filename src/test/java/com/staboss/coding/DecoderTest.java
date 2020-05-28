package com.staboss.coding;

import com.staboss.coding.decoder.Decoder;
import com.staboss.coding.model.CoderState;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DecoderTest {

    private static Map<String, CoderState> states;
    private static String[] vector;

    private static final String firstState = "0000";

    @BeforeClass
    public static void init() {
        states = new LinkedHashMap<>();
        vector = new String[]{"11", "01", "00", "10", "11", "10", "01", "11", "11", "11", "01", "11", "00", "00"};

        CoderState state00 = new CoderState("0000", "00", "11", "0000", "1000", "0000", "0001");
        CoderState state01 = new CoderState("0001", "11", "00", "0000", "1000", "0010", "0011");
        CoderState state02 = new CoderState("0010", "01", "10", "0001", "1001", "0100", "0101");
        CoderState state03 = new CoderState("0011", "10", "01", "0001", "1001", "0110", "0111");
        CoderState state04 = new CoderState("0100", "01", "10", "0010", "1010", "1000", "1001");
        CoderState state05 = new CoderState("0101", "10", "01", "0010", "1010", "1011", "1101");
        CoderState state06 = new CoderState("0110", "00", "11", "0011", "1011", "1100", "1101");
        CoderState state07 = new CoderState("0111", "11", "00", "0011", "1011", "1110", "1111");
        CoderState state08 = new CoderState("1000", "10", "01", "0100", "1100", "0000", "0001");
        CoderState state09 = new CoderState("1001", "01", "10", "0100", "1100", "0010", "0011");
        CoderState state10 = new CoderState("1010", "11", "00", "0101", "1101", "0100", "0101");
        CoderState state11 = new CoderState("1011", "00", "11", "0101", "1101", "0110", "0111");
        CoderState state12 = new CoderState("1100", "11", "00", "0110", "1110", "1000", "1001");
        CoderState state13 = new CoderState("1101", "00", "11", "0110", "1110", "1010", "1011");
        CoderState state14 = new CoderState("1110", "10", "01", "0111", "1111", "1100", "1101");
        CoderState state15 = new CoderState("1111", "01", "10", "0111", "1111", "1110", "1111");

        states.put(state00.getState(), state00);
        states.put(state01.getState(), state01);
        states.put(state02.getState(), state02);
        states.put(state03.getState(), state03);
        states.put(state04.getState(), state04);
        states.put(state05.getState(), state05);
        states.put(state06.getState(), state06);
        states.put(state07.getState(), state07);
        states.put(state08.getState(), state08);
        states.put(state09.getState(), state09);
        states.put(state10.getState(), state10);
        states.put(state11.getState(), state11);
        states.put(state12.getState(), state12);
        states.put(state13.getState(), state13);
        states.put(state14.getState(), state14);
        states.put(state15.getState(), state15);
    }


    @Test
    public void findMinWayTest() throws Exception {
        Decoder decoder = new Decoder(states, firstState, vector);
        assertEquals("1101001011101111100101110000", decoder.getDecodedSequenceString());
        assertEquals(3, decoder.getErrors(), 0);
    }
}
