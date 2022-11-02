package org.eclipse.epsilon.picto.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.egl.dom.GenerationRule;
import org.eclipse.epsilon.flexmi.FlexmiResourceFactory;
import org.eclipse.epsilon.picto.dom.Picto;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PictoProject {
  private String name;
  private File projectFile;
  private File pictoFile;
  private Set<File> files = new HashSet<File>();
  private Set<File> modelFiles = new HashSet<File>();
  private Set<File> metamodelFiles = new HashSet<File>();

  private static List<PictoProject> pictoProjects = new ArrayList<PictoProject>();

  public PictoProject(File projectFile) {
    this.setName(projectFile.getName());
    this.projectFile = projectFile;
  }

  public Set<File> getModelFiles() {
    return modelFiles;
  }

  public Set<File> getMetamodelFiles() {
    return metamodelFiles;
  }

  @Override
  public String toString() {
    return projectFile.getAbsolutePath() + " : ["
        + String.join(", ", files.stream().map(f -> f.getAbsolutePath()).collect(Collectors.toList())) + "]";
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public File getProjectFile() {
    return projectFile;
  }

  public void setProjectFile(File projectFile) {
    this.projectFile = projectFile;
  }

  public Set<File> getFiles() {
    return files;
  }

  public void setFiles(Set<File> files) {
    this.files = files;
  }

  public static List<PictoProject> getPictoProjects() {
    return pictoProjects;
  }

  protected void clear() {
    for (PictoProject project : pictoProjects) {
      project.getFiles().clear();
    }
    pictoProjects.clear();
  }

  protected static List<PictoProject> scanDirectory(String directory) throws Exception {
    File file = new File(directory);
    for (File f : file.listFiles()) {
      if (f.isDirectory()) {
        scanDirectory(f.getAbsolutePath());
      }
      if (f.isFile() && f.getName().endsWith(".pictoproj")) {
        createPictoProject(f);
      }
    }
    return pictoProjects;
  }

  protected static void createPictoProject(File projectFile) throws Exception {
    try {
      PictoProject pictoProject = new PictoProject(projectFile);

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(pictoProject.projectFile);
      doc.getDocumentElement().normalize();

      //get the project node 
      Node projectNode = doc.getFirstChild();
      String path = projectNode.getAttributes().getNamedItem("picto").getNodeValue();
      File pictoFile = new File(projectFile.getParentFile().getAbsolutePath() + File.separator + path);
      pictoProject.setPictoFile(pictoFile);
      pictoProject.getFiles().add(pictoFile);

      // load the picto file to get the egx module file and the templates
      ResourceSet resourceSet = new ResourceSetImpl();
      resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("picto",
          new FlexmiResourceFactory());
      Resource resource = resourceSet
          .getResource(org.eclipse.emf.common.util.URI.createFileURI(pictoFile.getAbsolutePath()), true);
      resource.load(null);

      Picto picto = (Picto) resource.getContents().get(0);
      String egxPath = picto.getTransformation();

      // get the egx module file
      EgxModule egxModule = new EgxModule();
      try {
        File egxFile = new File(projectFile.getParent() + File.separator + egxPath);
        egxModule.parse(egxFile);
        pictoProject.getFiles().add(egxFile);

        // get the template files
        for (GenerationRule rule : egxModule.getGenerationRules()) {
          if (rule.getTemplateBlock() == null) {
            continue;
          }
          String templateName = rule.getTemplateBlock().execute(egxModule.getContext());

          if (templateName != null) {
            File templateFile = new File(egxModule.getFile().getParent() + File.separator + templateName);
            pictoProject.getFiles().add(templateFile);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      // get models and metamodels in the project file
      NodeList nodes = projectNode.getChildNodes();

      for (int j = 0; j < nodes.getLength(); j++) {
        Node node = nodes.item(j);
        if (node.getNodeName().equals("model")) {

          String p = node.getAttributes().getNamedItem("path").getNodeValue();
          File file = new File(projectFile.getParentFile().getAbsolutePath() + File.separator + p);
          pictoProject.files.add(file);
          pictoProject.modelFiles.add(file);

        } else if (node.getNodeName().equals("metamodel")) {

          String p = node.getAttributes().getNamedItem("path").getNodeValue();
          File file = new File(projectFile.getParentFile().getAbsolutePath() + File.separator + p);
          pictoProject.files.add(file);
          pictoProject.metamodelFiles.add(file);

        }
      }

      pictoProjects.add(pictoProject);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @return the pictoFile
   */
  public File getPictoFile() {
    return pictoFile;
  }

  /**
   * @param pictoFile the pictoFile to set
   */
  public void setPictoFile(File pictoFile) {
    this.pictoFile = pictoFile;
  }

}