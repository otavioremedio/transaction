package caju.transaction.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "transaction")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_gen")
    @SequenceGenerator(name = "transaction_gen", sequenceName = "sq_transaction_id", allocationSize = 1)
    @Column(name = "id_transaction")
    val id: Int? = null,

    @Column(name = "num_amount")
    val amount: Double,

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "id_account", nullable = false)
    @JsonBackReference
    var account: Account,
) {
    override fun toString() =
        StringBuilder("Transaction(")
            .append("id=$id, ")
            .append("amount=$amount, ")
            .append(")")
            .toString()
}
