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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.eclipse.epsilon.picto.dom.PictoPackage;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
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

	/***
	 * Check if a file inside a work tree.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static File getWorkingGitDir(File file) {

		String workspace = new File(PictoApplication.WORKSPACE).getAbsolutePath();
		File workingGitFile = null;

		while (file.getParent() != null && !file.getAbsolutePath().equals(workspace)) {
			for (File f : file.getParentFile().listFiles()) {
				System.out.println(f.getAbsolutePath());
				if (f.getName().equals(".git") && f.isDirectory()) {
					workingGitFile = f;
					break;
				}
			}
			if (workingGitFile != null) {
				break;
			}
			file = file.getParentFile();
		}

		return (workingGitFile != null) ? workingGitFile.getParentFile() : null;
	}

	/***
	 * Get the the url of the file's repo.
	 * 
	 * @param file
	 * @return Return null if the file is not under a valid repo.s
	 */
	public static String getRepoUrl(File file) {
		String address = null;
		File repoDir = PictoRepository.getWorkingGitDir(file);
		if (repoDir != null) {
			try {

				Git git = Git.open(repoDir);
				address = git.getRepository().getConfig().getString("remote", "origin", "url");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return address;
	}

	/***
	 * Get the the file's repository.
	 * 
	 * @param file
	 * @return Return null if the file is not under a valid repo.
	 */
	public static Repository getRepo(File file) {
		File repoDir = PictoRepository.getWorkingGitDir(file);
		if (repoDir != null) {
			try {

				Git git = Git.open(repoDir);
				return git.getRepository();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/***
	 * Get the the branch name of the file's repo.
	 * 
	 * @param file
	 * @return Return null if the file is not under a valid repo.
	 */
	public static String getRepoBranch(File file) {
		String branch = null;
		File repoDir = PictoRepository.getWorkingGitDir(file);
		if (repoDir != null) {
			try {

				Git git = Git.open(repoDir);
				branch = git.getRepository().getBranch();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return branch;
	}

	/***
	 * Get Workspace
	 * 
	 * @return
	 */
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

		String localTargetDirName = (new File(workspace)).getAbsolutePath() + File.separator
				+ repoAddress.substring(repoAddress.lastIndexOf("/") + 1, repoAddress.length());
		File localTargetDir = new File(localTargetDirName);

		if (!localTargetDir.exists()) {
			localTargetDir.mkdirs();
		}

		System.out.println("Checking out '" + repoAddress + "' to '" + localTargetDirName + "'");

		// fetch, check if remote and local are same
		Git gitFetch = Git.open(localTargetDir);
		Repository repository = gitFetch.getRepository();

		FetchResult fetchResult = gitFetch.fetch().setRemote(repoAddress).setInitialBranch(branch)
				.setRefSpecs(new RefSpec("+refs/heads/*:refs/heads/*")).call();

		String name = Constants.R_HEADS + repository.getBranch();

		Ref trackingRefUpdate = fetchResult.getAdvertisedRef(name);

		if (trackingRefUpdate != null) {
			RevCommit localCommit = repository.parseCommit(repository.resolve(Constants.HEAD));
			RevCommit remoteCommit = repository.parseCommit(trackingRefUpdate.getObjectId());
			// return if they are equal
			if (localCommit.equals(remoteCommit)) {
				System.out.println("Local repository is in sync with the remote repository.");
				gitFetch.close();
				return;
			}
		}
		gitFetch.close();

		System.out.print("Clone remote to local ...");

		//delete the target local directory first
		if (localTargetDir.exists()) {
			Files.walk(localTargetDir.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
		}
		
		// clone
		CloneCommand command = Git.cloneRepository();
		command = command.setURI(repoAddress).setDirectory(localTargetDir).setBranch(branch);

		Git gitCheckout = command.call();

		if (revision != null && revision.trim().length() > 0) {
			gitCheckout.checkout().setName(revision).call();
//				git.checkout().setCreateBranch(true).setName(branch).setStartPoint(revision).call();
		}

		gitCheckout.close();
		System.out.print(" Done");
	}

}
