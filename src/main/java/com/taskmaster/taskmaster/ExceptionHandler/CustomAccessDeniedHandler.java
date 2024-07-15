package com.taskmaster.taskmaster.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmaster.taskmaster.model.response.WebResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
@AllArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        WebResponse<String> webResponse = WebResponse.<String>builder()
            .code(HttpStatus.FORBIDDEN.value())
            .message("Access Denied")
            .errors("You do not have permission to access this resource")
            .build();

        OutputStream out = response.getOutputStream();
        objectMapper.writeValue(out, webResponse);
        out.flush();
    }

}
