package com.mirker.utils.pom;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class PomDependecyAnalyzerTest {
	private static final String TARGET_POM = "D:\\Workspaces\\EclipseLuna\\2.ET\\Trunk\\Front-end\\pom.xml";
	private static final String MAVEN_REPO_BASE = "D:/mavenRepo/";
	
	public static void main(String[] args) throws Exception {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File(TARGET_POM))
				.getDocument();
		Element root = doc.getRootElement();
		List<Element> list = root.element("dependencies").elements("dependency");
		// 找到依赖列表
		for(Element e : list) {
			StringBuilder jarPath = new StringBuilder(MAVEN_REPO_BASE);
			
			String gid = e.elementText("groupId");
			String aid = e.elementText("artifactId");
			String v = e.elementText("version");
			// 如果使用配置的参数做版本号
			if(v.startsWith("$")) {
				String configVerName = v.substring(v.indexOf("{")+1, v.indexOf("}"));
				v = root.element("properties").elementText(configVerName);
			}
			jarPath.append(gid.replaceAll("\\.", "/")).append("/").append(aid).append("/").append(v);
			//System.out.println(jarPath.toString());
			File jarDir = new File(jarPath.toString());
			File[] jarFiles = jarDir.listFiles();
			
			// 找到对应jar的文件目录
			for(File jarFile : jarFiles) {
				if(jarFile.getName().endsWith(".pom")) {
					System.out.print("jarPath[" + jarPath + "] jarName[" +aid);
					String pomAbsPath = jarFile.getAbsolutePath();
					Document pomDoc = reader.read(new File(pomAbsPath)).getDocument();
					Element pomRoot = pomDoc.getRootElement();
					// 取得版权信息
					if(pomRoot.element("licenses") != null ) {
						Element license= pomRoot.element("licenses").element("license");
						System.out.print("] license[" + license.elementText("name"));
					} else {
						System.out.print("] license[" + "null");
					}
					System.out.println("]  *****  ");
				}
			}
		}
	}
	
}
