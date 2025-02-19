package com.foryuum.frontend.item.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.foryuum.frontend.common.exception.CommonException;
import com.foryuum.frontend.item.service.ItemService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/item")
public class ItemController {

	@Resource(name = "itemService")
	private ItemService itemService;

	@GetMapping("/manualOrder.do")
	public String goManualOrderr(HttpServletRequest request, HttpSession session) throws CommonException {
		return "/mobile/manualOrder";
	}
	
	@ResponseBody
	@PostMapping("/getItemInfoByItemNo.do")
	public JSONObject getItemInfo(HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> requestData) throws CommonException, IOException {
		JSONObject jo = new JSONObject();
		jo.put("returnData", itemService.getItemInfoByItemNo(requestData));
		
		return jo;
	}
	
	@ResponseBody
	@PostMapping("/getItemInfoByItemOrderNo.do")
	public JSONObject getItemInfoByItemOrderNo(HttpServletResponse response, HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> requestData) throws CommonException, IOException {
		JSONObject jo = new JSONObject();
		jo.put("returnData", itemService.getItemInfoByItemOrderNo(session, requestData));
	    System.out.println("Content-Type: " + response.getContentType());
	    System.out.println("Character Encoding: " + response.getCharacterEncoding());
	    
		return jo;
	}
	
	@ResponseBody
	@PostMapping("/initOrder.do")
	public JSONObject initOrder(HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> requestData) throws CommonException, IOException {
		JSONObject jo = new JSONObject();
		jo.put("returnData", itemService.initOrder(session, requestData));
		
		return jo;
	}
	
}
