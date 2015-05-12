package com.yetu.emscher.mantarepo;

import java.util.Comparator;

import com.joyent.manta.client.MantaObject;

public class MantaUpdateFolderComparator implements Comparator<MantaObject> {

	@Override
	public int compare(MantaObject o1, MantaObject o2) {
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}

		String dirName1 = o1.getPath();
		String dirName2 = o2.getPath();
		return dirName1.compareTo(dirName2);
	}

}
