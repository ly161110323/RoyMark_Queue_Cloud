package com.roymark.queue.filter;

import com.roymark.queue.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SuperFilter extends OncePerRequestFilter
{


    /** 登录验证过滤器 */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {


         String[] superOperateUrl=new String[]{"queue_area.jsp","SuperManager_MenuInfo.jsp"};//只有超管可以操作的url
        // 请求的uri
        String uri = request.getRequestURI();
        boolean doSuperFilter = false;
        for (String s : superOperateUrl)
        {
            if (uri.indexOf(s) != -1)
            {
                // 如果uri中包含不过滤的uri，则不进行过滤
                doSuperFilter = true;
                break;
            }
        }
        if(doSuperFilter)
        {
            User loginUser = (User) request.getSession().getAttribute("LOGIN_USER");
            Integer topAreaId = 0; // this program don't need
            int topAreaIdValue=topAreaId.intValue();
            if(topAreaIdValue!=0)
            {
                String context = request.getContextPath();
                String indexUrl = context + "/page/manage/index.jsp";
                PrintWriter out = response.getWriter();
                out.print("<html><head><meta charset='UTF-8'></head>");
                out.println("<script>");
                out.println("alert('该菜单只有超管有权限操作!')");
                out.println("top.location.href='"+indexUrl+"'");
                out.println("</script>");
                out.println("</html");
                return;
            }
            else
            {
                filterChain.doFilter(request, response);
            }
        }
        else
        {
            filterChain.doFilter(request, response);
        }


    }

    /** 判断是否为Ajax请求
     * <功能详细描述>
     * @param request
     * @return 是true, 否false
     * @see [类、类#方法、类#成员]
     */
    public static boolean isAjaxRequest(HttpServletRequest request)
    {
        String header = request.getHeader("X-Requested-With");
        if (header != null && "XMLHttpRequest".equals(header)) {
            return true;
        }
        else {
            return false;
        }
    }


}
