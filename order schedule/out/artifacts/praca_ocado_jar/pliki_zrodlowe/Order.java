package com.praca;
import java.math.BigDecimal;
        import java.time.Duration;
        import java.time.LocalTime;

public class Order {
    private String orderId;
    private BigDecimal orderValue;
    private Duration pickingTime;
    private LocalTime completeBy;
    private LocalTime startTimeAssigned;

    public Order(String orderId, BigDecimal orderValue, Duration pickingTime, LocalTime completeBy) {
        this.orderId = orderId;
        this.orderValue = orderValue;
        this.pickingTime = pickingTime;
        this.completeBy = completeBy;
    }

    public String getOrderId() {
        return orderId;
    }
    public BigDecimal getOrderValue() {
        return orderValue;
    }
    public Duration getPickingTime() {
        return pickingTime;
    }
    public LocalTime getCompleteBy() {
        return completeBy;
    }


    public LocalTime getStartTimeAssigned() {
        return startTimeAssigned;
    }

    public void setStartTimeAssigned(LocalTime startTimeAssigned) {
        this.startTimeAssigned = startTimeAssigned;
    }
}
