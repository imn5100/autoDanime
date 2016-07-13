package com.shaw.test;

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
		processBuilder();
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
				"D:\\Python27\\lib\\site-packages\\redis-2.10.5-py2.7.egg" };
		PySystemState sys = Py.getSystemState();
		sys.setdefaultencoding("UTF-8");
		for (String path : paths) {
			sys.path.append(new PyString(path));
		}
		interpreter.execfile("pythonScript//Torrent2Magnet.py");
		PyFunction func = (PyFunction) interpreter.get("torrent2magent", PyFunction.class);

		String filepath = "pythonScript/resource/konotsuba.torrent";
		PyObject pyobj = func.__call__(new PyString(filepath));
		System.out.println("magnet = " + pyobj.toString());
	}
}
