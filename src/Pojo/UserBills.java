package Pojo;
//$Id$

// POJO
public class UserBills {
   private int billId;
   private String timestamp;
   private String street;
   private String number;
   private String landMark;
   private double total;


private UserBills ( int billId, String timestamp, String street, String number, String landMark, double total) {
   this.billId = billId;
   this.timestamp = timestamp;
   this.street = street;
   this.number = number;
   this.landMark = landMark;
   this.total = total;
}

// Getters
   public int getBillId() {
        return this.billId;
    }

   public String getTimestamp() {
        return this.timestamp;
    }

   public String getStreet() {
        return this.street;
    }

   public String getNumber() {
        return this.number;
    }

   public String getLandMark() {
        return this.landMark;
    }
   
   public double gettotal() {
	   return this.total;
   }



// ToString

    @Override
    public String toString() {
        return "UserBills = [ " + ",billId = " + billId + ",timestamp = " + timestamp + ",street = " + street + ",number = " + number + ",landMark = " + landMark;
    }



    // Since using it in different package just made it public
    public static class UserBillsBuilder {
	   private int billId;
	   private String timestamp;
	   private String street;
	   private String number;
	   private String landMark;
	   private double total;


        public UserBillsBuilder() {}

    // Setters
    public UserBillsBuilder setBillId(int billId) {
        this.billId = billId;    
        return this;    
    }
    public UserBillsBuilder setTimestamp(String timestamp) {
        this.timestamp = timestamp;    
        return this;    
    }
    public UserBillsBuilder setStreet(String street) {
        this.street = street;    
        return this;    
    }
    public UserBillsBuilder setNumber(String number) {
        this.number = number;    
        return this;    
    }
    public UserBillsBuilder setLandMark(String landMark) {
        this.landMark = landMark;    
        return this;    
    }
    public UserBillsBuilder setTotal(double total) {
    	this.total = total;   
    	return this;    
    }
    

    // builder
    public UserBills build() {
        return new UserBills(this.billId,this.timestamp,this.street,this.number,this.landMark, this.total );
    }

}

}
