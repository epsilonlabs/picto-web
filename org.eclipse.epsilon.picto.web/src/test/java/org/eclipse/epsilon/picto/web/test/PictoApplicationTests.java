package org.eclipse.epsilon.picto.web.test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.eclipse.epsilon.picto.dom.PictoFactory;
import org.eclipse.epsilon.picto.dom.PictoPackage;
import org.eclipse.epsilon.picto.web.FileWatcher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class PictoApplicationTests {

  private static final String LOCALHOST = "http://localhost:8080/pictojson/picto?";

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    PictoPackage.eINSTANCE.eClass();
    FileWatcher.scanPictoFiles();
    FileWatcher.startWatching();
  }

  @AfterAll
  static void setUpAfterClass() throws Exception {
    FileWatcher.stopWatching();
  }

  @Autowired
  private MockMvc mvc;

  @Test
  void testGetSocialNetwork() throws Exception {
    MvcResult result = mvc.perform(MockMvcRequestBuilders.get(LOCALHOST)
//    mvc.perform(MockMvcRequestBuilders.get(LOCALHOST)
        .queryParam("file", "/socialnetwork/socialnetwork.model.picto")
        .queryParam("path", "/Social Network")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/Social Network"))
        .andDo(print()).andReturn();

    String x = result.getResponse().getContentAsString();
  }
}
