package com.example.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.deepoove.poi.XWPFTemplate;
import com.example.demo.WordUtilApplication;

@Service
public class Poi_tl_wordUtil {

	// 替換文件並輸出
	public static void replaceWord(String templatePath, Map<String, String> testMap) {

		XWPFTemplate template = XWPFTemplate.compile(templatePath).render(testMap);
		try {
			template.writeAndClose(new FileOutputStream("/Users/guowei/Desktop/我的證書.docx"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//替換寫入並下載檔案
	public static void downloadReportFile(String templatePath, String fileName, Map<String, String> replaceMap,
			HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {

		System.out.println("進入下載");

		XWPFDocument document = null;

		InputStream in = null;

		// 生成新的word
		response.reset();
		response.setContentType("applicatoin/octet-stream");

		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		OutputStream os = null;

		String userAgent = request.getHeader("User-Agent").toLowerCase();
		System.out.println(userAgent);

		if (userAgent.contains("safari")) {
			// 處理safari中文字檔名造成亂碼
			System.out.println("進到safari");
			String safariFileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");// fileName.getBytes("UTF-8")處理safari的亂碼問題
			response.addHeader("Content-Disposition", "attachment; filename=" + safariFileName);

		} else {
			response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
		}

		try {

			System.out.println("執行生成Word");

			// 會使用Classpath原因：因為我們會包成jar檔部署到公有雲，打包後Spring會去訪問系統的路徑，但無法訪問JAR中的路徑InputStream()
			in = new ClassPathResource(templatePath).getInputStream();
			// 替換標籤文字
			XWPFTemplate template = XWPFTemplate.compile(in).render(replaceMap);
			
			System.out.println("替換完畢");
			os = response.getOutputStream();
			template.writeAndClose(os);

			// 輸出到response
			os.write(ostream.toByteArray());

			in.close();
			os.close();
			ostream.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {

				if (in != null) {
					in.close();
				}
				if (document != null) {
					document.close();
				}
				if (os != null) {
					os.close();
				}
				if (ostream != null) {
					ostream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

//	// 測試
//	public static void main(String[] args) {
//
//		// 處理日期轉換成民國
//		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(System.currentTimeMillis());
//		int year = c.get(Calendar.YEAR) - 1911;
//		int month = c.get(Calendar.MONTH) + 1;
//		int day = c.get(Calendar.DAY_OF_MONTH);
//		System.out.print("民國" + year + "年");
//		System.out.print(month + "月");
//		System.out.println(day + "日");
//
//		// 用一個map將標籤替換指定文字
//		Map<String, String> testMap = new HashMap<String, String>();
//		testMap.put("Name", "王大大");
//		testMap.put("year", Integer.toString(year));
//		testMap.put("month", Integer.toString(month));
//		testMap.put("day", Integer.toString(day));
//		testMap.put("minister", "Kumamon");
//
//		Poi_tl_wordUtil.replaceWord("/Users/guowei/Desktop/certificate.docx", testMap);
//
//		System.out.println("執行完畢");
//
//	}
	
	

}
