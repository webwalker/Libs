package com.webwalker.framework.beans;

import java.util.HashMap;
import java.util.Map;

import com.webwalker.framework.utils.ValidatorUtil;

/**
 * @author xu.jian
 * 
 */
public class KeyLetters {
	private static final Map<String, Integer> keyLetters = new HashMap<String, Integer>();

	static {
		keyLetters.put("A", 2);
		keyLetters.put("B", 2);
		keyLetters.put("C", 2);
		keyLetters.put("D", 3);
		keyLetters.put("E", 3);
		keyLetters.put("F", 3);
		keyLetters.put("G", 4);
		keyLetters.put("H", 4);
		keyLetters.put("I", 4);
		keyLetters.put("J", 5);
		keyLetters.put("K", 5);
		keyLetters.put("L", 5);
		keyLetters.put("M", 6);
		keyLetters.put("N", 6);
		keyLetters.put("O", 6);
		keyLetters.put("P", 7);
		keyLetters.put("Q", 7);
		keyLetters.put("R", 7);
		keyLetters.put("S", 7);
		keyLetters.put("T", 8);
		keyLetters.put("U", 8);
		keyLetters.put("V", 8);
		keyLetters.put("W", 9);
		keyLetters.put("X", 9);
		keyLetters.put("Y", 9);
		keyLetters.put("Z", 9);
	}

	public static int getLetterNumber(String letter) {
		return keyLetters.get(letter).intValue();
	}

	public static String getLetterNumbers(String letters) {
		char[] letter = letters.toCharArray();
		StringBuilder sb = new StringBuilder();
		String letterStr;
		for (char c : letter) {
			letterStr = String.valueOf(c);
			if (ValidatorUtil.isLetter(letterStr))
				sb.append(getLetterNumber(letterStr));
			else
				sb.append(letterStr);
		}
		return sb.toString();
	}
}
