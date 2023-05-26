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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
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
	 * @throws NoSuchAlgorithmException
	 */
	public static void main(String[] args)
			throws InvalidRemoteException, TransportException, GitAPIException, IOException, NoSuchAlgorithmException {

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
	private static File getWorkingGitDir(File file) throws IOException {

		String workspace = new File(PictoApplication.WORKSPACE).getAbsolutePath();
		File workingGitFile = null;

		while (file.getParent() != null && !file.getAbsolutePath().equals(workspace)) {

			if (file.getParentFile() == null) {
				break;
			}

			Path path = Files.list(Paths.get(file.getParentFile().toURI()))
					.filter(p -> p.toFile().getName().equals(".git") && p.toFile().isDirectory()).findFirst()
					.orElse(null);

			if (path != null) {
				workingGitFile = path.toFile();
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
	 * @throws IOException
	 */
	public static String getRepoUrl(File file) throws IOException {
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
	 * @throws IOException
	 */
	public static Repository getRepo(File file) throws IOException {
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
	 * @throws IOException
	 */
	public static String getRepoBranch(File file) throws IOException {
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
			throws InvalidRemoteException, TransportException, IOException, GitAPIException, NoSuchAlgorithmException {
		this.retrievePicto(pictoFilePath, repoAddress, null, null);
	}

	public void retrievePicto(String pictoFilePath, String repoAddress, String branch)
			throws InvalidRemoteException, TransportException, IOException, GitAPIException, NoSuchAlgorithmException {
		this.retrievePicto(pictoFilePath, repoAddress, branch, null);
	}

	/***
	 * 
	 * @param pictoFilePath
	 * @param repo
	 * @param branch
	 * @param revision
	 * @throws InvalidRemoteException
	 * @throws TransportException
	 * @throws IOException
	 * @throws GitAPIException
	 * @throws NoSuchAlgorithmException
	 */

	public void retrievePicto(String pictoFilePath, String repo, String branch, String revision)
			throws InvalidRemoteException, TransportException, IOException, GitAPIException, NoSuchAlgorithmException {

		if (branch == null || branch.trim().length() == 0) {
			branch = "main";
		}

		String hashedRepo = generateHash(repo, branch);

		String localTargetDirName = (new File(workspace)).getAbsolutePath() + File.separator + hashedRepo;
		File localTargetDir = new File(localTargetDirName);

		if (!localTargetDir.exists()) {
			localTargetDir.mkdirs();
		}

		System.out.println("Checking out '" + repo + "' to '" + localTargetDirName + "'");

		// fetch, check if remote and local are same
		try {
			Git gitFetch = Git.open(localTargetDir);
			Repository repository = gitFetch.getRepository();

			FetchResult fetchResult = gitFetch.fetch().setRemote(repo).setInitialBranch(branch)
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
		} catch (Exception e) {
			System.out.println("Repo doesn't exist");
		}

		System.out.print("Clone remote to local ...");

		// delete the target local directory first
		if (localTargetDir.exists()) {
			Files.walk(localTargetDir.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile)
					.forEach(File::delete);
		}

		// clone
		CloneCommand command = Git.cloneRepository();
		command = command.setURI(repo).setDirectory(localTargetDir).setBranch(branch);

		Git gitCheckout = command.call();

		if (revision != null && revision.trim().length() > 0) {
			gitCheckout.checkout().setName(revision).call();
//				git.checkout().setCreateBranch(true).setName(branch).setStartPoint(revision).call();
		}

		gitCheckout.close();
		System.out.print(" Done");
	}

	public static String generateHash(String repo, String branch) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		byte[] bytes = digest.digest((repo + branch).getBytes(StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

}
