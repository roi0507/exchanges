package com.exchanges.web;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.exchanges.domain.Entity;
import com.exchanges.services.EntityService;

public abstract class AbstractController<T extends Entity<K>,S extends EntityService<T, K>,K> {

	protected abstract String getPlural();
	protected abstract S getService();

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid T entity, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("entity", entity);
			return MessageFormat.format("{0}/create", getPlural());
		}
		uiModel.asMap().clear();
		getService().save(entity);
		return MessageFormat.format("redirect:/{0}/{1}", getPlural(), encodeUrlPathSegment(entity.getId().toString(),
				httpServletRequest));
	}

	
	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(Model uiModel) {
		try {
			uiModel.addAttribute("entity", getService().getManagedClass().newInstance());
		} catch (Exception e) {
			throw(new RuntimeException(e));
		}
		return MessageFormat.format("{0}/create", getPlural());
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") K id, Model uiModel) {
		uiModel.addAttribute("entity", getService().find(id));
		uiModel.addAttribute("itemId", id);
		return getPlural() + "/show";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1)
					* sizeNo;
			uiModel.addAttribute("entities",
					getService().findEntries(firstResult, sizeNo));
			float nrOfPages = (float) getService().countAll() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("entities", getService().findAll());
		}
		return getPlural() + "/list";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@Valid T entity, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("entity", entity);
			return MessageFormat.format("{0}/update", getPlural());
		}
		uiModel.asMap().clear();
		getService().save(entity);
		return "redirect:/" + getPlural() 
				+ encodeUrlPathSegment(entity.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") K id, Model uiModel) {
		uiModel.addAttribute("entity", getService().find(id));
		return getPlural() + "/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") K id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		T entity = getService().find(id);
		getService().delete(entity);
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/" + getPlural();
	}

	@ModelAttribute("entities")
	public Collection<T> populateEntities() {
		return getService().findAll();
	}

	String encodeUrlPathSegment(String pathSegment,
			HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		} catch (UnsupportedEncodingException uee) {
		}
		return pathSegment;
	}

}
