package org.ll.comic.web;

import java.util.List;

import org.ll.comic.model.Comic;
import org.ll.comic.service.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comic")
public class ComicController {

	@Autowired
	private ComicService comicService;
	
	@GetMapping("/search?name={name}")
	List<Comic> search(@RequestParam String name){
		List<Comic> result = null;
		result = comicService.search(name);
		return result;
	} 
	
	@GetMapping("/nextPage?name={name}&cPage={cPage}&episode={episode}")
	String nextPage(@RequestParam String name, @RequestParam String cPage, @RequestParam String episode){
		String result = null;
		result = comicService.nextPage(name, cPage, episode);
		return result;
	} 
	
	
	
}
