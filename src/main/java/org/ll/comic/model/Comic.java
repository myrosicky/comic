package org.ll.comic.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Comic implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String coverImg;
	private String desc;
	private String url;
	
}
