package com.soham.shopapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String description;

    @Column(name = "Brand")
    private String brand;

    @Column(name = "Price")
    private BigDecimal price;

    @Column(name = "Category")
    private String category;

    @Column(name = "ReleaseDate")
    private Date releaseDate;

    @Column(name = "Available")
    private boolean available;

    @Column(name = "Quantity")
    private int Quantity;
}
