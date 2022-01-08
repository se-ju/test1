package service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.Controller;
import dao.UserDao;
import util.ScanUtil;
import util.View;

public class UserService {
	
	//싱글톤 패턴
	private UserService() {}
	private static UserService instance;
	public static UserService getInstance() {
		if(instance == null) {
			instance = new UserService();
		}
		return instance;
	}
	private UserDao userDao = UserDao.getInstance();
	
	
	public int join() { // int 타입 리턴은 다음 화면을 결정함
		
		String memId;
		String password;
		String memName;
		
		while (true) { // 아이디 유효성 검사
			System.out.println("===== 회원가입 =====");
			System.out.println("소문자, 숫자, -, _ 포함 3글자 이상 16글자 이하로 입력하세요");
			System.out.print("아이디 > ");
			memId = ScanUtil.nextLine();

			// 아이디 정규표현식 : 소문자, 숫자, _- 포함 3글자 이상 16글자 이하
			String IdOx = "^[a-z0-9_-]{3,16}$";

			Matcher m = Pattern.compile(IdOx).matcher(memId);
			if (m.matches() == false) {
				System.out.println("아이디 입력 형식이 맞지 않습니다.");
			} else {
				break;
			}
		}
		
		while (true) { // 비밀번호 유효성 검사
			System.out.println("소문자, 숫자, -, _ 포함 6글자 이상 18글자 이하로 입력하세요");
			System.out.print("비밀번호 > ");
			password = ScanUtil.nextLine();

			// 비밀번호 정규표현식 : 소문자, 숫자, _- 포함 6글자 이상 18글자 이하
			String passwordOx = "^[a-z0-9_-]{6,18}$";
			Matcher m = Pattern.compile(passwordOx).matcher(password);

			if (m.matches() == false) {
				System.out.println("비밀번호 입력 형식이 맞지 않습니다.");
			} else {
				break;
			}
		}

		while (true) { // 이름 유효성 검사
			System.out.println("한글만 입력하세요");
			System.out.print("이름 > ");
			memName = ScanUtil.nextLine();

			// 이름 정규표현식 : 한글만
			String nameOx = "^[가-힣]*$";
			Matcher m3 = Pattern.compile(nameOx).matcher(memName);

			if (m3.matches() == false) {
				System.out.println("이름 입력 형식이 맞지 않습니다.");
			} else {
				break;
			}
		}
		
				
		Map<String, Object> param = new HashMap<>();
		param.put("MEM_ID",memId);
		param.put("PASSWORD",password);
		param.put("MEM_NaME",memName);
		
		int result = userDao.insertMember(param);
		
		if(0 < result) {
			System.out.println("======= 회원가입 성공 =======");
		}else {
			System.out.println("======= 회원가입 실패 =======");
		}
		
		return View.Home;
		
		}


	public int login() {
		System.out.println("============== 로그인 ==============");
		System.out.print("아이디 > ");
		String memId = ScanUtil.nextLine();
		System.out.print("비밀번호 > ");
		String password = ScanUtil.nextLine();
		
		Map<String, Object> member = userDao.selectMember(memId,password);
		
		if(member == null) {
			System.out.println("아이디 혹은 비밀번호를 잘못 입력하셨습니다.");			
		}else {
			System.out.println("로그인 성공");
			
			Controller.LoginMember = member;
			
			return View.BOARD_LIST; //실패시 다시 로그인
		}
		
		return View.LOGIN;
	}
	

}
