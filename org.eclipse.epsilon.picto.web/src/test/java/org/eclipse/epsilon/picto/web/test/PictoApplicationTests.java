/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.epsilon.picto.web.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;

/***
 * Test using the built-in Spring's MockMVC
 * 
 * @author Alfa Yohannis
 *
 */
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

  /***
   * Using the '/socialnetwork/socialnetwork.model.picto' file under the
   * Workspace, this method tests whether '/Social Network' path returns a view
   * that contains Alice, Bob, and Charlie.
   * 
   * @throws IOException
   * @throws SAXException
   * @throws XPathExpressionException
   */
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

    JsonNode node = TestUtil.MAPPER.readTree(result.getResponse().getContentAsString());
    String content = node.get("content").textValue();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document html = builder.parse(new InputSource(new StringReader(content)));
    XPath xPath = XPathFactory.newInstance().newXPath();
    String expression = "//g[@class='node']/title";
    NodeList elements = (NodeList) xPath.compile(expression).evaluate(html, XPathConstants.NODESET);
    Set<String> actualNames = new HashSet<String>(TestUtil.toStringList(elements));

    Set<String> expectedNames = new HashSet<String>(Arrays.asList(new String[] { "Alice", "Bob", "Charlie" }));
    assertThat(actualNames).isSubsetOf(expectedNames);
  }
}
