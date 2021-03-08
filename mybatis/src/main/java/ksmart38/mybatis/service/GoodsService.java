package ksmart38.mybatis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ksmart38.mybatis.dao.GoodsMapper;
import ksmart38.mybatis.domain.Goods;

@Service
@Transactional
public class GoodsService {
	private final GoodsMapper goodsMapper;
	
	@Autowired
	public GoodsService(GoodsMapper goodsMapper){
		this.goodsMapper = goodsMapper;
	}
	
	
	public int addGoods(Goods goods) {
		int result = 0;
		if(goods.getGoodsName()!= null && !"".equals(goods.getGoodsPrice())) {
			result += goodsMapper.addGoods(goods);
		}
		
		return result;
	}
	public List<Goods> getGoodsList(String searchKey, String searchValue){
		if(searchKey != null) {
			if("goodsCode".equals(searchKey)) {
				searchKey = "g_code";
			}
			else if("goodsName".equals(searchKey)) {
				searchKey = "g_name";
			}
			else if("goodsPrice".equals(searchKey)) {
				searchKey = "g_price";
			}
			else if("goodsSellerId".equals(searchKey)) {
				searchKey = "g_seller_id";
			}
		}
		List<Goods> goodsList = goodsMapper.getGoodsList(searchKey, searchValue);
		return goodsList;
	}
	public Goods getGoodsByCode(String goodsCode) {
		Goods goods = goodsMapper.getGoodsByCode(goodsCode);
		
		return goods;
	}
	public int modifyGoods(Goods goods) {
		
		return goodsMapper.modifyGoods(goods);
	}
	public int removeGoods(Goods goods) {
		return goodsMapper.removeGoods(goods);
	}
}
