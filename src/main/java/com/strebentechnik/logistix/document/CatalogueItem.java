package com.strebentechnik.logistix.document;

import javax.persistence.*;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import java.util.Date;

@Entity
@Table(name = "catalogue_items", uniqueConstraints = {
        @UniqueConstraint(columnNames = "sku")
})
public class CatalogueItem extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    public Long id;

    @Column(unique = true, nullable = false, length = 16)
    public String sku;

    @Column(unique = true, nullable = false, length = 255)
    public String name;

    @Column(nullable = false, length = 500)
    public String description;

    @Column(nullable = false)
    public String category;

    @Column(nullable = false, precision = 10, scale = 2)
    public Double price;

    @Column(nullable = false)
    public Integer inventory;

    @Column(nullable = false, length = 19)
    public Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true, length = 19)
    public Date updatedOn;
}
