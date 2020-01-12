package com.staboss.convolutional_codes.decoder;

import com.staboss.convolutional_codes.model.Point;
import com.staboss.convolutional_codes.model.State;

import java.util.*;

/**
 * Декодер сверточного кода по алгоритму Витерби
 *
 * @author Boris Stasenko
 * @see State
 * @see Point
 */
public class Decoder {

    private Map<Integer, Set<Point>> layers;        //  слои решетки
    private Map<String, State> states;              //  состояния

    private String firstState;                      //  начальное состояние первой точки решетки
    private String[] vector;                        //  информационная последовательность
    private String format;                          //  формат хранимых состояний

    private Point lastPoint;                        //  последняя точка решетки

    private String[] pointSequence;                 //  полученный путь из точек
    private String[] decodedSequence;               //  полученная последовательность

    private int errors;                             //  количество ошибок в последовательности

    public Decoder(Map<String, State> states, String firstState, String[] vector) throws Exception {
        this.layers = new LinkedHashMap<>();
        this.states = states;

        this.firstState = firstState;
        this.vector = vector;

        int len = firstState.length();

        switch (len) {
            case 2:
                format = "%2s";
                break;
            case 3:
                format = "%3s";
                break;
            case 4:
                format = "%4s";
                break;
            case 5:
                format = "%5s";
                break;
            case 6:
                format = "%6s";
                break;
            default:
                format = null;
                break;
        }

        if (format == null) throw new Exception("Invalid Initial State Format");

        //  создание решетки
        createGrid();

        //  поиск пути
        findMinWay();
    }

    public Map<Integer, Set<Point>> getLayers() {
        return layers;
    }

    public String[] getPointSequence() {
        return pointSequence;
    }

    public String[] getDecodedSequence() {
        return decodedSequence;
    }

    public String getDecodedSequenceString() {
        return String.join("", decodedSequence);
    }

    public int getErrors() {
        return errors;
    }

    /**
     * Выводит сравнительный анализ полученной последовательности
     */
    private void findMinWay() {

        //  полученная информационная последовательность
        decodedSequence = new String[vector.length];

        //  зависимость точек решетки
        pointSequence = new String[vector.length];

        //  последняя точка решетки
        Point currentPoint = lastPoint.getPathFromPoint();

        //  в последней точке записано количество ошибок
        errors = lastPoint.getErrors();

        //  собираем путь
        for (int i = 0; i < vector.length; i++) {
            pointSequence[vector.length - i - 1] = currentPoint.getValue();
            currentPoint = currentPoint.getPathFromPoint();
        }

        //  собираем информационную последовательность
        for (int i = 0; i < decodedSequence.length; i++) {
            if (i + 1 < decodedSequence.length)
                decodedSequence[i] = getBranchValue(pointSequence[i], pointSequence[i + 1]);
            else
                decodedSequence[i] = getBranchValue(pointSequence[i], pointSequence[0]);
        }
    }

    /**
     * Строит решетку переходов состояний с количеством ошибок в каждой точке решетки
     */
    private void createGrid() {

        //  количество слоев -> (длина вектора / 2) + 1
        int l = vector.length + 1;
        int k = firstState.length();

        //  текущий слой решеткки
        Set<Point> currentLayer = new LinkedHashSet<>();

        //  будущий слой решетки
        Set<Point> nextLayer;

        //  множетсво для переходе в точку из двух точек
        Set<Point> points;

        //  выжившие состояния на решетке
        List<String> survivedStates;

        //  начальная точка на решетке
        State initialState = states.get(firstState);
        Point initialPoint = getOutPoint(firstState, 0, 0, initialState, null);

        //  начальный слой в решетке
        currentLayer.add(initialPoint);

        //  множество слоев решетки
        layers.put(0, currentLayer);

        //  количество выживших точек на последних слоях
        int c = (int) Math.pow(2, k) / 2;

        //  цикл по каждому слою
        for (int i = 1; i < l; i++) {

            //  инициализация будущего слоя
            nextLayer = new LinkedHashSet<>();

            //  заполняем слой в зависимости от номера слоя
            if (i >= (l - k)) {

                survivedStates = new ArrayList<>();

                //  проход по выжившим точкам
                for (int j = 0; j < c; j++) {
                    survivedStates.add(
                            String.format(format, Integer.toBinaryString(j)).replaceAll(" ", "0")
                    );
                }

                //  получаем предыдущий слой и делаем его текущим
                points = new HashSet<>(layers.get(i - 1));

                //  точки из предыдущего слоя совпадают с текущим слоем
                for (Point point : points) {

                    if (!survivedStates.contains(point.getValue())) continue;

                    //  получаем точку, в которую пришли две точки из предыдущего слоя
                    Point newPoint = getNewPoint(points, point, i);

                    if (c == 1) lastPoint = newPoint;

                    //  добавляем точку в следующий слой
                    nextLayer.add(newPoint);
                }

                //  уменьшаем количество выживших точек в 2 раза
                c /= 2;

            } else if (i < (l - k) && i > k) {

                //  получаем предыдущий слой и делаем его текущим
                points = new HashSet<>(layers.get(i - 1));

                //  точки из предыдущего слоя совпадают с текущим слоем
                for (Point point : points) {

                    //  получаем точку, в которую пришли две точки из предыдущего слоя
                    Point newPoint = getNewPoint(points, point, i);

                    //  добавляем точку в следующий слой
                    nextLayer.add(newPoint);
                }

                //  переходим к следующему слою
                currentLayer = new LinkedHashSet<>(nextLayer);

            } else {

                //  для каждой точки из текущего слоя
                for (Point point : currentLayer) {

                    //  получаем ветки, по которым точка выходит
                    String branch0 = point.getBranch0();
                    String branch1 = point.getBranch1();

                    //  получаем количество ошибок в текущей точке
                    int errors = point.getErrors();

                    //  получаем количество ошибок на в новых точках
                    int error0 = getErrors(vector[i - 1], point.getBranchValue0()) + errors;
                    int error1 = getErrors(vector[i - 1], point.getBranchValue1()) + errors;

                    //  добавляем точки в следующий слой
                    Point point0 = getOutPoint(branch0, error0, i, states.get(branch0), point);
                    Point point1 = getOutPoint(branch1, error1, i, states.get(branch1), point);

                    nextLayer.add(point0);
                    nextLayer.add(point1);
                }

                //  переходим к следующему слою
                currentLayer = new LinkedHashSet<>(nextLayer);
            }

            //  добавляем уровень и слой в множество слоев
            layers.put(i, nextLayer);
        }
    }

    /**
     * Возвращает точку, в которую пришли две точки из предыдущего слоя
     *
     * @param points множество точек
     * @param point  точка на решетке
     * @param i      слой
     * @return точка на решетке
     */
    private Point getNewPoint(Set<Point> points, Point point, int i) {
        //  в каждую точку приходят две другие точки (состояния точек)
        State inState1 = states.get(states.get(point.getValue()).getInState1());
        State inState2 = states.get(states.get(point.getValue()).getInState2());

        //  пришедшие точки
        Point inPoint1 = getInPoint(points, inState1);
        Point inPoint2 = getInPoint(points, inState2);

        //  количество ошибок в точках
        int errorsFromInState1 = inPoint1.getErrors();
        int errorsFromInState2 = inPoint2.getErrors();

        //  количество ошибок в новой точке
        int error1, error2;

        //  определяем по 0 или 1 пришли точки
        if (Integer.parseInt(point.getValue(), 2) >= (states.size() / 2)) {
            error1 = getErrors(vector[i - 1], inState1.getOutOne()) + errorsFromInState1;
            error2 = getErrors(vector[i - 1], inState2.getOutOne()) + errorsFromInState2;
        } else {
            error1 = getErrors(vector[i - 1], inState1.getOutZero()) + errorsFromInState1;
            error2 = getErrors(vector[i - 1], inState2.getOutZero()) + errorsFromInState2;
        }

        //  итоговое количество ошибок, которое будет записано
        int finalError;

        //  полученная точка
        Point newPoint = new Point(
                point.getValue(),
                i,
                point.getBranch0(),
                point.getBranch1()
        );

        //  устанавляваем оптимальный пуль и количество ошибок для новой точки
        if (error1 < error2) {
            finalError = error1;
            newPoint.setErrors(finalError);
            newPoint.setPathFromPoint(inPoint1);
        } else {
            finalError = error2;
            newPoint.setErrors(finalError);
            newPoint.setPathFromPoint(inPoint2);
        }

        //  устанавляваем значения С1 и С2 для переходов по 0 и 1
        newPoint.setBranchValue0(point.getBranchValue0());
        newPoint.setBranchValue1(point.getBranchValue1());

        return newPoint;
    }

    /**
     * Возвращает значение на ветке перехода
     *
     * @param from точка выхода
     * @param to   точка входа
     * @return значение ветки
     */
    private String getBranchValue(String from, String to) {
        State state = states.get(from);
        if (state.getOutStateZero().equals(to)) return state.getOutZero();
        else return state.getOutOne();
    }

    /**
     * Возвращает количество ошибок от 0 до 2
     *
     * @param s1 значение вектора
     * @param s2 значение ветки
     * @return количество ошибок
     */
    private int getErrors(String s1, String s2) {
        int count = 0;
        if (s1.charAt(0) != s2.charAt(0)) count++;
        if (s1.charAt(1) != s2.charAt(1)) count++;
        return count;
    }

    /**
     * В зависимости от состояния возвращает точку на решетке
     *
     * @param points  множество точек
     * @param inState состояние точки
     * @return новая входящая точка
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private Point getInPoint(Set<Point> points, State inState) {
        return points.stream().filter(p -> Objects.equals(p.getValue(), inState.getState())).findFirst().get();
    }

    /**
     * Создает новую выходящую точку
     *
     * @param branch значение точки
     * @param errors количество ошибок
     * @param level  уровень слоя
     * @param state  состояние
     * @param point  родительская точка
     * @return новая выходящая точка
     */
    private Point getOutPoint(String branch, int errors, int level, State state, Point point) {
        if (point == null)
            return new Point(
                    branch, errors, level,
                    state.getOutStateZero(), state.getOutStateOne(), state.getOutZero(), state.getOutOne()
            );
        else
            return new Point(
                    branch, errors, level,
                    state.getOutStateZero(), state.getOutStateOne(), state.getOutZero(), state.getOutOne(),
                    point
            );
    }
}
