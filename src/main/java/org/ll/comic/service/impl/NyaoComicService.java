package org.ll.comic.service.impl;

import java.util.Date;
import java.util.List;

import org.ll.comic.model.Comic;
import org.ll.comic.service.ComicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NyaoComicService implements ComicService {

	private final static Logger log = LoggerFactory.getLogger(NyaoComicService.class);
	
	@Value("${nyao.search.url}")
	private String searchUrL;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public List<Comic> search(String name) {
		log.debug("searching " + name);
		searchUrL = "https://sp0.baidu.com/5a1Fazu8AA54nxGko9WTAnF6hhy/su?wd="+name+"&p=3&cb=BaiduSuggestion.callbacks.give1&t=" + new Date().getTime();
		ResponseEntity<String> respone = restTemplate.getForEntity(searchUrL, String.class);
		if(log.isDebugEnabled()){
			log.debug("respone.getBody():" + respone.getBody());
		}
		
		String searchUrL2 = "https://nyaso.com/man/"+name+".html";
		respone = restTemplate.getForEntity(searchUrL2, String.class);
		if(log.isDebugEnabled()){
			log.debug("searchUrL2 respone.getBody():" + respone.getBody());
		}
		
		return null;
	}

	@Override
	public String nextPage(String name, String cPage, String episode) {
		return null;
	}

	
}
