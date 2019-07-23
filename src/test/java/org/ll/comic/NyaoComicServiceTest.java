package org.ll.comic;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ll.comic.model.Comic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class NyaoComicServiceTest {

	private final static Logger log = LoggerFactory.getLogger(NyaoComicServiceTest.class);
	
	@Autowired
	private RestTemplate restTempalte;
	
	@Test
	public final void testSearch() {
		List<Comic> resp = restTempalte.getForObject("http://localhost:8085/comic/search?name=全职猎人" , List.class);
	}

	@Test
	public final void testNextPage() {
		fail("Not yet implemented"); // TODO
	}

}
