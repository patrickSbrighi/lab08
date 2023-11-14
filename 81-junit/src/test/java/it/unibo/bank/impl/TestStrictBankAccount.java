package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.unibo.bank.impl.SimpleBankAccount.MANAGEMENT_FEE;
import static it.unibo.bank.impl.StrictBankAccount.TRANSACTION_FEE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestStrictBankAccount {

    private final static int INITIAL_AMOUNT = 100;

    // Create a new AccountHolder and a StrictBankAccount for it each time tests are executed.
    private AccountHolder mRossi;
    private BankAccount bankAccount;

    /**
     * Prepare the tests.
     */
    @BeforeEach
    public void setUp() {
        mRossi = new AccountHolder("Mario", "Rossi", 1);
        bankAccount = new StrictBankAccount(mRossi, INITIAL_AMOUNT);
    }

    /**
     * Test the initial state of the StrictBankAccount.
     */
    @Test
    public void testInitialization() {
        assertEquals(INITIAL_AMOUNT, bankAccount.getBalance());
        assertEquals(0, bankAccount.getTransactionsCount());
        assertEquals(mRossi, bankAccount.getAccountHolder());
    }
    
    /**
     * Perform a deposit of 100€, compute the management fees, and check that the balance is correctly reduced.
     */
    @Test
    public void testManagementFees() {
        double expectedValue = 200;
        bankAccount.deposit(mRossi.getUserID(), INITIAL_AMOUNT);
        assertEquals(expectedValue, bankAccount.getBalance());
        expectedValue -= MANAGEMENT_FEE + (bankAccount.getTransactionsCount()*TRANSACTION_FEE);
        bankAccount.chargeManagementFees(mRossi.getUserID());
        assertEquals(expectedValue, bankAccount.getBalance());
    }

    /**
     * Test that withdrawing a negative amount causes a failure.
     */
    @Test
    public void testNegativeWithdraw() {
        try{
            bankAccount.withdraw(mRossi.getUserID(), -bankAccount.getBalance());
            fail("Withdraw with a negative amount allowed");
        }catch(IllegalArgumentException e){
            assertEquals(e.getMessage(), "Cannot withdraw a negative amount");
        }
    }

    /**
     * Test that withdrawing more money than it is in the account is not allowed.
     */
    @Test
    public void testWithdrawingTooMuch() {
        try{
            bankAccount.withdraw(mRossi.getUserID(), bankAccount.getBalance()*2);
            fail("Withdraw with an amount of money bigger than the money in the bank account balance allowed");
        }catch(IllegalArgumentException e){
            assertEquals(e.getMessage(), "Insufficient balance");
        }
    }
}
