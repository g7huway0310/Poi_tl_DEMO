package com.example.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.impl.repackage.Repackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.ResponseObject;
import com.example.util.Poi_tl_wordUtil;

@Controller
public class CertificateController {

	@Autowired
	Poi_tl_wordUtil poiUtil;

	@PostMapping("/generateCertificate")
	public void generateItReport(@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "minister", required = false) String minister,
			@RequestParam(name="file")MultipartFile file,
			@RequestParam(name = "certificationName", required = false) String certificationName,
			HttpServletResponse response, HttpServletRequest request) {
		Date aDate = new java.util.Date();
		System.out.println("開始產生it報告");
		
		System.out.println(file.getName());
		
	    try {
			String fileToBase64 = fileToBase64(file);
			uploadImages(fileToBase64);
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
	
		
//		
//		try {
////			String fileToBase64 = fileToBase64(file);
//			
////			uploadImages(fileToBase64);
//			
//			
//			
//			
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
		
		
		
		
		

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
		// 讀取classpath下的資源
		ClassPathResource resource = new ClassPathResource(classPathResource);

		String folderpath = resource.getPath();

		String outPutPathResource = certificationName + ".docx";

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

	public void uploadImages(String base64String) {

		
		String imageUrl = "https://api.imgur.com/3/image";
		String id = "c043986de856a08"; // 填入 App 的 Client ID
		String token = "8018668808c11b87878a2268c4c8b61d04ba4442"; // 填入 token
		String album = "XXXX"; // 若要指定傳到某個相簿，就填入相簿的 ID

		CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPostRequest = new HttpPost(imageUrl);
        httpPostRequest.setHeader("Authorization","Client-ID "+id);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("image", base64String));
	
        
        try {
			httpPostRequest.setEntity(new UrlEncodedFormEntity(params));
			try {
				CloseableHttpResponse response = httpClient.execute(httpPostRequest);
				
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, "UTF-8");
				System.out.println(responseString);
				
				
				 
			     HttpEntity entity2 = response.getEntity();
				System.out.println("上傳成功"+entity2.getContentEncoding());
				System.out.println(entity2);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static String fileToBase64(MultipartFile file) throws IOException {
        
		InputStream inputStream = file.getInputStream();
				
	    byte[] bytes = IOUtils.toByteArray(inputStream);
		
	    String encoded = java.util.Base64.getEncoder().encodeToString(bytes);
        
       
        return new String(encoded);
    }
	
	

}
