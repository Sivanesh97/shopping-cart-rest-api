package Shopping;

//$Id$

import java.text.MessageFormat;
import java.util.Scanner;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import Pojo.Address;

public class Auth {
	static Scanner d = new Scanner(System.in);

	static Checker c = Checker.getInstance();
	
	// Both for Vendor and Customer
	public static int signIn(boolean isVendor, String username, String password) {
		if (c.isEmpty(username)) {
			return -1;
		}
		
		return Sql.signIn(isVendor, username, password);
		

//		String passwordConfirm = Sql.getPassword(username, table);
//		if (passwordConfirm == null) {
//			System.err.println("Wrong Username");
//			return false;
//		} else {
//			if (password.equals(passwordConfirm)) {
//				System.out.println("Logged in Successfully.\nWelcome " + username);
//				return true;
//			} else {
//				System.err.println("Wrong Sign in Credentials" + username + " " + password);
//				return false;
//			}
//		}
	}
	
	// Both for Vendor and Customer
	public static boolean signUp(boolean isVendor, String username, String password) {
		String table = isVendor ? "vendor" : "users";
		System.out.println("Username " + username);
		if (Sql.isUserAlreadyPresent(username, isVendor)) {
			System.out.println("Username already present");
			return false;
		}

		// Insertion
		if (!Sql.insertions(table, "(username, password)", String.format("'%s', '%s'", username, password))) {
			System.out.println("Username already Present");
			return false;
		}

		System.out.println("Inserted Successfully");
		return true;
	}

	// Update Phone number
	public static boolean updateUser(String username, String phoneNum) {
		String table = "users";
		String datas = "phone = '" + phoneNum + "'";
		String conditions = "username = '" + username + "'";
		if(!Sql.updations(table, datas, conditions)) {
			System.out.println("Issue in updation");
			return false;
		}
		System.out.println("Updated successfully\n");
		return true;
	}

	// Update Address TODO: Change this as only not null rows are updated.
	public static boolean updateAddress(Address address) {
		
		String columns = "(id, number, street, landmark, city, pincode, state, country, permanent)";
		String data = MessageFormat.format("{0}, ''{1}'', ''{2}'', ''{3}'', ''{4}'', {5}, ''{6}'', ''{7}'', {8}", 
				address.getId(), address.getNumber(), address.getStreet(), address.getLandmark(), address.getCity(), address.getPincode(), address.getState(), address.getCountry(), address.getPermanent()
				);
		if(Sql.insertions("address", columns, data)) {
			System.out.println("Address Updated Successfully");
		} else {
			return false;
		}

		return true;
	}
	
	

	// Further Details adding to Vendor
	public static boolean updateVendor(int id, String company, String name) {
		if(Sql.updations("vendor", "company = '" + company + "', username = '" + name  + "'", 
				"id = '" + id + "'")) {
			System.out.println("Updated Successfully\n");
			return true;
		} else { 
			System.out.println("Cannot update due to ");
			return false;
		}
	}

}

