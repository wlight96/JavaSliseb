package lab2;

import java.util.Scanner;

public class Lab2Account {
	private double account1 = 50.00;
	private double account2 = 0.00;

	public double getAccount1() {
		return account1;
	}

	public double getAccount2() {
		return account2;
	}
	public static void main(String[] args) {
		Lab2Account account = new Lab2Account();
		System.out.printf("account 1 : %f\n", account.getAccount1());
		System.out.printf("account 2 : %f\n", account.getAccount2());
	}
}

