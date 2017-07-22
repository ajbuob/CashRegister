package com.ajbuob.register.strategy;

import com.ajbuob.register.enums.BillDenomination;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;

public class OptimalChangeStrategyTest {

    private static final Integer ZERO = 0;
    private static final Integer EIGHT = 8;

    private ChangeStrategy changeStrategy = null;
    private TreeMap<BillDenomination, Integer> register = null;

    private List<TreeMap<BillDenomination, Integer>> changeList = null;
    private TreeMap<BillDenomination, Integer> change = null;

    @Before
    public void setup() {
        changeStrategy = new OptimalChangeStrategy();
    }

    @Test
    public void testCanMakeChangeForAmountCase1() {
        // $13 = $20x0 + $10x1 + $5x0 +$2x1 + $1x1
        register = new TreeMap<>();
        register.put(BillDenomination.ONE, 1);
        register.put(BillDenomination.TWO, 1);
        register.put(BillDenomination.FIVE, ZERO);
        register.put(BillDenomination.TEN, 1);
        register.put(BillDenomination.TWENTY, ZERO);
        assertThat(changeStrategy.canMakeChangeForAmount(register, EIGHT)).isFalse();

        changeList = changeStrategy.makeChangeForAmount(register, EIGHT);
        assertThat(changeList).isNotNull();
        assertThat(changeList).hasSize(0);

    }

    @Test
    public void testCanMakeChangeForAmountCase2() {
        // $13 = $20x0 + $10x0 + $5x2 +$2x1 + $1x1
        // $8  = $20x0 + $10x0 + $5x1 +$2x1 + $1x1
        final String EXPECTED_CHANGE = "Total=$8 $20x0 $10x0 $5x1 $2x1 $1x1";

        register = new TreeMap<>();
        register.put(BillDenomination.ONE, 1);
        register.put(BillDenomination.TWO, 1);
        register.put(BillDenomination.FIVE, 2);
        register.put(BillDenomination.TEN, ZERO);
        register.put(BillDenomination.TWENTY, ZERO);
        assertThat(changeStrategy.canMakeChangeForAmount(register, EIGHT)).isTrue();

        changeList = changeStrategy.makeChangeForAmount(register, EIGHT);
        assertThat(changeList).isNotNull();
        assertThat(changeList).hasSize(1);
        change = changeList.get(0);
        assertThat(getChangeString(change)).isEqualTo(EXPECTED_CHANGE);
    }

    @Test
    public void testCanMakeChangeForAmountCase3() {
        // $13 = $20x0 + $10x0 + $5x1 +$2x4 + $1x0
        //Would pass with a different strategy since $8  = $2x4
        register = new TreeMap<>();
        register.put(BillDenomination.ONE, ZERO);
        register.put(BillDenomination.TWO, 4);
        register.put(BillDenomination.FIVE, 1);
        register.put(BillDenomination.TEN, ZERO);
        register.put(BillDenomination.TWENTY, ZERO);
        assertThat(changeStrategy.canMakeChangeForAmount(register, EIGHT)).isFalse();

        changeList = changeStrategy.makeChangeForAmount(register, EIGHT);
        assertThat(changeList).isNotNull();
        assertThat(changeList).hasSize(0);

    }

    @Test
    public void testCanMakeChangeForAmountCase4() {
        // $13 = $20x0 + $10x0 + $5x1 +$2x3 + $1x2
        // $8  = $20x0 + $10x0 + $5x1 +$2x1 + $1x1
        final String EXPECTED_CHANGE = "Total=$8 $20x0 $10x0 $5x1 $2x1 $1x1";

        register = new TreeMap<>();
        register.put(BillDenomination.ONE, 2);
        register.put(BillDenomination.TWO, 3);
        register.put(BillDenomination.FIVE, 1);
        register.put(BillDenomination.TEN, ZERO);
        register.put(BillDenomination.TWENTY, ZERO);
        assertThat(changeStrategy.canMakeChangeForAmount(register, EIGHT)).isTrue();

        changeList = changeStrategy.makeChangeForAmount(register, EIGHT);
        assertThat(changeList).isNotNull();
        assertThat(changeList).hasSize(1);
        change = changeList.get(0);
        assertThat(getChangeString(change)).isEqualTo(EXPECTED_CHANGE);
    }

    @Test
    public void testCanMakeChangeForAmountCase5() {
        // $13 = $20x0 + $10x0 + $5x1 +$2x2 + $1x4
        // $8  = $20x0 + $10x0 + $5x1 +$2x1 + $1x1
        final String EXPECTED_CHANGE = "Total=$8 $20x0 $10x0 $5x1 $2x1 $1x1";

        register = new TreeMap<>();
        register.put(BillDenomination.ONE, 4);
        register.put(BillDenomination.TWO, 2);
        register.put(BillDenomination.FIVE, 1);
        register.put(BillDenomination.TEN, ZERO);
        register.put(BillDenomination.TWENTY, ZERO);
        assertThat(changeStrategy.canMakeChangeForAmount(register, EIGHT)).isTrue();

        changeList = changeStrategy.makeChangeForAmount(register, EIGHT);
        assertThat(changeList).isNotNull();
        assertThat(changeList).hasSize(1);
        change = changeList.get(0);
        assertThat(getChangeString(change)).isEqualTo(EXPECTED_CHANGE);
    }

    @Test
    public void testCanMakeChangeForAmountCase6() {
        // $13 = $20x0 + $10x0 + $5x1 +$2x1 + $1x6
        // $8  = $20x0 + $10x0 + $5x1 +$2x1 + $1x1
        final String EXPECTED_CHANGE = "Total=$8 $20x0 $10x0 $5x1 $2x1 $1x1";

        register = new TreeMap<>();
        register.put(BillDenomination.ONE, 6);
        register.put(BillDenomination.TWO, 1);
        register.put(BillDenomination.FIVE, 1);
        register.put(BillDenomination.TEN, ZERO);
        register.put(BillDenomination.TWENTY, ZERO);
        assertThat(changeStrategy.canMakeChangeForAmount(register, EIGHT)).isTrue();

        changeList = changeStrategy.makeChangeForAmount(register, EIGHT);
        assertThat(changeList).isNotNull();
        assertThat(changeList).hasSize(1);
        change = changeList.get(0);
        assertThat(getChangeString(change)).isEqualTo(EXPECTED_CHANGE);
    }

    @Test
    public void testCanMakeChangeForAmountCase7() {
        // $13 = $20x0 + $10x0 + $5x0 +$2x6 + $1x1
        // $8  = $20x0 + $10x0 + $5x0 +$2x4 + $1x0
        final String EXPECTED_CHANGE = "Total=$8 $20x0 $10x0 $5x0 $2x4 $1x0";

        register = new TreeMap<>();
        register.put(BillDenomination.ONE, 1);
        register.put(BillDenomination.TWO, 6);
        register.put(BillDenomination.FIVE, ZERO);
        register.put(BillDenomination.TEN, ZERO);
        register.put(BillDenomination.TWENTY, ZERO);
        assertThat(changeStrategy.canMakeChangeForAmount(register, EIGHT)).isTrue();

        changeList = changeStrategy.makeChangeForAmount(register, EIGHT);
        assertThat(changeList).isNotNull();
        assertThat(changeList).hasSize(1);
        change = changeList.get(0);
        assertThat(getChangeString(change)).isEqualTo(EXPECTED_CHANGE);
    }

    @Test
    public void testCanMakeChangeForAmountCase8() {
        // $13 = $20x0 + $10x0 + $5x0 +$2x5 + $1x3
        // $8  = $20x0 + $10x0 + $5x0 +$2x4 + $1x0
        final String EXPECTED_CHANGE = "Total=$8 $20x0 $10x0 $5x0 $2x4 $1x0";

        register = new TreeMap<>();
        register.put(BillDenomination.ONE, 3);
        register.put(BillDenomination.TWO, 5);
        register.put(BillDenomination.FIVE, ZERO);
        register.put(BillDenomination.TEN, ZERO);
        register.put(BillDenomination.TWENTY, ZERO);
        assertThat(changeStrategy.canMakeChangeForAmount(register, EIGHT)).isTrue();

        changeList = changeStrategy.makeChangeForAmount(register, EIGHT);
        assertThat(changeList).isNotNull();
        assertThat(changeList).hasSize(1);
        change = changeList.get(0);
        assertThat(getChangeString(change)).isEqualTo(EXPECTED_CHANGE);
    }

    @Test
    public void testCanMakeChangeForAmountCase9() {
        // $13 = $20x0 + $10x0 + $5x0 +$2x4 + $1x5
        // $8  = $20x0 + $10x0 + $5x0 +$2x4 + $1x0
        final String EXPECTED_CHANGE = "Total=$8 $20x0 $10x0 $5x0 $2x4 $1x0";

        register = new TreeMap<>();
        register.put(BillDenomination.ONE, 5);
        register.put(BillDenomination.TWO, 4);
        register.put(BillDenomination.FIVE, ZERO);
        register.put(BillDenomination.TEN, ZERO);
        register.put(BillDenomination.TWENTY, ZERO);
        assertThat(changeStrategy.canMakeChangeForAmount(register, EIGHT)).isTrue();

        changeList = changeStrategy.makeChangeForAmount(register, EIGHT);
        assertThat(changeList).isNotNull();
        assertThat(changeList).hasSize(1);
        change = changeList.get(0);
        assertThat(getChangeString(change)).isEqualTo(EXPECTED_CHANGE);
    }

    @Test
    public void testCanMakeChangeForAmountCase10() {
        // $13 = $20x0 + $10x0 + $5x0 +$2x3 + $1x7
        // $8  = $20x0 + $10x0 + $5x0 +$2x3 + $1x2
        final String EXPECTED_CHANGE = "Total=$8 $20x0 $10x0 $5x0 $2x3 $1x2";

        register = new TreeMap<>();
        register.put(BillDenomination.ONE, 7);
        register.put(BillDenomination.TWO, 3);
        register.put(BillDenomination.FIVE, ZERO);
        register.put(BillDenomination.TEN, ZERO);
        register.put(BillDenomination.TWENTY, ZERO);
        assertThat(changeStrategy.canMakeChangeForAmount(register, EIGHT)).isTrue();

        changeList = changeStrategy.makeChangeForAmount(register, EIGHT);
        assertThat(changeList).isNotNull();
        assertThat(changeList).hasSize(1);
        change = changeList.get(0);
        assertThat(getChangeString(change)).isEqualTo(EXPECTED_CHANGE);
    }

    @Test
    public void testCanMakeChangeForAmountCase11() {
        // $13 = $20x0 + $10x0 + $5x0 +$2x2 + $1x9
        // $8  = $20x0 + $10x0 + $5x0 +$2x2 + $1x4
        final String EXPECTED_CHANGE = "Total=$8 $20x0 $10x0 $5x0 $2x2 $1x4";

        register = new TreeMap<>();
        register.put(BillDenomination.ONE, 9);
        register.put(BillDenomination.TWO, 2);
        register.put(BillDenomination.FIVE, ZERO);
        register.put(BillDenomination.TEN, ZERO);
        register.put(BillDenomination.TWENTY, ZERO);
        assertThat(changeStrategy.canMakeChangeForAmount(register, EIGHT)).isTrue();

        changeList = changeStrategy.makeChangeForAmount(register, EIGHT);
        assertThat(changeList).isNotNull();
        assertThat(changeList).hasSize(1);
        change = changeList.get(0);
        assertThat(getChangeString(change)).isEqualTo(EXPECTED_CHANGE);
    }

    @Test
    public void testCanMakeChangeForAmountCase12() {
        // $13 = $20x0 + $10x0 + $5x0 +$2x1 + $1x11
        // $8  = $20x0 + $10x0 + $5x0 +$2x1 + $1x6
        final String EXPECTED_CHANGE = "Total=$8 $20x0 $10x0 $5x0 $2x1 $1x6";

        register = new TreeMap<>();
        register.put(BillDenomination.ONE, 11);
        register.put(BillDenomination.TWO, 1);
        register.put(BillDenomination.FIVE, ZERO);
        register.put(BillDenomination.TEN, ZERO);
        register.put(BillDenomination.TWENTY, ZERO);
        assertThat(changeStrategy.canMakeChangeForAmount(register, EIGHT)).isTrue();

        changeList = changeStrategy.makeChangeForAmount(register, EIGHT);
        assertThat(changeList).isNotNull();
        assertThat(changeList).hasSize(1);
        change = changeList.get(0);
        assertThat(getChangeString(change)).isEqualTo(EXPECTED_CHANGE);
    }

    @Test
    public void testCanMakeChangeForAmountCase13() {
        // $13 = $20x0 + $10x0 + $5x0 +$2x0 + $1x13
        // $8  = $20x0 + $10x0 + $5x0 +$2x0 + $1x8
        final String EXPECTED_CHANGE = "Total=$8 $20x0 $10x0 $5x0 $2x0 $1x8";

        register = new TreeMap<>();
        register.put(BillDenomination.ONE, 13);
        register.put(BillDenomination.TWO, ZERO);
        register.put(BillDenomination.FIVE, ZERO);
        register.put(BillDenomination.TEN, ZERO);
        register.put(BillDenomination.TWENTY, ZERO);
        assertThat(changeStrategy.canMakeChangeForAmount(register, EIGHT)).isTrue();

        changeList = changeStrategy.makeChangeForAmount(register, EIGHT);
        assertThat(changeList).isNotNull();
        assertThat(changeList).hasSize(1);
        change = changeList.get(0);
        assertThat(getChangeString(change)).isEqualTo(EXPECTED_CHANGE);
    }

    private String getChangeString(TreeMap<BillDenomination, Integer> change) {
        Integer total = 0;
        StringBuilder contents = new StringBuilder();

        for (Map.Entry<BillDenomination, Integer> entry : change.descendingMap().entrySet()) {
            total = total + entry.getKey().getAmount() * entry.getValue();
            contents.append(" $").append(entry.getKey().getAmount()).append("x").append(entry.getValue());
        }
        return "Total=$" + total + contents.toString();
    }

}


