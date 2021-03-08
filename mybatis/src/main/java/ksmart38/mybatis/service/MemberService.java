package ksmart38.mybatis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import ksmart38.mybatis.dao.MemberMapper;
import ksmart38.mybatis.domain.Member;

@Service
@Transactional
public class MemberService {
	
	
	private static final Logger log = LoggerFactory.getLogger(MemberService.class);

	
	/*
	//1. DI 필드 주입방식
	@Autowired
	private MemberMapper memberMappper;
	
	//2. DI SETTER 메소드 주입방식
	private MemberMapper memberMappper2;
	public void setMemberMapper(MemberMapper memberMapper){
		this.memberMappper2 = memberMapper;
	}
	 */
	
	@PostConstruct
	public void initialize() {
		log.debug(" ************** MemberService **************{}","initialize()");
		System.out.println("======================================");
		System.out.println("======= MembrService bean 등록 =========");
		System.out.println("======================================");
	}
	
	//3. DI 생성자 메소드 주입방식
	private final MemberMapper memberMapper;
	
	//3-1. spring framework 4.3 이후 부터 @Autowired 쓰지 않아도 주입가능
	@Autowired
	public MemberService(MemberMapper memberMapper) {
		this.memberMapper= memberMapper;
	}
	
	public Map<String,Object> getLoginHistory(int currentPage){
		
		// 페이지에 보여줄 행의 갯수
		int rowPerPage = 5;
		
		// login table 행의 시작점
		int startNum = 0;
		// 마지막 페이지
		int lastPage = 0;
		
		// 페이지 알고리즘(현재페이지 - 1)*페이지에 보여줄 행의 갯수 => limit ? , 5  '?를 구하는 과정'
		startNum = (currentPage-1) * rowPerPage;
		
		Map<String,Object> paramMap = new HashMap<String,Object>() ;
		paramMap.put("rowPerPage", rowPerPage);
		paramMap.put("startNum", startNum);
		List<Map<String, Object>> loginHistory = memberMapper.getLoginHistory(paramMap);
		
		// 로그인 이력 행의 갯수
		double loginHistoryCount = memberMapper.getLoginHistoryCount();
		
		// 마지막 페이지 구성
		lastPage = (int) Math.ceil(loginHistoryCount / rowPerPage);
		
		
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("loginHistory", loginHistory);
		resultMap.put("lastPage", lastPage);
		
		return resultMap;
	}
	
	
	public List<Member> getMemberList(String searchKey ,String searchValue){
		if(searchKey != null) {
			if("memberId".equals(searchKey)) {
				searchKey = "m_id";
			}
			else if("memberLevel".equals(searchKey)) {
				searchKey = "level_name";
			}
			else if("memberName".equals(searchKey)) {
				searchKey = "m_name";
			}
			else if("memberEmail".equals(searchKey)) {
				searchKey = "m_email";
			}
		}
		List<Member> memberList = memberMapper.getMemberList(searchKey,searchValue);
		
		int memberListSize = memberList.size();
		for(int i = 0 ; i < memberListSize ; i++) {
			int level = memberList.get(i).getMemberLevel();
			if(level==1) {
				memberList.get(i).setMemberLevelName("관리자");
			}else if(level == 2) {
				memberList.get(i).setMemberLevelName("판매자");
			}else if(level == 3) {
				memberList.get(i).setMemberLevelName("구매자");
			}
		}
		return memberList;
	}
		
	public Member getMemberInfoById(String memberId) {
		Member member = memberMapper.getMemberInfoById(memberId);
		int level = member.getMemberLevel();
		if(level==1) {
			member.setMemberLevelName("관리자");
		}else if(level == 2) {
			member.setMemberLevelName("판매자");
		}else if(level == 3) {
			member.setMemberLevelName("구매자");
		}
		return member;
	}
	
	public int addMember(Member member) {
		int result = memberMapper.addMember(member);
				
		return result;
	}
	public int modifyMember(Member member) {
		int result = memberMapper.modifyMember(member);
		
		return result;
	}
	public int removeMember(Member member) {
		//입력한 pw값
		String memberPw = member.getMemberPw();
		String memberId = member.getMemberId();
		//db에서 조회한 값 
		member = memberMapper.getMemberInfoById(memberId);
		int result = 0;
		//입력값, 조회값 비교 같으면 삭제
		if(memberPw.equals(member.getMemberPw())) {
			if(member.getMemberLevel()==2) {
				memberMapper.removeOrderSeller(memberId);
				memberMapper.removeGoods(memberId);
			}
			if(member.getMemberLevel()==3) {
				memberMapper.removeOrder(memberId);
			}
			result = memberMapper.removeMember(memberId);
		}
		
		return result;
	}
	
}
