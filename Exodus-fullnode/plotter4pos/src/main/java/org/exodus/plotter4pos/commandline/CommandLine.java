package org.exodus.plotter4pos.commandline;

import java.lang.reflect.Field;
import java.util.List;

import org.exodus.plotter4pos.util.ReflectionUtils;

/**
 * 
 * Copyright © exodus. All rights reserved.
 * 
 * @ClassName: CommandLine
 * @Description: parsing command into your param configuration class if
 *               possible.
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Aug 1, 2020
 *
 */
public class CommandLine {
	static String OPTION_PREFIX = "-";
	static String PARAMETER_PREFIX = "--";

//	public class Option {
//		private String name;
//
//		public Option(String name) {
//			this.name = name;
//		}
//
//		public String name() {
//			return this.name;
//		}
//	}
//
//	public class Parameter extends Option {
//		private String value;
//
//		public Parameter(String name, String value) {
//			super(name);
//			this.value = value;
//		}
//
//		public String value() {
//			return value;
//		}
//	}

	private static <T> void setOption(List<Field> fields, T parameter, String name) {

		for (Field f : fields) {
			Param param = f.getAnnotation(Param.class);
			if (param.value().equalsIgnoreCase(name)) {
				ReflectionUtils.setField(f, parameter, true);
			}
		}
	}

	private static <T> void setParameter(List<Field> fields, T parameter, String name, String value) {

		for (Field f : fields) {
			Param param = f.getAnnotation(Param.class);
			if (param.value().equalsIgnoreCase(name)) {
				ReflectionUtils.setField(f, parameter, value);
			}
		}
	}

	public static <T> T parse(String[] args, T parameter) {
		boolean skipNextArg = false;
		List<Field> fields = ReflectionUtils.findAnnotatedFields(parameter.getClass(), Param.class);
		int index = 0;

		for (String arg : args) {
			if (skipNextArg) {
				skipNextArg = false;
				index++;
				continue;
			}

			if (arg.startsWith(PARAMETER_PREFIX)) {
				String nextArg = args[index + 1];
				setParameter(fields, parameter, arg.substring(PARAMETER_PREFIX.length()), nextArg);

				skipNextArg = true;
			} else if (arg.startsWith(OPTION_PREFIX)) {
				setOption(fields, parameter, arg.substring(OPTION_PREFIX.length()));
			} else {
				System.err.println("Warning:" + arg + " is unrecognized");
			}

			index++;
		}

		return parameter;
	}
}
