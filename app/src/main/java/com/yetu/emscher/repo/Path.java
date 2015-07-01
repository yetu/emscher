package com.yetu.emscher.repo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface Path {

	public Collection<Path> list();

	public String getName();

	public InputStream getContent() throws IOException;

	public boolean isDirectory();
	
	public String getAbsolutePath();
	
	public boolean exists();

}
