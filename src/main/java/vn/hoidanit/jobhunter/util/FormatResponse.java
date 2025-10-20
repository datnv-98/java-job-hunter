package vn.hoidanit.jobhunter.util;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.hoidanit.jobhunter.domain.response.RestResponse;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@ControllerAdvice
public class FormatResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@SuppressWarnings("null") MethodParameter returnType,
            @SuppressWarnings({ "null", "rawtypes" }) Class converterType) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    @Nullable
    public Object beforeBodyWrite(@Nullable Object body, @SuppressWarnings("null") MethodParameter returnType,
            @SuppressWarnings("null") MediaType selectedContentType,
            @SuppressWarnings({ "null", "rawtypes" }) Class selectedConverterType,
            @SuppressWarnings("null") ServerHttpRequest request,
            @SuppressWarnings("null") ServerHttpResponse response) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(status);

        if (body instanceof String || body instanceof Resource) {
            return body;
        }
        String path = request.getURI().getPath();
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            return body;
        }
        // case error
        if (status >= 400) {

            return body;

        } else {
            res.setData(body);
            ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class);
            if (apiMessage != null) {
                res.setMessage(apiMessage.value());
            } else {
                res.setMessage("CALL API SUCCESS");
            }
        }

        // case success
        return res;
    }

}
