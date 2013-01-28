package com.taobao.b2c.neaten.javascript;

import java.io.IOException;

import com.taobao.b2c.neaten.Node;
import com.taobao.b2c.neaten.DefaultToken;
import com.taobao.b2c.neaten.WorkingBufferSeeker;
import com.taobao.b2c.neaten.html.TagNode;
/**
 * @author xiaoxie
 */
public class ScriptToken extends DefaultToken {
	private static String[] END_DELIMITER =  {" ", "\n", "\r", "\t", "\f","\u200B",">"};
	public ScriptToken(){
		super(new String[]{"<script"},new String[]{"</script"});
	}
	//<script language="javascript" type="text/javascript"/>
	//<script language="javascript" type="text/javascript"></script>
//	public ScriptToken(String[] startDelimiter,String[] endDelimiter){
//		super( startDelimiter , endDelimiter );
//		
//	}
//	public ScriptToken(String startDelmiter, String endDelimiter) {
//		super(startDelmiter, endDelimiter);
//	}//ע��bug ��ǰ��delimieter��¼����
	 
	public Node seekNode(WorkingBufferSeeker wb) throws IOException{
		Node node = new TagNode();
		String sw = wb.startsWith(this.getStartDelimiter());
		//�������/>��ʾ����,�������>��ʾҳ�����script
		//boolean srcipt = false;
		if(sw != null){
			wb.move(sw.length());
			//��ǰstartDelimieterΪsw
			//�����ڵ�
			while (!wb.isAllRead()) {
				
				//String end = ">";
				wb.readIfNeeded(1);
				if(wb.isToEnd(1)){
					wb.moveToEnd();
					wb.wrapNode(node,false);
					return node;
					//������ѭ��
				}
				//��ʾҳ�����script
				if(wb.startsWith(">")){
					//srcipt = true;
					wb.move(1);
					while (!wb.isAllRead()) {
					for (String value : this.getEndDelimiter()) {
						int valueLen = value.length();
						wb.readIfNeeded(valueLen);
						if(wb.isToEnd(valueLen)){
							wb.moveToEnd();
							 wb.wrapNode(node,false);
							return node;
							//������ѭ��
						}
						if (wb.startsWith(value)) {
								wb.move(value.length());
								if(value.equals("</script")){
									//��ȡ���һ��>
									 String rs = wb.startsWith(END_DELIMITER);
									 while(rs != null){
										wb.move(rs.length());
										if(!rs.equals(">")){
											rs = wb.startsWith(END_DELIMITER);
										}else{
											//return true;
											//node = wb.wrapNode(node,true);
											break;
										}
									}
								}
								//this.endDelimiter = value;
								 wb.wrapNode(node,true);
								//this.endDelimiter = value;
								
								return node;
						 
						}
					}
					wb.move();
					}
				}else{
					wb.readIfNeeded(2);
					if(wb.isToEnd(2)){
						wb.moveToEnd();
						wb.wrapNode(node,false);
						return node;
						//������ѭ��
					}
					//�������/>��ʾ����
					if (wb.startsWith("/>")) {
						wb.move(2);
						wb.wrapNode(node,true);
						return node;
					}
				}
				
				wb.move();
			}
			 
		}else{
			 return null;
		}
		return null;
	}
}
