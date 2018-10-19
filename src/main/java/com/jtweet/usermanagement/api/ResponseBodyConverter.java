package com.jtweet.usermanagement.api;

import java.util.HashMap;
import java.util.Map;

public class ResponseBodyConverter {
	public static Map<String, String> convert(ResponseBody responseBody) {
		Map<String, String> HashMapResponse = new HashMap<>();
		HashMapResponse.put("success", String.valueOf(responseBody.getSuccess()));
		HashMapResponse.put("message", responseBody.getMessage());
		return HashMapResponse;
	}
}
