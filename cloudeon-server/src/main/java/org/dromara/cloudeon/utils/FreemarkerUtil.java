package org.dromara.cloudeon.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

public class FreemarkerUtil {

	private static Configuration cfg;

	private static String encoding = StandardCharsets.UTF_8.name();

	static {
		cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
		cfg.setDefaultEncoding(encoding);
		cfg.setNumberFormat("computer");
		cfg.setEncoding(Locale.CHINA, encoding);
		cfg.setAutoFlush(true);
	}

	public static String templateEval(String templateStr, Map<String, Object> context) {
		BufferedOutputStream outputStream = null;
		try {
			Template template = new Template("templateStr", new StringReader(templateStr), cfg, encoding);
			StringWriter stringWriter = new StringWriter();
			template.process(context, stringWriter);
			return stringWriter.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IoUtil.close(outputStream);
		}
	}

	public static void templateEval(String templateStr, Map<String, Object> context, File newFile) {
		BufferedOutputStream outputStream = null;
		try {
			outputStream = FileUtil.getOutputStream(newFile);
			Template template = new Template("templateStr", new StringReader(templateStr), cfg, encoding);
			template.process(context, new OutputStreamWriter(FileUtil.getOutputStream(newFile), encoding));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IoUtil.close(outputStream);
		}
	}

}
