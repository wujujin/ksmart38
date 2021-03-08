package ksmart38.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import ksmart38.mybatis.domain.Member;

@Mapper
public interface MemberMapper {
	
	// 로그인 이력 전체행의 갯수
	public int getLoginHistoryCount();
	
	// 로그인 이력 조회
	public List<Map<String ,Object>> getLoginHistory(Map<String,Object> paramMap);
	
	// 회원 삭제 처리
	public int removeMember(String memberId);
	
	public int removeOrder(String memberId);
	
	public int removeOrderSeller(String memberId);
	
	public int removeGoods(String memberId);
	
	// 회원 수정 처리
	public int modifyMember(Member member);
	
	// 회원 수정 화면으로
	public Member getMemberInfoById(String memberId);
	
	// 회원 등록
	public int addMember(Member member);
	
	// 회원 목록 조회
	public List<Member> getMemberList(String searchKey , String searchValue);
}
