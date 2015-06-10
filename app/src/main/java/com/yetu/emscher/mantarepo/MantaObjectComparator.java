package com.yetu.emscher.mantarepo;

import java.util.Comparator;

import com.yetu.manta.client.representations.MantaObject;

public class MantaObjectComparator implements Comparator<MantaObject> {

	@Override
	public int compare(MantaObject o1, MantaObject o2) {
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
