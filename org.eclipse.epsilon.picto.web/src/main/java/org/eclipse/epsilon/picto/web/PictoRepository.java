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
 * This class is responsible for retrieving files from the supplied Git
 * repository address and Picto file path.
 * 
 * @author Alfa Yohannis
 *
 */
public class PictoRepository {

  String workspace = PictoApplication.WORKSPACE;

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
    pictoRepo.setWorkspace("dummy-workspace");
    pictoRepo.retrievePicto(pictoFilePath, repo, branch, revision);

    System.out.println("Finished!");
  }

  public String getWorkspace() {
    return workspace;
  }

  public void setWorkspace(String workspace) {
    this.workspace = workspace;
  }

  public void retrievePicto(String pictoFilePath, String repoAddress)
      throws InvalidRemoteException, TransportException, IOException, GitAPIException {
    this.retrievePicto(pictoFilePath, repoAddress, null, null);
  }

  public void retrievePicto(String pictoFilePath, String repoAddress, String branch)
      throws InvalidRemoteException, TransportException, IOException, GitAPIException {
    this.retrievePicto(pictoFilePath, repoAddress, branch, null);
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

    if (branch == null || branch.trim().length() == 0) {
      branch = "main";
    }

    String pictoParentDir = pictoFilePath.substring(0, pictoFilePath.lastIndexOf("/") + 1);

    String localTargetDirName = (new File(workspace)).getAbsolutePath() + File.separator
        + repoAddress.substring(repoAddress.lastIndexOf("/") + 1, repoAddress.length());
    File localTargetDir = new File(localTargetDirName);
    if (!localTargetDir.exists()) {
      localTargetDir.mkdirs();
    }

    System.out.println("Checking out '" + repoAddress + "' to '" + localTargetDirName + "'");

    DfsRepositoryDescription repoDesc = new DfsRepositoryDescription();
    InMemoryRepository repository = new InMemoryRepository(repoDesc);
    Git git = new Git(repository);
    git.fetch().setRemote(repoAddress).setRefSpecs(new RefSpec("+refs/heads/*:refs/heads/*")).call();

    ObjectId commitId = null;
    if (revision == null || revision.trim().length() == 0) {
      commitId = repository.resolve("refs/heads/" + branch);
    } else {
      commitId = repository.resolve(revision);
    }

    this.retrieveFiles(localTargetDir, repository, commitId, pictoParentDir);

    git.close();
  }

  /***
   * 
   * @param targetDir
   * @param repository
   * @param commitId
   * @param filePath
   * @return
   * @throws IOException
   * @throws GitAPIException
   * @throws InvalidRemoteException
   * @throws TransportException
   */
  private File retrieveFiles(File targetDir, InMemoryRepository repository, ObjectId commitId, String filePath)
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
          System.out.println("Error writing " + output + " from repo to local");
        }
      }
    }
    treeWalk.close();
    revWalk.close();

    return outputFile;
  }

}
