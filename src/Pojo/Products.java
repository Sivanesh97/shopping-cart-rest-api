package Pojo;
//$Id$

// POJO
public class Products {
	private int id;
	private String name;
	private String category;
	private double price;
	private int quantity;
	private int saledCount;
	private float rating;
	private int vId;
	private String img;
	
	public Products() {}

	private Products(int id, String name, String category, double price, int quantity, int saledCount, float rating,
			int vId, String img) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.price = price;
		this.quantity = quantity;
		this.saledCount = saledCount;
		this.rating = rating;
		this.vId = vId;
		this.img = img;

	}


	// Getters
	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getCategory() {
		return this.category;
	}

	public double getPrice() {
		return this.price;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public int getSaledCount() {
		return this.saledCount;
	}

	public float getRating() {
		return this.rating;
	}

	public int getVId() {
		return this.vId;
	}
	
	public String getImg() {
		return this.img;
	}

	// ToString

	public int getvId() {
		return vId;
	}

	public void setvId(int vId) {
		this.vId = vId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setSaledCount(int saledCount) {
		this.saledCount = saledCount;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Override
	public String toString() {
		return "Products = [ " + ",id = " + id + ",name = " + name + ",category = " + category + ",price = " + price
				+ ",quantity = " + quantity + ",saledCount = " + saledCount + ",rating = " + rating + ",vId = " + vId;
	}

	// Since using it in different package just made it public
	public static class ProductsBuilder {
		private int id;
		private String name;
		private String category;
		private double price;
		private int quantity;
		private int saledCount;
		private float rating;
		private int vId;
		private String img;

		public ProductsBuilder() {
		}

		// Setters
		public ProductsBuilder setId(int id) {
			this.id = id;
			return this;
		}

		public ProductsBuilder setName(String name) {
			this.name = name;
			return this;
		}

		public ProductsBuilder setCategory(String category) {
			this.category = category;
			return this;
		}

		public ProductsBuilder setPrice(double price) {
			this.price = price;
			return this;
		}

		public ProductsBuilder setQuantity(int quantity) {
			this.quantity = quantity;
			return this;
		}

		public ProductsBuilder setSaledCount(int saledCount) {
			this.saledCount = saledCount;
			return this;
		}

		public ProductsBuilder setRating(float rating) {
			this.rating = rating;
			return this;
		}

		public ProductsBuilder setVId(int vId) {
			this.vId = vId;
			return this;
		}
		
		public ProductsBuilder setImg(String img) {
			this.img = img;
			return this;
		}

		// builder
		public Products build() {
			return new Products(this.id, this.name, this.category, this.price, this.quantity, this.saledCount,
					this.rating, this.vId, this.img);
		}

	}

}
