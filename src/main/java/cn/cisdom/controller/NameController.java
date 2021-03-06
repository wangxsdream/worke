package cn.cisdom.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cisdom.pojo.Name;
import cn.cisdom.service.NameService;

import com.fasterxml.jackson.databind.util.JSONPObject;

@Controller
@RequestMapping(value="/abc")
public class NameController {

	@Autowired
	private NameService nameService;
	
	@ResponseBody
	@RequestMapping(value="/Login")//接口的URL
	public Object  getItemParamList(@RequestParam("user") String user,@RequestParam("password") String password,	@RequestParam(value ="callbackparam",required=false) String e ,HttpServletRequest request) throws Exception{
		//获取登录IP
		String saveIp ;
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			saveIp= ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
		// 多次反向代理后会有多个IP值，第一个为真实IP。
		int index = ip.indexOf(',');
		if (index != -1) {
			saveIp = ip.substring(0, index);
		} else {
			saveIp = ip;
		}
		} else {
			saveIp = request.getRemoteAddr();
		}
		
	String password1 =nameService.selectUserByID(user);
	String message = "";
	int res ;
	if (password1 == null){
		message="没有此用户";
		res= 3;
	}else{
	if (password1.equals(password)){
		message="正确";
		res= 1;
	}else
	{
		message="失败";
		res= 2;
	}
	}
		Name b = new Name();
		b.setMima(nameService.selectUserByID(user));
		Map map = new HashMap();
		String a= nameService.selectUserByID(user);
		map.put("res", res);
		map.put("msg", message);
	map.put("ip", saveIp);
		if (e==null){
			return  map;	
		}else{
			return new JSONPObject(e, map);	
		}
			
	}
	
	
	@ResponseBody
	@RequestMapping(value="/SignIn")//接口的URL
	public Object  insertUser(@RequestParam("user") String user,
			@RequestParam("password") String password,
			@RequestParam("shoujihao") String shoujihao,
			@RequestParam("zhuzhi") String zhuzhi,
			@RequestParam("callbackparam") String e ) throws Exception{
	Name name =new Name();
	name.setUser(user);
	name.setMima(password);
	name.setShoujihao(shoujihao);
	name.setZhuzhi(zhuzhi);
		
		int num =nameService.insertUser(name);
	String message = "";

	if (num == 0){
		message="失败";
		
	}else{
	
		message="成功";
	}
	Map map =new  HashMap();
		map.put("msg", message);
		if (e==null){
			return  map;	
		}else{
			return new JSONPObject(e, map);	
		}
			
	}
	
	

	
}
