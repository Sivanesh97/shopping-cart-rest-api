package Pojo;
//$Id$

import java.util.ArrayList;

// POJO
public class ViewCart {
   private ArrayList<ProductsVendorCart> products;
   private double total;


private ViewCart ( ArrayList<ProductsVendorCart> products, double total) {
   this.products = products;
   this.total = total;

}

// Getters
   public ArrayList<ProductsVendorCart> getProducts() {
        return this.products;
    }

   public double getTotal() {
        return this.total;
    }



// ToString

    @Override
    public String toString() {
        return "ViewCart = [ " + ",products = " + products + ",total = " + total;
    }



    // Since using it in different package just made it public
    public static class ViewCartBuilder {
   private ArrayList<ProductsVendorCart> products;
   private double total;


        public ViewCartBuilder() {}

    // Setters
    public ViewCartBuilder setProducts(ArrayList<ProductsVendorCart> products) {
        this.products = products;    
        return this;    
    }
    public ViewCartBuilder setTotal(double total) {
        this.total = total;    
        return this;    
    }
    

    // builder
    public ViewCart build() {
        return new ViewCart(this.products,this.total );
    }

}

}
