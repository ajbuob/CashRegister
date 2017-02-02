package com.ajbuob.register;

import com.ajbuob.register.enums.BillDenomination;
import com.ajbuob.register.exception.InsufficientFundsException;

import java.util.Map;

/**
 * Created by abuob on 1/31/17.
 */
public interface CashRegister {

    /**
     * Adds a specific quantity of each bill denomination to the register
     *
     * @param creditBills the denominations and quantities to be added to the register
     * @throws IllegalArgumentException if any of the quantities are not positive.
     *
     **/
    void addBillsToRegister(Map<BillDenomination, Integer> creditBills);

    /**
     * Removes a specific quantity of each bill denomination to the register
     *
     * @param debitBills the denominations and quantities to be removed from the register
     *
     **/
    void removeBillsFromRegister(Map<BillDenomination, Integer> debitBills);

    /**
     * Determines if change can be made for ther given dollar amount from the register
     *
     * @param amount the dollar amount requested for change
     * @return indicator if change can be made successfully or not
     *
     **/
    boolean canMakeChange(Integer amount);

    /**
     * Provides change for a given dollar amount from the register
     *
     * @param amount the dollar amount requested for change
     * @return a Map of the change combination removed from the register
     * @throws InsufficientFundsException if change cannot be made for the provided amount
     *
     **/
    Map<BillDenomination, Integer> makeChange(Integer amount) throws InsufficientFundsException;

    /**
     *
     * @return the total dollar amount in the register
     *
     */
    Integer getRegisterTotal();

    /**
     * Returns the contents of the register as a single String.
     * This includes the total amount money in the register well as the
     * quantity of each denomination in the register.
     *
     * @return the contents of the register
     *
     */
    String getRegisterContents();
}
