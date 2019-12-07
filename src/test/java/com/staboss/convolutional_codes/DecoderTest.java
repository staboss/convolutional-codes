package com.staboss.convolutional_codes;

import com.staboss.convolutional_codes.decoder.Decoder;
import com.staboss.convolutional_codes.model.State;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DecoderTest {

    private static Map<String, State> states;
    private static String[] vector;

    private static final String firstState = "0000";

    @BeforeClass
    public static void init() {
        states = new LinkedHashMap<>();
        vector = new String[]{"11", "01", "00", "10", "11", "10", "01", "11", "11", "11", "01", "11", "00", "00"};

        State state00 = new State("0000", "00", "11", "0000", "1000", "0000", "0001");
        State state01 = new State("0001", "11", "00", "0000", "1000", "0010", "0011");
        State state02 = new State("0010", "01", "10", "0001", "1001", "0100", "0101");
        State state03 = new State("0011", "10", "01", "0001", "1001", "0110", "0111");
        State state04 = new State("0100", "01", "10", "0010", "1010", "1000", "1001");
        State state05 = new State("0101", "10", "01", "0010", "1010", "1011", "1101");
        State state06 = new State("0110", "00", "11", "0011", "1011", "1100", "1101");
        State state07 = new State("0111", "11", "00", "0011", "1011", "1110", "1111");
        State state08 = new State("1000", "10", "01", "0100", "1100", "0000", "0001");
        State state09 = new State("1001", "01", "10", "0100", "1100", "0010", "0011");
        State state10 = new State("1010", "11", "00", "0101", "1101", "0100", "0101");
        State state11 = new State("1011", "00", "11", "0101", "1101", "0110", "0111");
        State state12 = new State("1100", "11", "00", "0110", "1110", "1000", "1001");
        State state13 = new State("1101", "00", "11", "0110", "1110", "1010", "1011");
        State state14 = new State("1110", "10", "01", "0111", "1111", "1100", "1101");
        State state15 = new State("1111", "01", "10", "0111", "1111", "1110", "1111");

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
