package com.shaw.test;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.python.core.Py;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public class MainTest {
	public static PythonInterpreter interpreter = new PythonInterpreter();

	public static void main(String[] args) throws Exception {
		// 获取系统python的path
		// BufferedReader br = null;
		// Process proc = Runtime.getRuntime().exec("python
		// pythonScript/TorrentDownload.py");
		// proc.waitFor();
		// br = new BufferedReader(new
		// InputStreamReader(proc.getInputStream()));
		// String line;
		// while (br.read() != -1) {
		// line = br.readLine();
		// System.out.println(line);
		// }
		// executePythonbyInterpreter();
		test();
	}

	public static void processBuilder() throws Exception {
		ProcessBuilder procB = new ProcessBuilder("python", "pythonScript/Magnet2Torrent.py");
		procB.start();
	}

	public static void executePythonbyInterpreter() throws Exception {
		String[] paths = { "D:\\Python27\\Lib\\idlelib", "D:\\Python27\\lib\\site-packages\\requests-2.9.1-py2.7.egg",
				"D:\\Python27\\lib\\site-packages\\bencode-1.0-py2.7.egg",
				"D:\\Python27\\lib\\site-packages\\magneturi-1.2-py2.7.egg", "C:\\Windows\\system32\\python27.zip",
				"D:\\Python27\\DLLs", "D:\\Python27\\lib", "D:\\Python27\\lib\\plat-win", "D:\\Python27\\lib\\lib-tk",
				"D:\\Python27", "D:\\Python27\\lib\\site-packages",
				"D:\\Python27\\lib\\site-packages\\redis-2.10.5-py2.7.egg", "D:\\Python27\\lib", "D:\\Python27\\Lib" };
		PySystemState sys = Py.getSystemState();
		sys.setdefaultencoding("UTF-8");
		for (String path : paths) {
			sys.path.append(new PyString(path));
		}
		interpreter.execfile("pythonScript//Magnet2Torrent.py");
		PyFunction func = (PyFunction) interpreter.get("main", PyFunction.class);
		//
		// String filepath = "pythonScript/resource/konotsuba.torrent";
		// PyObject pyobj = func.__call__(new PyString(filepath));
		PyObject pyobj = func.__call__();
		System.out.println("magnet = " + pyobj.toString());
	}

	public static void test() throws Exception {
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(1000 * 15);
		GetMethod getMethod = new GetMethod("http://back.2dfire-inc.com/openapi/auth/list.do");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		getMethod.addRequestHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		getMethod.addRequestHeader("Accept-Language", "gzip, deflate, sdch");
		getMethod.addRequestHeader("Content-Type", "gzip");
		getMethod.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		getMethod.addRequestHeader("Connection", "keep-alive");
		getMethod.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
		getMethod.addRequestHeader("Cookie",
				"JSESSIONID=1wiardj47ethv1nl3oly3x45rh; 1wiardj47ethv1nl3oly3x45rh=1wiardj47ethv1nl3oly3x45rh");
		getMethod.addRequestHeader("Host", "back.2dfire-inc.com");
		getMethod.addRequestHeader("referer", "http://back.2dfire-inc.com/user/index.do");
		// 执行请求获取response并解析
		int response = httpClient.executeMethod(getMethod);
		if (response == HttpStatus.SC_OK) {
			String html = getMethod.getResponseBodyAsString();
			System.out.println(html);
		}
	}
}
