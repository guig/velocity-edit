package com.taobao.b2c.neaten;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.taobao.b2c.neaten.html.TagNode;
import com.taobao.b2c.neaten.javascript.JavaScriptNode;

/**   
 * NeatenCleaner the entrance of the tidy project.
 *
 * <p>It represents public interface to the user. It's task is to tidy the
 * specified source VM/HTML(it's lie on specified Areas ).. ,the ouput result formatting lie on the Render.
 * It also offers a set of output interface to write resulting to StringBuffer,File,OutputStream.</p>
 * <p>Typical usage is the following:</p>
 *
 * <xmp>
 *		NeatenCleaner nc  = new NeatenCleaner(new File("d://sourceHtml.htm")); //Constructor specified the html source file
 *		nc.addArea(new HtmlArea());						//add HtmlArea to the NeatenCleaner
 *		nc.addArea(new JavaScriptArea());				//add HtmlArea to the JavaScriptArea
 *		nc.addArea(new VelocityArea());					//add HtmlArea to the VelocityArea
 *		nc.clean();										//creates NodeList by specified areas
 *		//NodeList nodeList = nc.getNodeList();			//get the nodeList
 *		nc.write(System.out);							//output the result
 * </xmp>
 * @author xiaoxie   
 * @create time��2008-5-8 07:13:22    
 */
public class NeatenCleaner {
 
	
	private NodeList 	nodeList 	= null;						//node�б�,ͨ��clean��������html�ĵ�������Node
	private Reader 		reader 		= null;						//html����Դ,�����ⲿ����ӿ�ȫ��ת��ΪReader��ʽ����
	private List<Area> 	areas 		= new ArrayList<Area>();
	private boolean 	startWrite 	= false;
	private Render 		render;									//�����Ⱦ
	private boolean 	isOmitFormField = false;				//�Ƿ���Ա�Ԫ��,Ĭ�ϲ�����
	private static final String DEFAULT_CHARSET = "gb2312";		//Ĭ�ϱ����ʽ
	public boolean isOmitFormField() {
		return isOmitFormField;
	}

	public void setOmitFormField(boolean isOmitFormField) {
		this.isOmitFormField = isOmitFormField;
	}

	public void setRender(Render render) {
		this.render = render;
	}

	/**
	 * Constructor - creates the instance with specified html or text content
	 * content as String.
	 * @param htmlContent
	 */
	public NeatenCleaner(String htmlContent) {
		this.reader = new StringReader(htmlContent);
	}

	/**
	 * Constructor - creates the instance for specified file and chareset.
	 * @param file
	 * @param charset
	 * @throws IOException
	 */
	public NeatenCleaner(File file, String charset) throws IOException {
		FileInputStream in = new FileInputStream(file);
		this.reader = new InputStreamReader(in, charset);
     }
 
    /**
     * Constructor - creates the instance for the specified input stream
     * @param in
     */
    public NeatenCleaner(InputStream in, String charset) throws IOException {
    	this.reader = new InputStreamReader(in, charset);
    }
    
    /**
     * Constructor - creates the instance for the specified input stream
     * @param in
     */
    public NeatenCleaner(InputStream in) throws IOException {
    	this.reader = new InputStreamReader(in, DEFAULT_CHARSET);
    }
    
	/**
	 * Constructor - creates the instance for specified file .
	 * @param file
	 * @throws IOException
	 */
	public NeatenCleaner(File file) throws IOException {
		this(file, DEFAULT_CHARSET);
	}
	
	public void clean() throws IOException{

		nodeList = new NodeList();
		WorkingBufferSeeker wb  = new WorkingBufferSeeker(reader);
		wb.init();
		//long t = System.currentTimeMillis();
		global: while(!wb.isAllRead() ){
			//����ÿһ��area,���area���ؽ��Ϊtrue,��ʾ��ǰpos����Ѱ����
			//ÿ��tokenizer�ҵ�token֮��pos��Ҫ�ƶ���token����һ��pos
			for(Area area : areas){
				area.setup();
				if(area.start(wb, nodeList)){
					 continue global;//������ǰpos������
				}
			}
			wb.move();
		}
		//long t1 = System.currentTimeMillis();
		//System.out.println("laps:"+(t1-t));
		//������������
		if(wb.getEndContentNode() != null){
			nodeList.addNode(wb.getEndContentNode());
		}

	}
	/**
	 * tidy the NodeList,create the relationship tree of the nodes(parent,child) between the nodes
	 */
	private Node tidy(){
		Node virtualNode = new TagNode("<V-V-NODE>");
 
		Node lastNode = null;
		if(this.getNodeList() != null && this.getNodeList().getNodeList() != null){
			List<Node> list = this.getNodeList().getNodeList();
			
			for(Node node : list){
 
				if(lastNode == null){
					
					lastNode = node;
					virtualNode.addChildrenNode(lastNode);
					virtualNode.setNextNode(lastNode);
					lastNode.setPreviousNode(virtualNode);
				}else{
					lastNode.setNextNode(node);
					node.setPreviousNode(lastNode);
					//����tag�����ϵĹ���
					
					Node addNode = lastNode.getParentNode();
					//LastNode����������(<div/>)��tag,ֱ�ӽ����ڵ�node�ӵ��丸�ڵ���
					if(Node.FORMAT_TYPE_INTACT.equals(lastNode.getFormatType())){
						addNode = lastNode.getParentNode();
					}
					//LastNode�ǿ�ʼ����(<div>)��tag,ֱ�ӽ����ڵ�node�ӵ���ڵ���
					else if(Node.FORMAT_TYPE_OPEN.equals(lastNode.getFormatType()) || Node.FORMAT_TYPE_CLOSE_OPEN.equals(lastNode.getFormatType())){
						addNode = lastNode;
					}
					//LastNode�ǹر�����(</div>)��tag,ֱ�ӽ����ڵ�node�ӵ��丸�ڵ���
					else if(Node.FORMAT_TYPE_CLOSE.equals(lastNode.getFormatType())){
						addNode = lastNode.getParentNode();
					}
					
					if(node instanceof TagNode || node instanceof ContentNode){
						//nothing to do
					}
					 
					if(node instanceof JavaScriptNode){
						//LastNode�ǿ�ʼ������ֻ����������һ�� FIXME
						if(Node.FORMAT_TYPE_OPEN_FIRST_LINE.equals(lastNode.getFormatType())){
							if("{".equals(node.getName())){
								addNode = lastNode.getParentNode();
							}else{
								addNode = lastNode;
							}
						}else{

							if( Node.FORMAT_TYPE_OPEN_FIRST_LINE.equals(lastNode.getParentNode().getFormatType())){
								Node tmpNode = lastNode;
								while(tmpNode.getParentNode() != null && Node.FORMAT_TYPE_OPEN_FIRST_LINE.equals(tmpNode.getParentNode().getFormatType())){
									tmpNode = tmpNode.getParentNode();
								}
								addNode = tmpNode.getParentNode();
							} else{
								if("{".equals(node.getName())){
									addNode = lastNode.getParentNode();
								} 
							}
						}
					}
					 
					if(Node.FORMAT_TYPE_CLOSE.equals(node.getFormatType()) || Node.FORMAT_TYPE_CLOSE_OPEN.equals(node.getFormatType())){
 
 
						Node tmp = lastNode;
						
						while(tmp != null && !"V-V-NODE".equals(tmp.getName()) ){
							if(!tmp.isClosed() && !Node.FORMAT_TYPE_CLOSE.equals(tmp.getFormatType()) && node.isMatch(tmp)){
								addNode = tmp.getParentNode();
								tmp.setClosed(true);
								tmp.setCloseNode(node);
								node.setOpenNode(tmp);
								break;
							}
							tmp = tmp.getParentNode() ;
						}
					}
					addNode.addChildrenNode(node);
					lastNode = node;
				}
			}
		}
		return virtualNode;
	}
	/**
	 * write the well-formed result to the specified OutputStream,the format is decided by the render
	 * @param os
	 * @throws IOException
	 */
	public void write(OutputStream os) throws IOException{
		//os.writer
		if(this.render != null && this.nodeList != null){
			Node vnode = tidy();
			
			List<Node> cnodes = vnode.getChildrenNodes();
			if(cnodes != null){
				for(Node node : cnodes)
				{	node.setParentNode(null);
					write(os,node);
				}
			}
		}
	}
	/**
	 * write the well-formed result to the specified StringBuffer,the format is decided by the render
	 * @param buffer
	 * @throws IOException
	 */
	public void write(StringBuffer buffer) throws IOException{
		//os.writer
		if(this.render != null && this.nodeList != null){
			Node vnode = tidy();
			
			List<Node> cnodes = vnode.getChildrenNodes();
			if(cnodes != null){
				for(Node node : cnodes)
				{	node.setParentNode(null);
					write(buffer,node);
				}
			}
		}
	}
	private void write(OutputStream os,Node node) throws IOException{
		if(node  != null){
			if(!startWrite){
				//System.out.println("start");
				//ȥ���س������
				String output = render.render(node);
				if("".equals(output.trim())){
					return;
				}
				if(output.startsWith("\r\n")){
					os.write(output.substring(2).getBytes());
				}else{
					os.write( render.render(node).getBytes());
				}
				startWrite = true;
			}else{
				os.write( render.render(node).getBytes());
			}
			if(node.getChildrenNodes() != null){
				
				for(Node cnode : node.getChildrenNodes())
				{
					write(os,cnode);
				}
			}
		}
	}
	private void write(StringBuffer os,Node node) throws IOException{
		if(node  != null){
			if(!startWrite){
				//System.out.println("start");
				//ȥ���س������
				String output = render.render(node);
				if("".equals(output.trim())){
					return;
				}
				if(output.startsWith("\r\n")){
					os.append((output.substring(2)));
				}else{
					os.append( render.render(node));
				}
				startWrite = true;
			}else{
				os.append( render.render(node));
			}
			if(node.getChildrenNodes() != null){
				
				for(Node cnode : node.getChildrenNodes())
				{
					write(os,cnode);
				}
			}
		}
	}
	/**
	 * Write the well-formed result to the specified File,the format is decided by the render
	 * @param outPutFile
	 * @throws IOException
	 */
	public void write(File outPutFile) throws IOException{
		FileOutputStream fos = new FileOutputStream(outPutFile);
		write(fos);
	}
	public NodeList getNodeList(){
		return this.nodeList;
	}
	public NeatenCleaner addArea(Area area){
		this.areas.add(area);
		area.setNeatenCleaner(this);
		return this;
	}
 
}
