/**
 * ProjectCreaterUtil.java
 * @author  qye.zheng
 * 	version 1.0
 */
package com.hua.util;

/**
 * ProjectCreaterUtil
 * 描述: Project Creater - 工具类
 * @author  qye.zheng
 */
public final class ProjectCreaterUtil
{
	
	private static final String PATH = "/conf/properties/project-creater.properties";

	private static ReadProperties readProps = new ReadProperties(PATH);
	
	/**
	 * 构造方法
	 * 描述: 私有 - 禁止实例化
	 * @author  qye.zheng
	 */
	private ProjectCreaterUtil()
	{
	}
	
	/**
	 * 
	 * @description 
	 * @return
	 * @author qianye.zheng
	 */
	public static final String getGroupId()
	{
		return readProps.getProperty("maven.pom.groupId");
	}
	
	/**
	 * 
	 * @description 
	 * @return
	 * @author qianye.zheng
	 */
	public static final String getProjectName()
	{
		return readProps.getProperty("project.name");
	}
	
	/**
	 * 
	 * @description 
	 * @return
	 * @author qianye.zheng
	 */
	public static final String getProjectNameTemplate()
	{
		return readProps.getProperty("project.name.template");
	}
	
	/**
	 * 
	 * @description 
	 * @return
	 * @author qianye.zheng
	 */
	public static final String getGroupIdTemplate()
	{
		return readProps.getProperty("maven.pom.groupId.template");
	}

	/**
	 * 
	 * @description 
	 * @return
	 * @author qianye.zheng
	 */
	public static final String[] getDirPaths()
	{
		return readProps.getProperty("scan.relative.dir.paths").split("\\.");
	}
	
	/**
	 * 
	 * @description 
	 * @return
	 * @author qianye.zheng
	 */
	public static final String[] getFilePaths()
	{
		return readProps.getProperty("scan.relative.file.paths").split("\\.");
	}
	
	/**
	 * 
	 * @description 
	 * @return
	 * @author qianye.zheng
	 */
	public static final Boolean getPackageCreateSwitch()
	{
		return Boolean.valueOf(readProps.getProperty("package.create.switch"));
	}
}
