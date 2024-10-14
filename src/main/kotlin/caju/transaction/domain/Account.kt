package caju.transaction.domain

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "account")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_gen")
    @SequenceGenerator(name = "account_gen", sequenceName = "sq_account_id", allocationSize = 1)
    @Column(name = "id_account")
    val id: Int? = null,

    @Column(name = "num_meal_balance")
    val mealBalance: Double,

    @Column(name = "num_food_balance")
    val foodBalance: Double,

    @Column(name = "num_cash_balance")
    val cashBalance: Double,

    @OneToMany(mappedBy = "account", cascade = [ALL], orphanRemoval = true, fetch = LAZY)
    @JsonManagedReference
    val transactions: MutableList<Transaction> = mutableListOf(),
)
