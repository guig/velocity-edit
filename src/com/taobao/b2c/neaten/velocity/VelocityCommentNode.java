/**
 * 
 */
package com.taobao.b2c.neaten.velocity;

import com.taobao.b2c.neaten.BaseNode;

/**   
 * @author xiaoxie   
 * @create time��2008-5-21 ����11:45:44   
 * @description  
 */

public class VelocityCommentNode extends BaseNode {
	public void setOriginalSource(String source){
		super.setOriginalSource(source);
		this.setName("##");
		this.setFormatType(FORMAT_TYPE_INTACT);
		this.setContentType(CONTENT_TYPE_COMMENT);
	}
}
