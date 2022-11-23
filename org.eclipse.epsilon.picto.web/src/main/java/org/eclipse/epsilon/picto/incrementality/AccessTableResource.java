package org.eclipse.epsilon.picto.incrementality;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.emc.emf.AbstractEmfModel;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.picto.LazyEgxModule.LazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule.IncrementalLazyGenerationRuleContentPromise;

public class AccessTableResource implements AccessRecordResource {

	List<AccessRecord> propertyAccessRecords = new LinkedList<>();

	@Override
	public void clear() {
		propertyAccessRecords.clear();
	}

	@Override
	public void updatePath(String modulePath, String ruleName, String contextResourceUri, String contextObjectId,
			String path) {
		propertyAccessRecords.stream()
				.filter(r -> IncrementalityUtil.equals(r.getModulePath(), modulePath)
						&& IncrementalityUtil.equals(r.getGenerationRuleName(), ruleName)
						&& IncrementalityUtil.equals(r.getContextResourceUri(), contextResourceUri)
						&& IncrementalityUtil.equals(r.getContextObjectId(), contextObjectId))
				.forEach(r -> r.setPath(path));
	}

	@Override
	public void add(AccessRecord propertyAccessRecord) {
		List<AccessRecord> records = propertyAccessRecords.stream()
				.filter(r -> IncrementalityUtil.equals(r.getModulePath(), propertyAccessRecord.getModulePath())
						&& IncrementalityUtil.equals(r.getTemplatePath(), propertyAccessRecord.getTemplatePath())
						&& IncrementalityUtil.equals(r.getGenerationRuleName(), propertyAccessRecord.getGenerationRuleName())
						&& IncrementalityUtil.equals(r.getContextResourceUri(), propertyAccessRecord.getContextResourceUri())
						&& IncrementalityUtil.equals(r.getContextObjectId(), propertyAccessRecord.getContextObjectId())
						&& IncrementalityUtil.equals(r.getElementResourceUri(), propertyAccessRecord.getElementResourceUri())
						&& IncrementalityUtil.equals(r.getElementObjectId(), propertyAccessRecord.getElementObjectId())
						&& IncrementalityUtil.equals(r.getPropertyName(), propertyAccessRecord.getPropertyName())
						&& IncrementalityUtil.equals(r.getPath(), propertyAccessRecord.getPath()))
				.collect(Collectors.toList());
		if (records.size() > 0) {
			for (AccessRecord record : records) {
				record.setValue(propertyAccessRecord.getValue());
				record.setState(AccessRecordState.MODIFIED);
			}
		} else {
			propertyAccessRecords.add(propertyAccessRecord);
		}
	}

	@Override
	public List<AccessRecord> getIncrementalRecords() {
		return propertyAccessRecords;
	}

	@Override
	public void printIncrementalRecords() {
		System.out.println();
		System.out.println("Trace at " + Timestamp.from(Instant.now()).toString() + ":");
		System.out.println();
		int lineNum = 1;
		for (AccessRecord record : propertyAccessRecords) {
			System.out.println(lineNum++ + ". " + record.toString());
		}
		System.out.println();
	}

	@Override
	public Set<String> getToBeProcessedPaths(List<IncrementalLazyGenerationRuleContentPromise> inProcessingPromises,
			EgxModule module) {
		Set<String> toBeProcessedPaths = new HashSet<String>();

		for (IncrementalLazyGenerationRuleContentPromise promise : inProcessingPromises) {
			String checkedPath = IncrementalityUtil.getPath(promise);
			System.out.println(checkedPath);

			if (checkedPath.equals("/Social Network/Alice")) {
				System.console();
			}

//			// check if the path is a new view
			AccessRecord pas = propertyAccessRecords.stream()
					.filter(r -> checkedPath.equals(r.getPath())).findFirst().orElse(null);
			if (pas == null) {
				toBeProcessedPaths.add(checkedPath);
			}

			// check if the property access record is new
			AccessRecord accessRecord = propertyAccessRecords.stream()
					.filter(r -> r.getState().equals(AccessRecordState.NEW) && checkedPath.equals(r.getPath())).findFirst()
					.orElse(null);
			if (accessRecord != null) {
				toBeProcessedPaths.add(accessRecord.getPath());

				Set<String> affectedPaths = propertyAccessRecords.stream()
						.filter(r -> ifValueContains(accessRecord.getElementObjectId(), r.getValue()))
						.map(r -> r.getPath()).collect(Collectors.toCollection(HashSet::new));				
				toBeProcessedPaths.addAll(affectedPaths);
			}

			// check objects and properties that view of the path has
			List<AccessRecord> checkedPathRecords = propertyAccessRecords.stream()
					.filter(r -> r.getPath() != null && checkedPath.equals(r.getPath()))
					.collect(Collectors.toCollection(ArrayList::new));

			for (AccessRecord record : checkedPathRecords) {

				String propertyName = record.getPropertyName();
				String previousValue = record.getValue();
				String uriFragment = record.getElementObjectId();
				String elementResourceUri = record.getElementResourceUri();

				EmfModel model = (EmfModel) module.getContext().getModelRepository().getModels().stream().filter(
						m -> ((AbstractEmfModel) m).getResource().getURI().toFileString().equals(elementResourceUri))
						.findFirst().orElse(null);

				if (model != null) {

					Resource currentResource = model.getResource();
					EObject currentEObject = currentResource.getEObject(uriFragment);

					// check if the object has been deleted, does not exists in the resource
					// (deleted, updated)
					if (currentEObject == null) {
						toBeProcessedPaths.add(record.getPath());
						
						// also add other paths affected by the deletion
						Set<String> affectedPaths = propertyAccessRecords.stream()
								.filter(r -> r.getPath() != null && ifValueContains(uriFragment, r.getValue()))
								.map(r -> r.getPath()).collect(Collectors.toCollection(HashSet::new));				
						toBeProcessedPaths.addAll(affectedPaths);
						System.console();
						
						// remove access records related to the object
						propertyAccessRecords.removeIf(r -> r.getElementObjectId().equals(uriFragment));
						propertyAccessRecords.removeIf(r -> {
							return ifValueContains(uriFragment, r.getValue());
						});
					}
					if (currentEObject != null) {
						EStructuralFeature currentProperty = currentEObject.eClass()
								.getEStructuralFeature(propertyName);
						Object currentValueObject = (currentProperty != null) ? currentEObject.eGet(currentProperty)
								: null;

						// check if the view of the path contains object with a changed property
						String currentValue = AccessRecord.convertValueToString(currentValueObject);
						if (!IncrementalityUtil.equals(previousValue, currentValue)) {
							toBeProcessedPaths.add(checkedPath);
						}
					}
				}
			}
		}
		return toBeProcessedPaths;
	}

	private boolean ifValueContains(String uriFragment, String value) {
		if (value == null) return false;
		String[] a = value.split("#");
		if (a[0].startsWith("[")) {
			String b = a[0].replace("[", "");
			b = b.replace("]", "");
			String[] c = b.split(",");
			for (String d : c) {
				String[] e = d.split(":");
				if (uriFragment.equals(e[0]))
					return true;
			}
		} else {
			if (uriFragment.equals(a[0]))
				return true;
		}
		return false;
	}

	@Override
	public void updateStatusToProcessed(Collection<String> paths) {
		propertyAccessRecords.stream().filter(r -> paths.contains(r.getPath())).forEach(r -> {
			r.setState(AccessRecordState.PROCESSED);
		});
	}

//	@Override
//	public void updateStatusToProcessed(String path) {
//		for (Access r : propertyAccessRecords.stream().filter(r -> path.equals(r.getPath()))
//				.collect(Collectors.toList())) {
//			r.setState(AccessState.PROCESSED);
//		}
//		System.console();
//	}

}
