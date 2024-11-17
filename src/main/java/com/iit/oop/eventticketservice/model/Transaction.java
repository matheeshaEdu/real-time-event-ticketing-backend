package com.iit.oop.eventticketservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    private Integer quantity;
    private Integer totalPrice;

    public Transaction() {
    }

    public Transaction(Customer customer, Ticket ticket, Integer quantity, Integer totalPrice) {
        this.customer = customer;
        this.ticket = ticket;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", customer=" + customer.getName() +
                ", ticket=" + ticket.getName() +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
