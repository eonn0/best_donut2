package com.example.demo.production.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.production.ProcessVO;
import com.example.demo.production.ProdInsDeVO;
import com.example.demo.production.ProdInsVO;
import com.example.demo.production.mapper.ProcessMapper;
import com.example.demo.production.service.ProcessService;

@Service
public class ProcessServiceImpl implements ProcessService {
	
	private final ProcessMapper processMapper;
	
	@Autowired
	public ProcessServiceImpl(ProcessMapper processMapper) {
		this.processMapper = processMapper;
	}

	
/* 1.당일 생산지시 */
	@Override
	public Map<String,Object> getTodayIns() {
		
		Map<String,Object> map = new HashMap<>();
		
		List<ProdInsVO> ins = processMapper.getTodayIns(); //지시
		map.put("prodIns", ins);
		
		//지시상세
		if(ins != null && ins.size() > 0) {
			
			List<ProdInsDeVO> insDe = processMapper.getTodayInsDetail(ins.get(0).getProdInstructCode());
			map.put("prodInsDe", insDe);
			
			//지시상세 상태(함수호출)
			for(int i=0; i < insDe.size(); i++) {
				String status = processMapper.getInsDeStatus(insDe.get(i).getProdInstructDetailCode());
				insDe.get(i).setInsDeStatus(status);
			}
		}
		return map;
	}
	

/* 2.공정 */
	//1)조회
	@Override
	public List<ProcessVO> getProcessInfo(String prodInsDetailCode) { //공정진행
		return processMapper.getProcessInfo(prodInsDetailCode);
	}
	@Override
	public List<ProcessVO> getProcMatInfo(String procDetailCode) { //투입자재
		return processMapper.getProcMatInfo(procDetailCode);
	}
	@Override
	public List<ProcessVO> getProcEqmInfo() { //사용설비 가동상태
		return processMapper.getProcEqmInfo();
	}
	@Override
	public List<ProcessVO> getEqmAllInfo(String eqmName) { //공정별 모든 설비 정보 조회
		return processMapper.getEqmAllInfo(eqmName);
	}

	//2)수정
	@Override
	public int updateProc(ProcessVO vo) {
		return processMapper.updateProc(vo);
	}

	@Override
	public int updateProcEqm(ProcessVO vo) { //공정사용 설비 변경
		return processMapper.updateProcEqm(vo);
	}


/* 3.공정실적 조회 */
	@Override
	public List<ProdInsDeVO> getProcResultDeList(ProdInsDeVO dvo) {
		return processMapper.getProcResultDeList(dvo);
	}


}
