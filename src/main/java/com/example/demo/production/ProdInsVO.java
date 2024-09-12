package com.example.demo.production;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdInsVO {

	// 생산지시
	private String prodInstructCode;
	private String prodPlanCode;
	private String usersCode;
	private String usersName;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date instructDate;
	private String prodInstructStatus;

	List<ProdInsDeVO> pidvo; // 생산지시 상세

	List<ProdPlanDeVO> pldvo; // 생산계획 상세
	private String prodPlanStatus; // 생산계획상태

	// 설비상태
	private String eqmCode;
	private String eqmName;
	private String eqmStatus;
	private String oprStatus;

}