package org.eclipse.epsilon.picto.web.test;

import java.io.UnsupportedEncodingException;
import org.w3c.dom.NodeList;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * A utility class used in Picto Web tests.
 * 
 * @author Alfa Yohannis
 *
 */

public class TestUtil {

  /***
   * This method constructs the parameters string for HTTP URL.
   * 
   * @param params A String to String Map that maps parameters and their values.
   * @return The parameters string.
   * @throws UnsupportedEncodingException
   */
  public static String getParamsString(Map<String, String> params)
      throws UnsupportedEncodingException {
    StringBuilder result = new StringBuilder();

    for (Map.Entry<String, String> entry : params.entrySet()) {
      result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
      result.append("=");
      result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
      result.append("&");
    }

    return result.toString();
  }

  /***
   * Construct a list of the values of interest contained in the returned view
   * (HTML document). The list contains the actual values and should compared
   * against the expected values defined in each test.
   * 
   * @param <T>
   * @param nodeList The nodeList retrieved using XPath.
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> List<T> toStringList(NodeList nodeList) {
    List<T> result = new ArrayList<>();
    for (int i = 0; i < nodeList.getLength(); i++) {
      T name = (T) nodeList.item(i).getTextContent();
      result.add(name);
    }
    return result;
  }

}