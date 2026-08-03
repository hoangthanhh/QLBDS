package com.qlbds.entity;

import com.qlbds.constant.PropertyTypeEnum;
import javax.persistence.*;

@Entity
@Table(name = "transaction_property_snapshot")
public class TransactionPropertySnapshot {

    @Id
    @Column(name = "transaction_id")
    private Integer transactionId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "address", length = 500, nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false)
    private PropertyTypeEnum propertyType;

    @Column(name = "price", nullable = false)
    private Long price;

    // GETTERS & SETTERS
    public Integer getTransactionId() { return transactionId; }
    public void setTransactionId(Integer transactionId) { this.transactionId = transactionId; }
    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public PropertyTypeEnum getPropertyType() { return propertyType; }
    public void setPropertyType(PropertyTypeEnum propertyType) { this.propertyType = propertyType; }
    public Long getPrice() { return price; }
    public void setPrice(Long price) { this.price = price; }
}