package com.webwalker.framework.common;

import com.webwalker.framework.interfaces.ICallback2;

/**
 * @author xu.jian
 * 
 */
public class MyCallbacks {
	public static ICallback2<Void, Object> smsCallback = new ICallback2<Void, Object>() {

		@Override
		public Void action(Object data) {
			return null;
		}

	};
}
