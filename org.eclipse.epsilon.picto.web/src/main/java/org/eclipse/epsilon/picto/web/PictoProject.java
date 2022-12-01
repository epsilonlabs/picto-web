package org.eclipse.epsilon.picto.web;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.egl.dom.GenerationRule;
import org.eclipse.epsilon.flexmi.FlexmiResourceFactory;
import org.eclipse.epsilon.picto.dom.Model;
import org.eclipse.epsilon.picto.dom.Parameter;
import org.eclipse.epsilon.picto.dom.Picto;
import org.eclipse.epsilon.picto.dom.PictoPackage;

public class PictoProject {
  private String name;
  private File pictoFile;
  private Set<File> files = new HashSet<File>();
  private Set<File> modelFiles = new HashSet<File>();
  private Set<File> metamodelFiles = new HashSet<File>();
  
  public PictoProject(File pictoFile) {
    this.setName(pictoFile.getName());
    this.pictoFile = pictoFile;
    this.getFiles().add(pictoFile);
  }

  public Set<File> getModelFiles() {
    return modelFiles;
  }

  public Set<File> getMetamodelFiles() {
    return metamodelFiles;
  }

  @Override
  public String toString() {
    return pictoFile.getAbsolutePath() + " : ["
        + String.join(", ", files.stream().map(f -> f.getAbsolutePath()).collect(Collectors.toList())) + "]";
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<File> getFiles() {
    return files;
  }

  public void setFiles(Set<File> files) {
    this.files = files;
  }

  protected void clear() {
    for (PictoProject project : PictoApplication.getPictoProjects()) {
      project.getFiles().clear();
    }
    PictoApplication.getPictoProjects().clear();
  }

  public static PictoProject createPictoProject(File pictoFile) throws Exception {
    PictoProject pictoProject = new PictoProject(pictoFile);

    Picto picto = loadPicto(pictoFile);

    // get the egx module file
    String egxPath = picto.getTransformation();
    EgxModule egxModule = new EgxModule();
    try {
      File egxFile = new File(pictoFile.getParent() + File.separator + egxPath);
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

    for (Model model : picto.getModels()) {

      for (Parameter parameter : model.getParameters()) {
        if (parameter.getName().equals("modelFile")) {
          String path = (String) parameter.getFile();
          File file = new File(pictoFile.getParentFile().getAbsolutePath() + File.separator + path);
          pictoProject.files.add(file);
          pictoProject.modelFiles.add(file);
        }
        if (parameter.getName().equals("metamodelFile")) {
          String path = (String) parameter.getFile();
          File file = new File(pictoFile.getParentFile().getAbsolutePath() + File.separator + path);
          pictoProject.files.add(file);
          pictoProject.metamodelFiles.add(file);
        }
      }
    }
    
    // clear the resource
    Resource resource = picto.eResource();
    resource.getContents().clear();
    resource.getResourceSet().getResources().clear();
    resource.unload();

    PictoApplication.getPictoProjects().add(pictoProject);
    return pictoProject;
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

  public static Picto loadPicto(File pictoFile) {
    try {
      ResourceSet resourceSet = new ResourceSetImpl();
      resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new FlexmiResourceFactory());
      Resource resource = resourceSet
          .getResource(org.eclipse.emf.common.util.URI.createFileURI(pictoFile.getAbsolutePath()), true);
      resource.load(null);
      Object x = resource.getContents();
      return (Picto) resource.getContents().iterator().next();
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

}