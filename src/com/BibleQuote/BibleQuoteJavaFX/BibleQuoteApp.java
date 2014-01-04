package com.BibleQuote.BibleQuoteJavaFX;

import com.BibleQuote.bqtj.jvm.CoreContextJvm;
import com.BibleQuote.bqtj.CoreContext;
import com.BibleQuote.bqtj.jvm.utils.LogSysJvm;
import com.BibleQuote.bqtj.utils.Log;
import com.BibleQuote.bqtj.utils.LogTxt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created with IntelliJ IDEA.
 * User: Nikita K.
 * Date: 10.11.13
 * Time: 14:33
 * To change this template use File | Settings | File Templates.
 */


public class BibleQuoteApp extends Application {

	private static final String TAG = "BibleQuoteApp";


	public static void main(String[] args) {
		launch(args);
	}

	public BibleQuoteApp() {
		super();
	}

	// NOTE: This method is NOT called on the JavaFX Application Thread.
	// An application must not construct a Scene or a Stage in this method.
	// An application may construct other JavaFX objects in this method.
	@Override
	public void init() {

	}

	// NOTE: This method is called on the JavaFX Application Thread.
	@Override
	public void start(Stage primaryStage) throws Exception {

		Log.Init(LogSysJvm.getLogSysJvm());

		LogTxt.Init(getCoreContext());

		getCoreContext().Init();


		// Bug: WebView, java.util.logging and Debug Mode
		// https://community.oracle.com/thread/2423650
		// solution:
		// http://stackoverflow.com/questions/18388512/why-is-webview-content-fading-out-disappearing
		// and:
		// https://community.oracle.com/message/11154629
		// and:
		// http://javafx-jira.kenai.com/browse/RT-23846
		// Должно быть после инициализации логера
		java.util.logging.Logger.getLogger(
				com.sun.webpane.sg.prism.WCGraphicsPrismContext.class.getName())
				.setLevel(java.util.logging.Level.OFF);


		primaryStage.setTitle("BibleQuoteJavaFX");
		Pane myPane = (Pane) FXMLLoader.load(getClass()
				.getResource("res/layout/ReaderActivity.fxml"));
		Scene myScene = new Scene(myPane, 400, 500);
		primaryStage.setScene(myScene);
		primaryStage.show();

	}

	@Override
	public void stop() {

	}

	public CoreContext getCoreContext() {
		return CoreContextJvm.getCoreContextJvm();
	}

}
