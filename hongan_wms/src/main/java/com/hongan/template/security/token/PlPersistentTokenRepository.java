package com.hongan.template.security.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PlPersistentTokenRepository implements PersistentTokenRepository {
    static final int TWO_HOURS = 60 * 60 * 24 * 100;//8640000

    @Autowired
    RedisTokenStore tokenService;

    private int tokenValiditySeconds = TWO_HOURS;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        tokenService.save(token, tokenValiditySeconds);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        tokenService.update(series, tokenValue, lastUsed, tokenValiditySeconds);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        return tokenService.get(seriesId);
    }

    @Override
    public void removeUserTokens(String username) {
        tokenService.remove(username);
    }

    public void setTokenValiditySeconds(int tokenValiditySeconds) {
        this.tokenValiditySeconds = tokenValiditySeconds;
    }

    public void setTokenService(RedisTokenStore tokenService) {
        this.tokenService = tokenService;
    }
}

