package com.example.demo.production.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.demo.production.ProdInsVO;
import com.example.demo.production.ProdPlanDeVO;
import com.example.demo.production.ProdPlanVO;
import com.example.demo.production.service.ProdPlanInsService;

@Controller
public class ProdPlanInsController {

	private final ProdPlanInsService prodPlanInsService;

	@Autowired
	public ProdPlanInsController(ProdPlanInsService prodPlanInsService) {
		this.prodPlanInsService = prodPlanInsService;
	}

/* 페이지 이동 */

	// 생산계획 등록
	@GetMapping("prodPlan")
	public String prodPlan() {
		return "production/prodPlan";
	}

	// 생산계획 관리
	@GetMapping("prodPlanList")
	public String prodPlanList() {
		return "production/prodPlanList";
	}

	// 생산지시 등록
	@GetMapping("prodInstruct")
	public String prodInstruct() {
		return "production/prodInstruct";
	}

	// ============================================================================

/* < 생산요청&상세 > 데이터 */
	@GetMapping("/ajax/prodReq")
	@ResponseBody
	public Map<String, Object> getProdeReq() {
		return prodPlanInsService.getProdReq();
	}


/* < 생산계획 > */

	// 1)조회
	@GetMapping("/ajax/beforeInsertPlanCode") // 생산계획코드 미리 읽어오기
	@ResponseBody
	public ProdPlanVO beforeInsertPlanCode() {
		return prodPlanInsService.beforeInsertPlanCode();
	}

	@PostMapping("/ajax/prodPlanList") // 생산계획목록
	@ResponseBody
	public List<ProdPlanVO> getProdPlanList(@RequestBody ProdPlanVO vo) {
		return prodPlanInsService.getProdPlan(vo);
	}

	@GetMapping("/ajax/prodPlanAll") // 생산계획상세 데이터
	@ResponseBody
	public List<ProdPlanDeVO> getProdPlanAll(@RequestParam String prodPlanCode) {
		return prodPlanInsService.getProdPlanDetail(prodPlanCode).getDvo();
	}

	// 2)등록
	@PostMapping("/ajax/insertProdPlan")
	@ResponseBody
	public ProdPlanVO save(@RequestBody ProdPlanVO vo) {
		prodPlanInsService.insertProdPlan(vo);
		return vo;
	}

	// 3)수정
	@PutMapping("/ajax/updateProdPlanDetail")
	@ResponseBody
	public int modify(@RequestBody List<ProdPlanDeVO> dvo) {
		return prodPlanInsService.updateProdPlanDetail(dvo);
	}

	// 4)삭제
	@DeleteMapping("/ajax/deleteProdPlan")
	@ResponseBody
	public int remove(@RequestBody ProdPlanVO vo) {
		return prodPlanInsService.deleteProdPlan(vo);
	}


/* < 생산지시 > */

	// 1)조회
	@GetMapping("/ajax/weeklyPlan") // +지시 전 주간계획&상세 데이터
	@ResponseBody
	public Map<String, Object> getWeeklyPlan() {
		return prodPlanInsService.getWeeklyPlan();
	}

	@GetMapping("/ajax/eqmCheck") // +지시 전 설비상태 데이터
	@ResponseBody
	public List<ProdInsVO> getEqm() {
		return prodPlanInsService.getEqm();
	}

	@GetMapping("/ajax/beforeInsertInsCode") // 생산지시코드 미리 읽어오기
	@ResponseBody
	public ProdInsVO beforeInsertInsCode() {
		return prodPlanInsService.beforeInsertInsCode();
	}

	// 2)등록
	@PostMapping("/ajax/insertProdInstruct")
	@ResponseBody
	public ProdInsVO saveIns(@RequestBody ProdInsVO vo) {
		prodPlanInsService.insertProdInstruct(vo);
		return vo;
	}

}
