package com.example.nabab.tourmate.PojoClass;

public class EventExpensesPojo {
    private String travelExpensesDetails;
    private String travelExpensesAmount;

    public EventExpensesPojo() {

    }

    public EventExpensesPojo(String travelExpensesDetails, String travelExpensesAmount) {
        this.travelExpensesDetails = travelExpensesDetails;
        this.travelExpensesAmount = travelExpensesAmount;
    }

    public String getTravelExpensesDetails() {
        return travelExpensesDetails;
    }

    public String getTravelExpensesAmount() {
        return travelExpensesAmount;
    }
}
