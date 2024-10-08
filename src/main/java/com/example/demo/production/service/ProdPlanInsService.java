package com.example.demo.production.service;

import java.util.List;
import java.util.Map;

import com.example.demo.production.ProdInsVO;
import com.example.demo.production.ProdPlanDeVO;
import com.example.demo.production.ProdPlanVO;

public interface ProdPlanInsService {

/* 1.생산요청&상세 */
	public Map<String, Object> getProdReq();

/* 2.생산계획 */
	// 1)조회
	public ProdPlanVO beforeInsertPlanCode(); // 계획코드 미리 보기
	public List<ProdPlanVO> getProdPlan(ProdPlanVO vo);
	public ProdPlanVO getProdPlanDetail(String prodPlanCode);

	// 2)등록
	public int insertProdPlan(ProdPlanVO vo);

	// 3)수정
	public int updateProdPlanDetail(List<ProdPlanDeVO> dvo); // updatedRows 배열

	// 4)삭제
	public int deleteProdPlan(ProdPlanVO vo);


/* 3.생산지시 */
	// 1)조회
	public Map<String, Object> getWeeklyPlan(); // 지시 전 주간생산계획
	public ProdInsVO beforeInsertInsCode(); // 지시코드 미리 보기
	public List<ProdInsVO> getEqm(); // 설비상태 확인

	// 2)등록
	public int insertProdInstruct(ProdInsVO vo);

}
