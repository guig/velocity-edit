package com.taobao.b2c.neaten.javascript;

import com.taobao.b2c.neaten.BaseTokenizer;
import com.taobao.b2c.neaten.Token;
/**
 * @author xiaoxie
 */
public class SubScriptTokenizer extends BaseTokenizer {

	public SubScriptTokenizer(Token token) {
		super(token);
	}
//	public Node seek(WorkingBufferSeeker wb) throws IOException {
//		//check token validate
//		Node node = super.seek(wb);
//		if(node != null && !(node instanceof OmitNode) ){
		  
//			����node��format����
			//����tag <script xx />
//			String source = node.getOriginalSource().trim();
//			if(source.startsWith("<") && source.endsWith(">")){
//				node.setContentType(Node.CONTENT_TYPE_TAG);
//				if(source.endsWith("/>") && source.startsWith("<") ){
//					 node.setFormatType(Node.FORMAT_TYPE_INTACT);
//					 
//				}
//	//			�ر�tag </script>
//				
//				else if(source.startsWith("</") && source.endsWith(">")){
//					node.setFormatType(Node.FORMAT_TYPE_CLOSE);
//				}
//	//			��ʼtag <sript>
//				else {
//					node.setFormatType(Node.FORMAT_TYPE_OPEN);
//				}
//			}else{
				//code ��ע�� ������
	 
//				if(source.startsWith("/")){
//					 node.setName("//");
//					node.setFormatType(Node.FORMAT_TYPE_INTACT);
//					node.setContentType(Node.CONTENT_TYPE_COMMENT);
//				}else{
//					node.setContentType(Node.CONTENT_TYPE_CODE);
//				}
				 
				
		//	}
//		}
//		return node;
//	}
}
