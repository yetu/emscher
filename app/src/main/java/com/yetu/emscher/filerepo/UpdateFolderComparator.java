package com.yetu.emscher.filerepo;

import java.io.File;
import java.util.Comparator;

public class UpdateFolderComparator implements Comparator<File> {

	@Override
	public int compare(File o1, File o2) {
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
