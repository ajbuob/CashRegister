package com.ajbuob.register;

import com.ajbuob.register.enums.BillDenomination;
import com.ajbuob.register.exception.InsufficientFundsException;
import com.ajbuob.register.strategy.ChangeStrategy;
import com.ajbuob.register.strategy.OptimalChangeStrategy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

public class CashRegisterTestImpl {

    private CashRegister cashRegister = null;
    private CashRegister cashRegisterSuccess = null;
    private CashRegister cashRegisterFail = null;
    private static final Integer ZERO = 0;

    private final ChangeStrategy changeStrategy = new OptimalChangeStrategy();
    private final ChangeStrategy changeStrategyMockSuccess = Mockito.mock(ChangeStrategy.class);
    private final ChangeStrategy changeStrategyMockFailure = Mockito.mock(ChangeStrategy.class);

    @Before
    public void setup() {
        cashRegister = new CashRegisterImpl(changeStrategy);
        cashRegisterSuccess = new CashRegisterImpl(changeStrategyMockSuccess);
        cashRegisterFail = new CashRegisterImpl(changeStrategyMockFailure);
        when(changeStrategyMockSuccess.canMakeChangeForAmount(any(TreeMap.class), anyInt())).thenReturn(true);
        when(changeStrategyMockFailure.canMakeChangeForAmount(any(TreeMap.class), anyInt())).thenReturn(false);
    }

    @Test
    public void testAddBillsToRegisterNegative() {
        final CashRegister EXPECTED_REGISTER = new CashRegisterImpl();
        //final String EXPECTED_CONTENTS = "Total=$0 $20x0 $10x0 $5x0 $2x0 $1x0";
        final String EX_MSG_ADD = "Cant add this quantity of bills to the register";

        //Populate the cash register
        Map<BillDenomination, Integer> creditBills = new HashMap<>();
        creditBills.put(BillDenomination.TWENTY, 5);
        creditBills.put(BillDenomination.TEN, -2);

        assertThatThrownBy(() -> {
            cashRegisterSuccess.addBillsToRegister(creditBills);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(EX_MSG_ADD);

        assertThat(cashRegisterSuccess).isEqualTo(EXPECTED_REGISTER);
        assertThat(cashRegisterSuccess.getRegisterTotal()).isEqualTo(ZERO);
    }

    @Test
    public void testAddBillsToRegisterZero() {
        final CashRegister EXPECTED_REGISTER = new CashRegisterImpl();
        final TreeMap<BillDenomination, Integer> register = new TreeMap<>();
        register.put(BillDenomination.TEN, 3);
        register.put(BillDenomination.TWO, 3);
        register.put(BillDenomination.ONE, 6);

        EXPECTED_REGISTER.addBillsToRegister(register);
        //final String EXPECTED_CONTENTS = "Total=$42 $20x0 $10x3 $5x0 $2x3 $1x6";

        //Populate the cash register
        Map<BillDenomination, Integer> creditBills = new HashMap<>();
        creditBills.put(BillDenomination.TEN, 3);
        creditBills.put(BillDenomination.FIVE, ZERO);
        creditBills.put(BillDenomination.TWO, 3);
        creditBills.put(BillDenomination.ONE, 6);

        cashRegisterSuccess.addBillsToRegister(creditBills);
        assertThat(cashRegisterSuccess.getRegisterTotal()).isEqualTo(42);
        assertThat(cashRegisterSuccess).isEqualTo(EXPECTED_REGISTER);
    }

    @Test
    public void testAddBillsToRegister() {
        Map<BillDenomination, Integer> creditBills = new HashMap<>();
        creditBills.put(BillDenomination.TWENTY, 4);
        creditBills.put(BillDenomination.FIVE, 3);
        creditBills.put(BillDenomination.ONE, 6);

        cashRegisterSuccess.addBillsToRegister(creditBills);
        assertThat(cashRegisterSuccess.getRegisterTotal()).isEqualTo(101);
    }


    @Test
    public void testRemoveBillsFromRegisterNegative() {
        final CashRegister EXPECTED_REGISTER = new CashRegisterImpl();

        final TreeMap<BillDenomination, Integer> register = new TreeMap<>();
        register.put(BillDenomination.TEN, 4);
        register.put(BillDenomination.TWO, 2);
        register.put(BillDenomination.ONE, 3);

        EXPECTED_REGISTER.addBillsToRegister(register);

        //final String EXPECTED_CONTENTS = "Total=$47 $20x0 $10x4 $5x0 $2x2 $1x3";
        final String EX_MSG_REMOVE = "Cant remove this quantity of bills to the register";

        //Populate the cash register
        Map<BillDenomination, Integer> creditBills = new HashMap<>();
        creditBills.put(BillDenomination.TEN, 4);
        creditBills.put(BillDenomination.TWO, 2);
        creditBills.put(BillDenomination.ONE, 3);

        cashRegisterSuccess.addBillsToRegister(creditBills);
        assertThat(cashRegisterSuccess.getRegisterTotal()).isEqualTo(47);
        assertThat(cashRegisterSuccess).isEqualTo(EXPECTED_REGISTER);

        Map<BillDenomination, Integer> debitBills = new HashMap<>();
        debitBills.put(BillDenomination.TWENTY, -1);

        //Try to remove a quantity of -1 twenties
        assertThatThrownBy(() -> {
            cashRegisterSuccess.removeBillsFromRegister(debitBills);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(EX_MSG_REMOVE);

        //Register is not changed after trying to remove negative quantity
        assertThat(cashRegisterSuccess.getRegisterTotal()).isEqualTo(47);
        assertThat(cashRegisterSuccess).isEqualTo(EXPECTED_REGISTER);
    }


    @Test
    public void testRemoveBillsFromRegisterZero() {
        final CashRegister EXPECTED_REGISTER = new CashRegisterImpl();

        final TreeMap<BillDenomination, Integer> register = new TreeMap<>();
        register.put(BillDenomination.FIVE, 2);
        register.put(BillDenomination.TWO, 3);
        register.put(BillDenomination.ONE, 5);

        EXPECTED_REGISTER.addBillsToRegister(register);

        //final String EXPECTED_CONTENTS = "Total=$21 $20x0 $10x0 $5x2 $2x3 $1x5";

        //Populate the cash register
        Map<BillDenomination, Integer> creditBills = new HashMap<>();
        creditBills.put(BillDenomination.FIVE, 2);
        creditBills.put(BillDenomination.TWO, 3);
        creditBills.put(BillDenomination.ONE, 5);

        cashRegisterSuccess.addBillsToRegister(creditBills);
        assertThat(cashRegisterSuccess.getRegisterTotal()).isEqualTo(21);
        assertThat(cashRegisterSuccess).isEqualTo(EXPECTED_REGISTER);

        //Try to remove a quantity of zero fives
        Map<BillDenomination, Integer> debitBills = new HashMap<>();
        debitBills.put(BillDenomination.FIVE, 0);
        cashRegisterSuccess.removeBillsFromRegister(debitBills);

        //Register is not changed after trying to remove zero quantity
        assertThat(cashRegisterSuccess.getRegisterTotal()).isEqualTo(21);
        assertThat(cashRegisterSuccess).isEqualTo(EXPECTED_REGISTER);
    }

    @Test
    public void testRemoveBillsFromRegister() {

        final CashRegister EXPECTED_REGISTER_BEFORE = new CashRegisterImpl();

        final TreeMap<BillDenomination, Integer> registerBefore = new TreeMap<>();
        registerBefore.put(BillDenomination.TEN, 6);
        registerBefore.put(BillDenomination.ONE, 5);

        EXPECTED_REGISTER_BEFORE.addBillsToRegister(registerBefore);

        //final String EXPECTED_CONTENTS_BEFORE = "Total=$65 $20x0 $10x6 $5x0 $2x0 $1x5";

        final CashRegister EXPECTED_REGISTER_AFTER = new CashRegisterImpl();

        final TreeMap<BillDenomination, Integer> registerAfter = new TreeMap<>();
        registerAfter.put(BillDenomination.TEN, 4);
        registerAfter.put(BillDenomination.ONE, 5);

        EXPECTED_REGISTER_AFTER.addBillsToRegister(registerAfter);

        //final String EXPECTED_CONTENTS_AFTER = "Total=$45 $20x0 $10x4 $5x0 $2x0 $1x5";

        //Populate the cash register
        Map<BillDenomination, Integer> creditBills = new HashMap<>();
        creditBills.put(BillDenomination.TEN, 6);
        creditBills.put(BillDenomination.ONE, 5);

        cashRegisterSuccess.addBillsToRegister(creditBills);
        assertThat(cashRegisterSuccess.getRegisterTotal()).isEqualTo(65);
        assertThat(cashRegisterSuccess).isEqualTo(EXPECTED_REGISTER_BEFORE);

        //Remove a quantity of 2 tens
        Map<BillDenomination, Integer> debitBills = new HashMap<>();
        debitBills.put(BillDenomination.TEN, 2);
        cashRegisterSuccess.removeBillsFromRegister(debitBills);

        //Check contents after
        assertThat(cashRegisterSuccess.getRegisterTotal()).isEqualTo(45);
        assertThat(cashRegisterSuccess).isEqualTo(EXPECTED_REGISTER_AFTER);
    }

    @Test
    public void testCanMakeChangeNotEnoughMoney() {
        final String EX_MSG = "Change can't be made for the amount: 20";

        //Populate the cash register
        Map<BillDenomination, Integer> creditBills = new HashMap<>();
        creditBills.put(BillDenomination.TEN, 1);
        creditBills.put(BillDenomination.ONE, 3);
        cashRegisterFail.addBillsToRegister(creditBills);
        assertThat(cashRegisterFail.canMakeChange(20)).isFalse();

        assertThatThrownBy(() -> {
            cashRegisterFail.makeChange(20);
        })
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessageContaining(EX_MSG);
    }

    @Test
    public void testCanMakeChangeNoChange() {
        final String EX_MSG = "Change can't be made for the amount: 5";

        //Populate the cash register
        Map<BillDenomination, Integer> creditBills = new HashMap<>();
        creditBills.put(BillDenomination.TEN, 1);
        creditBills.put(BillDenomination.TWO, 4);
        cashRegisterFail.addBillsToRegister(creditBills);
        assertThat(cashRegisterFail.canMakeChange(5)).isFalse();

        assertThatThrownBy(() -> {
            cashRegisterFail.makeChange(5);
        })
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessageContaining(EX_MSG);
    }

    @Test
    public void testCanMakeChangeSuccessIntegration() {
        final CashRegister EXPECTED_REGISTER_BEFORE = new CashRegisterImpl();

        final TreeMap<BillDenomination, Integer> registerBefore = new TreeMap<>();
        registerBefore.put(BillDenomination.TWO, 4);
        registerBefore.put(BillDenomination.ONE, 3);

        EXPECTED_REGISTER_BEFORE.addBillsToRegister(registerBefore);

        //final String EXPECTED_CONTENTS_BEFORE = "Total=$11 $20x0 $10x0 $5x0 $2x4 $1x3";

        final CashRegister EXPECTED_REGISTER_AFTER = new CashRegisterImpl();

        final TreeMap<BillDenomination, Integer> registerAfter = new TreeMap<>();
        registerAfter.put(BillDenomination.ONE, 2);

        EXPECTED_REGISTER_AFTER.addBillsToRegister(registerAfter);

        //final String EXPECTED_CONTENTS_AFTER = "Total=$2 $20x0 $10x0 $5x0 $2x0 $1x2";

        //Populate the cash register
        Map<BillDenomination, Integer> creditBills = new HashMap<>();
        creditBills.put(BillDenomination.TWO, 4);
        creditBills.put(BillDenomination.ONE, 3);
        cashRegister.addBillsToRegister(creditBills);

        assertThat(cashRegister.getRegisterTotal()).isEqualTo(11);
        assertThat(cashRegister).isEqualTo(EXPECTED_REGISTER_BEFORE);

        assertThat(cashRegister.canMakeChange(9)).isTrue();

        try {
            cashRegister.makeChange(9);
        } catch (InsufficientFundsException e) {
            fail("Insufficient Funds exception was thrown");
        }

        //Ensure that the correct amount was removed from the register
        assertThat(cashRegister.getRegisterTotal()).isEqualTo(2);
        assertThat(cashRegister).isEqualTo(EXPECTED_REGISTER_AFTER);
    }
}