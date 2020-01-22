package com.tzxx.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author tzxx
 */
@Getter
@Setter
class BasicException extends RuntimeException {
	BasicException(String message) {
		super(message);
	}
}
