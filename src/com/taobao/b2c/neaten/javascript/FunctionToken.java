package com.taobao.b2c.neaten.javascript;

import java.io.IOException;

import com.taobao.b2c.neaten.DefaultToken;
import com.taobao.b2c.neaten.Node;
import com.taobao.b2c.neaten.WorkingBufferSeeker;

/**   
 * @author xiaoxie   
 * @create time��2008-5-21 ����01:37:18   
 * @description  ����script���������,if(true)���뱻ʶ��Ϊif����,��var if������ʶ��
 * DefaultToken�����ṩ���Ҫ��.ֻ��ʶ��ʼ�ͽ�����ǵ�token
 */
public class FunctionToken extends DefaultToken {
	private static String[] END_DELIMITER =  {" ","("};
	private String startDelimiterEndToken = "(";
	public FunctionToken(String startDelimiter,String endDelimieter) {
		super(startDelimiter,endDelimieter);
		super.setEndDelimiterPartner("(");
		super.setPartnerLoopCount(1);
		super.setDefaultParnerLoopCount(1);
	
		
	}
	public Node seekNode(WorkingBufferSeeker wb ) throws IOException{
		Node node = createNodeInstance();//new TagNode();
		String[] startDelimiter = this.getStartDelimiter();
		boolean sw = wb.startsWith(startDelimiter[0]);
		if(sw){
			sw = false;
			//������ʱ����
			wb.createTempContent();
			wb.move(startDelimiter[0].length());
			String rs = wb.startsWith(END_DELIMITER);
			 while(rs != null){
					wb.move(rs.length());
					if(!rs.equals(startDelimiterEndToken)){
						rs = wb.startsWith(END_DELIMITER);
					}else{
						sw = true;
						break;
					}
			}
			 //�ͷ���ʱ����
			 if(!sw){
				 wb.cleanTempContent();
			 }
			
		}
		
		if(sw){
			//��ȡ���ݽڵ�
			wb.createContentNodesByTempContent();
			 
			//wb.move(sw.length());
			if(this.getEndDelimiter() == null){
				 wb.wrapNode(node,true);
				 //node.setStartDelimiter("");
				 return node;
			}
			//��ǰstartDelimieterΪsw
			//�����ڵ�
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
					
					if(isNeedDealEndDelimiterPartner()){
						if(wb.startsWith(getEndDelimiterPartner())){
							this.increasePartnerLoopCount();
						}
					}
					if (wb.startsWith(value)) {
						if(isNeedDealEndDelimiterPartner()){
							this.reducePartnerLoopCount();
							if(this.getPartnerLoopCount() == 0){
								//�ҵ����Ľڵ�
								wb.move(value.length());
								wb.wrapNode(node,true);
								return node;
							}
						}else{
							
							wb.move(value.length());
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
