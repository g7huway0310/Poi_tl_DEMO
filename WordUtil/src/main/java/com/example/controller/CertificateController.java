package com.example.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.util.Poi_tl_wordUtil;




@Controller
public class CertificateController {
	
	@Autowired
	Poi_tl_wordUtil poiUtil;

	@PostMapping("/generateCertificate")
	public void generateItReport(@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "minister", required = false) String minister,
			@RequestParam(name = "certificationName", required = false) String certificationName, HttpServletResponse response,
			HttpServletRequest request) {
		Date aDate = new java.util.Date();
		System.out.println("開始產生it報告");

		// 處理日期轉換成民國
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int year = c.get(Calendar.YEAR) - 1911;
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		System.out.print("民國" + year + "年");
		System.out.print(month + "月");
		System.out.println(day + "日");

		// 用一個map將標籤替換指定文字
		Map<String, String> testMap = new HashMap<String, String>();
		testMap.put("Name", name);
		testMap.put("year", Integer.toString(year));
		testMap.put("month", Integer.toString(month));
		testMap.put("day", Integer.toString(day));
		testMap.put("minister", minister);

		XWPFDocument document = null;

		
		String classPathResource = "/static/Ref/certificate.docx";
		//讀取classpath下的資源
		ClassPathResource resource = new ClassPathResource(classPathResource);

		String folderpath = resource.getPath();

		String outPutPathResource = certificationName+".docx";

		try {
			System.out.println("開始轉換");

			System.out.println("path" + resource.getPath());

			InputStream in = resource.getInputStream();

			poiUtil.downloadReportFile(classPathResource, outPutPathResource, testMap, response, request);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
		System.out.println("成功");


	}

}
