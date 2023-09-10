package com.k2dev.ca.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.k2dev.ca.model.ResultSetDto;

public class ResultSetToListMapper {

	public static List<ResultSetDto> convertToResultSetDtoList(List<Map<String, Object>> totAreaFeed){
		List<ResultSetDto> totList= new ArrayList<>();
		for(Map<String, Object> map: totAreaFeed) {
			ResultSetDto dto= new ResultSetDto();
			List<Object> objs= new ArrayList<>(map.values());
			for(Object o: objs) {
				if(o instanceof BigInteger) {
					BigInteger totFeed= (BigInteger)o;
					dto.setTotFeed(totFeed.longValue());
				}
				else if(o instanceof String){
					dto.setCrime(o.toString());
				}				
			}
			totList.add(dto);
		}
		return totList;
	}
	
	public static Map<String, Long> convertToMap(List<Map<String, Object>> totAreaFeed){
		Map<String, Long> validList= new HashMap<>();
		for(Map<String, Object> map: totAreaFeed) {
			ResultSetDto dto= new ResultSetDto();
			List<Object> objs= new ArrayList<>(map.values());
			for(Object o: objs) {
				if(o instanceof BigInteger) {
					BigInteger totFeed= (BigInteger)o;
					dto.setTotFeed(totFeed.longValue());
				}
				else if(o instanceof String){
					dto.setCrime(o.toString());
				}				
			}
			validList.put(dto.getCrime(),dto.getTotFeed());
		}
		return validList;
	}
}
