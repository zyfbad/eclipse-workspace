package com.zyf.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "goodmessage")
public class Goods {
	@Id
	private String goo_code;
	@Column
	private String goo_name;
	@Column
	private float goo_inprice;
	@Column
	private float goo_outprice;
	@Column
	private int goo_store;

	public String getGoo_code() {
		return goo_code;
	}

	public void setGoo_code(String goo_code) {
		this.goo_code = goo_code;
	}

	public String getGoo_name() {
		return goo_name;
	}

	public void setGoo_name(String goo_name) {
		this.goo_name = goo_name;
	}

	public float getGoo_inprice() {
		return goo_inprice;
	}

	public void setGoo_inprice(float goo_inprice) {
		this.goo_inprice = goo_inprice;
	}

	public float getGoo_outprice() {
		return goo_outprice;
	}

	public void setGoo_outprice(float goo_outprice) {
		this.goo_outprice = goo_outprice;
	}

	public int getGoo_store() {
		return goo_store;
	}

	public void setGoo_store(int goo_store) {
		this.goo_store = goo_store;
	}
}
