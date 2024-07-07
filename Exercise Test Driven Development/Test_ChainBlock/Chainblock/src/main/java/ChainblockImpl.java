import java.util.*;
import java.util.stream.Collectors;

public class ChainblockImpl implements Chainblock {

    // this class implement Chainblock -> extends Transaction

    private Map<Integer, Transaction> transactionMap;     // Map -> unique Key

    //Constructor
    public ChainblockImpl() {
        this.transactionMap = new LinkedHashMap<>();
    }

    //Methods
    @Override
    public int getCount() {
        return transactionMap.size();
    }

    @Override
    public void add(Transaction transaction) {

        transactionMap.putIfAbsent(transaction.getId(), transaction);
    }

    @Override
    public boolean contains(Transaction transaction) {

        return contains(transaction.getId());
    }

    @Override
    public boolean contains(int id) {
        return transactionMap.containsKey(id);
    }

    @Override
    public void changeTransactionStatus(int id, TransactionStatus newStatus) {

        if (!transactionMap.containsKey(id)) {
            throw new IllegalArgumentException();
        }
        Transaction transaction = getById(id);
        transaction.setNewStatus(newStatus);
    }

    @Override
    public void removeTransactionById(int id) {
        if (!transactionMap.containsKey(id)) {
            throw new IllegalArgumentException();
        }
        transactionMap.remove(id);
    }

    @Override
    public Transaction getById(int id) {

        if (!transactionMap.containsKey(id)) {
            throw new IllegalArgumentException();
        }
        return transactionMap.get(id);
    }

    @Override
    public Iterable<Transaction> getByTransactionStatus(TransactionStatus status) {

        List<Transaction> transactions = transactionMap.values().stream()
                .filter(transaction -> transaction.getStatus().equals(status))
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .collect(Collectors.toList());

        if ( transactions.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return transactions;
    }

    @Override
    public Iterable<String> getAllSendersWithTransactionStatus(TransactionStatus status) {

        List<Transaction> byTransactionStatus = convertIterableToList(getByTransactionStatus(status));

        return byTransactionStatus.stream()
                .map(Transaction::getFrom)
                .collect(Collectors.toList());

    }

    // Methods with whose convert Iterable to List
    private List<Transaction> convertIterableToList(Iterable<Transaction> iterable) {
        List<Transaction> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    @Override
    public Iterable<String> getAllReceiversWithTransactionStatus(TransactionStatus status) {

        List<Transaction> byTransactionsReceiver = convertIterableToList(getByTransactionStatus(status));

        return byTransactionsReceiver.stream()
                .map(Transaction::getTo)
                .collect(Collectors.toList());

    }

    @Override
    public Iterable<Transaction> getAllOrderedByAmountDescendingThenById() {
     return  transactionMap.values()
                .stream()
                .sorted(Comparator.comparing(Transaction::getAmount).reversed().thenComparing(Transaction::getId))
               .collect(Collectors.toList());
    }

    @Override
    public Iterable<Transaction> getBySenderOrderedByAmountDescending(String sender) {

        List<Transaction> sortedTransaction = transactionMap.values()
                .stream()
                .filter(transaction -> transaction.getFrom().equals(sender))
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .collect(Collectors.toList());
        if (sortedTransaction.isEmpty()){
            throw new IllegalArgumentException();
        }
        return sortedTransaction;
    }

    @Override
    public Iterable<Transaction> getByReceiverOrderedByAmountThenById(String receiver) {

        List<Transaction> sortedTransaction = convertIterableToList(getAllOrderedByAmountDescendingThenById());

        List<Transaction> actual = sortedTransaction.stream()
                .filter(transaction -> transaction.getTo().equals(receiver))
                .collect(Collectors.toList());

        if (actual.isEmpty()){
            throw new IllegalArgumentException();
        }
        return actual;
    }

    @Override
    public Iterable<Transaction> getByTransactionStatusAndMaximumAmount(TransactionStatus status, double amount) {
       return transactionMap.values()
                .stream()
                .filter(transaction -> transaction.getStatus().equals(status) && transaction.getAmount() < amount)
                .collect(Collectors.toList());

    }

    @Override
    public Iterable<Transaction> getBySenderAndMinimumAmountDescending(String sender, double amount) {
        List <Transaction> result = transactionMap.values()
                .stream()
                .filter(transaction -> transaction.getFrom().equals(sender) && transaction.getAmount() > amount)
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .collect(Collectors.toList());

        if (result.isEmpty()){
            throw new IllegalArgumentException();
        }
        return result;
    }

    @Override
    public Iterable<Transaction> getByReceiverAndAmountRange(String receiver, double lo, double hi) {

        List<Transaction> transactionByRange = convertIterableToList(getAllInAmountRange(lo, hi));

        List<Transaction> transactionsByRangeAndReceiver =
         transactionByRange.stream()
         .filter(transaction -> transaction.getTo().equals(receiver))
         .sorted(Comparator.comparing(Transaction::getAmount).reversed())
         .collect(Collectors.toList());

        if (transactionsByRangeAndReceiver.isEmpty()){
            throw new IllegalArgumentException();
        }

        return transactionsByRangeAndReceiver;
    }

    @Override
    public Iterable<Transaction> getAllInAmountRange(double low, double high) {
        return transactionMap.values()
                .stream()
                .filter(transaction -> transaction.getAmount() > low && transaction.getAmount() < high)
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<Transaction> iterator() {
        return null;
    }
}