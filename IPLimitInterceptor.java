package com.fyh.common;

import java.net.InetAddress;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class IPLimitInterceptor extends HandlerInterceptorAdapter {

	@Override

	public boolean preHandle(HttpServletRequest request,

			HttpServletResponse response, Object handler) throws Exception {
		String ip = getIpAddress(request);

		// 读取ip白名单配置文件ipwhite.properties

		Properties properties = PropertiesLoaderUtils.loadAllProperties("ipwhite.properties");

		String ipWhilte = properties.getProperty("ipWhilte");

		System.out.println(ipWhilte);

		//判断请求ip地址 是否在白名单呢

		if (ip.equals(ipWhilte)) {
			return super.preHandle(request, response, handler);

		}
		
		return false;
		//throw new Exception("IP非法访问!");

	}

	// 获取配置请求的ip地址

	private String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");

		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");

		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");

		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");

		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();

			System.out.println("访问ip=" + ip);

			if (ip.equals("127.0.0.1")) {

				// 根据网卡取本机配置的IP

				InetAddress inet = null;

				try {

					inet = InetAddress.getLocalHost();

				} catch (Exception e) {

					e.printStackTrace();

				}

				ip = inet.getHostAddress();

			}

		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割

		if (ip != null && ip.length() > 15) { // "***.***.***.***".length() = 15

			if (ip.indexOf(",") > 0) {

				ip = ip.substring(0, ip.indexOf(","));
			}
		}
		System.out.println("访问ip=========" + ip);

		return ip;

	}

}
