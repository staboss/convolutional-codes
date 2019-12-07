package com.staboss.convolutional_codes.model;

import java.util.Objects;

/**
 * Состояния сверточного кода
 */
public class State {

    private String state;           //  состояние

    private String outZero;         //  С1 и С2 при 0
    private String outOne;          //  С1 и С2 при 1

    private String outStateZero;    //  новое состояние после сдвига регистра на 0
    private String outStateOne;     //  новое состояние после сдвига регистра на 1

    private String inState1;        //  состояние до сдвига
    private String inState2;        //  состояние до сдвига

    public State(String state) {
        this.state = state;
    }

    public State(String state,
                 String outZero, String outOne,
                 String outStateZero, String outStateOne,
                 String inState1, String inState2) {
        this.state = state;
        this.outZero = outZero;
        this.outOne = outOne;
        this.outStateZero = outStateZero;
        this.outStateOne = outStateOne;
        this.inState1 = inState1;
        this.inState2 = inState2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOutStateZero() {
        return outStateZero;
    }

    public void setOutStateZero(String outStateZero) {
        this.outStateZero = outStateZero;
    }

    public String getOutStateOne() {
        return outStateOne;
    }

    public void setOutStateOne(String outStateOne) {
        this.outStateOne = outStateOne;
    }

    public String getOutZero() {
        return outZero;
    }

    public void setOutZero(String outZero) {
        this.outZero = outZero;
    }

    public String getOutOne() {
        return outOne;
    }

    public void setOutOne(String outOne) {
        this.outOne = outOne;
    }

    public String getInState1() {
        return inState1;
    }

    public void setInState1(String inState1) {
        this.inState1 = inState1;
    }

    public String getInState2() {
        return inState2;
    }

    public void setInState2(String inState2) {
        this.inState2 = inState2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state1 = (State) o;
        return Objects.equals(state, state1.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }

    @Override
    public String toString() {
        return String.format("%s:\t0|%s|%s\t1|%s|%s\t{%s, %s}",
                state,
                outZero,
                outStateZero,
                outOne,
                outStateOne,
                inState1,
                inState2
        );
    }
}
