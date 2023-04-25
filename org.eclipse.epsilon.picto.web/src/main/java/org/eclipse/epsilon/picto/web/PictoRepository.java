/*********************************************************************
* Copyright (c) 2023 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
*
* @author Alfa Yohannis
**********************************************************************/

/**
 * 
 */
package org.eclipse.epsilon.picto.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.epsilon.flexmi.FlexmiResourceFactory;
import org.eclipse.epsilon.picto.dom.CustomView;
import org.eclipse.epsilon.picto.dom.Model;
import org.eclipse.epsilon.picto.dom.Parameter;
import org.eclipse.epsilon.picto.dom.Picto;
import org.eclipse.epsilon.picto.dom.PictoPackage;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.internal.storage.dfs.DfsRepositoryDescription;
import org.eclipse.jgit.internal.storage.dfs.InMemoryRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * @author Alfa Yohannis
 *
 */
public class PictoRepository {

  static String workspace = "dummy-workspace";

  /**
   * @param args
   * @throws GitAPIException
   * @throws TransportException
   * @throws InvalidRemoteException
   * @throws IOException
   */
  public static void main(String[] args)
      throws InvalidRemoteException, TransportException, GitAPIException, IOException {

    Logger logger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    logger.setLevel(Level.OFF);
     
    PictoPackage.eINSTANCE.eClass();

    String repo = "https://github.com/epsilonlabs/picto-web";
    String pictoFilePath = "workspace/socialnetwork/socialnetwork.model.picto";
    String branch = "main";
    String revision = "";

    PictoRepository pictoRepo = new PictoRepository();
    pictoRepo.retrievePicto(pictoFilePath, repo, branch, revision);

    System.out.println("Finished!");
  }

  /***
   * 
   * @param pictoFilePath
   * @param repoAddress
   * @param branch
   * @param revision
   * @throws InvalidRemoteException
   * @throws TransportException
   * @throws IOException
   * @throws GitAPIException
   */

  public void retrievePicto(String pictoFilePath, String repoAddress, String branch, String revision)
      throws InvalidRemoteException, TransportException, IOException, GitAPIException {

    String pictoParentDir = pictoFilePath.substring(0, pictoFilePath.lastIndexOf("/") + 1);

    String localTargetDirName = (new File(workspace)).getAbsolutePath() + File.separator
        + repoAddress.substring(repoAddress.lastIndexOf("/") + 1, repoAddress.length());
    File localTargetDir = new File(localTargetDirName);
    if (!localTargetDir.exists()) {
      localTargetDir.mkdirs();
    }

    System.out.println("Checking out " + repoAddress + " to " + localTargetDirName);

    DfsRepositoryDescription repoDesc = new DfsRepositoryDescription();
    InMemoryRepository repository = new InMemoryRepository(repoDesc);
    Git git = new Git(repository);
    git.fetch().setRemote(repoAddress).setRefSpecs(new RefSpec("+refs/heads/*:refs/heads/*")).call();

    ObjectId commitId = repository.resolve("refs/heads/" + branch);

    File pictoFile = this.retrieveFile(localTargetDir, repository, commitId, pictoParentDir);

//    File pictoFile = this.retrieveFile(localTargetDir, repository, commitId, pictoFilePath);

//    // load and the retrieved picto file and then collect all other related files
//    // (e.g., metamodel, model, etc.)
//    ResourceSet resourceSet = new ResourceSetImpl();
//    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new FlexmiResourceFactory());
//    Resource resource = resourceSet
//        .getResource(org.eclipse.emf.common.util.URI.createFileURI(pictoFile.getAbsolutePath()), true);
//    resource.load(null);
//    Picto picto = (Picto) resource.getContents().get(0);
//
//    List<String> relatedFilesPaths = new ArrayList<>();
//    if (picto.getTransformation() != null) {
//      relatedFilesPaths.add(picto.getTransformation());
//    }
//    for (Model model : picto.getModels()) {
//      for (Parameter parameter : model.getParameters()) {
//        if (parameter.getName().equals("metamodelFile") && parameter.getFile() != null) {
//          relatedFilesPaths.add((String) parameter.getFile());
//        } else if (parameter.getName().equals("modelFile") && parameter.getFile() != null) {
//          relatedFilesPaths.add((String) parameter.getFile());
//        }
//      }
//    }
//    for (CustomView view : picto.getCustomViews()) {
//      if (view.getSource() != null) {
//        relatedFilesPaths.add(view.getSource());
//      }
//    }
//
//    // get all other files
//    for (String relatedFilePath : relatedFilesPaths) {
//      this.retrieveFile(localTargetDir, repository, commitId, pictoParentDir + relatedFilePath);
//    }

    git.close();
  }

  /***
   * 
   * @param targetDir
   * @param repository
   * @param commitId
   * @param filePath
   * @param repo
   * @param branch
   * @param revision
   * @return
   * @throws IOException
   * @throws GitAPIException
   * @throws InvalidRemoteException
   * @throws TransportException
   */
  private File retrieveFile(File targetDir, InMemoryRepository repository, ObjectId commitId, String filePath)
      throws IOException, GitAPIException, InvalidRemoteException, TransportException {

    File outputFile = null;

    String pictoFileNoSlash = filePath;
    while (pictoFileNoSlash.length() > 0 && pictoFileNoSlash.charAt(0) == '/') {
      pictoFileNoSlash = pictoFileNoSlash.substring(1, pictoFileNoSlash.length());
    }

    RevWalk revWalk = new RevWalk(repository);
    RevCommit commit = revWalk.parseCommit(commitId);
    RevTree tree = commit.getTree();
    TreeWalk treeWalk = new TreeWalk(repository);
    treeWalk.addTree(tree);
    treeWalk.setRecursive(true);
    treeWalk.setFilter(PathFilter.create(filePath));
    while (treeWalk.next()) {
      ObjectId blobId = treeWalk.getObjectId(0);
      try (ObjectReader objectReader = repository.newObjectReader()) {
        ObjectLoader objectLoader = objectReader.open(blobId);
//        objectLoader.copyTo(System.out);
        byte[] bytes = objectLoader.getBytes();
        String output = targetDir.getAbsolutePath() + "/" + treeWalk.getPathString();
        output = output.replace("/", File.separator);
        outputFile = new File(output);
        outputFile.getParentFile().mkdirs();
        try (FileOutputStream fOS = new FileOutputStream(output)) {
          fOS.write(bytes);
          System.out.println(outputFile.getAbsolutePath() + " created");
        } catch (Exception e) {
          System.out
              .println("Error writing " + output + " from repo to local");
        }
      }
    }
    treeWalk.close();
    revWalk.close();

//    try (RevWalk revWalk = new RevWalk(repository)) {
//      try (TreeWalk treeWalk = TreeWalk.forPath(repository, filePath, revWalk.parseCommit(commitId).getTree())) {
//        if (treeWalk == null) {
//          System.out.println(String.format("Could not find path '%s'", filePath));
//          return null;
//        }
//
//        ObjectId blobId = treeWalk.getObjectId(0);
//        try (ObjectReader objectReader = repository.newObjectReader()) {
//          ObjectLoader objectLoader = objectReader.open(blobId);
////          objectLoader.copyTo(System.out);
//          byte[] bytes = objectLoader.getBytes();
//          String output = targetDir.getAbsolutePath() + "/" + filePath;
//          output = output.replace("/", File.separator);
//          outputFile = new File(output);
//          outputFile.getParentFile().mkdirs();
//          try (FileOutputStream fOS = new FileOutputStream(output)) {
//            fOS.write(bytes);
//            System.out.println(outputFile.getAbsolutePath() + " created");
//          } catch (Exception e) {
//            System.out.println(
//                "There was an error writing the contents of the file in the repository into the provided file.");
//          }
//        }
//      } catch (Exception e) {
//        System.out.println("There was an error traversing the Git tree to retrieve the file contents.");
//      }
//    } catch (Exception e) {
//      System.out.println("There was an error accessing the Git repository to retrieve the file contents.");
//    }
    return outputFile;
  }

}
