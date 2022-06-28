package com.example.resto;

public class Table {
    private int id;
    private boolean isBooked;
    private String clientName;
    private long clientPhone;
    private String bookingTime;
    private int tableNumber;
    public Table(int id, boolean isBooked, String clientName, long clientNumber, String bookingTime,int tableNumber) {
        this.id = id;
        this.isBooked = isBooked;
        this.clientName = clientName;
        this.clientPhone = clientNumber;
        this.bookingTime = bookingTime;
        this.tableNumber = tableNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public boolean getIsBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public long getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(long clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    @Override
    public String toString() {
        return "Table{" +
                "id=" + id +
                ", isBooked=" + isBooked +
                ", clientName='" + clientName + '\'' +
                ", clientNumber=" + clientPhone +
                ", bookingTime='" + bookingTime + '\'' +
                '}';
    }
}
