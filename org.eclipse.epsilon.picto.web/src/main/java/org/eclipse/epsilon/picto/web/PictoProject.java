package org.eclipse.epsilon.picto.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.egl.dom.GenerationRule;
import org.eclipse.epsilon.flexmi.FlexmiResourceFactory;
import org.eclipse.epsilon.picto.dom.Picto;

public class PictoProject {
	private String name;
	private File pictoFile;
	private Set<File> files = new HashSet<File>();

	private static List<PictoProject> pictoProjects = new ArrayList<PictoProject>();

	public PictoProject(File pictoFile) {
		this.setName(pictoFile.getName());
		this.pictoFile = pictoFile;
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

	public File getPictoFile() {
		return pictoFile;
	}

	public void setPictoFile(File pictoFile) {
		this.pictoFile = pictoFile;
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
			if (f.isFile() && f.getName().endsWith(".picto")) {
				createPictoProject(f);
			}
		}
		return pictoProjects;
	}

	protected static void createPictoProject(File pictoFile) throws Exception {
		try {
			PictoProject pictoProject = new PictoProject(pictoFile);

			File modelFile = new File(
					pictoFile.getAbsolutePath().substring(0, pictoFile.getAbsolutePath().lastIndexOf(".picto")));
			pictoProject.getFiles().add(modelFile);

			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("picto",
					new FlexmiResourceFactory());
			Resource resource = resourceSet
					.getResource(org.eclipse.emf.common.util.URI.createFileURI(pictoFile.getAbsolutePath()), true);
			resource.load(null);

			Picto picto = (Picto) resource.getContents().get(0);
			String egxPath = picto.getTransformation();

			EgxModule egxModule = new EgxModule();
			try {
				File egxFile = new File(pictoFile.getParent() + File.separator + egxPath);
				egxModule.parse(egxFile);
				pictoProject.getFiles().add(egxFile);

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

			pictoProjects.add(pictoProject);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
