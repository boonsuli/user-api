package com.trailerplan.common;

import com.trailerplan.model.dto.AbstractDTO;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class ControllerTestFactory {

    public MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("UTF-8"));


    public MockHttpServletRequestBuilder getPostServletRequest(AbstractDTO dto, String uri) throws IOException {
        MockHttpServletRequestBuilder servletRequest = post(uri).content(dto.toJson())
                .contentType(APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_VALUE);
        return servletRequest;
    }

    public MockHttpServletRequestBuilder getPutServletRequest(AbstractDTO dto, String uri) throws IOException {
        MockHttpServletRequestBuilder servletRequest = put(uri).content(dto.toJson())
                .contentType(APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_VALUE);
        return servletRequest;
    }

    public MockHttpServletRequestBuilder getDeleteServletRequest(long id, String uri) throws IOException {
        MockHttpServletRequestBuilder servletRequest = delete(uri+"/{id}", id)
                .contentType(APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_VALUE);
        return servletRequest;
    }

    public MockHttpServletRequestBuilder getGetServletRequest(long id, String uri) throws IOException {
        MockHttpServletRequestBuilder servletRequest = get(uri+"/{id}", id)
            .contentType(APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_VALUE);
        return servletRequest;
    }

    public MockHttpServletRequestBuilder getGetServletRequest(String uri) throws IOException {
        MockHttpServletRequestBuilder servletRequest = get(uri)
                .contentType(APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_VALUE);
        return servletRequest;
    }
}
