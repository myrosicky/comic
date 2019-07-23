package org.ll.comic.service;

import java.util.List;

import org.ll.comic.model.Comic;

public interface ComicService {

	List<Comic> search(String name);

	String nextPage(String name, String cPage, String episode);

}
