package com.yetu.emscher.filerepo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.yetu.emscher.repo.Path;

public class FilePath implements Path {

	private File file;

	FilePath(String fullPath) {
		this.file = new File(fullPath);
	}

	FilePath(File file) {
		this.file = file;
	}

	@Override
	public Collection<Path> list() {
		File[] childs = file.listFiles();
		if (childs == null) {
			return Collections.unmodifiableCollection(new ArrayList<Path>(0));
		}
		List<Path> result = new ArrayList<Path>(childs.length);
		for (File c : childs) {
			result.add(new FilePath(c));
		}
		return Collections.unmodifiableCollection(result);
	}

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public InputStream getContent() throws IOException {
		FileInputStream fis = new FileInputStream(file);
		return fis;
	}

	@Override
	public boolean isDirectory() {
		return file.isDirectory();
	}

	@Override
	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}

	@Override
	public boolean exists() {
		return file.exists();
	}

}
