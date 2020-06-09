package com.tcts.foresight.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.ResolutionTypeEntity;
import com.tcts.foresight.entity.StatusDetailsEntity;
import com.tcts.foresight.entity.StatusDetailsRemarkEntity;
import com.tcts.foresight.entity.problem.ProblemStatusDetailsEntity;
import com.tcts.foresight.repository.ResolutionTypeRepo;
import com.tcts.foresight.repository.StatusDetailsRemarkRepo;
import com.tcts.foresight.repository.StatusDetailsRepo;
import com.tcts.foresight.service.StatusService;
@Service
@Transactional
public class StatusServiceImpl implements StatusService{
	Logger logger = LoggerFactory.getLogger(StatusServiceImpl.class);

	@Autowired
	private StatusDetailsRepo statusDetailsRepo;
	
	@Autowired
	private ResolutionTypeRepo resolutionTypeRepo;
	
	@Autowired
	private StatusDetailsRemarkRepo statusDetailsRemarkRepo;
	
	@Override
	public List<StatusDetailsEntity> getStatusforRemark() {
		List<StatusDetailsEntity> l1=new ArrayList<StatusDetailsEntity>();
		List<StatusDetailsEntity> list= statusDetailsRepo.findAll();
		for (StatusDetailsEntity l:list) {
			if(l.getStatus().equalsIgnoreCase("On Hold") || l.getStatus().equalsIgnoreCase("Cancelled"))
			{
				l1.add(l);
			}

		}
		return l1;
		
	}
	
	@Override
	public List<StatusDetailsEntity> getAllStatus() {
		List<StatusDetailsEntity> l1=new ArrayList<StatusDetailsEntity>();
		List<StatusDetailsEntity> list= statusDetailsRepo.findAll();
		//logger.info("Data="+list);
		  for (StatusDetailsEntity l:list) {
		  if(l.getStatus().equalsIgnoreCase("New") ||
		  l.getStatus().equalsIgnoreCase("In Progress") ||
		  l.getStatus().equalsIgnoreCase("On Hold") ||
		  l.getStatus().equalsIgnoreCase("Resolved"))
		  {
			  l1.add(l); 
		  }
		  
		  }
		 		return l1;
		
	}

	@Override
	public List<StatusDetailsEntity> getAllStatusIncident() {
		List<StatusDetailsEntity> l1=new ArrayList<StatusDetailsEntity>();
		List<StatusDetailsEntity> list= statusDetailsRepo.findAll();
		//logger.info("Data="+list);
		  for (StatusDetailsEntity l:list) {
		  if(l.getStatus().equalsIgnoreCase("In Progress") ||
		  l.getStatus().equalsIgnoreCase("On Hold") ||
		  l.getStatus().equalsIgnoreCase("Cancelled") ||
		  l.getStatus().equalsIgnoreCase("Resolved"))
		  {
			  l1.add(l); 
		  }
		  }
		return l1;
		
	}
	
    @Override
	public List<StatusDetailsRemarkEntity> getAllStatusRemarkList(String module) {
		return statusDetailsRemarkRepo.findAll().stream() 
				  .filter(name ->name.getModule().equalsIgnoreCase(module)) .collect(Collectors.toList());
	}
	
	@Override
	public List<ResolutionTypeEntity> getAllStatusResolutiontype(String module) {
		return resolutionTypeRepo.findAll().stream().filter(name -> name.getModule().equalsIgnoreCase(module))
				.collect(Collectors.toList());
	}
    
	@Override
	public StatusDetailsRemarkEntity addStatusRemark(StatusDetailsRemarkEntity statusDetails) {	
		return statusDetailsRemarkRepo.save(statusDetails);
	}

	@Override
	public StatusDetailsRemarkEntity updateStatusRemark(Long id, StatusDetailsRemarkEntity status) {
		StatusDetailsRemarkEntity statusRemarkDtls = statusDetailsRemarkRepo.findById(id).get();
		//logger.info("my data in statusUP=" + statusUP);
		if (statusRemarkDtls != null) {
			status.setId(id);
			statusDetailsRemarkRepo.save(status);

			return status;
		}
		return status;
	}
	
	@Override
	public void deleteStatusRemark(Long id) {
		statusDetailsRemarkRepo.deleteById(id);
	}

	@Override
	public StatusDetailsRemarkEntity checkDuplicateStatus(String status,String remark,String module) {
		StatusDetailsRemarkEntity statusRemarkDtls = null;
		try {
			statusRemarkDtls = statusDetailsRemarkRepo.findStatusRemarkByModule(status,remark,module);
		}catch (Exception e) {
			logger.error("Exception occur while in checkDuplicateStatus"+e.getMessage(),e);
		}
		return statusRemarkDtls;
		
	}
	
	@Override
	public List<StatusDetailsRemarkEntity> fetchStatusDetails(String status,String module) {
		List<StatusDetailsRemarkEntity> statusRemarkDtls = null;
		try {
			statusRemarkDtls = statusDetailsRemarkRepo.findStatusByModule(status,module);
		}catch (Exception e) {
		logger.error("Exception occur while in checkPerticularStatus"+e.getMessage(),e);
		}
		return statusRemarkDtls;
		
	}

	@Override
	public List<StatusDetailsEntity> getAllUniqueStatus() {
		// TODO Auto-generated method stub
		return statusDetailsRepo.findAll();
		
	}
	
	@Override
	public List<StatusDetailsEntity> getStatusCloseCancelled() {	
	return statusDetailsRepo.findAll().stream().filter(status -> (status.getStatus().equalsIgnoreCase("Closed"))
			|| (status.getStatus().equalsIgnoreCase("Cancelled"))).collect(Collectors.toList());
		
	}
	
}
