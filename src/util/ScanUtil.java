package util;

import java.util.Scanner;

public class ScanUtil {

	private static Scanner s = new Scanner(System.in);
	
	public static String nextLine() {
		return s.nextLine();
	}
	
	public static int nextInt() {
		return Integer.parseInt(s.nextLine());
	}//다른 클래스 안에서도 객체생성을 하지 않아도 공유가 가능하다.
	
}
