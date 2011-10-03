package com.exchanges.domain;

public interface Entity<K> {

	K getId();
	void setId(K id);
}
