package com.kdkj.koudailicai.lib.http;

public abstract class AbstractService {

	public static abstract class Response {

		protected Response() {

		}

	}

	public abstract Response execute() throws Exception;
}
