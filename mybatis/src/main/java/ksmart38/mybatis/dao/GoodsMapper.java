package ksmart38.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import ksmart38.mybatis.domain.Goods;

@Mapper
public interface GoodsMapper {
	//상품 리스트
	public List<Goods> getGoodsList(String searchKey, String searchValue);
	//상품 등록
	public int addGoods(Goods goods);
	//상품 수정 데이터 가져오기
	public Goods getGoodsByCode(String goodsCode);
	//판매자 아이디 가져오기
	public List<String> getSellerId();
	//상품 수정하기
	public int modifyGoods(Goods goods);
	//상품 및 오더 제거
	public int removeGoods(Goods goods);
}
