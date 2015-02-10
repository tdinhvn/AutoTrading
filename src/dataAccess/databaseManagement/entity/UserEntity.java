package dataAccess.databaseManagement.entity;

import java.io.Serializable;

public class UserEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8627210132728983694L;
    private long userID;
    private String name;
    private double cash;
    private double cash01;
    private double cash02;
    private double cash03;

    public UserEntity() {
    }

    public UserEntity(String name, double cash, double cash01, double cash02, double cash03) {
        this.name = name;
        this.cash = cash;
        this.cash01 = cash01;
        this.cash02 = cash02;
        this.cash03 = cash03;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getUserID() {
        return userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getCash() {
        return cash;
    }

    public double getCash01() {
        return cash01;
    }

    public void setCash01(double cash01) {
        this.cash01 = cash01;
    }

    public double getCash02() {
        return cash02;
    }

    public void setCash02(double cash02) {
        this.cash02 = cash02;
    }

    public double getCash03() {
        return cash03;
    }

    public void setCash03(double cash03) {
        this.cash03 = cash03;
    }
}
