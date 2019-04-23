package Shopping;
import Pojo.History;
import Pojo.Products;
import Pojo.Vendor;

//$Id$

public class ProductVendor {
	Products products;
	Vendor vendor;
	History history;
	int total;
	
	public ProductVendor() {}

	
	public Products getProducts() {
		return products;
	}


	public void setProducts(Products products) {
		this.products = products;
	}


	public Vendor getVendor() {
		return vendor;
	}


	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}


	public History getHistory() {
		return history;
	}


	public void setHistory(History history) {
		this.history = history;
	}


	public int getTotal() {
		return total;
	}


	public void setTotal(int total) {
		this.total = total;
	}


	public ProductVendor(Products products, Vendor vendor) {
		super();
		this.products = products;
		this.vendor = vendor;
	}

	public ProductVendor(Products products, Vendor vendor, History history, int total) {
		super();
		this.products = products;
		this.vendor = vendor;
		this.history = history;
		this.total = total;
	}
	
	@Override
	public String toString() {
		return "ProductVendor [products=" + products + ", vendor=" + vendor + ", history=" + history + ", total="
				+ total + "]";
	}
}
