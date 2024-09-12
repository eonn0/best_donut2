package com.example.demo.production.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.production.ProcessVO;
import com.example.demo.production.ProdInsDeVO;
import com.example.demo.production.ProdInsVO;
import com.example.demo.production.ProdPlanBVO;
import com.example.demo.production.ProdPlanDeVO;
import com.example.demo.production.ProdPlanVO;
import com.example.demo.production.mapper.ProdPlanInsMapper;
import com.example.demo.production.service.ProdPlanInsService;


@Service
public class ProdPlanInsServiceImpl implements ProdPlanInsService {
	
	private final ProdPlanInsMapper prodPlanInsMapper;
	
	@Autowired
	public ProdPlanInsServiceImpl(ProdPlanInsMapper prodPlanInsMapper) {
		this.prodPlanInsMapper = prodPlanInsMapper;
	}

	
/* < 생산요청 > */
	@Override
	public Map<String,Object> getProdReq() {
		Map<String,Object> map = new HashMap<>();
		List<ProdPlanBVO> list = prodPlanInsMapper.getProdReq();
		
		//요청
		map.put("prodReq", list);
		
		//요청상세
		if(list != null && list.size() > 0) {
			map.put("prodReqDe", prodPlanInsMapper.getProdReqDetail(list.get(0).getProdReqCode()));
		}
		return map;
	}


/* < 생산계획 > */
	//1)조회
	@Override
	public ProdPlanVO beforeInsertPlanCode() { //계획코드 미리보기
		return prodPlanInsMapper.beforeInsertPlanCode();
	}

	@Override
	public List<ProdPlanVO> getProdPlan(ProdPlanVO vo) { //계획
		return prodPlanInsMapper.getProdPlan(vo);
	}

	@Override
	public ProdPlanVO getProdPlanDetail(String prodPlanCode) { //상세
		
		ProdPlanVO prodPlan = new ProdPlanVO();
		prodPlan.setProdPlanCode(prodPlanCode);
		
		//생산계획 조회
		prodPlan = prodPlanInsMapper.getProdPlan(prodPlan).get(0);
		//상세정보 조회
		List<ProdPlanDeVO> details = prodPlanInsMapper.getProdPlanDetail(prodPlanCode);
		
		//생산계획 VO의 dvo필드에 상세정보 설정
		prodPlan.setDvo(details);
		
		return prodPlan;
	}
	
	
	//2)등록
	@Override
	@Transactional
	public int insertProdPlan(ProdPlanVO vo) {
		//생산계획
		prodPlanInsMapper.insertProdPlan(vo);
		//생산요청 상태 update
		prodPlanInsMapper.updateProdReqStatus(vo);
		
		//생산계획 상세
		int result = 0;
		for(ProdPlanDeVO dvo : vo.getDvo()) {
			dvo.setProdPlanCode(vo.getProdPlanCode());//생산계획코드
			result = prodPlanInsMapper.insertProdPlanDetail(dvo);
		}

		return result;
	}

	//3)수정
	@Override
	public int updateProdPlanDetail(List<ProdPlanDeVO> dvo) {
		int result = 0;

		for (ProdPlanDeVO d : dvo) {
			result += prodPlanInsMapper.updateProdPlanDetail(d);
		}
		return result;
	}

	//4)삭제
	@Override
	@Transactional
	public int deleteProdPlan(ProdPlanVO vo) {

		//생산계획 상세
		int result = 0;
		prodPlanInsMapper.deleteProdPlanDetail(vo.getProdPlanCode());
		
		//생산계획 (상세 삭제 후)
		prodPlanInsMapper.deleteProdPlan(vo);
		//생산요청 상태 수정
		result = prodPlanInsMapper.cancelProdReqStatus(vo);
		
		return result;
	}

	
/* < 생산지시 > */
	//1)조회
	//+지시 전 주간생산계획
	@Override
	public Map<String,Object> getWeeklyPlan() {

		Map<String,Object> map = new HashMap<>();

		//계획
		List<ProdPlanVO> plan = prodPlanInsMapper.getWeeklyPlan();
		map.put("weeklyPlan", plan);

		//계획상세
		if(plan != null && plan.size() > 0) {
			map.put("weeklyPlanDe", prodPlanInsMapper.getWeeklyPlanDetail(plan.get(0).getProdPlanCode()));
		}
		return map;
	}

	//+지시 전 설비상태 확인
	@Override
	public List<ProdInsVO> getEqm() {
		return prodPlanInsMapper.getEqm();
	}
	
	@Override
	public ProdInsVO beforeInsertInsCode() { //지시코드 미리보기
		return prodPlanInsMapper.beforeInsertInsCode();
	}
	
	
	//2)등록
	@Override
	@Transactional
	public int insertProdInstruct(ProdInsVO vo) {
		//생산지시
		prodPlanInsMapper.insertProdInstruct(vo);
		//생산계획 상태 update (미지시 -> 지시등록)
		prodPlanInsMapper.updateProdPlanStatus(vo);
		
		//생산지시 상세
		int result = 0;
		for(ProdInsDeVO dvo : vo.getPidvo()) {
			dvo.setProdInstructCode(vo.getProdInstructCode()); //생산지시코드
			result = prodPlanInsMapper.insertProdInstructDetail(dvo);
		}

		//공정생성
		ProcessVO pvo = new ProcessVO();
		pvo.setProdInstructCode(vo.getProdInstructCode()); //생산지시코드
		prodPlanInsMapper.insertProcDetail(pvo);
		
		return result;
	}

}
