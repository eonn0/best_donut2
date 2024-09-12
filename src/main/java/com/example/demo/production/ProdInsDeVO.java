package com.example.demo.production;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProdInsDeVO {

	//생산지시상세
	private String prodInstructDetailCode;
	private String prodPlanDetailCode;
	private String prodInstructCode; //생산지시코드
	private int instructCnt;
	private int notProdCnt;
	private int prodCnt;
	//+ 지시상세 공정상태
	private String insDeStatus;

	// 자재 불출용 컬럼
	private String matOutgoingStatus;
	
	//상품
	private String productCode;
	private String productName;
	
	
	//공정실적 ( proc_result )
	private String productLotCode; //완제품 lot 코드
	private String qltyCheckStatus; //품질검사 상태 (PQ1 미검사 / PQ2 검사완료)
	private Integer failCnt; //불량수량
	private String usersName;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date instructDate; //지시일자
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Date allBeginTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Date allEndTime;
	//검색
	private Date searchStartDate;
	private Date searchEndDate;
	private String matLotCode;

}
