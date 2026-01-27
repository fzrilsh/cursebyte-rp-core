package com.cursebyte.plugin.modules.state.authority;

public class AuthorityResult {
    public boolean allowed;
    public String reason;

    public static AuthorityResult allow(){
        return new AuthorityResult(true, "OK");
    }

    public static AuthorityResult deny(String reason){
        return new AuthorityResult(false, reason);
    }

    private AuthorityResult(boolean allowed, String reason){
        this.allowed = allowed;
        this.reason = reason;
    }
}