package com.staboss.convolutional_codes.coder;

import com.staboss.convolutional_codes.model.State;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Кодер сверточного кода
 *
 * @author Boris Stasenko
 * @see State
 */
public abstract class Coder {

    private Map<String, State> states;      //  последовательности переходов

    private String format;                  //  формат хранимых состояний

    private char[] g1;                      //  полином 1
    private char[] g2;                      //  полином 2

    private int maxPow;                     //  максимальная степень полинома
    private int stateCount;                 //  количество состояний

    Coder(String g1, String g2) throws Exception {
        this.states = new LinkedHashMap<>();

        this.g1 = g1.toCharArray();
        this.g2 = g2.toCharArray();

        this.maxPow = Math.max(g1.length() - 1, g2.length() - 1);
        this.stateCount = (int) Math.pow(2, maxPow);

        setStates(stateCount);
        setOuts();
    }

    public Map<String, State> getStates() {
        return states;
    }

    public int getMaxPow() {
        return maxPow;
    }

    public int getStateCount() {
        return stateCount;
    }

    char[] getG1() {
        return g1;
    }

    char[] getG2() {
        return g2;
    }

    /**
     * Определяет состояния на выходе после сдвига регистра на 0 или 1 для каждого сотояния
     *
     * @param state состояние
     * @param u     входной бит 0 или 1
     */
    protected abstract void code(State state, int u) throws Exception;

    /**
     * Составляет последовательность переходов
     *
     * @param stateCount количество состояний
     * @throws Exception если передано недопустимое количество состояний
     */
    private void setStates(int stateCount) throws Exception {
        //  доступные форматы : '00', '000', '0000', '00000', '000000'
        format = (stateCount == 4) ? "%2s" : (stateCount == 8) ? "%3s" : (stateCount == 16) ? "%4s" : (stateCount == 32) ? "%5s" : (stateCount == 64) ? "%6s" : null;
        if (format == null) throw new Exception("Invalid Number of States");

        for (int i = 0; i < stateCount; i++) {
            String value = String.format(format, Integer.toBinaryString(i)).replaceAll(" ", "0");
            states.put(value, new State(value));
        }
    }

    /**
     * Для каждого состояния получает С1 и С2 при передаче 0 и 1
     */
    private void setOuts() {
        states.forEach((value, state) -> {
            try {
                code(state, 0);
                code(state, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Кодирует входную информационную последовательность
     *
     * @param u информационная последовательность
     * @return статистически зависимая информационная последовательность
     */
    public String encode(String u) {
        //  результат кодера
        StringBuilder sb = new StringBuilder();

        //  запоминаем значение нулевого состояния
        String firstState = String.format(format, Integer.toBinaryString(0)).replaceAll(" ", "0");

        //  текущее состояние перехода
        State currentState = states.get(firstState);

        //  "101" -> {'1', '0', '1'}
        char[] seq = u.toCharArray();

        for (char s : seq) {
            if (s == '0') {
                sb.append(currentState.getOutZero());
                currentState = states.get(currentState.getOutStateZero());
            } else if (s == '1') {
                sb.append(currentState.getOutOne());
                currentState = states.get(currentState.getOutStateOne());
            }
        }

        //  переводим регистр сдвига в нулевое состояние
        for (int i = 0; i < firstState.length(); i++) {
            sb.append(currentState.getOutZero());
            currentState = states.get(currentState.getOutStateZero());
        }

        return sb.toString();
    }

    /**
     * Возвращает указанный кодер
     *
     * @param type тип кодера
     * @param g1   полином 1
     * @param g2   полином 2
     * @return нужный кодер
     * @throws Exception если ошибка входных данных
     */
    public static Coder getCoder(String type, String g1, String g2) throws Exception {
        return type.equals("-s") ? new SystematicCoder(g1, g2) : new NonSystematicCoder(g1, g2);
    }
}
