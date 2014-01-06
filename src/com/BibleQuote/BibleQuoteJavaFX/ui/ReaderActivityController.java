package com.BibleQuote.BibleQuoteJavaFX.ui;

import com.BibleQuote.bqtj.CoreContext;
import com.BibleQuote.bqtj.entity.BibleReference;
import com.BibleQuote.bqtj.exceptions.BookNotFoundException;
import com.BibleQuote.bqtj.exceptions.OpenModuleException;
import com.BibleQuote.bqtj.managers.Librarian;
import com.BibleQuote.bqtj.utils.LogTxt;
import com.BibleQuote.bqtj.utils.PreferenceHelper;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;

public class ReaderActivityController implements Initializable {

	private static final String TAG = "ReaderActivityController";

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

		myLibrarian = CoreContext.getCoreContext().getLibrarian();

		BibleReference osisLink = new BibleReference(PreferenceHelper.restoreStateString("last_read"));

		if (!myLibrarian.isOSISLinkValid(osisLink)) {
//			onChooseChapterClick();

			osisLink = new BibleReference(
//					"ds:fs;id:/mnt/sdcard/BibleQuote/modules/Bible_Rus_RstStrong_2012_fix/bibleqt.ini;m:RST+FIX;b:Ruth;ch:1;v:1"
					"ds:fs;id:E:\\Dev\\projects\\Java\\BibleQuoteJava" +
							"\\BibleQuoteJavaFX\\out\\production\\Data\\modules" +
							"\\Bible_Deu_SCH2000NEU_fix\\bibleqt.ini;" +
							"m:RST+FIX;b:Ps;ch:116;v:1"
//					"E:\\Dev\\projects\\Java\\BibleQuoteJava\\BibleQuoteJavaFX\\out\\production\\Data\\modules\\Bible_Deu_SCH2000NEU_fix\\bibleqt.ini"
			);

			openChapterFromLink(osisLink);

		} else {
			openChapterFromLink(osisLink);
		}


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
		setText(myLibrarian.getBaseUrl(), chapterInHTML, OSISLink.getFromVerse(), nightMode, myLibrarian.isBible());

		PreferenceHelper.saveStateString("last_read", OSISLink.getExtendedPath());

	}

	public void setText(String baseUrl, String text, int currVerse, Boolean nightMode, Boolean isBible) {
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
//		html.append(getStyle(nightMode));
		html.append("</head>\r\n");
		html.append("<body").append(currVerse > 1 ? (" onLoad=\"document.location.href='#verse_" + currVerse + "';\"") : "").append(">\r\n");
		html.append(text);
		html.append("</body>\r\n");
		html.append("</html>");

//		loadDataWithBaseURL("file://" + baseUrl, html.toString(), "text/html", "UTF-8", "about:config");
//		jsInterface.clearSelectedVerse();

		ReaderWebView.getEngine().loadContent(html.toString());
	}

	@FXML
	protected void onMouseClicked(MouseEvent event) {

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


