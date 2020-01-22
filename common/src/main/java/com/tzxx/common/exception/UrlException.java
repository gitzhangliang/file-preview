package com.tzxx.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlException extends BasicException {

    public UrlException(String message) {
        super(message);
    }

}
