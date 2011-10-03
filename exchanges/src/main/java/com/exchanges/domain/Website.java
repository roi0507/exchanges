package com.exchanges.domain;

import javax.persistence.Entity;

import org.springframework.roo.addon.javabean.RooJavaBean;

@Entity
@RooJavaBean
public class Website extends AbstractGuidBasedEntity {
	
	private String url;
}
