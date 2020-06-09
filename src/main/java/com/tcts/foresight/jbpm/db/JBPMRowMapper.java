package com.tcts.foresight.jbpm.db;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.entity.problem.ProblemDetailsEntity;
import com.tcts.foresight.jbpm.db.pojo.ActualVariableInstanceLogVO;
import com.tcts.foresight.util.StringUtil;

@Component
public class JBPMRowMapper {

	Logger logger = LoggerFactory.getLogger(JBPMRowMapper.class);

	public HashMap<String, MappedVariableInstanceLogVO> getConvertedJBPMResponseAsMAP(
			List<ActualVariableInstanceLogVO> lActualVariableInstanceLogVO) {

		HashMap<String, MappedVariableInstanceLogVO> convertedMap = new HashMap<String, MappedVariableInstanceLogVO>();

		if (lActualVariableInstanceLogVO != null && lActualVariableInstanceLogVO.size() > 0) {

			for (ActualVariableInstanceLogVO pActualVariableInstanceLogVO : lActualVariableInstanceLogVO) {

				if (StringUtil.isNotNullNotEmpty(pActualVariableInstanceLogVO.getProcessInstanceId())) {
					if (convertedMap.containsKey(pActualVariableInstanceLogVO.getProcessInstanceId())) {
						MappedVariableInstanceLogVO pMappedVariableInstanceLogVO = (MappedVariableInstanceLogVO) convertedMap
								.get(pActualVariableInstanceLogVO.getProcessInstanceId());
						if (StringUtil.isNotNullNotEmpty(pActualVariableInstanceLogVO.getVariableid())) {
							pMappedVariableInstanceLogVO
									.setProcessInstanceId(pActualVariableInstanceLogVO.getProcessInstanceId());
							pMappedVariableInstanceLogVO = setObject(pActualVariableInstanceLogVO,
									pMappedVariableInstanceLogVO);
						}

					} else {
						MappedVariableInstanceLogVO newMappedVariableInstanceLogVO = new MappedVariableInstanceLogVO();
						newMappedVariableInstanceLogVO = setObject(pActualVariableInstanceLogVO,
								newMappedVariableInstanceLogVO);

						convertedMap.put(pActualVariableInstanceLogVO.getProcessInstanceId(),
								newMappedVariableInstanceLogVO);

					}
				}
			}
		}

		return convertedMap;

	}

	public static MappedVariableInstanceLogVO setObject(ActualVariableInstanceLogVO pActualVariableInstanceLogVO,
			MappedVariableInstanceLogVO pMappedVariableInstanceLogVO) {

//		String value = pActualVariableInstanceLogVO.getValue();
		setObjects(pActualVariableInstanceLogVO, pMappedVariableInstanceLogVO);
		return pMappedVariableInstanceLogVO;
	}

	private static void setObjects(ActualVariableInstanceLogVO pActualVariableInstanceLogVO,
			MappedVariableInstanceLogVO lMappedVariableInstanceLogVO) {
		try {
			Method methods[] = lMappedVariableInstanceLogVO.getClass().getMethods();
			boolean callSetter = false;
			for (Method method : methods) {
				String methodName = method.getName();
				if (methodName.startsWith("get") && !methodName.startsWith("getIncidentAttachList") && !methodName.startsWith("getIncidentAgeing")) {
					String lowerMEthodName = methodName.toLowerCase();
					String subStringMethodName = lowerMEthodName.substring(3, lowerMEthodName.length());
					if (pActualVariableInstanceLogVO.getVariableid().equalsIgnoreCase(subStringMethodName)) {
						Method callingClassMEthod = lMappedVariableInstanceLogVO.getClass().getMethod(methodName);
						String response = (String) callingClassMEthod.invoke(lMappedVariableInstanceLogVO);
						if (response == null) {
							callSetter = true;
						}
					}

				}
			} 

			if (callSetter) {
				for (Method method : methods) {
					String methodName = method.getName();
					if (methodName.startsWith("set") && !methodName.startsWith("setIncidentAttachList") &&  !methodName.startsWith("setIncidentAgeing")) {
						String lowerMEthodName = methodName.toLowerCase();
						String subStringMethodName = lowerMEthodName.substring(3, lowerMEthodName.length());

						Method callingClassMEthod = lMappedVariableInstanceLogVO.getClass().getMethod(methodName,
								String.class);
						if (pActualVariableInstanceLogVO.getVariableid().equalsIgnoreCase(subStringMethodName)) {
							callingClassMEthod.invoke(lMappedVariableInstanceLogVO,
									pActualVariableInstanceLogVO.getValue());
						}
					}
				} 

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<MappedVariableInstanceLogVO> getConvertedJBPMResponseAsList(
			List<ActualVariableInstanceLogVO> lActualVariableInstanceLogVO) {
		List<MappedVariableInstanceLogVO> lMappedVariableInstanceLogVO = null;
		ArrayList<MappedVariableInstanceLogVO> filteredMap = new ArrayList<MappedVariableInstanceLogVO>();

		HashMap<String, MappedVariableInstanceLogVO> convertedMap = getConvertedJBPMResponseAsMAP(
				lActualVariableInstanceLogVO);

		Collection<MappedVariableInstanceLogVO> mappedValues = convertedMap.values();
		lMappedVariableInstanceLogVO = new ArrayList<MappedVariableInstanceLogVO>(mappedValues);
		for (MappedVariableInstanceLogVO mappedVariableInstanceLogVO : mappedValues) {
			if (mappedVariableInstanceLogVO.getIncidentID() != null) {
				filteredMap.add(mappedVariableInstanceLogVO);
			}
		}
		return filteredMap;

	}
	
	public List<ProblemDetailsEntity> getProblemConvertedJBPMResponseAsList(
			List<ActualVariableInstanceLogVO> lActualVariableInstanceLogVO) {
		List<ProblemDetailsEntity> lMappedVariableInstanceLogVO = null;
		ArrayList<ProblemDetailsEntity> filteredMap = new ArrayList<ProblemDetailsEntity>();

		HashMap<String, ProblemDetailsEntity> convertedMap = getProblemConvertedJBPMResponseAsMAP(
				lActualVariableInstanceLogVO);

		Collection<ProblemDetailsEntity> mappedValues = convertedMap.values();
		lMappedVariableInstanceLogVO = new ArrayList<ProblemDetailsEntity>(mappedValues);
		for (ProblemDetailsEntity mappedVariableInstanceLogVO : mappedValues) {
			if (mappedVariableInstanceLogVO.getProblemID() != null) {
				filteredMap.add(mappedVariableInstanceLogVO);
			}
		}
		return filteredMap;

	}

	private HashMap<String, ProblemDetailsEntity> getProblemConvertedJBPMResponseAsMAP(
			List<ActualVariableInstanceLogVO> lActualVariableInstanceLogVO) {
		// TODO Auto-generated method stub


		HashMap<String, ProblemDetailsEntity> convertedMap = new HashMap<String, ProblemDetailsEntity>();

		if (lActualVariableInstanceLogVO != null && lActualVariableInstanceLogVO.size() > 0) {

			for (ActualVariableInstanceLogVO pActualVariableInstanceLogVO : lActualVariableInstanceLogVO) {

				if (StringUtil.isNotNullNotEmpty(pActualVariableInstanceLogVO.getProcessInstanceId())) {
					if (convertedMap.containsKey(pActualVariableInstanceLogVO.getProcessInstanceId())) {
						ProblemDetailsEntity pMappedVariableInstanceLogVO = (ProblemDetailsEntity) convertedMap
								.get(pActualVariableInstanceLogVO.getProcessInstanceId());
						if (StringUtil.isNotNullNotEmpty(pActualVariableInstanceLogVO.getVariableid())) {
							pMappedVariableInstanceLogVO.setProcessInstanceId(pActualVariableInstanceLogVO.getProcessInstanceId());
							pMappedVariableInstanceLogVO = setProblemObject(pActualVariableInstanceLogVO,
									pMappedVariableInstanceLogVO);
						}

					} else {
						ProblemDetailsEntity newMappedVariableInstanceLogVO = new ProblemDetailsEntity();
						newMappedVariableInstanceLogVO = setProblemObject(pActualVariableInstanceLogVO,
								newMappedVariableInstanceLogVO);

						convertedMap.put(pActualVariableInstanceLogVO.getProcessInstanceId(),
								newMappedVariableInstanceLogVO);

					}
				}
			}
		}

		return convertedMap;

	
	
	}

	private ProblemDetailsEntity setProblemObject(ActualVariableInstanceLogVO pActualVariableInstanceLogVO,ProblemDetailsEntity lMappedVariableInstanceLogVO) 
	{
		try {
			Method methods[] = lMappedVariableInstanceLogVO.getClass().getMethods();
			boolean callSetter = false;
			for (Method method : methods) {
				String methodName = method.getName();
				if (methodName.startsWith("get") && !methodName.startsWith("getProblemAttachmentList") && !methodName.startsWith("getAssociatedIncidentList")) {
					String lowerMEthodName = methodName.toLowerCase();
					String subStringMethodName = lowerMEthodName.substring(3, lowerMEthodName.length());
					if (pActualVariableInstanceLogVO.getVariableid().equalsIgnoreCase(subStringMethodName)) {
						Method callingClassMEthod = lMappedVariableInstanceLogVO.getClass().getMethod(methodName);
						String response = (String) callingClassMEthod.invoke(lMappedVariableInstanceLogVO);
						if (response == null) {
							callSetter = true;
						}
					}

				}
			} 

			if (callSetter) {
				for (Method method : methods) {
					String methodName = method.getName();
					
					if (methodName.startsWith("set") && !methodName.startsWith("setProblemAttachmentList") &&  !methodName.startsWith("setAssociatedIncidentList")) {
						String lowerMEthodName = methodName.toLowerCase();
						String subStringMethodName = lowerMEthodName.substring(3, lowerMEthodName.length());

						Method callingClassMEthod = lMappedVariableInstanceLogVO.getClass().getMethod(methodName,
								String.class);
						if (pActualVariableInstanceLogVO.getVariableid().equalsIgnoreCase(subStringMethodName)) {
							callingClassMEthod.invoke(lMappedVariableInstanceLogVO,
									pActualVariableInstanceLogVO.getValue());
						}
					}
				} 

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lMappedVariableInstanceLogVO;
	}

}
