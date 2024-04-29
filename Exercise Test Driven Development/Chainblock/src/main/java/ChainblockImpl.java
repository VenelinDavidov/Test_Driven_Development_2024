import java.util.*;
import java.util.stream.Collectors;

public class ChainblockImpl implements Chainblock {

    Map<Integer, Transaction> transactionsMap;

    //constructor
    public ChainblockImpl() {
        this.transactionsMap = new HashMap<>();
    }

    //methods
    public int getCount() {
        return this.transactionsMap.size();
    }

    public void add(Transaction transaction) {
        if (!contains(transaction)) {
            this.transactionsMap.put(transaction.getId(), transaction);
        }
    }

    public boolean contains(Transaction transaction) {
        // return true ako има такава транзакция
        // return false ako не се съдържа транзакцята
        return this.transactionsMap.containsValue(transaction);
    }

    public boolean contains(int id) {
        return this.transactionsMap.containsKey(id);
    }

    public void changeTransactionStatus(int id, TransactionStatus newStatus) {
        if (!contains(id)) {
            throw new IllegalArgumentException();
        }
        Transaction transactionForChange = transactionsMap.get(id);
        transactionForChange.setStatus(newStatus);
    }

    public void removeTransactionById(int id) {
        // ако няма транзакция с id
        if (!contains(id)) {
            throw new IllegalArgumentException("Invalid transaction id");
        }
        // ако има транзакция с id
        this.transactionsMap.remove(id);
    }

    public Transaction getById(int id) {
        if (!contains(id)) {
            throw new IllegalArgumentException("Invalid transaction id");
        }

        return transactionsMap.get(id);
    }

    public Iterable<Transaction> getByTransactionStatus(TransactionStatus status) {
        List<Transaction> filterTransaction = new ArrayList<>();

        for (Transaction transaction : transactionsMap.values()) {
            if (transaction.getStatus() == status) {
                filterTransaction.add(transaction);
            }
        }
        if (filterTransaction.size() == 0) {
            throw new IllegalArgumentException("Transactions with given not found");
        }

        filterTransaction.sort(Comparator.comparing(Transaction::getAmount).reversed());
        return filterTransaction;
    }

    public Iterable<String> getAllSendersWithTransactionStatus(TransactionStatus status) {

        List<Transaction> filterTransaction = new ArrayList<>();
        for (Transaction transaction : transactionsMap.values()) {
            if (transaction.getStatus() == status) {
                filterTransaction.add(transaction);
            }
        }
        if (filterTransaction.size() == 0) {
            throw new IllegalArgumentException("Transactions with given not found");
        }
        filterTransaction.sort(Comparator.comparing(Transaction::getAmount).reversed());
        return filterTransaction.stream().map(Transaction::getFrom).collect(Collectors.toList());
    }

    public Iterable<String> getAllReceiversWithTransactionStatus(TransactionStatus status) {
        return null;
    }

    public Iterable<Transaction> getAllOrderedByAmountDescendingThenById() {
        return null;
    }

    public Iterable<Transaction> getBySenderOrderedByAmountDescending(String sender) {
        return null;
    }

    public Iterable<Transaction> getByReceiverOrderedByAmountThenById(String receiver) {
        return null;
    }

    public Iterable<Transaction> getByTransactionStatusAndMaximumAmount(TransactionStatus status, double amount) {
        return null;
    }

    public Iterable<Transaction> getBySenderAndMinimumAmountDescending(String sender, double amount) {
        return null;
    }

    public Iterable<Transaction> getByReceiverAndAmountRange(String receiver, double lo, double hi) {
        return null;
    }

    public Iterable<Transaction> getAllInAmountRange(double lo, double hi) {
        return null;
    }

    public Iterator<Transaction> iterator() {
        return null;
    }
}
