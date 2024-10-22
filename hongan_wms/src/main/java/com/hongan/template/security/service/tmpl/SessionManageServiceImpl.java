package com.hongan.template.security.service.tmpl;

import com.hongan.template.base.utils.ClientType;
import com.hongan.template.security.service.ISessionManageService;
import com.hongan.template.security.common.TokenUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
@Slf4j
public class SessionManageServiceImpl implements ISessionManageService {

    private final FindByIndexNameSessionRepository sessionRepository;

    public SessionManageServiceImpl(FindByIndexNameSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Boolean checkLoginUser(HttpServletRequest request, TokenUser user) {
        //当前登陆客户端
        ClientType clientType = ClientType.fromRequest(request);
        //获取当前登陆的sessionId
        String curSessionId = request.getSession().getId();
        String name = user.getName();
        //获取该用户已经登陆的session
        Map<String, Object> byPrincipalName = sessionRepository.findByPrincipalName(name);
        // 非当前Session
        SessionRepository sessionRepository = (SessionRepository) request.getAttribute(SessionRepositoryFilter.SESSION_REPOSITORY_ATTR);
        boolean res = false;
        for (Map.Entry<String, Object> entry : byPrincipalName.entrySet()) {
            String sessionId = entry.getKey();
            if (!res && !sessionId.equals(curSessionId)) {
                Session session = sessionRepository.findById(sessionId);
                if (session != null) {
                    SecurityContext context_session = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
                    Authentication auth = context_session.getAuthentication();
                    TokenUser otherUser = (TokenUser) auth.getPrincipal();
                    if (otherUser.getClientType().equals(clientType)) {
                        res = true;
                    }
                }
            }
        }
        return res;
    }

    @Override
    public void clearOtherLoginUser(HttpServletRequest request, TokenUser user) {
        //当前登陆客户端
        ClientType clientType = ClientType.fromRequest(request);
        //获取当前登陆的sessionId
        String curSessionId = request.getSession().getId();
        String name = user.getName();
        //获取该用户已经登陆的session
        Map<String, RedisIndexedSessionRepository> byPrincipalName = sessionRepository.findByPrincipalName(name);
        // 非当前Session
        SessionRepository sessionRepository = (SessionRepository) request.getAttribute(SessionRepositoryFilter.SESSION_REPOSITORY_ATTR);
        for (Map.Entry<String, RedisIndexedSessionRepository> entry : byPrincipalName.entrySet()) {
            String sessionId = entry.getKey();
            if (!sessionId.equals(curSessionId)) {
                Session session = sessionRepository.findById(sessionId);
                if (session != null) {
                    SecurityContext context_session = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
                    Authentication auth = context_session.getAuthentication();
                    TokenUser otherUser = (TokenUser) auth.getPrincipal();
                    if (otherUser.getClientType().equals(clientType)) {
                        log.debug("删除会话，ID: {}", sessionId);
                        sessionRepository.deleteById(sessionId);
                    }
                }
            }
        }
    }
}
