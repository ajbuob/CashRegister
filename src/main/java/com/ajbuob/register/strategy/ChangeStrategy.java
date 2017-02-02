package com.ajbuob.register.strategy;

import com.ajbuob.register.enums.BillDenomination;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by abuob on 1/31/17.
 */
public interface ChangeStrategy {
    /**
     * @param register the Map representing the bills and quantities of each in the cash register
     * @param amount   the currency amount requested for making change
     * @return if the strategy can make change for the given amount
     */
    public boolean canMakeChangeForAmount(final TreeMap<BillDenomination, Integer> register, final Integer amount);

    /**
     * @param register the Map representing the bills and quantities of each in the cash register
     * @param amount   the currency amount requested for making change
     */
    public List<TreeMap<BillDenomination, Integer>> makeChangeForAmount(
            final TreeMap<BillDenomination, Integer> register, final Integer amount);
}
