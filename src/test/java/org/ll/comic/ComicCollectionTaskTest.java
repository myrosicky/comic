package org.ll.comic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ll.comic.service.impl.ComicCollectionTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ComicCollectionTaskTest extends TestConfig {

	@Autowired
	private ComicCollectionTask comicCollectionTask;
	
	@Test
	public final void testCollect() {
//		comicCollectionTask.collect();
	}

}
