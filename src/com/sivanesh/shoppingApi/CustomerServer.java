//$Id$
package com.sivanesh.shoppingApi;

import java.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.catalina.User;

import Pojo.Address;
import Pojo.Bill;
import Pojo.Users;
import Shopping.Auth;
import Shopping.Customer;
import Shopping.ProductVendor;
import Shopping.Sql;
import Pojo.Products;
import Pojo.ProductsVendor;
import Pojo.ProductsVendorCart;
import Pojo.UserBills;

@Path("/customer")
public class CustomerServer {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean signUp(Users user) {
		if (Auth.signUp(false, user.getUsername(), user.getPassword())) {
			System.out.println("[CustomerServer] SignUp Successfully");

			// TODO: Check a way to return when phone not updated
			if (Auth.updateUser(user.getUsername(), user.getPhone())) {
				System.out.println("[CustomerServer] Phone Updated");
			} else {
				System.err.println("[CustomerServer] Phone NOT Updated");
			}
			return true;
		} else {
			System.out.println("[CustomerServer] SignUp Failed");
			return false;
		}
	}

	@POST
	@Path("sign-in")
	@Produces("application/json")
	public int signIn(Users user) {
		System.out.println("[CustomerServer] Signed in, Username" + user.getUsername());
		return Auth.signIn(false, user.getUsername(), user.getPassword());
	}
	
	@GET
	@Path("/{user-id}")
	@Produces("application/json")
	public Users getUserDetails(@PathParam("user-id") int userId) {
		System.out.println("[CustomerServer] Returned User Details.");
		return Customer.getUser(userId);
	}

	@GET
	@Path("/cart/{user-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProductsVendorCart> getUserCart(@PathParam("user-id") int id) {
		System.out.println("[CustomerServer] Returned User Cart");
		return Customer.viewCart(id);
	}

	@DELETE
	@Path("/cart/{user-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteUserCart(@PathParam("user-id") int userId) {
		if (Customer.clearCart(userId)) {
			System.out.println("[CustomerServer] DELETED user cart successfully");
			return true;
		} else {
			System.out.println("[CustomerServer] No products to Delete");
			return false;
		}
	}

	@POST
	@Path("/cart/{user-id}/{p-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean insert(@PathParam("user-id") int id, @PathParam("p-id") int pId, Integer quantity) {
		if (Customer.insert(id, pId, quantity) == "Success") {
			System.out.println("[CustomerServer] Product inserted to User cart" + id);
			return true;
		} else {
			System.err.println("[CustomerServer] Product not Inserted to cart." + id);
			return false;
		}
	}

	@PUT
	@Path("/cart/{user-id}/{p-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateCartProductQuantity(@PathParam("user-id") int userId, @PathParam("p-id") int pId,
			Integer quantity) {
		if (Customer.update(userId, pId, quantity)) {
			System.out.println("[CustomerServer] Updated Cart Product's Quantity");
			return true;
		} else {
			return false;
		}
	}

	@DELETE
	@Path("/cart/{user-id}/{p-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteProductFromCart(@PathParam("user-id") int id, @PathParam("p-id") int pId) {
		if (Customer.delete(id, pId)) {
			return true;
		} else {
			return false;
		}
	}
	
	// WISHLIST
	@GET
	@Path("/wish-list/{user-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Products> viewWishList(@PathParam("user-id") int userId) {
		return Customer.viewWishList(userId);
	}
	
	@DELETE
	@Path("/wish-list/{user-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean clearWishList(@PathParam("user-id") int userId) {
		return Customer.clearWishList(userId);
	}
	
	@POST
	@Path("/wish-list/{user-id}/{p-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean addtoWishList(@PathParam("user-id") int userId, @PathParam("p-id") int pId) {
		System.out.println(userId + " " + pId);
		if(Customer.addToWishList(userId, pId)) {
			return true;
		} else {
			return false;
		}
	}
	
	@DELETE
	@Path("/wish-list/{user-id}/{p-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deletetoWishList(@PathParam("user-id") int userId, @PathParam("p-id") int pId) {
		if(Customer.removeFromWishList(userId, pId)) {
			return true;
		} else {
			return false;
		}
	}
	

	@POST
	@Path("/buy/{user-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean buy(@PathParam("user-id") int userId) {
		System.out.println("[CustomerServer] Checker " + userId);
		if (Customer.buy(userId)) {
			return true;
		} else {
			System.err.println("[CustomerServer] There's issue in Buying product or No Products Present");
			return false;
		}
	}

	@GET
	@Path("/bills/{user-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Bill> viewBills(@PathParam("user-id") int userId) {
		System.out.println("[CustomerServer] Bills " + userId);
		return Customer.billList(userId);
	}

	@GET
	@Path("/bill/{user-id}/{bill-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ProductVendor> getBill(@PathParam("bill-id") int billId) {
		return Customer.viewBill(billId);
	}

	@GET
	@Path("address/{user-id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Address> getAllAddresses(@PathParam("user-id") int id,
			@QueryParam("isPermanent") boolean isPermanent) {
		if (isPermanent) {
			ArrayList<Address> addresses = Customer.getAllAddresses(id);
			for (int i = 0; i < addresses.size(); i++) {
				if (addresses.get(i).getPermanent() == 1) {
					ArrayList<Address> addresses2 = new ArrayList<Address>();
					addresses2.add(addresses.get(i));
					return addresses2;
				}
			}
			System.out.println("[CustomerServer] No permanent Address");
			return null;

		} else {
			return Customer.getAllAddresses(id);
		}
	}

	@POST
	@Path("address/{user-id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateAddress(@PathParam("user-id") int id, Address address) {
		address.setId(id);
		System.out.println(address);

		if (Auth.updateAddress(address)) {
			System.out.println("[CustomerServer] Updated Address Successfully");
			return true;
		} else {
			System.out.println("[CustomerServer] Address NOT Updated");
			return false;
		}
	}
	
//	@DELETE
//	@Path("address/{user-id}/{address-id}")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public boolean deleteAddress(@PathParam("user-id")int userId, @PathParam("address-id") int addressId) {
//		
//	}

	// Checker
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Users getString() {
		return new Users.UsersBuilder().setUsername("Sivanesh").build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String check(String quantity) {
		System.out.println("[CustomerServer] Works " + quantity);
		return quantity;
	}

}
