package com.holafresco.catalog.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal caloriesPerUnit;
    private BigDecimal stockLevel;
    private String unit;

    protected Ingredient() { }

    public Ingredient(String name,
                      BigDecimal caloriesPerUnit,
                      BigDecimal stockLevel,
                      String unit) {
        this.name = name;
        this.caloriesPerUnit = caloriesPerUnit;
        this.stockLevel = stockLevel;
        this.unit = unit;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getCaloriesPerUnit() { return caloriesPerUnit; }
    public void setCaloriesPerUnit(BigDecimal caloriesPerUnit) { this.caloriesPerUnit = caloriesPerUnit; }
    public BigDecimal getStockLevel() { return stockLevel; }
    public void setStockLevel(BigDecimal stockLevel) { this.stockLevel = stockLevel; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
