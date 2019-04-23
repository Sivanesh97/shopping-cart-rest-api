package Shopping;
import java.util.*;


import Pojo.Products;

public class Seller {
	Scanner d = new Scanner(System.in);
	Checker c = Checker.getInstance();
	String username;
	int id;

	protected void main(String username) {
		this.username = username;
		this.id = Sql.getId("vendor", username);
//		mainMenu();
	}
	
//	public static void main(String[] args) {
//		System.out.println(getVendorDetails(4));
//	}
	
	public static Pojo.Vendor getVendorDetails(int vId) {
		return Sql.getVendorDetails(vId);
	}

	public static boolean insert(Products products) {
		if (Sql.insertions("products", "(name, vId, category, price, quantity, img)",
				String.format("'%s', %d, '%s', '%s', '%s', '%s'", products.getName(), 
						products.getVId(), 
						products.getCategory(), 
						products.getPrice(), 
						products.getQuantity(),
						products.getImg()
						))) {
			System.out.println("Inserted successfully");
		} else {
			System.err.println("There's Error in insertion");
			return false;
		}

		System.out.println();
		return true;

	}
	
	public static boolean update(int vId, Products product) {
		System.out.println("Vendor: Product Updation");
		if (!Sql.isVendorProduct(vId, product.getId())) {
			System.out.println("Wrong id used.");
			return false;
		}

		return updatePriceQuantity(product.getId(), product);
	}

	private static boolean updatePriceQuantity(int id, Products product) {

		if(Sql.updations("products", "price = " + product.getPrice() 
				 + ", quantity =  " + product.getQuantity()
				 + ", name = '" + product.getName()
				 + "', category = '" + product.getCategory() + "'", 
				"id = " + id)) {
			System.out.println("Updated successfully.");
			return true;
		} else {
			System.out.println("There's some error in Updation. Update correctly or contact Admin.");
			return false;
		}
	}
	

	public static boolean delete(int vId, int deleteId) {
		System.out.println("DELETION");
		if (deleteId == 0) {
			return false;
		}
		
		if (Sql.isVendorProduct(vId, deleteId)) {
			Sql.deletions("products", "id = " + deleteId);

			System.out.println("Deletion made successfully");
			return true;
		} else {
			System.out.println("Wrong Product id is given.");
			return false;
		}
	}

	

}