package com.simon.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "stock")
@IdClass(Sku.class)
public class Stock implements Serializable {

	@Id
	@Column(name = "location", nullable = false)
	private String location;

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "quantity", nullable = false)
    private long quantity;


    public Stock() {

	}

	public Stock(String location, String code, long quantity) {
		this.location = location;
		this.code = code;
		this.quantity = quantity;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public void transferAddQuantity(long value) {
		quantity += value ;
	}

	public void transferMinusQuantity(long value) {
		quantity -= value ;
	}

	@Override
	public String toString() {
		return "Stock [location=" + location + ", code=" + code + ", quantity=" + quantity + "]";
	}
}
