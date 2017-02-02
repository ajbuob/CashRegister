package com.ajbuob.register.strategy;

import com.ajbuob.register.enums.BillDenomination;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by abuob on 1/31/17.
 */
public class OptimalChangeStrategy implements ChangeStrategy {

    private static final Integer ZERO = 0;

    @Override
    public boolean canMakeChangeForAmount(final TreeMap<BillDenomination, Integer> register, final Integer amount) {
        List<TreeMap<BillDenomination, Integer>> changeList = internalMakeChange(register, amount);
        return (changeList != null && !changeList.isEmpty());
    }

    @Override
    public List<TreeMap<BillDenomination, Integer>> makeChangeForAmount(
            final TreeMap<BillDenomination, Integer> register, final Integer amount) {
        return internalMakeChange(register, amount);
    }

    private List<TreeMap<BillDenomination, Integer>> internalMakeChange(final TreeMap<BillDenomination,
            Integer> register, final Integer amount) {

        List<TreeMap<BillDenomination, Integer>> changeList = new ArrayList<>();
        TreeMap<BillDenomination, Integer> change = createNewChangeMap();
        Integer remaining = amount;
        Integer billQuantity;
        Integer billQuantityActual;
        Integer billValue;

        //Iterate over Map and subtract out the value of each bill amount from the provided amount
        for (Map.Entry<BillDenomination, Integer> entry : register.descendingMap().entrySet()) {
            billQuantity = entry.getValue();
            billValue = entry.getKey().getAmount();

            //Amount contains a portion which needs change from the register.
            //At least one bill of the type in this iteration must be in the register
            //and its value should not be greater than the remaining amount.
            //The min is taken to avoid cases where there is not enough bills of a certain type in the register
            if (remaining > ZERO && billQuantity > ZERO &&
                    remaining >= billValue) {
                billQuantityActual = Math.min(remaining / billValue, billQuantity);
                //Compute the remaining value
                remaining = remaining - billQuantityActual * billValue;
                //Add the change combination to the Map
                change.put(entry.getKey(), billQuantityActual);
            }
        }
        //Zero remaining indicates change can be made from the register
        if (remaining == ZERO) {
            changeList.add(change);
        }
        return changeList;
    }

    private TreeMap<BillDenomination, Integer> createNewChangeMap() {
        TreeMap<BillDenomination, Integer> change = new TreeMap<>();
        change.put(BillDenomination.ONE, ZERO);
        change.put(BillDenomination.TWO, ZERO);
        change.put(BillDenomination.FIVE, ZERO);
        change.put(BillDenomination.TEN, ZERO);
        change.put(BillDenomination.TWENTY, ZERO);
        return change;
    }
}
