package com.jamesdube.cache;

import javax.persistence.*;
import java.io.Serializable;

import static java.lang.Long.valueOf;

@Entity
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column
    private String productCode;

    public Transaction() {
    }

    public Transaction(int id,String productCode) {
        this.id = (long) id;
        this.productCode = productCode;
    }

    public Transaction(String productCode) {
        this.productCode = productCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
