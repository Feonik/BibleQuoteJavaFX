package com.BibleQuote.BibleQuoteJavaFX.ui;

import com.BibleQuote.bqtj.jvm.CoreContextJvm;
import com.BibleQuote.bqtj.jvm.utils.LogSysJvm;
import com.BibleQuote.bqtj.utils.Log;
import com.BibleQuote.bqtj.utils.LogTxt;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class ReaderActivityController implements Initializable {

	@FXML
	private Pane leftPart;
	@FXML
	private WebView webView;


	@Override
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		SplitPane.setResizableWithParent(leftPart, Boolean.FALSE);
	}


	@FXML
	protected void handleSubmitButtonAction(MouseEvent event) {
		final WebEngine eng = webView.getEngine();
		eng.load("http://jesuschrist.ru/bible/?b=19&c=118");
	}

}


