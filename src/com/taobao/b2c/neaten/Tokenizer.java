package com.taobao.b2c.neaten;

import java.io.IOException;

/**   
 * <p>���ݰ󶨵�Token,������Node������//node������ tagNode->htmlTagNode</p>
 * @author xiaoxie   
 * @create time��2008-5-8 ����02:24:57   
 */
public interface Tokenizer {
	Token getToken();
	void setToken(DefaultToken token);
	Node seek(WorkingBufferSeeker wb) throws IOException;
	Area getSubArea();
	Tokenizer setSubArea(Area area);
}
