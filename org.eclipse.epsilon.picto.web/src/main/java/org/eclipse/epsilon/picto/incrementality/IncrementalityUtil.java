package org.eclipse.epsilon.picto.incrementality;

import java.security.MessageDigest;
import java.util.Collection;

import org.eclipse.epsilon.egl.dom.GenerationRule;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule.IncrementalLazyGenerationRuleContentPromise;

public class IncrementalityUtil {

  public static String getPath(Collection<String> path) {
    return "/" + String.join("/", path);
  }

  @SuppressWarnings("unchecked")
  public static String getPath(GenerationRule rule) {
//    rule.getPa
//    Variable pathVar = content.getVariables().stream().filter(v -> v.getName().equals("path")).findFirst()
//        .orElse(null);
//    Collection<String> path = (pathVar != null) ? ((Collection<String>) pathVar.getValue()) : null;
//    String pathStr = "/" + String.join("/", path);
//    return pathStr;
    return "";
  }

  @SuppressWarnings("unchecked")
  public static String getPath(IncrementalLazyGenerationRuleContentPromise content) {
    Variable pathVar = content.getVariables().stream().filter(v -> v.getName().equals("path")).findFirst().orElse(null);
    Collection<String> path = (pathVar != null) ? ((Collection<String>) pathVar.getValue()) : null;
    String pathStr = "/" + String.join("/", path);
    return pathStr;
  }

  public static boolean equals(String value1, String value2) {
    boolean result = false;
    if (value1 != null) {
      result = value1.equals(value2);
    } else if (value2 != null) {
      result = value2.equals(value1);
    } else {
      result = value1 == value2;
    }
    return result;
  }

  public static boolean equals(Object value1, Object value2) {
    boolean result = false;
    if (value1 != null) {
      result = value1.equals(value2);
    } else if (value2 != null) {
      result = value2.equals(value1);
    } else {
      result = value1 == value2;
    }
    return result;
  }

  public static String getHash(String text) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      messageDigest.update(text.getBytes());
      byte[] digest = messageDigest.digest();
      StringBuilder result = new StringBuilder();
      for (byte aByte : digest) {
        result.append(String.format("%02x", aByte));
      }
      String myHash = result.toString().toUpperCase();
      return myHash;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
