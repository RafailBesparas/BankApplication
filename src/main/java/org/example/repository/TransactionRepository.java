package org.example.repository;

import org.example.model.AccountModel;
import org.example.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for accessing {Transaction} records.
 * <p>
 * This interface handles persistent read/write operations for all financial transactions
 * linked to user accounts. It plays a key role in:
 * <ul>
 *   <li>Transaction history display</li>
 *   <li>Balance reconciliation</li>
 *   <li>Audit trails</li>
 *   <li>Fraud monitoring</li>
 * </ul>
 *
 * <b>Audit & Compliance:</b> Every transaction is an immutable, traceable, and time-bound
 * financial event. Changes to transaction logic must pass through risk and compliance review.
 *
 * <b>Indexing:</b> The {@code account_id} foreign key should be indexed in the database
 * for performance when querying by account.
 *
 * <b>Usage:</b> Primarily consumed by {@link org.example.service.AccountService} for retrieving
 * transaction histories and persisting deposits, withdrawals, and transfers.
 *
 * @author Rafael Besparas
 */

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /**
     * Retrieves all transactions linked to a specific account.
     * <p>
     * Returned list is expected to be ordered by default insertion order (chronological).
     * Sorting or pagination should be handled in the service or query layer if needed.
     *
     * @param account the account associated with the transactions
     * @return a list of {@link Transaction} records
     */
    List<Transaction> findByAccount(AccountModel account);

    @Query("SELECT t FROM Transaction t WHERE t.account = :account " +
            "AND (:type IS NULL OR t.type = :type) " +
            "AND (:minAmount IS NULL OR t.amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.amount <= :maxAmount) " +
            "AND (:fromDate IS NULL OR t.timestamp >= :fromDate) " +
            "AND (:toDate IS NULL OR t.timestamp <= :toDate)")
    List<Transaction> searchTransactions(
            @Param("account") AccountModel account,
            @Param("type") String type,
            @Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

}
