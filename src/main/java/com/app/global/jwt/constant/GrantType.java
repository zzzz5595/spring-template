package com.app.global.jwt.constant;

import lombok.Getter;

@Getter
public enum GrantType {

    //Bearer Token은 서버로부터 발급받은 토큰을 소지한 사람이 리소스에 접근할 수 있는 권한을 갖게 되는 방식
    BEARER("Bearer");

    GrantType(String type) {
        this.type = type;
    }

    private String type;

}
