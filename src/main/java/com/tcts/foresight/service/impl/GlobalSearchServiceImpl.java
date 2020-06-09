package com.tcts.foresight.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.repository.MappedVariableInstanceRepo;
import com.tcts.foresight.util.JSONUtil;
import com.tcts.foresight.util.StringUtil;

@Service
public class GlobalSearchServiceImpl {
	
	Logger logger = LoggerFactory.getLogger(GlobalSearchServiceImpl.class);
	
	@Autowired
	private MappedVariableInstanceRepo mappedVariableInstanceRepo;

	public List<MappedVariableInstanceLogVO> fetchGlobalSearch(String payload) {
		// TODO Auto-generated method stub
		if (StringUtil.isNotNullNotEmpty(payload)) {
			HashMap<String, String> filter = new HashMap<String, String>();
			List<String> groupList = new ArrayList<String>();
			// SearchParam
			String searchParam = null;
			filter = JSONUtil.jsonpayloadMapToHashMap(payload);
			try {
				if (filter != null && filter.size() > 0) {
					String groupName = filter.get("assignmentGroup");
					String module = filter.get("module");
					searchParam = filter.get("searchParam");
					if (StringUtil.isNotNullNotEmpty(groupName) && StringUtil.isNotNullNotEmpty(module)
							&& StringUtil.isNotNullNotEmpty(searchParam)) {

						String group[] = groupName.split(",");
						groupList = Arrays.asList(group);
						searchParam = filter.get("searchParam");
						searchParam = searchParam.trim();
						searchParam = "%" + searchParam + "%";
						return mappedVariableInstanceRepo.fetchGlobalSearchData(groupList, searchParam,
								filter.get("module"));
					}
					// GroupName

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("Exception occur while GLOBAL SEARCH  " + e.getMessage(), e);
			}

		}

		return null;
	}
	
	

}
