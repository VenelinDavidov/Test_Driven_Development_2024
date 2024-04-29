public interface Transaction {

    int getId();

    void setStatus(TransactionStatus newStatus);

    TransactionStatus getStatus();

    double getAmount();

    String getFrom();
}
