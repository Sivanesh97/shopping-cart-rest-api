package Shopping;

//$Id$

import java.sql.*;
import java.util.*;

import Pojo.Address;
import Pojo.Bill;
import Pojo.Cart;
import Pojo.Products;
import Pojo.ProductsVendorCart;
import Pojo.UserBills;
import Pojo.Users;

public class Customer {
	String username;
	static int id;
	int billId;
	Scanner d = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println(
			new Customer().billList(48) + "Working"
		);
	}

	public static Users getUser(int userId) {
		return Sql.getUser(userId);
	}
	
	public static String insert(int id, int productId, int quantity) {
		Sql.beginTransaction();

		// Checking whether already a product is present or not.
		if (Sql.isProductPresent("products", productId)) {
			System.out.printf("User ID = %d\n", id);
			System.out.println("Enter quantity");

			if (Sql.isAvailable(productId, quantity)) {

				if (Sql.insertions("cart", "(userId, productId, quantity)",
						String.format("%d, %d, %d", id, productId, quantity))) {
					Sql.commitTransaction();
					System.out.println("Inserted successfully");
					return "Success";
				} else {
					Sql.rollbackTransaction();
					return "failed: Insertion not made, Already element present.";
				}
			} else {
				Sql.rollbackTransaction();

				System.out.println("Check the stock available and enter again.");
				return "failed: Stock value exceeded";
			}

		} else {
			Sql.rollbackTransaction();
			System.out.println("Not a valid ID enter another.");
			return "failed: Invalid ID";
		}

	}
	
	public static boolean update(int id, int productId, int quantity) {

		if (Sql.isHisProduct(productId, id)) {
			if (Sql.isAvailable(productId, quantity)) {
				Sql.updations("cart", "quantity = " + quantity, "productId = " + productId);
				System.out.println("Updated Successfully");
				return true;
			} else {
				System.out.println("Check the quantity available and enter carefully.");
				return false;
			}
		} else {
			System.out.println("No product found");
			return false;
		}
	}


	public static boolean delete(int id, int productId) {
		if(Sql.deletions("cart", String.format("userId = %d AND productId = %d", id, productId))) {
			System.out.println("Deleted the product from the cart successfully");
			return true;
		}
		System.out.println("Failed: DELETION FROM CART");
		return false;
		
	}
	
	

	public static List<ProductVendor> productPreview() {
		return Sql.productPreview();
	}

	public static boolean buy(int id) {
			int total = Sql.getTotal(id);
			if (total == 0) {
				System.out.println("No products bought.");
				return false;
			} else {

//				// Address
//				int addressId;
//				switch (choice) {
//				case 1:
//					addressId = assignPermanentAddress(id);
//					break;
//				case 2:
//					addressId = chooseAddress(id);
//					break;
//				case 3:
//					// TODO get address and add an extra parameter Address
////					addressId = Auth.updateAddress(username);
//					addressId = 1;
//					break;
//				default:
//					addressId = assignPermanentAddress(id);
//				}

				// printAddress();

				// INSERTION in bill table
				Sql.beginTransaction();
				
				if(!Sql.insertions("bill", "(userId, timestamp, total)",
						String.format("%d, CURRENT_TIMESTAMP, %d", id, Sql.getTotal(id)))) {
					Sql.rollbackTransaction();
					return false;
				}
				

				// getBillId
				int billId = Sql.getCurrentBillId();

				// INSERTION in history table
				ArrayList<Cart> carts = Sql.getCartProducts(id);

				for (int i = 0; i < carts.size(); i++) {

					// Statement smt1 = Sql.con.createStatement();
					if(!Sql.insertions("history", "(billId, pId, quantity)", String.format("%d, %d, %d", billId,
							carts.get(i).getProductId(), carts.get(i).getQuantity()))) {
						Sql.rollbackTransaction();
						return false;
					}
					
					
					if(!Sql.updations("products",
							String.format("quantity = quantity - %d, saledCount = saledCount + %d",
									carts.get(i).getQuantity(), carts.get(i).getQuantity()),
							"id = " + carts.get(i).getProductId())) {
								Sql.rollbackTransaction();
						return false;
					}

				}

				// DELETE all (From particular user) From Cart table
				Sql.deletions("cart", "userId = " + id);

				// Update details in Products (QUANTITY)
				System.out.println("Buyed successfully");
				System.out.println("Thanks for Coming... <3");
				Sql.commitTransaction();
				return true;
			}
	}

	public static int assignPermanentAddress(int id) throws SQLException {
		int addressId = Sql.assignPermanentAddress(id);
		if (addressId != -1) {
			return addressId;
		} else {
			System.out.println("No permanent address found.");
			return chooseAddress(id);
		}
	}

	public static int chooseAddress(int id) throws SQLException {
		System.out.println("--------- Address lists ------------");		
//		TODO Hardcoded as 1 in below line. Check and change
		int address = Sql.getAddressId(1, id);
		
		if (address != -1) {
			return Sql.rs.getInt(1);
		} else {
			System.out.println("Wrong addressId chosen. Choose again");
			return chooseAddress(id);
		}
	}
	


	public static ArrayList<Address> getAllAddresses(int id) {		
		return Sql.getUserAddresses(id);
	}

	public static ArrayList<ProductVendor> viewBill(int billId)  {
			//TODO Get Bill total in calling Sql.getBillTotal(billId)
			return Sql.getProducts(billId);
	}
	
	public static List<ProductsVendorCart> viewCart(int userId) {
		return Sql.viewCart(userId);
	}

	
	public static boolean clearCart(int userId) {
		System.out.println("--- Clear Cart ---");
		return Sql.deletions("cart", "userId = " + userId);
	}

	
	
	public static ArrayList<Bill> billList( int userId) {
			return Sql.allUserBills(userId);
	}


	public static ArrayList<Products> viewWishList(int userId) {
		System.out.println("--- Wish List <3 ---");

		return Sql.viewWishList(userId);
	}

	public static boolean addToWishList(int userId, int pId) {
		if(Sql.insertions("wishlist", "(uid, pid)", String.format("%d, %d", userId, pId))) {
			System.out.println("Added wish List successfully");
			return true;
		} else {
			System.out.println("Already this product is present.");
			return false;
		}
	}

	public static boolean clearWishList(int userId) {
		if(Sql.deletions("wishlist", "uid = " + userId)) {
			System.out.println("Cleared wish List");
			return true;
		} else {
			System.out.println("There are no Items in wishlist");
			return false;
		}
		
	}

	public static boolean removeFromWishList(int userId, int choice) {
		if(Sql.removeFromWishList(userId, choice)) {
			System.out.println("Removed Item from wishlist successfully");	
			return true;
		} else {
			System.out.println("Choose product correctly`");
			return false;
		}

	}

	/*
	
	public static void logout() throws SQLException {
		System.out.println("Logged out " + username + " successfully.");
		Auth.mainMenu();
	}
	
	
	*/
}
	