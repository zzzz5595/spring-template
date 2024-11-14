package com.app.global.error;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Slf4j
public class FeignClientExceptionErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    /**
     * FeignClientExceptionErrorDecoder는 Feign 클라이언트가 API 호출 중에 발생한 예외를 어떻게 처리할지 결정하는 커스터마이즈된 오류 처리 클래스
     * @param methodKey
     * @param response
     * @return
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        // 요청이 실패했을 때 로그 기록
        log.error("Feign 요청 실패 - methodKey: {}, status: {}, reason: {}", methodKey, response.status(), response.reason());

        FeignException exception = FeignException.errorStatus(methodKey, response);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());


        // 서버 오류 (HTTP 5xx) 발생 시, 자동으로 재시도하도록 RetryableException 발생
        if(httpStatus.is5xxServerError()) {
            return new RetryableException(
                    response.status(),
                    exception.getMessage(),
                    response.request().httpMethod(),
                    exception,
                    (Long) null,
                    response.request()
            );
        }

        // 기본 예외 처리로 위임
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
