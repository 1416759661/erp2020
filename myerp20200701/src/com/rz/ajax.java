package com.rz;
import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ajax
 */
@WebServlet("/ajax")
public class ajax extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private List<tbtype> listall = new ArrayList<tbtype>();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ajax() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String rnum= request.getParameter("rnum");
		switch(rnum){
		    case "1":rolehasmenuadd(request,response);break; 
		    case "2":rolehasmenuremove(request,response);break; 
		    case "3":userhasroleadd(request,response);break; 
		    case "4":userhasroleremove(request,response);break; 
		    case "5":getprotypehtml(request,response);break; 
		    case "6":getprotypehtmlforedit(request,response);break; 
		    case "7":getproviewbynum(request,response);break; 
		    default : break;
		}
		
		/**
		 * response.setCharacterEncoding("utf-8");
		response.setContentType("text/json;charset=utf-8");
		response.getWriter().write("{\"msg\":\"ok\"}");
		
		String jsonstr =JSON.toJSONString(listall); //把查询的结果对象转为json格式的字符串。
		response.getWriter().write(jsonstr);//把字符串发送给小程序。
		 * */
		
	}
	
	
	protected void getproviewbynum(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String pronum=request.getParameter("pronum");
		DBHelper Dal = new DBHelper();
		String strSql = " select * from v_product where pronum=?";		
		List<Object> params = new ArrayList<Object>();
		params.add(pronum);
		Map<String, Object> objbyid = null;
		objbyid=Dal.getSingleObject(strSql, params);	
		String html="{";
		html+="\"id\":\""+objbyid.get("id")+"\",";
		html+="\"proname\":\""+objbyid.get("proname")+"\",";
		html+="\"pronum\":\""+objbyid.get("pronum")+"\",";
		html+="\"fullpath\":\""+objbyid.get("fullpath")+"\",";
		html+="\"price\":\""+objbyid.get("price")+"\",";
		html+="\"imgurl\":\""+objbyid.get("imgurl")+"\",";
		html+="\"procount\":\""+objbyid.get("procount")+"\"";
		html+="}";
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/json;charset=utf-8");
		response.getWriter().write(html);	
	}
	
	
	/*
	 添加菜单权限
	*/
	protected void rolehasmenuadd(HttpServletRequest request,HttpServletResponse response) throws IOException{
		DBHelper Dal=new DBHelper();
		String idstr=request.getParameter("idstr");
		String roleidstr=request.getParameter("roleid");
        String[] idarray = idstr.split(",");
        Connection conn=Dal.getConnection();
        PreparedStatement pstmt;
        try {
        	pstmt = conn.prepareStatement("insert into tbrolehasmenu (menuid,roleid) values (?, ?)");
            for(String s:idarray)
            {
            	if(!s.equals("0"))
            	{
            		pstmt.clearParameters();
                	int sv=Integer.valueOf(s);
                	int roleid= Integer.valueOf(roleidstr);
                	pstmt.setInt(1, sv);
                	pstmt.setInt(2,roleid);
                	pstmt.execute();
            	}
            }
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		response.getWriter().write("ok");
	}
	
	/*
	 移除菜单权限
	*/
	protected void rolehasmenuremove(HttpServletRequest request,HttpServletResponse response) throws IOException{
		DBHelper Dal=new DBHelper();
		String idstr=request.getParameter("idstr");
		String roleid=request.getParameter("roleid");
		String strSql= "delete from tbrolehasmenu where roleid=? and menuid in ("+idstr+")";
		List<Object> params = new ArrayList<Object>();
		params.add(roleid);
		Dal.excuteSql(strSql, params);		 
		response.getWriter().write("ok");
	}
	
	/*
	 添加菜单权限
	*/
	protected void userhasroleadd(HttpServletRequest request,HttpServletResponse response) throws IOException{
		DBHelper Dal=new DBHelper();
		String idstr=request.getParameter("idstr");
		String useridstr=request.getParameter("userid");
       String[] idarray = idstr.split(",");
       Connection conn=Dal.getConnection();
       PreparedStatement pstmt;
       try {
       	pstmt = conn.prepareStatement("insert into tbuserhasrole (roleid,userid) values (?, ?)");
           for(String s:idarray)
           {
           	if(!s.equals("0"))
           	{
           		pstmt.clearParameters();
               	int sv=Integer.valueOf(s);
               	int userid= Integer.valueOf(useridstr);
               	pstmt.setInt(1, sv);
               	pstmt.setInt(2,userid);
               	pstmt.execute();
           	}
           }
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		response.getWriter().write("ok");
	}
	
	/*
	 移除菜单权限
	*/
	protected void userhasroleremove(HttpServletRequest request,HttpServletResponse response) throws IOException{
		DBHelper Dal=new DBHelper();
		String idstr=request.getParameter("idstr");
		String userid=request.getParameter("userid");
		String strSql= "delete from tbuserhasrole where userid=? and roleid in ("+idstr+")";
		List<Object> params = new ArrayList<Object>();
		params.add(userid);
		Dal.excuteSql(strSql, params);		 
		response.getWriter().write("ok");
	}
	
	public void getData(String pid)
	{
		DBHelper Dal = new DBHelper();
		String strSql = " select * from tbtype where parentid=?";		
		List<Object> params = new ArrayList<Object>();
		params.add(pid);
		List<Map<String, Object>> listcurrent = null;
		try {
			listcurrent = Dal.executeQuery(strSql, params);
			for (Object item : listcurrent) {
				Map<String, Object> temp=(Map<String, Object>)item;
				tbtype item2=new tbtype();
				item2.setId((int)temp.get("id"));
				item2.setTypeName(temp.get("typename").toString());
				item2.setParentid((int)temp.get("parentid"));
				item2.setParentName(temp.get("parentname").toString());
				item2.setFullPath(temp.get("fullpath").toString());
				item2.setlevelnum((int)temp.get("levelnum"));
				listall.add(item2);
				getData(temp.get("id").toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 获取商品类别html字符串for添加页面
	*/
	protected void getprotypehtml(HttpServletRequest request,HttpServletResponse response) throws IOException{
		listall.clear();
		getData("0");
		String html="<select name='typeid'>";
		for(tbtype item:listall)
		{
			String spacehtml="|--";
			for(int i=1;i<=item.getlevelnum();i++)
			{
				spacehtml+="|--";
			}
			
			html+="<option value='"+item.getId()+"'>"+spacehtml+item.getTypeName()+"</option>";
		}
		html+="</select>";
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/json;charset=utf-8");
		response.getWriter().write("{\"msg\":\""+html+"\"}");		
	}
	
	/*
	 获取商品类别html字符串for编辑页面
	*/
	protected void getprotypehtmlforedit(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String typeid=request.getParameter("typeid");
		listall.clear();
		getData("0");
		String html="<select name='typeid'>";
		for(tbtype item:listall)
		{
			
			String spacehtml="|--";
			for(int i=1;i<=item.getlevelnum();i++)
			{
				spacehtml+="|--";
			}
			if(String.valueOf(item.getId()).equals(typeid))
			{
				html+="<option value='"+item.getId()+"' selected='selected'>"+spacehtml+item.getTypeName()+"</option>";
			}
			else
			{
				html+="<option value='"+item.getId()+"'>"+spacehtml+item.getTypeName()+"</option>";
			}
		}
		html+="</select>";
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/json;charset=utf-8");
		response.getWriter().write("{\"msg\":\""+html+"\"}");		
	}
	
	

}
