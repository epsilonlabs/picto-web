package org.eclipse.epsilon.picto.incrementality;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PropertyAccessRecordTable implements IncrementalResource {

	List<PropertyAccessRecord> propertyAccessRecords = new LinkedList<>();

	@Override
	public void add(PropertyAccessRecord propertyAccessRecord) {
		List<PropertyAccessRecord> records = propertyAccessRecords.stream()
				.filter(r -> Util.equals(r.getModulePath(), propertyAccessRecord.getModulePath())
						&& Util.equals(r.getGenerationRuleName(), propertyAccessRecord.getGenerationRuleName())
						&& Util.equals(r.getContextResourceUri(), propertyAccessRecord.getContextResourceUri())
						&& Util.equals(r.getContextObjectId(), propertyAccessRecord.getContextObjectId())
						&& Util.equals(r.getContextResourceUri(), propertyAccessRecord.getContextResourceUri())
						&& Util.equals(r.getElementObjectId(), propertyAccessRecord.getElementObjectId())
						&& Util.equals(r.getPropertyName(), propertyAccessRecord.getPropertyName())
						&& Util.equals(r.getTarget(), propertyAccessRecord.getTarget())
				)
				.collect(Collectors.toList());
		if (records.size() > 0) {
			for (PropertyAccessRecord record : records) {
				if (record.getValue() != propertyAccessRecord.getValue()) {
					record.setValue(propertyAccessRecord.getValue());
				}
			}
		} else {
			propertyAccessRecords.add(propertyAccessRecord);
		}
	}

	@Override
	public List<PropertyAccessRecord> getIncrementalRecords() {
		return propertyAccessRecords;
	}

	@Override
	public void printIncrementalRecords() {
		System.out.println();
		System.out.println("Trace at " + Timestamp.from(Instant.now()).toString() + ":");
		System.out.println();
		int lineNum = 1;
		for (PropertyAccessRecord record : propertyAccessRecords) {
			System.out.println(lineNum++ + ". " + record.toString());
		}
		System.out.println();
	}

	@Override
	public List<PropertyAccessRecord> getModifiedPropertiesTargets() {
		List<PropertyAccessRecord> records = propertyAccessRecords.stream().filter(
				r -> 
				!Util.equals(r.oldValue, PropertyAccessRecord.INITIAL_VALUE) && 
				!Util.equals(r.oldValue, r.value))
				.collect(Collectors.toList());
		return records;
	}

}
