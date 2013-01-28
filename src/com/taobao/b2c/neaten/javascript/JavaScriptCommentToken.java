package com.taobao.b2c.neaten.javascript;

import java.io.IOException;

import com.taobao.b2c.neaten.DefaultToken;
import com.taobao.b2c.neaten.Node;
import com.taobao.b2c.neaten.WorkingBufferSeeker;
 

/**   
 * @author xiaoxie   
 * @create time��2008-5-22 ����11:47:52   
 * @description  ����//</script>���������
 */
public class JavaScriptCommentToken extends DefaultToken {

 
	//private static String[] END_DELIMITER =  {" ", "\n", "\r", "\t", "\f","\u200B",">"};
	public JavaScriptCommentToken(){
		super(new String[]{"//"},new String[]{"\n","</script"});
	}
	public JavaScriptCommentToken(String[] startDelimiter,String[] endDelimiter){
		super( startDelimiter , endDelimiter );
		
	}
	//�޸�startswitch���������ػ�ƥ�䵽���ַ�,���û��ƥ�䵽�򷵻�null
	public Node seekNode(WorkingBufferSeeker wb) throws IOException{
		Node node = createNodeInstance();
		String sw = wb.startsWith(this.getStartDelimiter());
		
		if(sw != null){
			//��ȡ���ݽڵ�
			wb.createContentNodes();
			wb.move(sw.length());
			//��ǰstartDelimieterΪsw
			//�����ڵ�
			while (!wb.isAllRead()) {
				//getEndDelimiter���������length
				for (String value : this.getEndDelimiter()) {
					int valueLen = value.length();
					wb.readIfNeeded(valueLen);
					if(wb.isToEnd(valueLen)){
						wb.moveToEnd();
						wb.wrapNode(node,false);
						return node;
						//������ѭ��
					}
					if(value.equals("\n")){
						if (wb.startsWith(value)) {
								node.setEndDelimiter(value);
								wb.wrapNode(node,true);
	 
								return node;
						 
						}
					}
					if(value.equals("</script")){
						String ss = wb.startsWith(value,">");
						if (ss != null) {
							node.setEndDelimiter(value);
							wb.wrapNode(node,true);
 
							return node;
					 
						}
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
