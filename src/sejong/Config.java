package sejong;

import java.util.HashMap;

/**
 * @author Heoungjun Yu
 * @version 2013.04.13
 * 
 */
public class Config {
	/**
	 * 게시판 종류 파싱을 원하면 골라서 사용
	 */
	public static final String NOTICE = "notice";
	public static final String ADMISSION = "admission";
	public static final String COLLEGE = "college";
	public static final String FOREIGN = "foreign";
	public static final String JOB = "job";
	public static final String SCHOOL = "school";

	/**
	 * Map 생성시 Key String 으로 사용하기 위한 문자열. 내부에서 사용
	 */
	public static final String PREVIEW = "preview";
	public static final String CONTENT = "content";

	private HashMap<String, HashMap<String, String>> map;

	/**
	 * 고정적으로 파싱할 대상의 URL을 담게 구현
	 */
	public Config() {
		try {
			HashMap<String, String> value = new HashMap<String, String>(); // value 임시 저장
			map = new HashMap<String, HashMap<String,String>>();

			/* url input */
			value = mapping(
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?command=list&client_id=board&handle=57&curPage=1&board_seq=62911&search=&column=&sBoard_id=57",
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?client_id=board&handle=57&command=view&curPage=1&board_seq=");
			map.put(NOTICE, value);

			value = mapping(
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?command=list&client_id=board&handle=59&curPage=1&board_seq=62805&search=&column=&sBoard_id=59",
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?client_id=board&handle=59&command=view&curPage=1&board_seq=");
			map.put(ADMISSION, value);

			value = mapping(
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?command=list&client_id=board&handle=60&curPage=1&board_seq=62752&search=&column=&sBoard_id=60",
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?client_id=board&handle=60&command=view&curPage=1&board_seq=");
			map.put(COLLEGE, value);

			value = mapping(
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?command=list&client_id=board&handle=131&curPage=1&board_seq=62942&search=&column=&sBoard_id=131",
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?client_id=board&handle=131&command=view&curPage=1&board_seq=");
			map.put(FOREIGN, value);

			value = mapping(
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?command=list&client_id=board&handle=181&curPage=1&board_seq=62942&search=&column=&sBoard_id=181",
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?client_id=board&handle=181&command=view&curPage=1&board_seq=");
			map.put(JOB, value);

			value = mapping(
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?command=list&client_id=board&handle=197&curPage=1&board_seq=62947&search=&column=&sBoard_id=197",
					"http://cm.sejong.ac.kr/servlet/kr.co.k2web.jwizard.contents.board.boardUser.servlet.userMainServlet?client_id=board&handle=197&command=view&curPage=1&board_seq=");
			map.put(SCHOOL, value); // 수정 필요... 이상하게 안됨.. ;

			/* end */
		} catch (Exception e) {
			System.out.println("Exception : " + e);
		}
	}

	/**
	 * @param preview
	 * @param content
	 * @return preview, content 의 키로 지정된 맵
	 */
	public HashMap<String, String> mapping(String preview, String content) {
		HashMap<String, String> urls = new HashMap<String, String>();
		urls.put(PREVIEW, preview);
		urls.put(CONTENT, content);

		return urls;
	}

	/**
	 * 고정 삽입된 문자열이 저장된 HashMap 리턴
	 * 
	 * @return HashMap
	 */
	public HashMap<String, HashMap<String, String>> getMap() {
		return this.map;
	}
}
