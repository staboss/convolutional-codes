package com.staboss.convolutional_codes.model;

/**
 * Точка на решетке
 */
public class Point {

    private String value;           //  значение точки
    private int errors;             //  количество ошибок
    private int level;              //  уровень слоя

    private String branch0;         //  путь по branch0
    private String branch1;         //  путь по branch1

    private String branchValue0;    //  значение на branch0
    private String branchValue1;    //  значение на branch1

    private Point pathFromPoint;    //  из какой точки получилась эта точка

    public Point(String value, int level,
                 String branch0, String branch1) {
        this.value = value;
        this.level = level;
        this.branch0 = branch0;
        this.branch1 = branch1;
    }

    public Point(String value, int errors, int level,
                 String branch0, String branch1,
                 String branchValue0, String branchValue1) {
        this(value, level, branch0, branch1);
        this.errors = errors;
        this.branchValue0 = branchValue0;
        this.branchValue1 = branchValue1;
    }

    public Point(String value, int errors, int level,
                 String branch0, String branch1,
                 String branchValue0, String branchValue1,
                 Point pathFromPoint) {
        this(value, errors, level, branch0, branch1, branchValue0, branchValue1);
        this.pathFromPoint = pathFromPoint;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getBranchValue0() {
        return branchValue0;
    }

    public void setBranchValue0(String branchValue0) {
        this.branchValue0 = branchValue0;
    }

    public String getBranchValue1() {
        return branchValue1;
    }

    public void setBranchValue1(String branchValue1) {
        this.branchValue1 = branchValue1;
    }

    public String getBranch0() {
        return branch0;
    }

    public void setBranch0(String branch0) {
        this.branch0 = branch0;
    }

    public String getBranch1() {
        return branch1;
    }

    public void setBranch1(String branch1) {
        this.branch1 = branch1;
    }

    public Point getPathFromPoint() {
        return pathFromPoint;
    }

    public void setPathFromPoint(Point pathFromPoint) {
        this.pathFromPoint = pathFromPoint;
    }

    @Override
    public String toString() {
        return String.format("%s : %d", value, errors);
    }
}
