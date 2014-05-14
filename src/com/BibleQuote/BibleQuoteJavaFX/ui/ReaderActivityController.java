package com.BibleQuote.BibleQuoteJavaFX.ui;

import com.BibleQuote.bqtj.CoreContext;
import com.BibleQuote.bqtj.entity.BibleReference;
import com.BibleQuote.bqtj.exceptions.BookNotFoundException;
import com.BibleQuote.bqtj.exceptions.OpenModuleException;
import com.BibleQuote.bqtj.managers.Librarian;
import com.BibleQuote.bqtj.utils.DataConstants;
import com.BibleQuote.bqtj.utils.LogTxt;
import com.BibleQuote.bqtj.utils.PreferenceHelper;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebView;

import java.io.File;

public class ReaderActivityController implements Initializable {

	private static final String TAG = "ReaderActivityController";

	@FXML
	public Button BtnChapterPrev;
	@FXML
	public Button BtnChapterNext;

	@FXML
	private Pane leftPart;
	@FXML
	private WebView ReaderWebView;

	private Librarian myLibrarian;
	private String chapterInHTML = "";
	private boolean nightMode = false;


	@Override
	public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
		SplitPane.setResizableWithParent(leftPart, Boolean.FALSE);
//		ReaderWebView.setFontSmoothingType(FontSmoothingType.LCD);
//		ReaderWebView.setFontScale(1);

		// TODO перенести кэш WebView из %TEMP% в Data/cache
		// From API doc:
		// Currently, the directory specified by this property is used only to
		// store the data that backs the window.localStorage objects. In the
		// future, more types of data can be added.
//		ReaderWebView.getEngine().setUserDataDirectory(new File(DataConstants.CACHE_PATH));

		myLibrarian = CoreContext.getCoreContext().getLibrarian();

//		BibleReference osisLink = new BibleReference(
//				PreferenceHelper.restoreStateString("last_read"));

//		if (!myLibrarian.isOSISLinkValid(osisLink)) {
//			onChooseChapterClick();

			BibleReference osisLink = new BibleReference(
//					"ds:fs;id:/mnt/sdcard/BibleQuote/modules/Bible_Rus_RstStrong_2012_fix/bibleqt.ini;m:RST+FIX;b:Ruth;ch:1;v:1"
					"ds:fs;id:E:\\Dev\\projects\\Java\\BibleQuoteJava" +
							"\\BibleQuoteJavaFX\\out\\production\\Data\\modules" +
							"\\NT_Gre_Textus_Receptus_variantes\\bibleqt.ini;" +
//							"m:RST+FIX;b:Ps;ch:116;v:1"
							"m:TEXTUS-VARS;b:3John;ch:1;v:1"
//					"E:\\Dev\\projects\\Java\\BibleQuoteJava\\BibleQuoteJavaFX\\out\\production\\Data\\modules\\Bible_Deu_SCH2000NEU_fix\\bibleqt.ini"
			);

			openChapterFromLink(osisLink);

//		} else {
//			openChapterFromLink(osisLink);
//		}


//		ReaderWebView.getEngine().load("http://jesuschrist.ru/bible/?b=19&c=118");
	}


	private void openChapterFromLink(BibleReference osisLink) {

		try {
			myLibrarian.openChapter(osisLink, false);
			chapterInHTML = myLibrarian.getParChapterHTMLView();
			setTextInWebView();
		} catch (BookNotFoundException e) {
			e.printStackTrace();
		} catch (OpenModuleException e) {
			e.printStackTrace();
		}

	}

	private void setTextInWebView() {
		BibleReference OSISLink = myLibrarian.getCurrentOSISLink();
		setText(myLibrarian.getBaseUrl(), chapterInHTML, OSISLink.getFromVerse(),
				nightMode, myLibrarian.isBible());

		PreferenceHelper.saveStateString("last_read", OSISLink.getExtendedPath());

	}


	public void setText(String baseUrl, String text, int currVerse,
							  Boolean nightMode, Boolean isBible) {
//		mPageLoaded = false;
		String modStyle = isBible ? "bible_style.css" : "book_style.css";

		StringBuilder html = new StringBuilder();
		html.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n");
		html.append("<html>\r\n");
		html.append("<head>\r\n");
		html.append("<meta http-equiv=Content-Type content=\"text/html; charset=UTF-8\">\r\n");
//		html.append("<script language=\"JavaScript\" src=\"file:///android_asset/reader.js\" type=\"text/javascript\"></script>\r\n");
//		html.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/reader.css\">\r\n");
//		html.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/").append(modStyle).append("\">\r\n");
		html.append(getStyle(nightMode));
		html.append("</head>\r\n");
		html.append("<body").append(currVerse > 1 ? (" onLoad=\"document.location.href='#verse_" + currVerse + "';\"") : "").append(">\r\n");
		html.append(text);
		html.append("</body>\r\n");
		html.append("</html>");

//		loadDataWithBaseURL("file://" + baseUrl, html.toString(), "text/html", "UTF-8", "about:config");
//		jsInterface.clearSelectedVerse();

//		ReaderWebView.getEngine().loadContent(html.toString());
		ReaderWebView.getEngine().load("file:///E:/Dev/projects/Java/BibleQuoteJava/BibleQuoteJavaFX/out/production/Data/modules/NT_Gre_Textus_Receptus_variantes/64.htm");
//		ReaderWebView.getEngine().loadContentWithBaseURLMy(html.toString(), "text/html", "file:///E:/Dev/projects/Java/BibleQuoteJava/BibleQuoteJavaFX/out/production/Data/modules/NT_Gre_Textus_Receptus_variantes/");
	}


	private String getStyle(Boolean nightMode) {
		String textColor;
		String backColor;
		String selTextColor;
		String selTextBack;

//		getSettings().setStandardFontFamily(PreferenceHelper.getFontFamily());

		if (!nightMode) {
			backColor = PreferenceHelper.getTextBackground();
			textColor = PreferenceHelper.getTextColor();
//			selTextColor = "#FEF8C4";
			selTextColor = PreferenceHelper.getTextColorSelected();
			selTextBack = PreferenceHelper.getTextBackgroundSelected();

		} else {
			textColor = "#EEEEEE";
			backColor = "#000000";
			selTextColor = "#EEEEEE";
			selTextBack = "#562000";
		}
		String textSize = PreferenceHelper.getTextSize();

		StringBuilder style = new StringBuilder();
		style.append("<style type=\"text/css\">\r\n");
		style.append("body {\r\n");
		style.append("padding-bottom: 50px;\r\n");
		if (PreferenceHelper.textAlignJustify()) {
			style.append("text-align: justify;\r\n");
		}
		//style.append("font-family: Georgia, Tahoma, Verdana, sans-serif;\r\n");
		style.append("color: ").append(textColor).append(";\r\n");
		style.append("font-size: ").append(textSize).append("pt;\r\n");
		style.append("line-height: 1.25;\r\n");
		style.append("background: ").append(backColor).append(";\r\n");
		style.append("}\r\n");
		style.append(".verse {\r\n");
		style.append("background: ").append(backColor).append(";\r\n");
		style.append("}\r\n");
		style.append(".selectedVerse {\r\n");
		style.append("color: ").append(selTextColor).append(";\r\n");
		style.append("background: ").append(selTextBack).append(";\r\n");
		style.append("}\r\n");
		style.append("img {\r\n");
		style.append("max-width: 100%;\r\n");
		style.append("}\r\n");
		style.append(myLibrarian.getCurrModule().getStyleCss());
		style.append("\r\n");
		style.append("</style>\r\n");

		return style.toString();
	}


	@FXML
	private void onClickChapterPrev(Event event) {
		prevChapter();
	}

	@FXML
	private void onClickChapterNext(Event event) {
		nextChapter();
	}

	public void prevChapter() {
		try {
			myLibrarian.prevChapter();
		} catch (OpenModuleException e) {
			LogTxt.e(TAG, "prevChapter()", e);
		}
		viewCurrentChapter();
	}

	public void nextChapter() {
		try {
			myLibrarian.nextChapter();
		} catch (OpenModuleException e) {
			LogTxt.e(TAG, "nextChapter()", e);
		}
		viewCurrentChapter();
	}

	private void viewCurrentChapter() {
		openChapterFromLink(myLibrarian.getCurrentOSISLink());
	}

}
