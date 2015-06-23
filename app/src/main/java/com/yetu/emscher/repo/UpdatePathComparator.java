package com.yetu.emscher.repo;

import java.util.Comparator;

public class UpdatePathComparator implements Comparator<Path> {

	@Override
	public int compare(Path o1, Path o2) {
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}

		String dirName1 = o1.getName();
		String dirName2 = o2.getName();
		return dirName1.compareTo(dirName2);
	}

}
