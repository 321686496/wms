package com.hongan.template.security;


import com.hongan.template.base.configure.PLApplicationContext;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.BaseCrypto;
import com.hongan.template.security.common.PlSecurityConstConfig;
import com.hongan.template.security.token.RedisTokenStore;
import com.hongan.template.listener.CustomSessionExpiredHandler;
import com.hongan.template.security.common.PlAuthServiceFilter;
import com.hongan.template.security.common.PlSecurityHandler;
import com.hongan.template.security.common.PlUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.Filter;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-02 15:38
 * @Description: WebSecurityConfig
 */

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PlUserDetailService userDetailService;
    @Autowired
    PLApplicationContext applicationContext;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(new PLPasswordEncoder());
        return provider;
    }

    @Autowired
    RedisTokenStore tokenStore;

    public Filter getAuthFiler() {
        PlAuthServiceFilter filter = new PlAuthServiceFilter();
        filter.setContext(applicationContext.getContext());
        return filter;
    }


    @Override
    @Bean // share AuthenticationManager for web and oauth
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (log.isTraceEnabled()) {
            log.trace("configure httpSecurity...");
        }
        Filter filter = getAuthFiler();
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                //设置放行接口
                .antMatchers(PlSecurityConstConfig.getAllLoginURL()).permitAll()
//                .antMatchers("/auth/**").permitAll()
                .antMatchers("/base/**").permitAll()
//                .antMatchers("/user/**").hasRole("CUSTOMER")
                .antMatchers("/user/**").hasAnyRole("CUSTOMER")
                .antMatchers("/store/**").hasRole("STORE")
//                .antMatchers("/web/admin/**", "/admin/**").hasAnyRole(RoleType.adminRoles())
                .antMatchers("/web/admin/**", "/admin/**").hasAnyRole("SUPER", "ADMIN")
                .antMatchers("/super/**").hasAnyRole("SUPER")

                .and()
                .httpBasic().disable()
                .formLogin().loginPage(PlSecurityConstConfig.NameLogin).permitAll()
                .successHandler(new PlSecurityHandler.PlLoginSuccessHandler())
                .failureHandler(new PlSecurityHandler.PlLoginFailureHandler())
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(PlSecurityConstConfig.Logout))
                .clearAuthentication(true)
                .logoutSuccessHandler(new PlSecurityHandler.PlLogoutSuccessHandler())
//                .and().rememberMe().rememberMeCookieName(QhSecurityConstConfig.TokenName)
//                .tokenRepository(persistentTokenRepository())
//                // 有效时间：单位s
//                .tokenValiditySeconds(QhSecurityConstConfig.ValiditySeconds)
//                .userDetailsService(userDetailService)


                .and()
                // 在使用jwt token 授权时 session中不需要保存SecurityContext登录状态
//                .sessionFixation().migrateSession()
//
                .exceptionHandling()
                .accessDeniedHandler(new PlSecurityHandler.PlAccessDeniedHandler())
                .authenticationEntryPoint(new PlSecurityHandler.PlUnauthenticatedEntryPoint())
                .and()
                .csrf().disable()
                //此次为登陆Session控制
                .sessionManagement()
                //maximumSessions 表示配置最大会话数为 1，这样后面的登录就会自动踢掉前面的登录
                .maximumSessions(1)
                .expiredSessionStrategy(new CustomSessionExpiredHandler())
        //禁止新的登录
//                .maxSessionsPreventsLogin(true)
        ;
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers(PlSecurityConstConfig.getAllIgnore());
    }

    static class PLPasswordEncoder extends BCryptPasswordEncoder {
        @Override
        public boolean matches(CharSequence charSequence, String s) {
            try {
                return BaseCrypto.checkHmac(s, charSequence.toString()) || super.matches(charSequence, s);
            } catch (BaseException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}

