package com.shaw.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.shaw.utils.PropertiesUtil;
import com.shaw.utils.ResponseMap;

@Controller
public class ParkingController {

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping("/getType")
	public ModelAndView getParkingCardType(HttpServletRequest request) {
		return null;
	}

	@RequestMapping("/getTypeJson")
	@ResponseBody
	public ResponseMap<String> getParkingCardType2(HttpServletRequest request){
		return null;
	}

	@RequestMapping("/getOpenApiData")
	@ResponseBody
	public ResponseMap<String> getOpenApiData() {
		final String appId = PropertiesUtil.getConfiguration().getString("wechat.appid");
		final String appSecret = PropertiesUtil.getConfiguration().getString("wechat.appsecret");
		final String serverUrl = PropertiesUtil.getConfiguration().getString("wechat.getTokenUrl");
		ResponseMap<String> responseMap = new ResponseMap<String>();
		RestTemplate rest = new RestTemplate();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("grant_type", "client_credential");
		params.add("appid", appId);
		params.add("secret", appSecret);
		String response = rest.postForObject(serverUrl, params, String.class);
		System.out.println(response);
		responseMap.setData(response);
		return responseMap;
	}

}
