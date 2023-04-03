package com.praca;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Picker {
    private final String id;
    private LocalTime startTime;
    private final LocalTime endTime;
    private final List<Order> orders;


    public Picker(String id, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.orders = new ArrayList<Order>();
    }

    public String getId() {
        return id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public boolean isAvailable(Order order) {
        LocalTime finishTime = startTime.plus(order.getPickingTime());
        return finishTime.isBefore(endTime) || finishTime.equals(endTime);
    }

    public void addOrder(Order order) {
        order.setStartTimeAssigned(this.startTime);
        orders.add(order);
        startTime = startTime.plus(order.getPickingTime());

    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Order order : orders) {
            sb.append(id).append(" ").append(order.getOrderId()).append(" ").append(startTime.plus(order.getPickingTime())).append("\n");
        }
        return sb.toString();
    }
}
