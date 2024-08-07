package com.taskmaster.taskmaster.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingWebResponse<T> {

    private int code;

    private String message;

    private T data;

    private String errors;

    private PagingResponse paging;

}
