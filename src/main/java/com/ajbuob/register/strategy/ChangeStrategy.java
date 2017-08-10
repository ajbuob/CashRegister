package com.ajbuob.register.strategy;

import com.ajbuob.register.enums.BillDenomination;

import java.util.List;
import java.util.TreeMap;

public interface ChangeStrategy {
    /**
     * @param register the Map representing the bills and quantities of each in the cash register
     * @param amount   the currency amount requested for making change
     * @return if the strategy can make change for the given amount
     */
    boolean canMakeChangeForAmount(final TreeMap<BillDenomination, Integer> register, final Integer amount);

    /**
     * @param register the Map representing the bills and quantities of each in the cash register
     * @param amount   the currency amount requested for making change
     */
    List<TreeMap<BillDenomination, Integer>> makeChangeForAmount(
            final TreeMap<BillDenomination, Integer> register, final Integer amount);
}