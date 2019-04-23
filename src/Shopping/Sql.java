package Shopping;

//$Id$
import java.sql.*;
import java.util.*;

import javax.persistence.OrderBy;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

import Pojo.Address;
import Pojo.Bill;
import Pojo.Cart;
import Pojo.History;
import Pojo.Products;
import Pojo.ProductsVendor;
import Pojo.ProductsVendorCart;
import Pojo.UserBills;
import Pojo.Users;
import Pojo.Vendor;

/*
 * INSERTIONS, UPDATIONS, DELETIONS alone shares a common method.
 * SELECTIONS alone requires different methods to call for, for Different POJOS
 */

// Singleton Class - It reduces many object creations.
public class Sql {
	static Connection con;
	static Statement smt;
	static ResultSet rs;
	static Sql sql = null;

	static {
		try {
			getInstance();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
		}
	}

	// SQL Utils
	protected static Sql getInstance() throws SQLException {
		if (sql == null) {
			Sql sql = new Sql();
			sql.dbConnect();
		}
		return sql;
	}

	protected void dbConnect() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopping?useSSL=false", "root", "");
		smt = con.createStatement();
	}

	// Common
	protected static boolean insertions(String table, String columns, String data) {
		String query = "INSERT INTO " + table + " " + columns + " VALUES (" + data + ")";
		System.out.println(query);
		try {
			smt.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
			return false;
		}
		return true;
	}

	protected static boolean updations(String table, String datas, String conditions) {
		String query = "UPDATE " + table + " SET " + datas + " WHERE " + conditions;
		System.out.println(query);
		try {

			if (smt.executeUpdate(query) == 0)
				return false;
			System.out.println("Updatedssss");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error Occured in Updations");
			return false;
		}
		return true;
	}

	protected static boolean deletions(String table, String conditions) {

		String checker = "DELETE FROM " + table + " WHERE " + conditions;
		try {
			if (smt.executeUpdate(checker) > 0)
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	protected static void beginTransaction() {
		try {
			smt.execute("BEGIN");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
		}
	}

	protected static void commitTransaction() {
		try {
			smt.execute("COMMIT");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
		}
	}

	protected static void rollbackTransaction() {
		try {
			smt.execute("ROLLBACK");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
		}
	}

	public static int signIn(boolean isVendor, String username, String password) {
		String table = isVendor ? "vendor" : "users";

		String str = "SELECT id FROM " + table + " WHERE username='" + username + "' AND password='" + password + "'";
		System.out.println(str);

		try {
			rs = smt.executeQuery(str);
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in");
		}
		return -1;
	}

	// Users
	public static Users getUser(int userId) {
		try {
			rs = smt.executeQuery("SELECT * FROM users WHERE id = " + userId);
			if (rs.next()) {
				Users users = new Users.UsersBuilder().setId(rs.getInt("id")).setUsername(rs.getString("username"))
						.setPhone(rs.getString("phone")).build();
				return users;

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected static String getPassword(String username, String table) {
		try {
			rs = smt.executeQuery("SELECT username, password FROM " + table + " WHERE username = '" + username + "'");
			if (rs.next())
				return rs.getString("password");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
		}
		return null;
	}

	protected static boolean isUserAlreadyPresent(String username, boolean isVendor) {
		try {
			rs = smt.executeQuery(
					"SELECT username FROM " + (isVendor ? "vendor" : "users") + " WHERE username = '" + username + "'");
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
		}
		return false;
	}

	public static int getId(String table, String username) {
		try {
			rs = smt.executeQuery("SELECT id FROM " + table + " WHERE username = '" + username + "'");
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
		}
		return -1;
	}

	protected static int getAddress() {
		try {
			rs = smt.executeQuery("SELECT MAX(addressId) FROM address");
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return -1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
			return -1;
		}
	}

	public static List<ProductsVendor> viewProductsVendor(int vId, int offset, int limit) {
		List<ProductsVendor> productsVendors = new ArrayList<>();
		String query = "SELECT products.*, company FROM products INNER JOIN vendor ON products.vId = vendor.id";
		if (vId != -1) {
			query += " WHERE vId = " + vId;
		}
		if (offset != -1) {
			query += " LIMIT " + offset + ", " + limit;
		}
		try {
			Statement smt = con.createStatement();
			ResultSet rs;
			rs = smt.executeQuery(query);
			while (rs.next()) {
				Products products = new Products.ProductsBuilder().setId(rs.getInt("id"))
						.setQuantity(rs.getInt("quantity")).setName(rs.getString("name"))
						.setPrice(rs.getDouble("price")).setRating(rs.getFloat("rating"))
						.setCategory(rs.getString("category")).setVId(rs.getInt("vId")).setImg(rs.getString("img"))
						.build();

				Vendor vendor = new Vendor.VendorBuilder().setCompany(rs.getString("company")).build();

				ProductsVendor productsVendor = new ProductsVendor.ProductsVendorBuilder().setProducts(products)
						.setVendor(vendor).build();

				productsVendors.add(productsVendor);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return productsVendors;
	}

	// Customers
	public static List<Products> getAllProducts() {

		List<Products> products = new ArrayList<>();

		try {
			rs = smt.executeQuery("SELECT * FROM products");

			while (rs.next()) {
				Products products2 = new Products.ProductsBuilder().setId(rs.getInt("id")).setName(rs.getString("name"))
						.setCategory(rs.getString("category")).setName(rs.getString("name"))
						.setPrice(rs.getDouble("price")).setQuantity(rs.getInt("quantity"))
						.setRating(rs.getFloat("rating")).setVId(rs.getInt("vId")).build();

				products.add(products2);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return products;

	}

	protected static boolean isProductPresent(String table, int id) {
		try {
			rs = smt.executeQuery("SELECT id FROM " + table + " WHERE id = " + id);
			if (rs.next())
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
		}
		return false;
	}

	protected static boolean isAvailable(int productId, int quantity) {
		try {
			rs = smt.executeQuery(String.format("SELECT quantity FROM products WHERE id = %d", productId));
			if (rs.next()) {
				if (rs.getInt(1) >= quantity) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	protected static boolean isHisProduct(int productId, int id) {
		try {
			rs = smt.executeQuery(
					String.format("SELECT productId FROM cart WHERE userId = %d AND productId = %d", id, productId));
			if (rs.next())
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<ProductVendor> productPreview() {
		ArrayList<ProductVendor> productVendors = new ArrayList<>();

		try {
			rs = smt.executeQuery("SELECT products.id," + "products.name," + "products.category," + "products.price,"
					+ "products.quantity," + "vendor.username," + "vendor.company," + "products.saledCount, "
					+ "products.rating, "

					+ "vendor.rating " + "FROM vendor " + "INNER JOIN " + "products ON products.vid = vendor.Id");

			while (rs.next()) {
				Products.ProductsBuilder products = new Products.ProductsBuilder();
				Vendor.VendorBuilder vendor = new Vendor.VendorBuilder();
				products.setId(rs.getInt("id"));
				products.setName(rs.getString("name"));
				products.setCategory(rs.getString("category"));
				products.setPrice(rs.getInt("price"));
				products.setQuantity(rs.getInt("quantity"));
				vendor.setUsername(rs.getString("username"));
				vendor.setCompany(rs.getString("company"));
				products.setRating(rs.getInt("rating"));
				products.setSaledCount(rs.getInt("saledCount"));
				products.setRating(rs.getFloat("rating"));

				ProductVendor productVendor = new ProductVendor(products.build(), vendor.build());
				productVendors.add(productVendor);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return productVendors;

	}

	protected static int getCurrentBillId() {
		try {
			rs = smt.executeQuery("SELECT MAX(billId) FROM bill");
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	protected static ArrayList<Cart> getCartProducts(int id) {
		ArrayList<Cart> carts = new ArrayList<>();
		try {
			rs = smt.executeQuery("SELECT productId, quantity FROM cart WHERE userId = " + id);
			while (rs.next()) {
				carts.add(new Cart.CartBuilder().setProductId(rs.getInt("productId")).setQuantity(rs.getInt("quantity"))
						.build());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return carts;

	}

	protected static int assignPermanentAddress(int id) {
		try {
			rs = smt.executeQuery("SELECT addressId FROM address WHERE id = " + id + " AND permanent = 1");
			if (rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	protected static int getAddressId(int addressId, int userId) {
		try {
			rs = smt.executeQuery(String.format("SELECT addressId, id FROM address WHERE addressId = %d AND id = %d",
					addressId, userId));
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	protected static ArrayList<Address> getUserAddresses(int userId) {
		ArrayList<Address> addresses = new ArrayList<>();
		try {
			rs = smt.executeQuery("SELECT * FROM address WHERE id = " + userId);
			while (rs.next()) {
				Address address = new Address.AddressBuilder().setAddressId(rs.getInt("addressId"))
						.setNumber(rs.getString("number")).setStreet(rs.getString("street"))
						.setLandmark(rs.getString("landmark")).setCity(rs.getString("city"))
						.setPincode(rs.getInt("pincode")).setState(rs.getString("state"))
						.setCountry(rs.getString("country")).setPermanent(rs.getByte("permanent")).build();
				addresses.add(address);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addresses;
	}

	protected static ArrayList<ProductVendor> getProducts(int billId) {

		ArrayList<ProductVendor> productVendors = new ArrayList<>();

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT ");
		stringBuilder.append("products.name, ");
		stringBuilder.append("products.id, ");
		stringBuilder.append("vendor.company, ");
		stringBuilder.append("history.quantity, ");
		stringBuilder.append("products.price AS `Unit price`, ");
		stringBuilder.append("history.quantity * products.price AS `Total Price` ");
		stringBuilder.append("FROM history ");
		stringBuilder.append("INNER JOIN products ON products.id = history.pId ");
		stringBuilder.append("INNER JOIN vendor ON vendor.id = products.vId ");
		stringBuilder.append("WHERE history.billId = ");
		stringBuilder.append(billId);

		try {
			Statement smt = con.createStatement();
			ResultSet rs;
			rs = smt.executeQuery(String.format(stringBuilder.toString()));
			System.err.println(true);
			while (rs.next()) {
				Products products = new Products.ProductsBuilder().setName(rs.getString("name")).setId(rs.getInt("id"))
						.setPrice(rs.getInt("unit price")).build();

				History history = new History.HistoryBuilder().setQuantity(rs.getInt("quantity"))

						.build();

				Vendor vendor = new Vendor.VendorBuilder().setCompany(rs.getString("company")).build();

				ProductVendor productVendor = new ProductVendor(products, vendor, history, rs.getInt("total price"));
				productVendors.add(productVendor);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return productVendors;

	}

	public static boolean updateCompany(Vendor vendor) {
		try {
			Statement statement = con.createStatement();
			String str = "UPDATE vendor SET company = '" + vendor.getCompany() + "' WHERE username = '"
					+ vendor.getUsername() + "'";
			System.out.println(str);
			int update = statement.executeUpdate(str);
			if (update > 0)
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	protected static int getBillTotal(int billId) {
		try {
			rs = smt.executeQuery("SELECT total FROM bill WHERE billId = " + billId);
			if (rs.next())
				return rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	protected static ArrayList<ProductsVendorCart> viewCart(int userId) {

		ArrayList<ProductsVendorCart> productsVendorCarts = new ArrayList<>();

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT ");
		stringBuilder.append("products.id,");
		stringBuilder.append("products.name,");
		stringBuilder.append("products.img,");
		stringBuilder.append("products.category,");
		stringBuilder.append("vendor.company,");
		stringBuilder.append("products.price AS `Unit Price`,");
		stringBuilder.append("cart.quantity,");
		stringBuilder.append("cart.quantity * products.price AS total ");
		stringBuilder.append("FROM cart ");
		stringBuilder.append("INNER JOIN products ON cart.productId = products.id ");
		stringBuilder.append("INNER JOIN vendor ON products.vId = vendor.id ");
		stringBuilder.append("WHERE userId = ");
		stringBuilder.append(userId);
		try {
			Statement smt = con.createStatement();
			ResultSet rs;
			rs = smt.executeQuery(stringBuilder.toString());
			while (rs.next()) {
				Products products = new Products.ProductsBuilder().setId(rs.getInt("id")).setName(rs.getString("name"))
						.setCategory(rs.getString("category")).setPrice(rs.getDouble("unit price"))
						.setQuantity(rs.getInt("quantity")).setImg(rs.getString("img")).build();

				ProductsVendorCart productsVendorCart = new ProductsVendorCart.ProductsVendorCartBuilder()
						.setProducts(products).setCompany(rs.getString("company")).setTotalPrice(rs.getDouble("total"))
						.build();
				productsVendorCarts.add(productsVendorCart);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productsVendorCarts;
	}

	public static int getTotal(int userId) {
		try {
			rs = smt.executeQuery("SELECT SUM(cart.quantity * products.price) FROM cart"
					+ " INNER JOIN products ON products.id = cart.productId " + "WHERE userId = " + userId);
			if (rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static ArrayList<Bill> allUserBills(int id) {
		ArrayList<Bill> userBills = new ArrayList<>();

		try {
			Statement smt = con.createStatement();
			ResultSet rs;
			rs = smt.executeQuery("SELECT * FROM bill WHERE userId = " + id);
			while (rs.next()) {
				Bill bill = new Bill.BillBuilder().setUserId(rs.getInt("userId"))
						.setTimestamp(rs.getString("timestamp")).setAddressId(rs.getInt("billId"))
						.setTotal(rs.getInt("total")).setBillId(rs.getInt("billId")).build();

				userBills.add(bill);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(userBills.size() + "Sizes check");
		return userBills;
	}

	public static boolean isBillIdExist(int userId, int bill) {
		try {
			rs = smt.executeQuery(
					String.format("SELECT billId FROM bill WHERE userId = %d AND billId = %d", userId, bill));
			if (rs.next())
				return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	protected static ArrayList<Products> viewWishList(int userId) {
		ArrayList<Products> productsArray = new ArrayList<>();
		try {
			Statement smt = con.createStatement();
			
			ResultSet rs = smt.executeQuery("SELECT products.id, products.img, products.name, products.price FROM products "
					+ "INNER JOIN wishlist ON wishlist.pid = products.id WHERE wishlist.uid = " + userId);
			while (rs.next()) {
				Products products = new Products.ProductsBuilder().setName(rs.getString("name")).setId(rs.getInt("id"))
						.setImg(rs.getString("img"))
						.setPrice(rs.getDouble("price")).build();
				productsArray.add(products);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productsArray;
	}

	public static boolean removeFromWishList(int id, int choice) {
		try {
			rs = smt.executeQuery("SELECT pid from wishList WHERE uid = " + id + " AND pid = " + choice);
			if (rs.next()) {
				smt.execute("DELETE FROM wishList WHERE uid = " + id + " AND pid = " + choice);
				return true;
			} else {
				System.out.println("Issue is here");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	public static List<String> getCategories() {
		List<String> categories = new ArrayList<>();
		try {
			Statement smt = con.createStatement();
			ResultSet rs = smt.executeQuery("SELECT DISTINCT category FROM products");
			while (rs.next()) {
				categories.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categories;
	}

	protected static boolean isVendorProduct(int vId, int productId) {

		try {
			rs = smt.executeQuery(String.format("SELECT id FROM products WHERE vId = %d", vId));
			while (rs.next()) {
				if (rs.getInt(1) == productId) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	protected static ArrayList<Products> getProductVendor(int id) {
		ArrayList<Products> productsArray = new ArrayList<>();

		try {
			rs = smt.executeQuery(String.format("SELECT * FROM products WHERE vId = %s", id));

			while (rs.next()) {

				Products products = new Products.ProductsBuilder().setId(rs.getInt("id")).setName(rs.getString("name"))
						.setCategory(rs.getString("category")).setPrice(rs.getDouble("price"))
						.setQuantity(rs.getInt("quantity")).setSaledCount(rs.getInt("saledCount")).build();

				productsArray.add(products);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occurs here");
			e.printStackTrace();
		}
		return productsArray;
	}

	public static Pojo.Vendor getVendorDetails(int vId) {
		try {
			rs = smt.executeQuery("SELECT * FROM vendor WHERE id = " + vId);
			if (rs.next()) {
				Vendor vendor = new Vendor.VendorBuilder().setCompany(rs.getString("company"))
						.setUsername(rs.getString("username")).setCompany(rs.getString("company"))
						.setRating(rs.getByte("rating")).build();
				return vendor;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<ProductsVendor> getCategoryProducts(int offset, int limit, String category, int minPrice, int maxPrice, String sortBy) {
		List<ProductsVendor> productsVendors = new ArrayList<>();
		try {
			StringBuilder link = new StringBuilder();
			link = new StringBuilder("SELECT products.*, company FROM products INNER JOIN vendor ON products.vId = vendor.id ");
			
			link.append("WHERE ");
			link.append("price > " + minPrice + " ");
			
			if(category != null) {
				link.append("AND category = '" + category + "' ");
			}
			
			if(maxPrice != 0) {
				link.append("AND price < " + maxPrice + " ");				
			}
			
			if(sortBy != null) {
				link.append("ORDER BY " + sortBy + " ");
			}
			
			link.append("LIMIT " + offset + ", " + limit + " ");
			System.out.println(link);
			
			ResultSet rs;
			rs = smt.executeQuery(link.toString());
			while (rs.next()) {
				Products products = new Products.ProductsBuilder().setId(rs.getInt("id"))
						.setQuantity(rs.getInt("quantity")).setName(rs.getString("name"))
						.setPrice(rs.getDouble("price")).setRating(rs.getFloat("rating"))
						.setCategory(rs.getString("category")).setVId(rs.getInt("vId")).setImg(rs.getString("img"))
						.build();

				Vendor vendor = new Vendor.VendorBuilder().setCompany(rs.getString("company")).build();

				ProductsVendor productsVendor = new ProductsVendor.ProductsVendorBuilder().setProducts(products)
						.setVendor(vendor).build();
				productsVendors.add(productsVendor);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return productsVendors;
	}

}
