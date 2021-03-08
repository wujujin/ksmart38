package ksmart38.mybatis.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ksmart38.mybatis.domain.Member;
import ksmart38.mybatis.service.MemberService;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	
	@RequestMapping(value="/ajax/idCheck", method = RequestMethod.POST)
	public @ResponseBody String idCheck(@RequestParam(value="memberId" ,required = false) String memberId) {
		
		return memberId;
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "main";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam(value = "memberId", required = false) String memberId
					   ,@RequestParam(value = "memberPw", required = false) String memberPw
					   ,HttpSession session) {
		
		  if(memberId != null && !"".equals(memberId) && 
			 memberPw != null &&  !"".equals(memberPw) ) {
			  Member member = memberService.getMemberInfoById(memberId);
			  if(memberId.equals(member.getMemberId()) && 
				 memberPw.equals(member.getMemberPw())){
				
				 //아이디
				 session.setAttribute("SID", member.getMemberId()); 
				 //권한
				 session.setAttribute("SLEVEL", member.getMemberLevelName()); 
				 //이름 
				 session.setAttribute("SNAME", member.getMemberName());
				 //주소 (추가해야돼)
				 
			  }
			  
			 
		  return "redirect:/"; }
		 
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String login() {
		return "login/login";
	}

	@PostConstruct
	public void initialize() {
		System.out.println("======================================");
		System.out.println("======= MembrController bean 등록 ======");
		System.out.println("======================================");
	}



	// required = get방식으로 전달하지 않아도 받기위해서 값이 필수적인가?,defaultValue= "get방식"
	@GetMapping("/modifyMember")
	public String modifyMember(Model model, @RequestParam(name = "memberId", required = false) String memberId) {
		System.out.println("memberId start");
		System.out.println("memberId >>" + memberId);
		System.out.println("memberId stop");

		Member member = memberService.getMemberInfoById(memberId);
		System.out.println(member + "<<service getMemberInfoById");
		model.addAttribute("member", member);
		return "member/modifyMember";
	}

	@PostMapping("/modifyMember")
	public String modifyMember(Member member) {
		System.out.println(member + "modifyMember");
		memberService.modifyMember(member);
		return "redirect:/memberList";
	}

	@GetMapping("/memberList")
	public String getMemberList(Model model, @RequestParam(value = "searchKey", required = false) String searchKey,
											 @RequestParam(value = "searchValue", required = false) String searchValue) {
		List<Member> memberList = memberService.getMemberList(searchKey, searchValue);
		System.out.println("memberList start");
		System.out.println(memberList);
		System.out.println("memberList stop");
		model.addAttribute("memberList", memberList);
		return "member/memberList";
	}

	@PostMapping("/addMember")
	public String addMember(Member member) {
		System.out.println("-----member start-----");
		System.out.println(member + "<<member");
		System.out.println("-----member end-----");
		// 회원 등록
		if (member != null && !"".equals(member.getMemberId())) {
			memberService.addMember(member);
		}
		return "redirect:/memberList";
	}

	@GetMapping("/removeMember")
	public String removeMember(Model model, String memberId,
			@RequestParam(value = "result", required = false) String result) {
		model.addAttribute("memberId", memberId);
		model.addAttribute("result", result);
		return "member/removeMember";
	}

	@PostMapping("/removeMember")
	public String removeMember(Member member, RedirectAttributes redirectAttr) {
		System.out.println("removeMember start");
		System.out.println(member.toString());
		System.out.println("removeMember stop");
		int check = memberService.removeMember(member);
		if (check == 0) {
			String result = "삭제실패";
			redirectAttr.addAttribute("memberId", member.getMemberId());
			redirectAttr.addAttribute("result", result);
			return "redirect:/removeMember";
		}
		return "redirect:/memberList";
	}

	@RequestMapping(value = "/addMember", method = RequestMethod.GET)
	public String addMember() {
		return "member/addMember";
	}
}
