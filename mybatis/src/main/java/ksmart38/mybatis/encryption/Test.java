package ksmart38.mybatis.encryption;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class Test {

	public static void main(String[] args) {
		StandardPBEStringEncryptor stringPBEConfig = new StandardPBEStringEncryptor();

		stringPBEConfig.setPassword("ksmart38");			//대칭키 (암호화키)
		stringPBEConfig.setAlgorithm("PBEWithMD5AndDES"); 	//사용할 암호화 알고리즘
		
		String jdbcUrl = "jdbc:log4jdbc:mysql://localhost:3306/ksmart38db?serverTimezone=UTC&characterEncoding=UTF8";
		String userName = "ksmart38id";
		String password = "ksmart38pw";
		
		String test1 = stringPBEConfig.encrypt(jdbcUrl);
		String test2 = stringPBEConfig.encrypt(userName);
		String test3 = stringPBEConfig.encrypt(password);
		System.out.println("평문(jdbcUrl)" + jdbcUrl);
		System.out.println("암호문(jdbcUrl)" + test1);
		System.out.println("평문(userName)" + userName);
		System.out.println("암호문(userName)" + test2);
		System.out.println("평문(password)" + password);
		System.out.println("암호문(password)" + test3);
		
		System.out.println("복호화(jdbcUrl)" + stringPBEConfig.decrypt(test1));
		System.out.println("암호문(userName)" + stringPBEConfig.decrypt(test2));
		System.out.println("암호문(password)" + stringPBEConfig.decrypt(test3));
		
	}
}
