package app.sejongcloud.notice;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sejong.Article;
import sejong.Parser;

public class GetNotice {
	public static ArrayList<Article> getNotice(String address) {
		Pattern mPattern = Pattern.compile("handle=(.*)&board_id");
		Matcher mTemp = mPattern.matcher(address); // �ΰ��� ����
		String handle = null;

		if (mTemp.find()) {
			handle = mTemp.group(1);
		}

		Parser parser = new Parser();
		ArrayList<Article> articles = new ArrayList<Article>();

		try { // notice
			articles = parser.previews(address, handle);
		} catch (Exception e) {
			System.out.print("GetNotice Error : "+e.toString());
			return null;
		}
		return articles;
	}
}
