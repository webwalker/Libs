package com.webwalker.framework.system.telephone;

import android.content.Context;
import android.media.AudioManager;

/**
 * 
 * @author xu.jian
 * 
 *         android.permission.MODIFY_AUDIO_SETTINGS
 * 
 */
public class TelephoneMgr {

	private static int currentVolume = 0;
	private static boolean haSilence = false;

	/**
	 * 扬声器免提
	 * 
	 * @param context
	 */
	public static void speaker(Context context) {
		AudioManager audioManager = getAudioManager(context);
		if (audioManager.isSpeakerphoneOn())
			return;
		audioManager.setSpeakerphoneOn(true);
		if (currentVolume == 0)
			currentVolume = audioManager
					.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
		// currentVolume
		// =audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
		audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
				currentVolume, AudioManager.STREAM_VOICE_CALL);
	}

	/**
	 * 静音
	 * 
	 * @param context
	 */
	public static void speakerOff(Context context) {
		AudioManager audioManager = getAudioManager(context);
		if (!audioManager.isSpeakerphoneOn())
			return;
		currentVolume = audioManager
				.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
		audioManager.setSpeakerphoneOn(false);
		// audioManager.setMicrophoneMute(!audioManager.isSpeakerphoneOn());
		audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 0,
				AudioManager.STREAM_VOICE_CALL);
	}

	public static void silence(Context context) {
		AudioManager audioManager = getAudioManager(context);
		if (haSilence)
			audioManager.setStreamMute(AudioManager.MODE_IN_CALL, false);
		else
			audioManager.setStreamMute(AudioManager.MODE_IN_CALL, true);
	}

	private static AudioManager getAudioManager(Context context) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setMode(AudioManager.MODE_IN_CALL);
		// audioManager.setMode(AudioManager.ROUTE_SPEAKER);
		return audioManager;
	}

}
