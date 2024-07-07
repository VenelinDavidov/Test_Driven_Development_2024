public interface Transaction {

    int getId ();

    void setNewStatus (TransactionStatus newStatus);

    TransactionStatus getStatus ();

    double getAmount ();

    String getFrom ();

    String getTo();
}
