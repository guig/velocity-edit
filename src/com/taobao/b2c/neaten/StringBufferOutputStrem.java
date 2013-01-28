package com.taobao.b2c.neaten;

import java.io.IOException;
import java.io.OutputStream;

/**   
 * @author xiaoxie   
 * @create time��2008-5-22 ����04:38:25   
 * @description  
 */
public class StringBufferOutputStrem extends OutputStream {
	private StringBuffer buffer = null;
	@Override
	public void write(int b) throws IOException {
		if(buffer == null){
			buffer = new StringBuffer();
		}

		buffer.append((char)b);

	}
	public StringBuffer getBuffer(){
		return buffer;
	}
 
}
