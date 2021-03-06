package com.ajbuob.register;

import com.ajbuob.register.enums.BillDenomination;
import com.ajbuob.register.exception.InsufficientFundsException;
import com.ajbuob.register.strategy.ChangeStrategy;
import com.ajbuob.register.strategy.OptimalChangeStrategy;
import com.google.common.base.Objects;

import java.util.Map;
import java.util.TreeMap;

public class CashRegisterImpl implements CashRegister {

    private static final Integer ZERO = 0;

    private TreeMap<BillDenomination, Integer> register = null;

    private ChangeStrategy changeStrategy = null;
    private static final ChangeStrategy DEFAULT_CHANGE_STRATEGY = new OptimalChangeStrategy();

    public CashRegisterImpl() {
        this(DEFAULT_CHANGE_STRATEGY);
    }

    public CashRegisterImpl(ChangeStrategy changeStrategy) {
        initRegister();
        this.changeStrategy = changeStrategy;
    }

    private void initRegister() {
        //Initialize an empty register
        this.register = new TreeMap<>();
        register.put(BillDenomination.ONE, ZERO);
        register.put(BillDenomination.TWO, ZERO);
        register.put(BillDenomination.FIVE, ZERO);
        register.put(BillDenomination.TEN, ZERO);
        register.put(BillDenomination.TWENTY, ZERO);
    }

    @Override
    public void addBillsToRegister(Map<BillDenomination, Integer> creditBills) {
        //Validate the input
        if (!canAddAllBillsToRegister(creditBills)) {
            throw new IllegalArgumentException("Cant add this quantity of bills to the register");
        }

        //Iterate over input and add each denomination and quantity to the register
        for (Map.Entry<BillDenomination, Integer> entry : creditBills.entrySet()) {
            addBillToRegister(entry.getKey(), entry.getValue());
        }
    }

    private void addBillToRegister(BillDenomination bill, Integer quantity) {
        //Add the quantity to the current value in the register
        register.put(bill, register.get(bill) + quantity);
    }

    private boolean canAddAllBillsToRegister(Map<BillDenomination, Integer> creditBills) {
        //Iterate over the Map and look for any negative values in user input
        for (Map.Entry<BillDenomination, Integer> entry : creditBills.entrySet()) {
            if (entry.getValue() < ZERO) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void removeBillsFromRegister(Map<BillDenomination, Integer> debitBills) {
        //Validate the input
        if (!canRemoveAllBillsFromRegister(debitBills)) {
            throw new IllegalArgumentException("Cant remove this quantity of bills to the register");
        }
        //Iterate over input and remove each denomination and quantity from the register
        for (Map.Entry<BillDenomination, Integer> entry : debitBills.entrySet()) {
            removeBillFromRegister(entry.getKey(), entry.getValue());
        }
    }

    private void removeBillFromRegister(BillDenomination bill, Integer quantity) {
        //Remove the quantity from the current value in the register
        register.put(bill, register.get(bill) - quantity);
    }

    private boolean canRemoveAllBillsFromRegister(Map<BillDenomination, Integer> debitBills) {
        //Iterate over the Map and look for any negative values
        // or values strictly larger than what is in the current register
        for (Map.Entry<BillDenomination, Integer> entry : debitBills.entrySet()) {
            if (entry.getValue() < ZERO || entry.getValue() > register.get(entry.getKey())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canMakeChange(Integer amount) {
        //An amount less than what is in the register AND
        //the provided strategy must be able to make change for the given amount
        return amount < getRegisterTotal() && changeStrategy.canMakeChangeForAmount(getRegister(), amount);
    }

    @Override
    public Map<BillDenomination, Integer> makeChange(Integer amount) throws InsufficientFundsException {
        //Validate the input
        if (!canMakeChange(amount)) {
            throw new InsufficientFundsException("Change can't be made for the amount: " + amount);
        }

        //Obtain the change Map from the provided strategy
        Map<BillDenomination, Integer> change =
                changeStrategy.makeChangeForAmount(getRegister(), amount).get(ZERO);

        //Remove the bills from the register
        this.removeBillsFromRegister(change);

        return change;
    }

    @Override
    public String getRegisterContents() {
        StringBuilder contents = new StringBuilder();

        contents.append("Total=$").append(getRegisterTotal());

        //Iterate over register and add the contents of each bill type to the output
        for (Map.Entry<BillDenomination, Integer> entry : register.descendingMap().entrySet()) {
            contents.append(" $").append(entry.getKey().getAmount()).append("x").append(entry.getValue());
        }

        return contents.toString();
    }

    @Override
    public Integer getRegisterTotal() {
        Integer total = ZERO;

        //Iterate over the register to compute the total amount
        for (Map.Entry<BillDenomination, Integer> entry : register.entrySet()) {
            total = total + entry.getKey().getAmount() * entry.getValue();
        }
        return total;
    }

    private TreeMap<BillDenomination, Integer> getRegister() {
        return register;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(register);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CashRegisterImpl other = (CashRegisterImpl) obj;
        return this.getRegister().equals(other.getRegister());
    }

    @Override
    public String toString() {
        return getRegisterContents();
    }
}