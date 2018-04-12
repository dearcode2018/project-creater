/**
 * 描述: 
 * MavenProjectCreater.java
 * @author	qye.zheng
 * 
 *  version 1.0
 */
package com.hua.starter;

import org.junit.Test;

import com.hua.driver.MavenProjectDriver;

/**
 * 描述: 启动器
 * @author  qye.zheng
 * 
 * MavenProjectCreater
 */
public final class MavenProjectCreater
{


	

	// 启动器模板
	/**
	 * 
	 * 描述: 
	 * @author qye.zheng
	 * 
	 */
	@Test
	public void start()
	{
		/** ===== begin 驱动参数设置  ===== */
		// 设置例子
		
		
		/** ===== end of 驱动参数设置 ===== */

		// 启动驱动
		MavenProjectDriver.creater();
		
	}

}
