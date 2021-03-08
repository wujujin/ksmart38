package ksmart38.mybatis.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ksmart38.mybatis.domain.Goods;
import ksmart38.mybatis.domain.Member;
import ksmart38.mybatis.service.GoodsService;
import ksmart38.mybatis.service.MemberService;

@Controller
public class GoodsController {
	
	@Autowired
	private GoodsService goodsService;

	@Autowired
	private MemberService memberService;
	
	@GetMapping("/goodsList")
	public String goodsList(Model model 
					,@RequestParam(value = "searchKey", required= false) String searchKey
					,@RequestParam(value = "searchValue", required= false) String searchValue ) {
		
		List<Goods> goodsList = goodsService.getGoodsList(searchKey, searchValue);
		model.addAttribute(goodsList);
		return "goods/goodsList";
	}
	@GetMapping("/addGoods")
	public String addGoods(HttpSession session, Model model) {
		String SLEVEL = (String)session.getAttribute("SLEVEL");
		if(SLEVEL != null && !"".equals(SLEVEL)) {
			if(SLEVEL.equals("관리자")) {
				List<Member> sellerIdList = memberService.getMemberList("memberLevel", "2");
				model.addAttribute("sellerIdList",sellerIdList);
			}
		}
		return "goods/addGoods";
	}
	@PostMapping("/addGoods")
	public String addGoods(Goods goods) {
		if(goods.getGoodsSellerId() != null && !"".equals(goods.getGoodsSellerId())){
			goodsService.addGoods(goods);
		}
		return "redirect:/goodsList";
	}
	@GetMapping("/modifyGoods")
	public String modifyGoods(Model model
							 ,@RequestParam(value = "goodsCode", required= false) String goodsCode
							 ,HttpSession session) {
		Goods goods = goodsService.getGoodsByCode(goodsCode);
		String SLEVEL = (String)session.getAttribute("SLEVEL");
		if(SLEVEL != null && !"".equals(SLEVEL)) {
			if(SLEVEL.equals("관리자")) {
				List<Member> sellerIdList = memberService.getMemberList("memberLevel", "2");
				model.addAttribute("sellerIdList",sellerIdList);
			}
		}
		model.addAttribute("goods",goods);
		return "goods/modifyGoods";
	}
	@PostMapping("/modifyGoods")
	public String modifyGoods(Goods goods) {
		if((goods.getGoodsName()!= null && !"".equals(goods.getGoodsName())) &&
			goods.getGoodsPrice()!=null && !"".equals(goods.getGoodsPrice())) {
			
			goodsService.modifyGoods(goods);
			return "redirect:/goodsList";
		}
		return "goods/modifyGoods";
	}
	@GetMapping("/removeGoods")
	public String removeGoods(Model model, @RequestParam (value ="goodsCode", required=false )String goodsCode) {
		Goods goods = goodsService.getGoodsByCode(goodsCode);
		
		model.addAttribute("goods", goods);
		
		return "goods/removeGoods";
	}
	@PostMapping("/removeGoods")
	public String removeGoods(Goods goods
							, @RequestParam (value ="memberPw", required=false )String memberPw
							, HttpSession session) {
		
		String SID = (String)session.getAttribute("SID");
		String SLEVEL = (String)session.getAttribute("SLEVEL");
		
		Member member = memberService.getMemberInfoById(goods.getGoodsSellerId());
		if(memberPw != null && !"".equals(memberPw)) {
			if("관리자".equals(SLEVEL)) {
				//입력한 값과, 판매자의 패스워드 비교
				if(memberPw.equals(member.getMemberPw())) {
					goodsService.removeGoods(goods);
				}
			}else {
				//관리자가 아닐경우 로그인한 아이디와 판매자의 아이디 비교
				if(SID.equals(goods.getGoodsSellerId())) {
					goodsService.removeGoods(goods);
				}
			}
		}
		return "redirect:/goodsList";
	}
	
}
