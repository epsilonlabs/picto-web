package org.eclipse.epsilon.picto.web.generator;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.modisco.infra.discovery.core.exception.DiscoveryException;
import org.junit.Test;

public class BigModelTest {

  enum ChangeType {
    CHANGE, ADD, MOVE, DELETE;
  }

  @Test
  public void generateBigModel() throws DiscoveryException, CoreException, IOException {
    System.out.println("Start...");
    BigModelGenerator generator = new BigModelGenerator();
    generator.generate();
    assertEquals(true, true);
    System.out.println("Done");
  }
}
