package com.k2dev.ca.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

public class PageUtil {
	public final static int SIZE= 20;
	
	public static Pageable pageable(int pageNo) {
		return PageRequest.of(pageNo-1, SIZE);
	}
	
	public static void setPaginationProperties(Model model, int pageNo, int totalPages, long totalElements) {
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalItems", totalElements);
	}
}
