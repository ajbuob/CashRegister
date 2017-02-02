Cash Register to dispense change to the user if available.

This register uses the strategy pattern to determine if the provided algorithm can provide change to the user and then how the change is provided to the user.

Interfaces have been created for the cash register and change strategy so that they may be implemented and tested independently from each other.
Mockito is being used to stub out the change strategy in the cash register implementation and I have also provided an integration test in the cash register test class to demonstrate full functionality.

I have chosen to implement an optimal change strategy which tries to remove the largest amount of the largest bills of the cash register in descending order and subtracts match to try and get the remaining to be zero.

remaining = remaining - billQuantityActual * billValue

where billQuantityActual = Minimum(remaining / billValue, billQuantity)

NOTE: the minimum is taken to guard against the case of not having enough bills in the register                
                
             

Ways of having $13 using the bills: $10,$5,$2,$1 and trying to make $8 

$10  $5  $2  $1   RESULT

1    0   1   1     FAIL

0    2   1   1     PASS

0    1   4   0     FAIL - would pass with a different non-optimal strategy as $5 is fist taken for tring to make change

0    1   3   2     PASS

0    1   2   4     PASS

0    1   1   6     PASS

0    0   6   1     PASS

0    0   5   3     PASS

0    0   4   5     PASS

0    0   3   7     PASS

0    0   2   9     PASS

0    0   1   11    PASS

0    0   0   13    PASS
