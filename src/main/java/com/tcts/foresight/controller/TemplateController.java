package com.tcts.foresight.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.entity.TemplateEntity;
import com.tcts.foresight.scheduler.entity.SLAConfigurationEntity;
import com.tcts.foresight.scheduler.service.impl.TemplateServiceImpl;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/template")
public class TemplateController {

	@Autowired
	TemplateServiceImpl lTemplateServiceImpl;

	@PostMapping("/savetamplate")
	public TemplateEntity saveFeedback(@RequestBody TemplateEntity lTemplateEntity) {
		System.out.println("in a controller");
		System.out.println(lTemplateEntity.toString());
		return lTemplateServiceImpl.saveTemplate(lTemplateEntity);
	}

	@GetMapping("/findalltemplate/{module}")
	public List<TemplateEntity> findByModule(@PathVariable String module) {
		return lTemplateServiceImpl.findByModule(module);
	}

	@PutMapping("/updatetemplate")
	public TemplateEntity updateSLAWorkFlow(@RequestBody TemplateEntity lTemplateEntity) {
		return lTemplateServiceImpl.updateTemplate(lTemplateEntity);
	}

	@DeleteMapping("/deletetemplate/{templateId}")
	public void fetchSLAWorkfolwSlaName(@PathVariable Long templateId) {
		lTemplateServiceImpl.deleteById(templateId);
	}

	@PostMapping("/viewTemplateBasedOnGroup")
	public List<TemplateEntity> viewTemplateBasedOnGroup(@RequestBody String payLoad) {
		List<TemplateEntity> list = new ArrayList<TemplateEntity>();
		list = lTemplateServiceImpl.findTemplateByGroup(payLoad);
		return list;
	}

	@PostMapping("/getAttachedSlaTemplate")
	public List<SLAConfigurationEntity> getSlaTemplate(@RequestBody String payload) {
		return lTemplateServiceImpl.getSlaTemplate(payload);
	}
}
