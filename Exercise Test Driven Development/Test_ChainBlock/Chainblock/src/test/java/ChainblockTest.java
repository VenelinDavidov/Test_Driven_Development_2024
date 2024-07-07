import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ChainblockTest {


    private Chainblock dataBase;   // създава база данни

    List<Transaction> transactions;

    @Before
    public void setUp() {
        this.dataBase = new ChainblockImpl();  // празна база данни с транзакции
        this.transactions = new ArrayList<>();  // празен лист
        this.prepareTransactions();
    }

    private void fillChainBlockTransactions() {
        transactions.forEach(transaction -> dataBase.add(transaction));
    }

    private void prepareTransactions() {
        Transaction transaction1 = new TransactionImpl(2020, TransactionStatus.SUCCESSFUL, "Ivan", "Pesho", 15);
        Transaction transaction2 = new TransactionImpl(2021, TransactionStatus.FAILED, "Veni", "Pesho", 20);
        Transaction transaction3 = new TransactionImpl(2022, TransactionStatus.SUCCESSFUL, "Tosho", "Pesho", 17.20);
        Transaction transaction4 = new TransactionImpl(2023, TransactionStatus.UNAUTHORIZED, "Mitko", "Tosho", 16.50);
        Transaction transaction5 = new TransactionImpl(2024, TransactionStatus.SUCCESSFUL, "Ivan", "Pesho", 18.20);
        Transaction transaction6 = new TransactionImpl(2025, TransactionStatus.SUCCESSFUL, "Mitko", "Pesho", 16.80);
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);
        transactions.add(transaction5);
        transactions.add(transaction6);
    }

    // add methods different transactions;
    @Test
    public void test_ShouldAdd_Transactions() {
        dataBase.add(transactions.get(0));
        assertEquals(1, dataBase.getCount());
        dataBase.add(transactions.get(1));
        assertEquals(2, dataBase.getCount());
    }

    // add methods should not add duplicated transactions;
    @Test
    public void test_ShouldAdd_NotDuplicate_Transactions() {
        dataBase.add(transactions.get(0));
        dataBase.add(transactions.get(0));
        assertEquals(1, dataBase.getCount());
    }

    //contains??
    // methods contains true we hava a transactions
    @Test
    public void test_Contains_Should_Return_False() {
        dataBase.add(transactions.get(0));
        boolean chainblockContainsTransactions = dataBase.contains(transactions.get(1));
        assertFalse(chainblockContainsTransactions);
    }

    // methods contains false, because we don't have a transactions
    @Test
    public void test_Contains_Should_Return_True() {
        dataBase.add(transactions.get(0));
        boolean chainblockContainsTransaction = dataBase.contains(transactions.get(0));
        assertTrue(chainblockContainsTransaction);
    }

    // methods contains true or false, with search Id;
    @Test
    public void test_Contains_Should_WithTransaction_Return_False() {
        dataBase.add(transactions.get(0));
        boolean chainBlockContainsTransactions = dataBase.contains(transactions.get(1).getId());
        assertFalse(chainBlockContainsTransactions);
    }

    @Test
    public void test_Contains_Should_WithTransaction_Return_True() {
        dataBase.add(transactions.get(0));
        boolean chainBlockContainsTransactions = dataBase.contains(transactions.get(0).getId());
        assertTrue(chainBlockContainsTransactions);
    }

    // methods test_changeTransactionStatus -> we don't have a transactions
    @Test(expected = IllegalArgumentException.class)
    public void test_changeTransactionStatus_Should_Throw_MissingTransactions() {

        dataBase.changeTransactionStatus(1000, TransactionStatus.FAILED);
    }
    // methods test_changeTransactionStatus -> we have a transactions and change a status

    @Test
    public void test_changeTransactionStatus_Should_ChangeStatus() {
        dataBase.add(transactions.get(0));
        dataBase.changeTransactionStatus(transactions.get(0).getId(), TransactionStatus.FAILED);
        TransactionStatus newTransactions = dataBase.getById(transactions.get(0).getId()).getStatus();
        assertEquals(TransactionStatus.FAILED, newTransactions);
    }

    //methods getById -> we have transactions ById

    @Test
    public void test_getByIdStatus_ReturnTransaction() {
        dataBase.add(transactions.get(0));
        Transaction actualTransaction = dataBase.getById(transactions.get(0).getId());
        assertEquals(transactions.get(0), actualTransaction);
    }
    //methods getById -> we have not transactions ById

    @Test(expected = IllegalArgumentException.class)
    public void test_getByIdStatus_NotExist_Transactions() {
        dataBase.add(transactions.get(0));
        dataBase.add(transactions.get(1));
        dataBase.add(transactions.get(2));

        dataBase.getById(200);
    }

    // removeTransactionById -> remove id if exist
    @Test
    public void test_removeTransactionById_ShouldRemoveIt_Exist() {
        fillChainBlockTransactions();
        dataBase.removeTransactionById(2020);
        assertFalse(dataBase.contains(2020));
    }

    // removeTransactionById -> otherwise throw exception
    @Test(expected = IllegalArgumentException.class)
    public void test_removeTransactionById_Should_Throw_Missing_Transaction() {

        fillChainBlockTransactions();
        dataBase.removeTransactionById(200);
    }

    // getByTransactionStatus -> given status ordered by amount descending
    @Test
    public void test_getByTransactionStatus_Should_Return_Transactions() {
        fillChainBlockTransactions();

        Iterable<Transaction> expectedTransaction = transactions.stream()
                .filter(transaction -> transaction.getStatus().equals(TransactionStatus.SUCCESSFUL))
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .collect(Collectors.toList());

        Iterable<Transaction> actualTransaction = dataBase.getByTransactionStatus(TransactionStatus.SUCCESSFUL);

        assertEquals(expectedTransaction, actualTransaction);
    }

    // getByTransactionStatus -> no transactions with the given status, throw exception
    @Test(expected = IllegalArgumentException.class)

    public void test_getByTransactionStatus_Should_Throw_If_NoSuch_Transactions() {

        fillChainBlockTransactions();
        dataBase.getByTransactionStatus(TransactionStatus.ABORTED);
    }

    //getAllSendersWithTransactionStatus -> returns all senders which have transaction, with the given status ordered by transactions amount
    @Test
    public void test_getAllSendersWithTransactionStatus_Should_Return_SortedNames() {

        fillChainBlockTransactions();

        List<String> expectedTransactionSenders = transactions.stream()
                .filter(transaction -> transaction.getStatus().equals(TransactionStatus.SUCCESSFUL))
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .map(Transaction::getFrom)
                .collect(Collectors.toList());

        Iterable<String> actualTransactionSenders = dataBase.getAllSendersWithTransactionStatus(TransactionStatus.SUCCESSFUL);

        assertEquals(expectedTransactionSenders, actualTransactionSenders);
    }

    //getAllSendersWithTransactionStatus -> If no transactions exist, throw exception
    @Test(expected = IllegalArgumentException.class)
    public void test_getAllSendersWithTransactionStatus_Should_NoExist_Transaction() {
        fillChainBlockTransactions();
        dataBase.getAllSendersWithTransactionStatus(TransactionStatus.ABORTED);
    }


    //getAllReceiversWithTransactionStatus -> returns all receivers which have transactions
    @Test
    public void test_getAllReceiversWithTransactionStatus_Should_Return_SortedReceiver() {
        fillChainBlockTransactions();

        List<String> expectedReceiverTransaction = transactions.stream()
                .filter(transaction -> transaction.getStatus().equals(TransactionStatus.SUCCESSFUL))
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .map(Transaction::getTo)
                .collect(Collectors.toList());

        Iterable<String> actualReceiverTransactions = dataBase.getAllReceiversWithTransactionStatus(TransactionStatus.SUCCESSFUL);

        assertEquals(expectedReceiverTransaction, actualReceiverTransactions);
    }

    //getAllReceiversWithTransactionStatus -> If no transactions exist, throw exception
    @Test(expected = IllegalArgumentException.class)
    public void test_getAllReceiversWithTransactionStatus_Should_NoExist_Transaction() {

        fillChainBlockTransactions();

        dataBase.getAllReceiversWithTransactionStatus(TransactionStatus.ABORTED);
    }

    // getAllInAmountRange -> returns all transactions ordered by amount
    @Test
    public void test_getAllInAmountRange_Should_Return_Transactions() {
        fillChainBlockTransactions();

        Iterable<Transaction> expectedTransaction = transactions.stream()
                .filter(transaction -> transaction.getAmount() < 20 && transaction.getAmount() > 15)
                .collect(Collectors.toList());

        Iterable<Transaction> actualTransaction = dataBase.getAllInAmountRange(15, 20);

        assertEquals(expectedTransaction, actualTransaction);

    }

    // getAllInAmountRange -> returns empty collections
    @Test
    public void test_getAllInAmountRange_Should_Return_Empty_Collections() {
        fillChainBlockTransactions();

        Iterable<Transaction> expectedTransaction = transactions.stream()
                .filter(transaction -> transaction.getAmount() < 300 && transaction.getAmount() > 600)
                .collect(Collectors.toList());

        Iterable<Transaction> actulaTransaction = dataBase.getAllInAmountRange(300, 600);

        assertEquals(expectedTransaction, actulaTransaction);
    }

    //Methods getByReceiverAndAmountRange -> returns all receivers which have transactions with the given status
    @Test
    public void test_getByReceiverAndAmountRange_Should_Return_Transactions() {
        fillChainBlockTransactions();

        List<Transaction> expectedTransaction = transactions.stream()
                .filter(transaction -> transaction.getAmount() < 18 && transaction.getAmount() > 15)
                .filter(transaction -> transaction.getTo().equals("Pesho"))
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .collect(Collectors.toList());

        Iterable<Transaction> actualTransaction = dataBase.getByReceiverAndAmountRange("Pesho", 15, 18);

        assertEquals(expectedTransaction, actualTransaction);

    }

    //Methods getByReceiverAndAmountRange -> if no such transactions throw except
    @Test(expected = IllegalArgumentException.class)
    public void test_getByReceiverAndAmountRange_Should_Trow_NoSuchReceiver() {
        fillChainBlockTransactions();

        dataBase.getByReceiverAndAmountRange("Anatoli", 550, 680);
    }

    // Method getAllOrderedByAmountDescendingThenById -> returns all transactions ordered by amount descending and by id.
    @Test
    public void test_getAllOrderedByAmountDescendingThenById_Should_Throw_Transactions(){

        fillChainBlockTransactions();

       List<Transaction> expectedTransactions = transactions.stream()
               .sorted(Comparator.comparing(Transaction::getAmount).reversed().thenComparing(Transaction::getId))
               .collect(Collectors.toList());

        Iterable<Transaction> actualTransaction = dataBase.getAllOrderedByAmountDescendingThenById();

        assertEquals(expectedTransactions,actualTransaction);
    }

    // Methods getByReceiverOrderedByAmountThenById ->returns all transactions with a particular receiver ordered by amount descending
    @Test
    public void test_getByReceiverOrderedByAmountThenById_Should_Return_TransactionById_Then_ByID(){
        fillChainBlockTransactions();

        Iterable<Transaction> expectedTransaction = transactions.stream()
                .filter(transaction -> transaction.getTo().equals("Pesho"))
                .sorted(Comparator.comparing(Transaction::getAmount).reversed().thenComparing(Transaction::getId))
                .collect(Collectors.toList());

        Iterable<Transaction> actualTransaction = dataBase.getByReceiverOrderedByAmountThenById("Pesho");

        assertEquals(expectedTransaction,actualTransaction);

    }
    // Methods getByReceiverOrderedByAmountThenById ->If there are no such transactions throw exception
    @Test(expected = IllegalArgumentException.class)
    public void test_getByReceiverOrderedByAmountThenById_Should_ThrowNoTransaction(){

        fillChainBlockTransactions();

        dataBase.getByReceiverOrderedByAmountThenById("Anatoli");
    }

    // Method getBySenderOrderedByAmountDescending -> earch for all transactions with a specific sender and return them ordered by amount descending.
    @Test
    public void test_getBySenderOrderedByAmountDescending_ShouldThrow_Transaction(){

        fillChainBlockTransactions();

        Iterable<Transaction> expectedTransaction = transactions.stream()
                .filter(transaction -> transaction.getFrom().equals("Ivan"))
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .collect(Collectors.toList());

        Iterable<Transaction> actualTransaction = dataBase.getBySenderOrderedByAmountDescending("Ivan");

        assertEquals(expectedTransaction,actualTransaction);
    }
    // Method getBySenderOrderedByAmountDescending -> If there are no such transactions throw except
    @Test(expected = IllegalArgumentException.class)
    public void test_getBySenderOrderedByAmountDescending_ShouldThrow_NoSuchTransaction(){

        fillChainBlockTransactions();

        dataBase.getBySenderOrderedByAmountDescending("Anatoli");
    }

    //Method getByTransactionStatusAndMaximumAmount -> returns all transactions with given status and the amount less or equal to a maximum
    @Test
    public void test_getByTransactionStatusAndMaximumAmount(){

        fillChainBlockTransactions();

        Iterable <Transaction> expectedTransacation = transactions.stream()
                .filter(transaction -> transaction.getStatus().equals(TransactionStatus.SUCCESSFUL) &&
                 transaction.getAmount() < 18.20)
                .collect(Collectors.toList());

        Iterable<Transaction> actualTransaction = dataBase.getByTransactionStatusAndMaximumAmount(TransactionStatus.SUCCESSFUL, 18.20);

        assertEquals(expectedTransacation, actualTransaction);
    }

    //Method getBySenderAndMinimumAmountDescending ->returns all transactions with the given sender and amounts bigger than the given amount ordered by amount descending
    @Test
    public void test_getBySenderAndMinimumAmountDescending_Should_SortedAndFilter_Transaction(){

        fillChainBlockTransactions();

        Iterable <Transaction> expectedTransaction = transactions.stream()
                .filter(transaction -> transaction.getFrom().equals("Ivan") && transaction.getAmount() > 15)
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .collect(Collectors.toList());

        Iterable<Transaction> actualTransaction = dataBase.getBySenderAndMinimumAmountDescending("Ivan", 15);

        assertEquals(expectedTransaction,actualTransaction);

    }
    //Method getBySenderAndMinimumAmountDescending ->If there are no such transactions throw exception
    @Test (expected = IllegalArgumentException.class)
    public void test_getBySenderAndMinimumAmountDescending_Should_Throw_NoSuchTransaction(){

        fillChainBlockTransactions();

        dataBase.getBySenderAndMinimumAmountDescending("Anatoli", 1000);
    }

}
