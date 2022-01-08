package service;


import java.util.List;
import java.util.Map;

import controller.Controller;
import dao.BoardDao;
import util.ScanUtil;
import util.View;

public class BoardService {
	
	//싱글톤 패턴
		private BoardService() {}
		private static BoardService instance;
		public static BoardService getInstance() {
			if(instance == null) {
				instance = new BoardService();
			}
			return instance;
		}
		
		private BoardDao boardDao = BoardDao.getInstance();
		public static int boardNo;
		
		public int boardList() {
			List<Map<String,Object>> boardList = boardDao.selectBoardList();
			
			
			// 게시판 목록
			System.out.println("===================================");
			System.out.println("번호\t제목\t작성자\t작성일");			
			for (Map<String, Object> board : boardList) {
				System.out.println(board.get("BOARD_NO") + "\t");
				System.out.println(board.get("TITLE") + "\t");
				System.out.println(board.get("MEM_NAME") + "\t");
				System.out.println(board.get("REG_DATE") + "\t");				
				
		}
		System.out.println("===================================");
		
		System.out.print("1.조회 2.등록 0.로그아웃 >  ");
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1://조회
			return View.BOARD_DETAIL;
			
		case 2://등록
			return View.BOARD_INSERT;
			
		case 0://로그아웃
			Controller.LoginMember = null;
			return View.Home; // 홈 화면으로 돌아감
		}
		
		return View.BOARD_LIST;
		
	}
		
		// 등록
		public int boardInsert() {
			System.out.print("제목 > ");
			String title = ScanUtil.nextLine();
			System.out.print("내용 > ");
			String content = ScanUtil.nextLine();
			String userId = (String) Controller.LoginMember.get("USER_ID");

			int result = boardDao.insertBoard(title, content, userId);

			if (result > 0) {
				System.out.println("게시물 등록 완료");
			} else {
				System.out.println("게시물 등록 실패");
			}

			return View.BOARD_LIST;
		}

		// 조회
		public int boardDetail() {
			System.out.print("게시글 번호 > ");
			boardNo = ScanUtil.nextInt();

			Map<String, Object> board = boardDao.selectBoard(boardNo);

			System.out.println("=================================");
			System.out.println("번호\t: " + board.get("BOARD_NO"));
			System.out.println("작성자\t: " + board.get("USER_NAME"));
			System.out.println("작성일\t: " + board.get("REG_DATE"));
			System.out.println("제목\t: " + board.get("TITLE"));
			System.out.println("내용\t: " + board.get("CONTENT"));
			System.out.println("=================================");

			System.out.print("1.수정  2.삭제  0.목록 > ");
			int input = ScanUtil.nextInt();
			switch (input) {
			case 1: // 수정
				return View.BOARD_UPDATE;

			case 2: // 삭제
				return View.BOARD_DELETE;
			}

			return View.BOARD_LIST;
		}

		// 조회 - 수정(Controller에서 호출)
		public int boardUpdate() {
			System.out.print("제목 > ");
			String title = ScanUtil.nextLine();
			System.out.print("내용 > ");
			String content = ScanUtil.nextLine();

			int result = boardDao.updateBoard(title, content, boardNo);

			if (result > 0) {
				System.out.println("게시물 수정 성공");
			} else {
				System.out.println("게시물 수정 실패");
			}

			return View.BOARD_LIST;
		}

		// 조회 - 삭제
		public int boardDelete() {
			System.out.print("정말 삭제하시겠습니까(y/n) > ");

			if (ScanUtil.nextLine().equals("y")) {
				int result = boardDao.deleteBoard(boardNo);

				if (result > 0) {
					System.out.println("게시물 삭제 성공");
				} else {
					System.out.println("게시물 삭제 실패");
				}
			}

			return View.BOARD_LIST;
		}
		
		

}
