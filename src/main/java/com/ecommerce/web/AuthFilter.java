package com.ecommerce.web;

import com.ecommerce.bean.InternauteBean;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** Protege les pages reservees aux internautes connectes (et la page admin). */
@WebFilter(filterName = "AuthFilter",
           urlPatterns = {"/panier.xhtml", "/commandes.xhtml", "/admin.xhtml"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        InternauteBean bean = (session == null) ? null : (InternauteBean) session.getAttribute("internauteBean");

        if (bean == null || !bean.isAuthentifie()) {
            response.sendRedirect(request.getContextPath() + "/login.xhtml");
            return;
        }
        if (request.getRequestURI().endsWith("/admin.xhtml") && !bean.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/index.xhtml");
            return;
        }
        chain.doFilter(req, res);
    }
}
