package com.hongan.template.security.token;

import com.hongan.template.base.service.IRedisValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RedisTokenStore {
    @Autowired
    IRedisValue<TokenData> redisToken;

    @Autowired
    IRedisValue<String> redisString;

    static final String REMEMBER_ME_TOKEN = "REMEMBERME:TOKEN:";
    static final String REMEMBER_ME_USERNAME = "REMEMBERME:USERNAME:";


    void save(PersistentRememberMeToken token, int tokenValiditySeconds) {
        TokenData data = new TokenData(token);
        redisString.setValue(data.usernameKey(), token.getSeries(), tokenValiditySeconds);
        redisToken.setValue(data.seriesKey(), data, tokenValiditySeconds);
    }

    void update(String series, String value, Date lastUsed, int tokenValiditySeconds) {
        String key = REMEMBER_ME_TOKEN + series;
        TokenData data = redisToken.getValue(key);
        data.setTokenValue(value);
        data.setDate(lastUsed);
        redisString.expire(data.usernameKey(), tokenValiditySeconds);
        redisToken.setValue(data.seriesKey(), data, tokenValiditySeconds);
    }
    PersistentRememberMeToken get(String series) {
        TokenData data =  redisToken.getValue(REMEMBER_ME_TOKEN + series);
        if (data == null) {
            return null;
        }
        return data.token();
    }

    void remove(String username) {
        String nameKey = REMEMBER_ME_USERNAME + username;
        String seriesKey = REMEMBER_ME_TOKEN + redisString.getValue(nameKey);
        redisString.delete(nameKey);
        redisToken.delete(seriesKey);
    }

    @Setter
    @Getter
    @NoArgsConstructor
    static class TokenData {
        private String username;
        private String series;
        private String tokenValue;
        private Date date;


        public String usernameKey() {
            return REMEMBER_ME_USERNAME + username;
        }

        public String seriesKey() {
            return REMEMBER_ME_TOKEN + series;
        }

        public PersistentRememberMeToken token() {
            return new PersistentRememberMeToken(username, series, tokenValue, date);
        }

        TokenData(PersistentRememberMeToken token) {
            this.username = token.getUsername();
            this.series = token.getSeries();
            this.tokenValue = token.getTokenValue();
            this.date = token.getDate();
        }
    }
}
