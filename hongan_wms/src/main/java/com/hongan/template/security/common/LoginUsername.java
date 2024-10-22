package com.hongan.template.security.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongan.template.base.utils.ClientType;
import com.hongan.template.base.utils.JacksonObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@Data
@NoArgsConstructor
public class LoginUsername {
    private String username;
    private LoginUserType userType;
    private ClientType clientType;

    public LoginUsername(String username, HttpServletRequest request, String path) {
        this.username = username;
        this.clientType = ClientType.fromRequest(request);
        this.userType = LoginUserType.fromLoginUrl(path);
    }

    public String toJSON() throws JsonProcessingException {
        ObjectMapper mapper = JacksonObject.getJSONMapper();
        return mapper.writeValueAsString(this);
    }

    public static LoginUsername parseJSON(String s) throws JsonProcessingException {
        ObjectMapper mapper = JacksonObject.getJSONMapper();
        return mapper.readValue(s, LoginUsername.class);
    }

    public enum LoginUserType {
        AdminType,
        UserType,
        StoreType,
        ManageType,
        NONE;

        public static LoginUserType fromLoginUrl(String s) {
            if (s == null || "".equals(s.trim())) {
                return NONE;
            }
            String[] userLoginURL = PlSecurityConstConfig.getUserLoginURL();
            for (String url : userLoginURL) {
                if (url.equals(s)) {
                    return UserType;
                }
            }
            String[] adminLoginURL = PlSecurityConstConfig.getAdminLoginURL();
            for (String url : adminLoginURL) {
                if (url.equals(s)) {
                    return AdminType;
                }
            }
            String[] technicianLoginURL = PlSecurityConstConfig.getStoreLoginURL();
            for (String url : technicianLoginURL) {
                if (url.equals(s)) {
                    return StoreType;
                }
            }
            String[] manageLoginURL = PlSecurityConstConfig.getManageLoginURL();
            for (String url : manageLoginURL) {
                if (url.equals(s)) {
                    return ManageType;
                }
            }
            return NONE;
        }
    }
}
