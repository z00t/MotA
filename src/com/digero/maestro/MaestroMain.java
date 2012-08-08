package com.digero.maestro;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.digero.common.util.Version;
import com.digero.maestro.view.ProjectFrame;

public class MaestroMain {
	public static final String APP_NAME = "LOTRO Maestro";
	public static final String APP_URL = "http://lotro.acasylum.com/maestro/";
	public static final Version APP_VERSION = new Version(0, 2, 1);

	private static ProjectFrame mainWindow = null;

	public static void main(String[] args) throws Exception {
		System.setProperty("sun.sound.useNewAudioEngine", "true");

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {}

		mainWindow = new ProjectFrame();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
		openSongFromCommandLine(args);
		try {
			ready();
		}
		catch (UnsatisfiedLinkError err) {
			// Ignore (we weren't started via WinRun4J)
		}
	}

	/** Tells the WinRun4J launcher that we're ready to accept activate() calls. */
	public static native void ready();

	/** A new activation from WinRun4J (a.k.a. a file was opened) */
	public static void activate(String[] args) {
		openSongFromCommandLine(args);
	}

	public static void execute(String cmdLine) {
		openSongFromCommandLine(new String[] {
			cmdLine
		});
	}

	private static void openSongFromCommandLine(String[] args) {
		if (mainWindow == null)
			return;

		mainWindow.setExtendedState(mainWindow.getExtendedState() & ~JFrame.ICONIFIED);

		if (args.length > 0) {
			File file = new File(args[0]);
			if (file.exists())
				mainWindow.openSong(file);
		}
	}

	/** @deprecated Use isNativeVolumeSupported() instead. */
	public static native boolean isVolumeSupported();

	public static boolean isNativeVolumeSupported() {
		try {
			return isVolumeSupported();
		}
		catch (UnsatisfiedLinkError err) {
			return false;
		}
	}

	public static native float getVolume();

	public static native void setVolume(float volume);

	public static void onVolumeChanged() {
		if (mainWindow != null)
			mainWindow.onVolumeChanged();
	}
}
