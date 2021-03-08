package ksmart38.mybatis.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ksmart38.mybatis.service.MemberService;

@Controller
public class CommonController {
	private final MemberService memberService;
	
	public CommonController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	
	@GetMapping("/")
	public String main() {

		return "main";
	}
	
	@GetMapping("/loginHistory")
	public String loginHistory(Model model
			,@RequestParam(value="currentPage", required=false, defaultValue ="1") int currentPage) {
		
		Map<String , Object> resultMap = memberService.getLoginHistory(currentPage);
		
		List<Integer> indexNum = null;
		
		for(int i =1 ; i< (int)resultMap.get("lastPage"); i++) {
			if(i >= currentPage) {
				if(indexNum == null) {
					indexNum = new ArrayList<Integer>();
					for(int j = i; j < i+10; j++) {
						if(j <= (int)resultMap.get("lastPage")) {
							indexNum.add(j);
						}
					}
				}
			}
		}
		
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("lastPage", resultMap.get("lastPage"));
		model.addAttribute("loginHistory", resultMap.get("loginHistory"));
		model.addAttribute("indexNum", indexNum);
		return "login/loginHistory";
	}

}
