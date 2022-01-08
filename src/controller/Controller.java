package controller;

import java.util.Map;

import service.BoardService;
import service.UserService;
import util.ScanUtil;
import util.View;

public class Controller {

	public static void main(String[] args) {
		/*
		 *  발표내용 : 조 소개  . 주제 소개 > 주제 선정 배경 > 메뉴 구조 > 시연 > 프로젝트 후기 >
		 *  발표인원 : 발표자 1명, ppt 및 시연 도우미 1명
		 *  
		 *  Controller : 화면이동 (화면을 이동시키는 역할)
		 *  Service : 화면 기능 (화면적 기능,다오를 통한 서비스 db접속)
		 *  Dao : 쿼리 작성 
		 *  
		 *  중복이 일어날 수 있기에 dao로 빼놓는 것이다.
		 *  
		 *  웹 개발을 할 때 사용을 한다.
		 */	
		
		new Controller().start();
		

	}
	
	
	public static Map<String, Object> LoginMember = null;
	
	
	private UserService userService = UserService.getInstance();
	private BoardService boardService = BoardService.getInstance();

	private void start() {
		int view = View.Home;
		
		while(true) {
			switch (view) {
			case View.Home:
				view = home();
				
				break;
				
			case View.JOIN: //회원가입
				view = userService.join();
				
				break;
				
			case View.LOGIN: //로그인
				view = userService.login();
				
				break;
				
			case View.BOARD_LIST: //게시판 목록
				view = boardService.boardList();
				
				break;
				
			case View.BOARD_INSERT: // 게시판 등록
				view = boardService.boardInsert();
				
				break;

			case View.BOARD_DETAIL: // 게시판 조회
				view = boardService.boardDetail();
				
				break;

			case View.BOARD_UPDATE: // 게시판 조회 - 수정
				view = boardService.boardUpdate();
				
				break;

			case View.BOARD_DELETE: // 게시판 조회 - 삭제
				view = boardService.boardDelete();
				
				break;				
			
			
			}
		}
		
	}

	private int home() {
		System.out.println("-----------------------------------------");
		System.out.println("1.로그인  2.회원가입  0.프로그램 종료>");
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1:
			return View.LOGIN;
		case 2:
			return View.JOIN;
		case 0:
			System.out.println("프로그램이 종료되었습니다.");
			System.exit(0);
		}
				
		return View.Home; //사용자가 잘못 입력했을 경우 홈 화면으로 돌아감
	}

}
