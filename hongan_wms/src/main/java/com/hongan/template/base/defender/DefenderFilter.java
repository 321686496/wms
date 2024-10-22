package com.hongan.template.base.defender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * 验证码接口保护
 *
 */

//@Component
@Slf4j
public class DefenderFilter extends OncePerRequestFilter implements ApplicationListener<ContextRefreshedEvent> {
    static Collection<Defender> defenders;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Defender[] list = defenders.stream().filter(defender -> defender.getMatcher().match(request)).toArray(Defender[]::new);

        for (Defender defender : list) {
            String message = defender.preCheck(request, response);
            if (message != null)
                throw new ServletException(message);
        }
        filterChain.doFilter(request, response);
        for (Defender defender : list) {
            defender.postCheck(request, response);
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        defenders = contextRefreshedEvent.getApplicationContext().getBeansOfType(Defender.class).values();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        if (Pattern.matches("^(/assets).*", uri)) {
            return true;
        }
        for (Defender defender : defenders) {
            Matcher matcher = defender.getMatcher();
            if (matcher.match(request)) {
                return false;
            }
        }
        return true;
    }

}
