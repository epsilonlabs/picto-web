package org.eclipse.epsilon.picto.web.test;

import org.eclipse.epsilon.picto.web.FileWatcher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PictoApplicationTests {
  
    public PictoApplicationTests() {
      FileWatcher.pauseWatching();
    }

	@Test
	void contextLoads() {

	}

}
