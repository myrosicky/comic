package org.ll.comic.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.ll.HackTool.tool.HttpResponse;
import org.ll.HackTool.tool.MyClient;
import org.ll.comic.model.Comic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.google.gson.Gson;

@Service
public class ComicCollectionTask  implements ItemProcessor<String, List<Comic>>, ItemWriter<List<Comic>>, ItemReader<String>{

	private static final Logger log = LoggerFactory.getLogger(ComicCollectionTask.class);
	
	@Autowired
	private Jedis jedis;
	
	@Override
	public String read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		MyClient myclient = new MyClient();
		String url = "http://cnc.dm5.com/manhua-rexue/?area=36";
		Map<String, String> custom_headers = new HashMap<>(10);
		custom_headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		custom_headers.put("Accept-Encoding", "gzip, deflate");
		custom_headers.put("Accept-Language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7,de;q=0.6,ja;q=0.5");
		custom_headers.put("Cache-Control", "max-age=0");
		custom_headers.put("Connection", "keep-alive");
		custom_headers.put("Cookie", "DM5_MACHINEKEY=6b6d0bdc-8964-48cc-86ff-50570f3e9a7a; SERVERID=node5; UM_distinctid=16acd719fef190-0b9a047feaaad4-162a1c0b-100200-16acd719ff0a1; CNZZDATA30089965=cnzz_eid%3D748706815-1558223829-%26ntime%3D1558223829; CNZZDATA30087176=cnzz_eid%3D290767109-1558219843-%26ntime%3D1558219843; __utma=1.930005539.1558224937.1558224937.1558224937.1; __utmc=1; __utmz=1.1558224937.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmt=1; firsturl=http%3A%2F%2Fcnc.dm5.com%2Fm206660%2F; CNZZDATA1261430596=2010226094-1558224642-http%253A%252F%252Fcnc.dm5.com%252F%7C1558224642; dm5cookieenabletest=1; ComicHistoryitem_zh=History=18625,636938506151343585,206660,2,0,0,0,1&ViewType=0; readhistory_time=1-18625-206660-2; image_time_cookie=206660|636938506151500052|1; dm5imgpage=206660|2:1:55:0; dm5imgcooke=206660%7C16; dm5_newsearch=%5b%7b%22Title%22%3a%22jojo%22%2c%22Url%22%3a%22%5c%2fsearch%3ftitle%3djojo%26language%3d1%22%7d%2c%7b%22Title%22%3a%22JoJo%e5%a5%87%e5%a6%99%e5%86%92%e9%99%a9%e7%ac%ac07%e9%83%a8%22%2c%22Url%22%3a%22%5c%2fsearch%3ftitle%3dJoJo%25E5%25A5%2587%25E5%25A6%2599%25E5%2586%2592%25E9%2599%25A9%25E7%25AC%25AC07%25E9%2583%25A8%26language%3d1%22%7d%2c%7b%22Title%22%3a%22%e5%b1%b1%e6%b5%b7%e9%80%86%e6%88%98%22%2c%22Url%22%3a%22%5c%2fsearch%3ftitle%3d%25E5%25B1%25B1%25E6%25B5%25B7%25E9%2580%2586%25E6%2588%2598%26language%3d1%22%7d%5d; CNZZDATA30090267=cnzz_eid%3D1998659770-1558219655-%26ntime%3D1558225035; __utmb=1.7.10.1558224937");
		custom_headers.put("Host", "cnc.dm5.com");
		custom_headers.put("If-Modified-Since", "Sunday, 19 May 2019 00:20:26");
		custom_headers.put("If-None-Match", " 636938505325252458-0--1-18625-206660-2-0--52");
		custom_headers.put("Referer", "http://cnc.dm5.com/manhua-jp/");
		custom_headers.put("Upgrade-Insecure-Requests", " 1");
		custom_headers.put("User-Agent", " Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
		HttpResponse resp = myclient.sendGetPacket(url, custom_headers);
		if(log.isDebugEnabled()){
			log.debug(resp.getEntityContent());
		}
		return resp.getEntityContent();
//		List<Comic> parsedComic = parseData(resp.getEntityContent());
//		store(parsedComic);
	}
	
	@Override
	public List<Comic> process(String data) throws Exception{
		List<Comic> result = new ArrayList<>(50);
		String startPattern = "\"mh-item-tip\"";
		String pathStartPattern = "<a href=\"";
		String pathEndPattern = "\"";
		String coverStartPattern = "url(";
		String coverEndPattern = ")";
		String titleStartPattern = "title=\"";
		String titleEndPattern = "\"";
		String descStartPattern = "\"desc\">";
		String descEndPattern = "<";
		
		String authorStartPattern = "\"author\"";
		String authorPageStartPattern = "<a href=\"";
		String authorPageEndPattern = "\"";
		String authorNameStartPattern = "target=\"_blank\">";
		String authorNameEndPattern = "<";
		
		String chaptorStartPattern = "\"chapter\"";
		String chaptorPageStartPattern = "<a href=\"";
		String chaptorPageEndPattern = "\"";
		String chaptorTitleStartPattern = "title=\"";
		String chaptorTitleEndPattern = "\"";
		
		
		int comicStartIdx = 0;
		int authorStartIdx = 0;
		int chaptorStartIdx = 0;
		while(comicStartIdx > -1){
			Comic comic = new Comic();
			comicStartIdx = data.indexOf(startPattern, comicStartIdx + 1);
			String path =  cutString(data, pathStartPattern, pathEndPattern, comicStartIdx);
			String title =  cutString(data, titleStartPattern, titleEndPattern, comicStartIdx);
			String coverImage = cutString(data, coverStartPattern, coverEndPattern, comicStartIdx);
			String desc = cutString(data, descStartPattern, descEndPattern, comicStartIdx);
			
			
			authorStartIdx = data.indexOf(authorStartPattern, comicStartIdx);
			String authorPage = cutString(data, authorPageStartPattern, authorPageEndPattern, authorStartIdx);
			String authorName = cutString(data, authorNameStartPattern, authorNameEndPattern, authorStartIdx);
			
			chaptorStartIdx = data.indexOf(chaptorStartPattern, comicStartIdx);
			String chaptorPage = cutString(data, chaptorPageStartPattern, chaptorPageEndPattern, chaptorStartIdx);
			String chaptorTitle = cutString(data, chaptorTitleStartPattern, chaptorTitleEndPattern, chaptorStartIdx);
			
			if(log.isDebugEnabled()){
				log.debug("----------------------");
				log.debug("path:" +  path);
				log.debug("title:" +  title);
				log.debug("coverImage:" +  coverImage);
				log.debug("desc:" +  desc);
				log.debug("authorPage:" +  authorPage);
				log.debug("authorName:" +  authorName);
				log.debug("chaptorPage:" +  chaptorPage);
				log.debug("chaptorTitle:" +  chaptorTitle);
			}
			comic.setCoverImg(coverImage);
			comic.setDesc(desc);
			comic.setName(title);
			comic.setUrl(path);
			result.add(comic);
		}
		
		return result;
	}
	
	private String cutString(String fullStr, String startPattern, String endPattern, int searchIdx){
		int tmpStartIdx = fullStr.indexOf(startPattern, searchIdx);
		return fullStr.substring(tmpStartIdx + startPattern.length(), fullStr.indexOf(endPattern, tmpStartIdx + startPattern.length())).trim();
	}
	

	@Override
	public void write(List<? extends List<Comic>> parsedComicsList) throws Exception {
		Gson gson = new Gson();
		int cnt = 0;
		for(List<Comic> parsedComics : parsedComicsList){
			for(Comic comic: parsedComics){
				String key = "cn:" + comic.getName();
				jedis.hmset(key, gson.fromJson(gson.toJson(comic), Map.class));
				jedis.expire(key, ThreadLocalRandom.current().nextInt(500000, 600000));
			}
			cnt += parsedComics.size();
		}
		jedis.set("comic:cnt", cnt + "");
	}

}
