package com.taobao.b2c.neaten.html;

import com.taobao.b2c.neaten.BaseTokenizer;
import com.taobao.b2c.neaten.Token;
/**
 * @author xiaoxie
 */
public class HtmlTokenizer extends BaseTokenizer {

	public HtmlTokenizer(Token token) {
		super(token);
		
	}
//	public Node seek(WorkingBufferSeeker wb) throws IOException {
//		//check token validate
//		Node node = super.seek(wb);
//		if(node != null  ){
//		 
//				if(node.getName() != null){
//					//����node��format����
//					//����tag
//					if(node.getOriginalSource().endsWith("/>") || isHtmlFullTag(node)){
//						 node.setFormatType(Node.FORMAT_TYPE_INTACT);
//					}
//					//�ر�tag
//					else if(node.getOriginalSource().startsWith("</")){
//						node.setFormatType(Node.FORMAT_TYPE_CLOSE);
//					}
//					//��ʼtag
//					else {
//						node.setFormatType(Node.FORMAT_TYPE_OPEN);
//					}
//					//����node��content����
//					node.setContentType(Node.CONTENT_TYPE_TAG);
//					if(node.getName().equals("!--")){
//						node.setContentType(Node.CONTENT_TYPE_COMMENT);
//					}
//				
//				 
//				
//			}
//			return node;
//		}
//		return node;
//	}
//	public boolean isHtmlFullTag(Node node){
//		
//			for(HtmlTagInfo info : HtmlTagInfo.values()){
//				 
//				if(node.getName().startsWith(info.getToken())){
//					if(info.getType().equals(Node.FORMAT_TYPE_INTACT)){
//						return true;
//					}
//					 
//				}
//			}
//			return false;
//	 
//	}
}
