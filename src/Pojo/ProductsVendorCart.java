package Pojo;
//$Id$

// POJO
public class ProductsVendorCart {
	private Products products;
	private String company;
	private double totalPrice;

	private ProductsVendorCart(Products products, String company, double totalPrice) {
		this.products = products;
		this.company = company;
		this.totalPrice = totalPrice;

	}
	
	public ProductsVendorCart() {}

	// Getters
	public Products getProducts() {
		return this.products;
	}

	public String getCompany() {
		return this.company;
	}

	public double getTotalPrice() {
		return this.totalPrice;
	}

	// ToString

	@Override
	public String toString() {
		return "ProductsVendorCart = [ " + ",products = " + products + ",company = " + company + ",totalPrice = "
				+ totalPrice;
	}

	// Since using it in different package just made it public
	public static class ProductsVendorCartBuilder {
		private Products products;
		private String company;
		private double totalPrice;

		public ProductsVendorCartBuilder() {
		}

		// Setters
		public ProductsVendorCartBuilder setProducts(Products products) {
			this.products = products;
			return this;
		}

		public ProductsVendorCartBuilder setCompany(String company) {
			this.company = company;
			return this;
		}

		public ProductsVendorCartBuilder setTotalPrice(double totalPrice) {
			this.totalPrice = totalPrice;
			return this;
		}

		// builder
		public ProductsVendorCart build() {
			return new ProductsVendorCart(this.products, this.company, this.totalPrice);
		}

	}

}
