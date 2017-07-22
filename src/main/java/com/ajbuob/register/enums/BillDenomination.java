package com.ajbuob.register.enums;

public enum BillDenomination {

    ONE("one", 1),
    TWO("two", 2),
    FIVE("five", 5),
    TEN("ten", 10),
    TWENTY("twenty", 20);

    private String description;
    private int amount;

    BillDenomination(String description, int amount) {
        this.description = description;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public static BillDenomination findByAmount(Integer amount) {
        for (BillDenomination billDenomination : values()) {
            if (billDenomination.getAmount() == amount) {
                return billDenomination;
            }
        }
        throw new IllegalArgumentException(String.valueOf(amount));
    }
}
