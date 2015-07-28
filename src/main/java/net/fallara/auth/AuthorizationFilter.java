package net.fallara.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet Filter implementation class AuthorizationFilter
 */
@WebFilter("/*")
public class AuthorizationFilter implements Filter {
	
	protected static Logger log = Logger.getLogger(AuthorizationFilter.class);

    /**
     * Default constructor.
     */
    public AuthorizationFilter() {
    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {

    }

    /**
     * @param request
     * @param response
     * @param chain
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest thisRequest = (HttpServletRequest) request;
        HttpServletResponse thisResponse = (HttpServletResponse) response;

        Boolean excludedPage = false;
        String[] excludeList = {"/index.jsp", "/GoogleAuth.do", "googleAuth.js", "dvc.css"};
        String requestPath = thisRequest.getRequestURI();

        for (String ep : excludeList) {
            if (requestPath.endsWith(ep)) {
                excludedPage = true;
            }
        }

        if (excludedPage == false && thisRequest.getSession().getAttribute("token") == null) {
        	log.debug("User is not logged in, redirecting from " + requestPath + " Source=" + thisRequest.getRemoteAddr());
            thisResponse.sendRedirect("index.jsp");
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * @param fConfig
     * @throws javax.servlet.ServletException
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {

    }

}
