package com.tcts.foresight.entity;

import java.util.List;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcts.foresight.repository.ConfigRepo;

@Component
public class ConfigEntityCached {

	@Autowired
	ConfigRepo configRepo;
	
	private TreeMap<String, String> configMap = new TreeMap<String, String>();
	
	
	public TreeMap<String, String> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(TreeMap<String, String> configMap) {
		this.configMap = configMap;
	}
	
	public String getValue(String key)
	{
		if(configMap.get(key) ==null)
		{
			prepareMap(configRepo.findAll());
			return configMap.get(key);
		}else {
			return configMap.get(key);
		}
		
	}
	
	public TreeMap<String, String> getAllValue()
	{
		if(configMap==null || configMap.size()==0 )
		{
			prepareMap(configRepo.findAll());
		}
		return configMap;
			 
	}
	
	public void cleanAllValue()
	{
		configMap = new TreeMap<String, String>();
	}

		
	
	private void prepareMap(List<ConfigEntity> list)
	{
		for(ConfigEntity lConfigEntity: list)	
		{
			configMap.put(lConfigEntity.getConfigKey(), lConfigEntity.getConfigValue());
		}
	}
	

}
