getProdPlanList();

//목록출력 기본날짜 (한달)

const d = new Date();
const month = d.getMonth();
//const date = d.getDate();

// 한달 전
document.getElementById('searchStartDate').value = new Date(new Date().setMonth(month - 1)).toISOString().substring(0, 10);
document.getElementById('searchEndDate').value = new Date().toISOString().substring(0, 10);

//로딩시 바로 실행
window.onload = function() {
	getProdPlanList();
};  

//============================================================================

// 생산관리자 권한 확인
if(document.querySelector('#auth').innerHTML != '1'){
	document.querySelector('#deleteBtn').setAttribute('style', 'display : none;');
	document.querySelector('#updateBtn').setAttribute('style', 'display : none;');
}
//생산계획 진행상태
class PlanStatus {
	constructor(props) {
		const el = document.createElement('div');

		this.el = el;
		this.render(props);
	}
	render(props) {
		this.el.innerText = props.formattedValue == 'LS1' ? '미지시' : '지시등록';
	}
	getElement() {
		return this.el;
	}
}
//생산요청코드 없으면 '-'로 표시
class ProdReqCode {
	constructor(props) {
		const el = document.createElement('div');

		this.el = el;
		this.render(props);
	}
	render(props) {
		this.el.innerText = props.formattedValue == '' ? '-' : props.formattedValue;
	}
	getElement() {
		return this.el;
	}
}

class CustomNumberEditor {
	constructor(props) {
		const el = document.createElement('input');
		const { maxLength } = props.columnInfo.editor.options;
	
		el.type = 'number';
		el.min = 0;
		el.max = 1000;
		el.step = 100;
		el.style.width = '100%';
		this.el = el;
	}
	
	getElement() {
		return this.el;
	}
	
	getValue() {
		return this.el.value;
	}
	
	mounted() {
		this.el.select();
	}
	};

//============================================================

/* < 생산계획 목록 > */
const plList = new tui.Grid({
	el : document.getElementById('plList'),
	bodyHeight: 200,
	scrollX : false,
	scrollY : true,
	
	columns : [
		{
			header : '생산계획코드',
			name : 'prodPlanCode',
			align: 'center'
		}, 
		{
			header : '생산계획일자',
			name : 'planDate',
			align: 'center'
		},
		{
			header : '생산요청코드',
			name : 'prodReqCode',
			align: 'center',
			renderer: {type: ProdReqCode}
		},
		{
			header : '진행상태',
			name : 'prodPlanStatus',
			align: 'center',
			renderer: {type: PlanStatus}
		}, 
		{
			header : '담당자',
			name : 'usersCode',
			align: 'center',
			hidden: true
		},
		{
			header : '담당자',
			name : 'usersName',
			align: 'center'
		}
	]
});


// 생산계획 목록 조회(ajax) -검색포함
async function getProdPlanList(){

	const searchStartDate = document.getElementById('searchStartDate').value;
	const searchEndDate = document.getElementById('searchEndDate').value;
	const prodPlanCode = document.getElementById('prodPlanCode').value;
	const prodPlanStatus = document.querySelector("[name=prodPlanStatus]:checked").value;

	const obj = {searchStartDate, searchEndDate, prodPlanCode, prodPlanStatus};
	//console.log(obj);
	
	const data = {
		method: 'POST',
		headers: jsonHeaders,
		body : JSON.stringify(obj)
	};

	const response = await fetch("/ajax/prodPlanList", data);
	const res = await response.json();

    //console.log(res);
    plList.resetData(res);
};

//+ 공통함수: 검색 엔터키 입력
function enterKeyListener(elementId, callback) {
	document.getElementById(elementId).addEventListener('keyup', e => {
		if(e.key == 'Enter') {
			callback();
		}
	});
}

//검색버튼
document.getElementById('searchBtn').addEventListener('click', getProdPlanList);
enterKeyListener('prodPlanCode', getProdPlanList);
enterKeyListener('prodPlanStatus0', getProdPlanList);
enterKeyListener('prodPlanStatus1', getProdPlanList);
enterKeyListener('prodPlanStatus2', getProdPlanList);


//초기화버튼
document.getElementById('resetBtn').addEventListener('click', function() {
	document.getElementById('searchStartDate').value = '';
	document.getElementById('searchEndDate').value = '';
	document.getElementById('prodPlanCode').value = '';
	document.getElementById('prodPlanStatus0').checked = true;

	plList.resetData([]);
	getProdPlanList();
});


/* < 생산계획 상세 목록 > */
const plAll = new tui.Grid({
	el : document.getElementById('plAll'),
	scrollX : false,
	scrollY : false,
	columns : [
		{
			header : '생산계획상세코드',
			name : 'prodPlanDetailCode',
			align: 'center'
		}, 
			{
			header : '생산요청상세코드',
			name : 'prodReqDetailCode',
			align: 'center',
			renderer: {type: ProdReqCode}
			
		},
		{
			header : '제품코드',
			name : 'productCode',
			align: 'center'
		},
		{
			header : '제품명',
			name : 'productName',
			align: 'center'
		},
		{
			header : '고정수량',
			name : 'fixCnt',
			align: 'center',
			editor: {
				type: CustomNumberEditor,
				options: {
				}
			},
			formatter: function(price) {
				return priceFormat(price.value);
			},
		}, 
		{
			header : '요청수량',
			name : 'reqCnt',
			align: 'center',
			formatter: function(price) {
				return priceFormat(price.value);
			},
		},
		{
			header : '계획수량',
			name : 'planCnt',
			align: 'center',
			formatter: function(price) {
				return priceFormat(price.value);
			}
		},
	],
	summary: {
		//align: 'right',
		height: 40,
		position: 'bottom',
		columnContent: {
			prodPlanDetailCode: {
				template: function() {
					return '총 계획수량 합계';
				}, 
			},
			planCnt: {
				//align: 'right',
				template: function(value) {
					return priceFormat(value.sum);
				}, 
			},
		},
	}	
});

//생산계획 클릭 시 => 아래 생산계획상세내용 출력
plList.on('click', e => {
	let status = plList.getValue(e.rowKey, "prodPlanStatus");
	let plCode = plList.getValue(e.rowKey, "prodPlanCode");

	//계획상세목록
	getProdPlanAll(plCode);

	//계획상태가 미지시이면 수정가능하게
	if(status == 'LS1') {
		plAll.enableColumn('fixCnt');

	}else { //아니면 수정불가
		plAll.disableColumn('fixCnt');
	}		
});

//계획상세 목록
async function getProdPlanAll(plCode){
	const response = await fetch(`/ajax/prodPlanAll?prodPlanCode=${plCode}`);
	const res = await response.json();

    //console.log(res);
    plAll.resetData(res);
}

//수정 시 변경 값 계산
plAll.on('afterChange', e => {
	let row = plAll.getRow(e.changes[0].rowKey);
	let fixCnt = parseInt(row.fixCnt);
	let reqCnt = parseInt(row.reqCnt);

	let planCnt = parseInt(fixCnt + reqCnt);
	//console.log(planCnt)
	let notInstructCnt = planCnt;

	plAll.setValue(row.rowKey, 'planCnt', planCnt);
	plAll.setValue(row.rowKey, 'notInstructCnt', notInstructCnt);
})

//수정 유효성
function beforeUpdateCheck() {
	const alert = document.getElementById('alertMsg2');

	const updateCnt = plAll.getModifiedRows().updatedRows;

	if(updateCnt.length == 0) {
		alert.innerHTML = '<span style="color:red">※</span> 수정할 고정수량을 입력하세요';
		return false;
	}

	alert.innerHTML = '';
	return true;
};

//생산계획 상세 수정하기
async function updatePlanDetail() {

	if(!beforeUpdateCheck()){
		return;
	}

	plAll.blur();
	const plDe = plAll.getModifiedRows().updatedRows
	console.log(plDe);
	
	const row = plList.getFocusedCell().rowKey;
	const plcode = plList.getValue(row, "prodPlanCode");

	const response = await fetch('ajax/updateProdPlanDetail', {
		method: 'put',
		headers: jsonHeaders,
		body : JSON.stringify(plDe)
	})
	const res = await response.json();

	if(res == 1){ 
		Swal.fire({
			position: "center",
			icon: "success",
			title: "생산계획 수정 완료",
			showConfirmButton: false,
			timer: 2000
		});
		getProdPlanAll(plcode);
		//plDeInsert.resetData([]);

	} else {
		Swal.fire({
			position: "center",
			icon: "error",
			title: "생산계획 수정 실패",
			showConfirmButton: false,
			timer: 2000
		});
	};
};

//삭제 유효성
function beforeDelCheck() {
	const alert = document.getElementById('alertMsg'); //삭제
	
	const row = plList.getFocusedCell().rowKey;

	if(row == null) {
		alert.innerHTML = '<span style="color:red">※</span> 삭제할 계획을 선택하세요';
		return false;
	}

	alert.innerHTML = '';
	return true;
};


//미지시 생산계획 삭제
async function deletePlan() {
	
	if(!beforeDelCheck()){
		return;
	}
	
	let row = plList.getFocusedCell().rowKey;
	let plan = plList.getData()[row];

	//계획상세
	plan.dvo =  plAll.getData();

	if(plan.prodPlanStatus == 'LS1') {
		const response = await fetch('ajax/deleteProdPlan', {
			method: 'delete',
			headers: jsonHeaders,
			body : JSON.stringify(plan)
		})
		const res = await response.json();

		// SweetAlert
		if(res == 1){
			Swal.fire({
				position: "center",
				icon: "success",
				title: "생산계획 삭제 완료",
				showConfirmButton: false,
				timer: 2000
			});
			getProdPlanList();
			plAll.resetData([]);

		} else {
			Swal.fire({
				position: "center",
				icon: "error",
				title: "생산계획 삭제 실패",
				showConfirmButton: false,
				timer: 2000
			});
		};

	} else {
		Swal.fire({
			position: "center",
			icon: "error",
			title: "지시등록된 건으로 삭제 불가",
			showConfirmButton: false,
			timer: 2000
		});
	}
};