package com.taobao.b2c.neaten;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**   
 * <p>
 * <code>Area</code> contains many tokenizer,tokenizer returns <code>Node</code>,
 * <code>Area</code> add the <code>Node</code> to the <code>NodeList</code>
 * </p>
 * @author xiaoxie   
 * @create time��2008-5-8 ����02:10:56   
 */
public abstract class Area {
	private String name;
	private NeatenCleaner neatenCleaner;
	private List<Tokenizer> tokenizerList = new ArrayList<Tokenizer>();
	private Class<? extends Node> conentNodeClzz = ContentNode.class;

	public Area(String name){
		this.name = name;
	}
	public Area addTokenizer(Tokenizer tokenizer){
		tokenizerList.add(tokenizer);
		return this;
	}
	abstract public void setup();
	
	public boolean start(WorkingBufferSeeker wb,NodeList tlist) throws IOException {
		wb.setContentNodeClzz(this.getConentNodeClzz());
		for(Tokenizer tok : this.getTokenizerList() ){
			Node node = tok.seek(wb);
			if(node != null){
				//��������
				 List<Node>  cnodes = wb.getContentNodes();
				 if(cnodes != null){
					 //��������cnodeΪ�ض���node
					 tlist.addNodes(cnodes);
					 wb.cleanContentNodes();
				 }
				 //�����ȡ��node
				 //�����ǰ��token�������ˣ���area,��ô����
				  if(tok.getSubArea() != null){
					  	Reader reader =  new StringReader(node.getOriginalSource());
						WorkingBufferSeeker cwb  = new WorkingBufferSeeker(reader);
						cwb.init();
						//cwb.seekNode()
						//System.out.println(node.getOriginalSource());
						 
						Area area = tok.getSubArea();
						area.setup();
						cwb.setContentNodeClzz(area.getConentNodeClzz());
						 while(!cwb.isAllRead() ){
							//����ÿһ��area,���area���ؽ��Ϊtrue,��ʾ��ǰpos����Ѱ����
							//ÿ��tokenizer�ҵ�token֮��pos��Ҫ�ƶ���token����һ��pos
							
							
							if(area.start(cwb, tlist)){
								 continue ;//������ǰpos������
							}
							
							cwb.move();
						}
						//������������
						if(cwb.getEndContentNode() != null){
							tlist.addNode(cwb.getEndContentNode());
						}
					 
					 
				  }else{
				 	tlist.addNode(node);
				  }
				return true; //����true,neatenCleaner��������
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public List<Tokenizer> getTokenizerList() {
		return tokenizerList;
	}
	public NeatenCleaner getNeatenCleaner() {
		return neatenCleaner;
	}
	public void setNeatenCleaner(NeatenCleaner neatenCleaner) {
		this.neatenCleaner = neatenCleaner;
	}
	public Class<? extends Node> getConentNodeClzz() {
		return conentNodeClzz;
	}
	public void setConentNodeClzz(Class<? extends Node> conentNodeClzz) {
		this.conentNodeClzz = conentNodeClzz;
	}
}
