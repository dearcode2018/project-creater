/**
 * 描述: 
 * MavenProjectDriver.java
 * @author	qye.zheng
 *  version 1.0
 */
package com.hua.driver;

import java.io.File;

import com.hua.util.EmptyUtil;
import com.hua.util.FileUtil;
import com.hua.util.ProjectCreaterUtil;
import com.hua.util.ProjectUtil;


/**
 * 描述:  Maven 项目创建器
 * @author  qye.zheng
 * MavenProjectDriver
 */
public class MavenProjectDriver
{
	
	private static final String INPUT_PATH = ProjectUtil.getAbsolutePath("/doc/template/maven-project-template-entire", true);
	
	private static final String OUTPUT_PATH = ProjectUtil.getAbsolutePath("/doc", true);
	
	private static final String targetProjectPath = ProjectUtil.getAbsolutePath("/doc/maven-project-template-entire", true);
	
	/**
	 * 构造方法
	 * 描述: 
	 * @author qye.zheng
	 */
	private MavenProjectDriver()
	{
	}
	
	/**
	 * 
	 * 描述: 
	 * @author  qye.zheng
	 * @return
	 */
	public static final boolean creater()
	{
		boolean flag = false;
		try
		{
			/*
			 * 1. 把项目模板拷贝到 doc目录下
			 */
			FileUtil.copy(INPUT_PATH, OUTPUT_PATH);
			// 从处理 entire 项目开始
			handleEntireProject(targetProjectPath);
			
			flag = true;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return flag;
	}
	
	/**
	 * 
	 * @description 处理entire项目
	 * 1. 重命名
	 * 2.执行多个文件处理
	 * 3. 列出子模块
	 * @param projectRootPath
	 * @author qianye.zheng
	 */
	private static final void handleEntireProject(final String projectRootPath)
	{
		System.out.println("entireRootPath: " + projectRootPath);
		File file = new File(projectRootPath);
		final String nameTemplate = ProjectCreaterUtil.getProjectNameTemplate();
		final String name = ProjectCreaterUtil.getProjectName();
		final String newName = file.getName().replace(nameTemplate, name);
		final String newProjectRootPath = file.getParent() + "/" + newName;
		final File oldProjectFile = new File(newProjectRootPath);
		if (oldProjectFile.exists())
		{
			FileUtil.delete(oldProjectFile);
		}
		// 重命名
		FileUtil.rename(file, newName);
		
		// 重命名之后 需要重新构造文件对象
		
		file = new File(newProjectRootPath);
		
		final File[] files = file.listFiles();
		if (!EmptyUtil.isEmpty(files))
		{
			String filename = null;
			for (int i = 0; i < files.length; i++)
			{
				filename = files[i].getName();
				if (filename.startsWith(nameTemplate))
				{
					// 处理子模块
					handleModuleProject(files[i]);
					
					continue;
				}
				if (filename.equals("pom.xml"))
				{
					// 处理 pom.xml 文件
					handlePomFile(files[i]);
					
					continue;
				}
				// 处理.settings 下的目录
				if (filename.equals(".settings"))
				{
					final File settingsFile = new File(newProjectRootPath + "/.settings");
					final File[] settingsFiles = settingsFile.listFiles();
					for (int j = 0; j < settingsFiles.length; j++)
					{
						handleNormalFile(settingsFiles[j]);
					}
					
					continue;
				}
				if (filename.equals(".project"))
				{
					// 处理其他文件
					handleNormalFile(files[i]);
					
					continue;
				}
			}
		}
	}
	
	/**
	 * 
	 * @description 处理模块项目
	 * 1. 重命名
	 * 2.执行多个文件处理
	 * @param file
	 * @author qianye.zheng
	 */
	private static final void handleModuleProject(File file)
	{
		System.out.println("moduleRootPath: " + file.getAbsolutePath());
		final String nameTemplate = ProjectCreaterUtil.getProjectNameTemplate();
		final String name = ProjectCreaterUtil.getProjectName();
		final String newName = file.getName().replace(nameTemplate, name);
		// 重命名
		FileUtil.rename(file, newName);
		
		// 重命名之后 需要重新构造文件对象
		final String newProjectRootPath = file.getParent() + "/" + newName;
		file = new File(newProjectRootPath);
		
		final File[] files = file.listFiles();
		if (!EmptyUtil.isEmpty(files))
		{
			String filename = null;
			for (int i = 0; i < files.length; i++)
			{
				filename = files[i].getName();
				if (filename.equals("pom.xml"))
				{
					// 处理 pom.xml 文件
					handlePomFile(files[i]);
					
					continue;
				}
				
				// 处理.settings 下的目录
				if (filename.equals(".settings"))
				{
					final File settingsFile = new File(newProjectRootPath + "/.settings");
					final File[] settingsFiles = settingsFile.listFiles();
					for (int j = 0; j < settingsFiles.length; j++)
					{
						handleNormalFile(settingsFiles[j]);
					}
					
					continue;
				}
				
				if (filename.equals(".project"))
				{
					// 处理其他文件
					handleNormalFile(files[i]);
					
					continue;
				}
			}
		}
		// 处理web.xml文件
		final String webXmlPath = newProjectRootPath + "/src/main/webapp/WEB-INF/web.xml";
		final File webXmlFile = new File(webXmlPath);
		if (webXmlFile.exists())
		{
			// 存在再处理
			handleNormalFile(webXmlFile);
		}
		/*
		 * 创建包
		 * groupId + 模块名称后缀
		 */
		final String suffix = newName.substring(newName.lastIndexOf('-') + 1);
		final String packageName = ProjectCreaterUtil.getGroupId() + "." + suffix;
		if (ProjectCreaterUtil.getPackageCreateSwitch())
		{
			createPackate(file.getAbsolutePath(), packageName);
		}
	}
	
	/**
	 * 
	 * @description 处理普通文件
	 * 替换名称
	 * @param file
	 * @author qianye.zheng
	 */
	private static final void handleNormalFile(final File file)
	{
		final String nameTemplate = ProjectCreaterUtil.getProjectNameTemplate();
		final String name = ProjectCreaterUtil.getProjectName();
		String content = FileUtil.getString(file);
		// 替换
		content = content.replace(nameTemplate, name);
		// 输出
		FileUtil.writeString(file, content);
	}
	
	/**
	 * 
	 * @description 处理 pom 文件
	 * 1. groupId
	 * 2.替换名称
	 * @param file
	 * @author qianye.zheng
	 */
	private static final void handlePomFile(final File file)
	{
		String content = FileUtil.getString(file);
		
		final String gropuIdTemplate = ProjectCreaterUtil.getGroupIdTemplate();
		final String gropuId = ProjectCreaterUtil.getGroupId();
		// 替换1
		content = content.replace(gropuIdTemplate, gropuId);
		
		final String nameTemplate = ProjectCreaterUtil.getProjectNameTemplate();
		final String name = ProjectCreaterUtil.getProjectName();
		// 替换2
		content = content.replace(nameTemplate, name);
		// 输出
		FileUtil.writeString(file, content);
	}
	
	/**
	 * 
	 * @description 
	 * @param projectPath 项目路径
	 * @param packagePath 包路径，例如  a.b.c
	 * @author qianye.zheng
	 */
	private static final void createPackate(final String projectPath, final String packagePath)
	{
		/*
		 * 在 src/main/java 目录下分别创建针对该项目的包
		 */
		// 替换为/的路径
		final String relativePath = packagePath.replace('.', '/');
		final String dirPath = projectPath + "/src/main/java/" + relativePath;
		// 创建目录
		FileUtil.createDirectory(dirPath);
	}
	
}
