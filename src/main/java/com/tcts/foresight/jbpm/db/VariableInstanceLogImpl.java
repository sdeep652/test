package com.tcts.foresight.jbpm.db;



import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcts.foresight.jbpm.db.pojo.ActualVariableInstanceLogVO;



public class VariableInstanceLogImpl {

	Logger logger = LoggerFactory.getLogger(VariableInstanceLogImpl.class);
	
private VariableInstanceLogInterface lVariableInstanceLogInterface;
	
	public VariableInstanceLogImpl()
	{

	}
	
	public ArrayList<ActualVariableInstanceLogVO> getProcessInstancesList() {
		ArrayList<ActualVariableInstanceLogVO> lActualVariableInstanceLogVO = new ArrayList<ActualVariableInstanceLogVO>();
		lActualVariableInstanceLogVO = lVariableInstanceLogInterface.getProcessInstancesList();
		return lActualVariableInstanceLogVO;
	}
	
}
