package sejong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Heoungjun Yu
 * @version 2013.04.13
 * 
 */
public class Parser {
	Article article; // �ϳ��� �Խñ�
	List<Article> list; // ���� �Խñ��� �����ϱ� ���� list
	public final static int TIMEOUT = 15 * 1000;

	public Parser() {
		article = null;
		list = null;
	}

	/**
	 * �Խñ� ��� �Ľ��ϴ� �Լ�
	 * 
	 * @param urls
	 * @return �Խñ��� id �� title �� ������ Ŭ���� ����Ʈ
	 * @throws IOException
	 */
	public List<Article> preview(HashMap<String, String> urls)
			throws IOException {
		String temp = null;
		String[] values = null;

		try {
			Document doc = Jsoup.connect(urls.get(Config.PREVIEW))
					.timeout(TIMEOUT).get();

			Elements previews = doc.select(".k2_list_cnt a"); // �Ϲ� �Խñ�
			Elements notices = doc.select(".k2_list_notice a"); // ��������

			list = new ArrayList<Article>();

			for (Element e : notices) {
				previews.add(0, e); // �������� �۵� ����
			}

			/* preview start */
			for (Element e : previews) {
				temp = e.attr("onclick");
				values = temp.split(",");

				article = new Article();
				article.setId(values[2].split("'")[1]);
				article.setTitle(e.text());

				list.add(article);
			}
			return list;
			/* preview end */
		} catch (Exception e) {
			System.out.println("Exception : " + e);
			return null;
		}
	}

	/**
	 * �� ������ �Ľ��ϴ� �Լ�
	 * 
	 * @param url
	 * @return �� ����
	 * @throws IOException
	 */
	public String content(String url) throws IOException {
		try {
			Document doc = Jsoup.connect(url).timeout(TIMEOUT).get();

			// Elements title = doc.select(".k2_list_itm");
			Elements content = doc.select("#contents_td");

			System.out.println(content.html());
			return content.html();
		} catch (Exception e) {
			System.out.println("Exception : " + e);
			return null;
		}
	}

	public ArrayList<Article> previews(String url, String handle)
			throws IOException {
		Article article = null;
		int i = 0;
		String temp = null;
		String[] values = null;

		try {
			Document doc = Jsoup.connect(url).timeout(TIMEOUT).get();
			Elements notices = doc.select(".k2_list_cnt"); // ��������

			ArrayList<Article> articles = new ArrayList<Article>();
			article = new Article();

			for (Element e : notices) {
				switch (i++) {
				default: // ����
					temp = Jsoup.parse(e.html()).select("a").attr("onclick");
					if (temp == "") {
						// 글의 제목을 발견한 순간이 아니라면 계속해서 반복문에 머무르면 찾는다.
						i = 0;
						continue;
					}
					// 글의제목을 발견했다면 다음 단계로 진행
					values = temp.split(",");

					article.setId(values[2].split("'")[1]);
					article.setTitle(e.text());
					break;
				case 1: // �����
					article.setUser(e.text());
					break;
				case 2: // ��¥
					article.setDate(e.text());
					break;
				case 3: // ��ȸ��
					article.setHit(e.text());
					article.setHandle(handle);
					articles.add(article);
					System.out.println("Id :" + article.getId());
					System.out.println("Title :" + article.getTitle());
					System.out.println("User :" + article.getUser());
					System.out.println("Date :" + article.getDate());
					System.out.println("Hit :" + article.getHit());
					/* �ʱ�ȭ */
					article = null;
					article = new Article();
					i = 0;
					break;
				}
			}

			return articles;
		} catch (Exception e) {
			System.out.println("Parse Exception : " + e.toString());
			return null;
		}
	}
}
