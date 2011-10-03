package com.exchanges.services;

import org.springframework.stereotype.Service;

import com.exchanges.domain.Website;


@Service
public class WebsiteServiceImpl extends AbstractEntityService<Website, String> implements WebsiteService {

	@Override
	public Class<Website> getManagedClass() {
		return Website.class;
	}

}
