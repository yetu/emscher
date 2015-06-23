package com.yetu.emscher.mantarepo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.yetu.emscher.Utils;
import com.yetu.emscher.repo.Path;
import com.yetu.manta.client.MantaClient;
import com.yetu.manta.client.representations.MantaObject;

public class MantaPath implements Path {

	private MantaClient client;
	private MantaObject object;
	private String fullPath;

	MantaPath(MantaClient client, MantaObject object, String fullPath) {
		this.client = client;
		this.object = object;
		this.fullPath = fullPath;
	}

	@Override
	public Collection<Path> list() {
		Collection<MantaObject> objects = client.listMantaObjects(fullPath);
		List<Path> result = new ArrayList<Path>(objects.size());
		for (MantaObject o : objects) {
			result.add(new MantaPath(client, o, Utils.concatUrl(fullPath,
					o.getName())));
		}
		return Collections.unmodifiableCollection(result);
	}

	@Override
	public String getName() {
		return object.getName();
	}

	@Override
	public InputStream getContent() throws IOException {
		return client.getObjectInputStream(fullPath);
	}

	@Override
	public boolean isDirectory() {
		return object.isDirectory();
	}

}
