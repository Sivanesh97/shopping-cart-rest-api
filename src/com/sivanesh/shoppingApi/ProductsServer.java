//$Id$
package com.sivanesh.shoppingApi;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import Pojo.Products;
import Pojo.ProductsVendor;
import Shopping.Seller;
import Shopping.Sql;

@Path("/products")
public class ProductsServer {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProductsVendor> getProducts(@QueryParam("offset") int offset, 
			@QueryParam("limit") int limit, 
			@QueryParam("category") String category,
			@QueryParam("min-price") int minPrice,
			@QueryParam("max-price") int maxPrice,
			@QueryParam("sort-by") String sortBy
	) {
		System.out.println("Offset = " + offset 
			+ " Limit = " + limit 
			+ " category = " + category
			+ " Min Price = " + minPrice
			+ " Max Price = " + maxPrice
			+ " Sort by = " + sortBy
		);
		System.out.println(category);
		return Sql.getCategoryProducts(offset, limit, category, minPrice, maxPrice, sortBy);
	}

	// TODO This is just to insert all fields. After that this should be gone to
	// VendorServer.java
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public boolean addProduct(Products products) {
		System.out.println(products);
		if (Seller.insert(products)) {
			return true;
		} else {
			return false;
		}
	}

	@GET
	@Path("/categories")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getCategories() {
		return Sql.getCategories();
	}
}
