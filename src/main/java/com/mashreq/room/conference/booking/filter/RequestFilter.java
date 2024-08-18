package com.mashreq.room.conference.booking.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpServletRequest) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            String requestURI = httpServletRequest.getRequestURI();
            if (requestURI.contains("swagger") || requestURI.contains("api-docs")) {
                chain.doFilter(request, response);
                return;
            }

            String requestId = httpServletRequest.getHeader("request-id");

            if (requestId == null || requestId.isEmpty()) {
                httpServletResponse.setContentType("application/json");
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpServletResponse.getWriter().write("{\"message\":\"Request ID is missing\"}");
                return;
            }


            ThreadContext.put("request-id", requestId);

        }
        try {
            chain.doFilter(request, response);
        } finally {
            ThreadContext.clearMap();
        }
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}