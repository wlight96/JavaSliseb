package lab2;

import java.util.Scanner;

public class Lab2AccountTest {
	public static void main(String[] args) {
		System.out.print("which account wana u withdrawal ? ");
		Scanner input = new Scanner(System.in);
		int num = input.nextInt();
		while(true){
			if(num==1){
				System.out.print("Enter withdrawal for account 1 : ");
				int money1 = input.nextInt();
				Lab2Account account = new Lab2Account();
				double a = account.getAccount1();
				if(a >= money1){
					a =  a - money1;
					System.out.printf("substracting %f from account1 balance\n",money1);
					System.out.printf("account 1 is now : %f\n",account.getAccount1());
				}
				else{
					System.out.print("can't do that\n");
				}
			}
			if(num==2){
				System.out.print("Enter withdrawal for account 2 : ");
				int money2 = input.nextInt();
				Lab2Account account = new Lab2Account();
				double b = account.getAccount2();
				if(money2 > b){
					System.out.print("account 2 is emtpy ");
				}
				else{
					b =  b - money2;
					System.out.printf("substracting %f from account1 balance\n",money2);
					System.out.printf("account 2 is now : %f\n",account.getAccount2());
				}
			}
			break;
		}	
	}	
}