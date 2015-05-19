package com.yetu.emscher;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Metadata {
	private String sha1;
	private long metadataSize;
	private String metadataHash;
	private boolean isDelta;
	private String sha256;
	private long size;

	public String getSha1() {
		return sha1;
	}

	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	@JsonProperty(value="metadata_size")
	public long getMetadataSize() {
		return metadataSize;
	}

	public void setMetadataSize(long metadataSize) {
		this.metadataSize = metadataSize;
	}

	@JsonProperty(value="metadata_hash")
	public String getMetadataHash() {
		return metadataHash;
	}

	public void setMetadataHash(String metadataHash) {
		this.metadataHash = metadataHash;
	}

	@JsonProperty(value="is_delta")
	public boolean isDelta() {
		return isDelta;
	}

	public void setDelta(boolean isDelta) {
		this.isDelta = isDelta;
	}

	public String getSha256() {
		return sha256;
	}

	public void setSha256(String sha256) {
		this.sha256 = sha256;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
}
