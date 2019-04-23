package Pojo;
//$Id$

// POJO
public class ProductsVendor {
   private Products products;
   private Vendor vendor;


private ProductsVendor ( Products products, Vendor vendor) {
   this.products = products;
   this.vendor = vendor;

}

// Getters
   public Products getProducts() {
        return this.products;
    }

   public Vendor getVendor() {
        return this.vendor;
    }



// ToString

    @Override
    public String toString() {
        return "ProductsVendor = [ " + ",products = " + products + ",vendor = " + vendor;
    }



    // Since using it in different package just made it public
    public static class ProductsVendorBuilder {
   private Products products;
   private Vendor vendor;


        public ProductsVendorBuilder() {}

    // Setters
    public ProductsVendorBuilder setProducts(Products products) {
        this.products = products;    
        return this;    
    }
    public ProductsVendorBuilder setVendor(Vendor vendor) {
        this.vendor = vendor;    
        return this;    
    }
    

    // builder
    public ProductsVendor build() {
        return new ProductsVendor(this.products,this.vendor );
    }

}

}
