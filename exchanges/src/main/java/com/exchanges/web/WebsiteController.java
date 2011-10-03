package com.exchanges.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.exchanges.domain.Website;
import com.exchanges.services.WebsiteService;

@RequestMapping("/websites")
@Controller
public class WebsiteController extends AbstractController<Website, WebsiteService, String>{


	@Autowired
	private WebsiteService websiteService;
	
	@Override
	protected WebsiteService getService() {
		return websiteService;
	}

	@Override
	protected String getPlural() {
		return "websites";
	}
}
