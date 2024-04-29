import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ChainBlockTest {


    Chainblock database; // база от данни с транзакции

    //before
    @Before
    public void setUp() {
        this.database = new ChainblockImpl();  //празна база oт данни  с транзакции
    }

    // add
    @Test
    public void testAddShouldReturnCorrectTransactions() {
        Assert.assertEquals(0, this.database.getCount());
        Transaction transaction = (Transaction) new TransactionImpl(1, TransactionStatus.SUCCESSFUL, "Venelin", "Raya", 150);
        this.database.add(transaction);
        Assert.assertEquals(1, this.database.getCount());
        Assert.assertTrue(this.database.contains(transaction));  //проверка по транзакция
    }

    @Test
    public void testExistingTransaction() {
        Assert.assertEquals(0, this.database.getCount());
        Transaction transaction1 = (Transaction) new TransactionImpl(1, TransactionStatus.SUCCESSFUL, "Venelin", "Raya", 150);
        this.database.add(transaction1);
        Assert.assertEquals(1, this.database.getCount());
        Assert.assertTrue(this.database.contains(transaction1.getId())); //проверка по id
        // добаваме същата транзакция
        this.database.add(transaction1);
        Assert.assertEquals(1, this.database.getCount());
    }

    @Test
    public void testSuccessfulChangeTransactionStatus() {
        Assert.assertEquals(0, this.database.getCount());
        Transaction transaction = (Transaction) new TransactionImpl(1, TransactionStatus.SUCCESSFUL, "Venelin", "Raya", 150);
        database.add(transaction);
        Assert.assertEquals(1, this.database.getCount());
        database.changeTransactionStatus(1, TransactionStatus.ABORTED);
        Assert.assertEquals(TransactionStatus.ABORTED, database.getById(1).getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangeTransactionStatusShouldReturnInvalidId() {
        Assert.assertEquals(0, this.database.getCount());
        Transaction transaction = (Transaction) new TransactionImpl(1, TransactionStatus.SUCCESSFUL, "Venelin", "Raya", 150);
        database.add(transaction);
        database.changeTransactionStatus(2, TransactionStatus.ABORTED);
    }

    @Test
    public void testRemoveTransactionsById() {
        Assert.assertEquals(0, this.database.getCount());
        Transaction transaction1 = (Transaction) new TransactionImpl(1, TransactionStatus.SUCCESSFUL, "Venelin", "Raya", 150.20);
        Transaction transaction2 = (Transaction) new TransactionImpl(2, TransactionStatus.ABORTED, "Viki", "Raya", 200.10);
        database.add(transaction1);
        database.add(transaction2);
        Assert.assertEquals(2, database.getCount());
        //премахваме
        database.removeTransactionById(1);
        Assert.assertEquals(1, database.getCount());
        Assert.assertFalse(database.contains(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveTransactionShouldReturnInvalidTransactionById() {
        Assert.assertEquals(0, this.database.getCount());
        Transaction transaction1 = (Transaction) new TransactionImpl(1, TransactionStatus.SUCCESSFUL, "Venelin", "Raya", 150.20);
        database.add(transaction1);
        // transaction ->  1, TransactionStatus.SUCCESSFUL, "Venelin", "Raya", 150.20;
        database.removeTransactionById(5);
    }

    @Test
    public void testGetTransactionById() {
        Assert.assertEquals(0, this.database.getCount());
        Transaction transaction1 = (Transaction) new TransactionImpl(1, TransactionStatus.SUCCESSFUL, "Venelin", "Raya", 150.20);
        database.add(transaction1);
        Transaction returnTransaction = database.getById(1);
        Assert.assertEquals(transaction1, returnTransaction);
        Assert.assertEquals(transaction1.getId(), returnTransaction.getId());
        Assert.assertEquals(transaction1.getStatus(), returnTransaction.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByTransactionShouldReturnInvalidId() {
        Transaction transaction1 = (Transaction) new TransactionImpl(1, TransactionStatus.SUCCESSFUL, "Venelin", "Raya", 150.20);
        database.add(transaction1);
        database.getById(5);
    }

    @Test
    public void testGetByTransactionsStatus() {
        Assert.assertEquals(0, this.database.getCount());
        Transaction transaction1 = new TransactionImpl(1, TransactionStatus.SUCCESSFUL, "Venelin", "Raya", 150.20);
        Transaction transaction2 = new TransactionImpl(2, TransactionStatus.ABORTED, "Desi", "Ivan", 200.10);
        Transaction transaction3 = new TransactionImpl(3, TransactionStatus.SUCCESSFUL, "Desi", "Venelin", 120.00);
        database.add(transaction1);
        database.add(transaction2);
        database.add(transaction3);

        Assert.assertEquals(3, database.getCount());
        Iterable<Transaction> result = database.getByTransactionStatus(TransactionStatus.SUCCESSFUL);
        List<Transaction> returnedTransactions = new ArrayList<>();
        result.forEach(returnedTransactions::add);
        Assert.assertEquals(2, returnedTransactions.size());
        returnedTransactions.forEach(transaction -> Assert.assertEquals(TransactionStatus.SUCCESSFUL, transaction.getStatus()));
        Assert.assertEquals(returnedTransactions.get(0), transaction1);
        Assert.assertEquals(returnedTransactions.get(1), transaction3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByTransactionStatusShouldReturnInvalidStatus() {
        Assert.assertEquals(0, this.database.getCount());
        Transaction transaction1 = new TransactionImpl(1, TransactionStatus.SUCCESSFUL, "Venelin", "Raya", 150.20);
        Transaction transaction2 = new TransactionImpl(2, TransactionStatus.ABORTED, "Desi", "Ivan", 200.10);
        Transaction transaction3 = new TransactionImpl(3, TransactionStatus.SUCCESSFUL, "Desi", "Venelin", 120.00);
        database.add(transaction1);
        database.add(transaction2);
        database.add(transaction3);

        Assert.assertEquals(3, database.getCount());
        Iterable<Transaction> result = database.getByTransactionStatus(TransactionStatus.FAILED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAllSendersWithTransactionStatusShouldReturnMissingStatus() {
        Assert.assertEquals(0, this.database.getCount());
        Transaction transaction1 = new TransactionImpl(1, TransactionStatus.SUCCESSFUL, "Venelin", "Raya", 150.20);
        Transaction transaction2 = new TransactionImpl(2, TransactionStatus.ABORTED, "Desi", "Ivan", 200.10);
        Transaction transaction3 = new TransactionImpl(3, TransactionStatus.SUCCESSFUL, "Desi", "Venelin", 120.00);
        database.add(transaction1);
        database.add(transaction2);
        database.add(transaction3);

        Assert.assertEquals(3, database.getCount());
        database.getAllSendersWithTransactionStatus(TransactionStatus.UNAUTHORIZED);
    }

    @Test
    public void testGetAllSendersWithTransactionStatusShouldReturnExistingStatus() {
        Assert.assertEquals(0, this.database.getCount());
        Transaction transaction1 = new TransactionImpl(1, TransactionStatus.SUCCESSFUL, "Venelin", "Raya", 150.20);
        Transaction transaction2 = new TransactionImpl(2, TransactionStatus.ABORTED, "Kaloqn", "Ivan", 200.10);
        Transaction transaction3 = new TransactionImpl(3, TransactionStatus.SUCCESSFUL, "Desi", "Venelin", 120.00);
        Transaction transaction4 = new TransactionImpl(4, TransactionStatus.SUCCESSFUL, "Muci", "Venelin", 220.30);
        database.add(transaction1);
        database.add(transaction2);
        database.add(transaction3);
        database.add(transaction4);
        Assert.assertEquals(4, database.getCount());

        Iterable<String> result = database.getAllSendersWithTransactionStatus(TransactionStatus.SUCCESSFUL);
        List<String> resultSanders = new ArrayList<>();
        result.forEach(resultSanders::add);
        Assert.assertEquals(3, resultSanders.size());
        Assert.assertEquals("Muci", resultSanders.get(0));
        Assert.assertEquals("Venelin", resultSanders.get(1));
        Assert.assertEquals("Desi", resultSanders.get(2));


    }
}
