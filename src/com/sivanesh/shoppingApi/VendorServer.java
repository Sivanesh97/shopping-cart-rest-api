//$Id$
package com.sivanesh.shoppingApi;

import java.util.ArrayList;
import java.util.List;

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

import Pojo.Products;
import Pojo.ProductsVendor;
import Pojo.Users;
import Pojo.Vendor;
import Shopping.Auth;
import Shopping.Seller;
import Shopping.Sql;

@Path("vendor")
public class VendorServer {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public boolean insertVendor(Pojo.Vendor vendor)  {
		
		if(Auth.signUp(true, vendor.getUsername(), vendor.getPassword())) {
			if(Sql.updateCompany(vendor)) {
				return true;
			} else {
				System.err.println("Issue in updating Company");
				return false;
			}
		} else {
			System.err.println("Issue in Adding Vendor");
			return false;
		}
	}

	@POST
	@Path("/sign-in")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int signIn(Users user) {
		System.out.println("Username" + user.getUsername() + " " +  user.getPassword());
		 return Auth.signIn(true, user.getUsername(), user.getPassword());
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{v-id}")
	public Pojo.Vendor getVendorDetails(@PathParam("v-id") int vId) {
		return Seller.getVendorDetails(vId);
	}
	
	@PUT
	@Path("/{v-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateVendor(@PathParam("v-id") int vId, Vendor vendor) {
		if(Auth.updateVendor(vId, vendor.getCompany(), vendor.getUsername())) {
			System.out.println("Updated Vendor");
			return true;
		} else {	
			return false;
		}
	}
	
	@POST 
	@Path("product/{v-id}")
	@Produces(MediaType.APPLICATION_JSON) 
	public boolean addProduct(@PathParam("v-id")int vId , Products products) {
		products.setvId(vId);
		System.out.println(products);
		if(Seller.insert(products)) {
			return true; 
		} else {
			return false;
		}
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("product/{v-id}/")
	public boolean updateProduct(@PathParam("v-id") int vId ,Products product) {
		if(Seller.update(vId, product)) {
			return true;
		} else {
			return false;
		}
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("product/{v-id}/{p-id}")
	public boolean deleteProduct(@PathParam("v-id") int vId, @PathParam("p-id") int pId) {
		if(Seller.delete(vId, pId)) {
			return true;
		} else {
			return false;
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/products/{v-id}")
	public List<ProductsVendor> getVendorProducts(@PathParam("v-id") int vId) {
		System.out.println(vId);
		List<ProductsVendor> products = Sql.viewProductsVendor(vId, -1, -1);
		System.out.println("Works well here" + products.toString());
		return products;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayHello() {
		return "Helloooo";
	}
	
}