package com.simon.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "products")
public class Product implements Serializable {

	@Id
	@Column(name = "code")
	private String code;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "weight", nullable = false)
	private long weight;

	public Product() {

	}

	public Product(String code, String name, long weight) {
		this.code = code;
		this.name = name;
		this.weight = weight;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getWeight() {
		return weight;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}


	@Override
	public String toString() {
		return "Product [code=" + code + ", name=" + name + ", weight=" + weight + "]";
	}
}
