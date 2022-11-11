package org.eclipse.epsilon.picto.web.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class TestUtil {
  
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
  
}