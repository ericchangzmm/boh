package com.jije.boh.core.entity;

import java.io.Serializable;

/**
 * 统一定义id的entity基类.
 * 
 * @author eric.zhang
 * 
 */
public abstract class IdEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
