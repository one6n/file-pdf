package it.one6n.pdfutils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PdfUtils {

	// private static final Logger log = LoggerFactory.getLogger(PdfUtils.class);

	public static void savePDF(PDDocument document, String absolutePath) {
		if (document != null && absolutePath != null && absolutePath.length() > 0 && absolutePath != "") {
			try {
				document.save(absolutePath);
				log.info("Saved document={} in path={}", document, absolutePath);
			} catch (IOException e) {
				log.error("Error in save pdf={}, path={}", document.toString(), absolutePath);
			}
		}
	}

	public static PDDocument loadPDF(String absolutePath) {
		PDDocument loadedPDF = null;
		if (absolutePath != null) {
			File file = new File(absolutePath);
			try {
				loadedPDF = PDDocument.load(file);
				log.info("Loaded document={}", loadedPDF.toString());
			} catch (IOException e) {
				log.error("Error during the load of file={}", absolutePath);
			}
		}
		return loadedPDF;
	}

	public static PDDocument loadPDF(File file) {
		PDDocument loadedPDF = null;
		if (file != null) {
			try {
				loadedPDF = PDDocument.load(file);
				log.info("Loaded document={}", loadedPDF.toString());
			} catch (IOException e) {
				log.error("Error during the load of file={}", file);
			}
		}
		return loadedPDF;
	}

	public static void closeDocument(PDDocument document) {
		if (document != null) {
			try {
				document.close();
				log.info("Closed document={}", document.toString());
			} catch (IOException e) {
				log.error("Error in closing document={}", document.toString());
			}
		}
	}

	public static PDDocument createPdfWithBlankPages(final int NUMBER_OF_PAGE) {
		PDDocument document = new PDDocument();
		if (document != null) {
			for (int i = 0; i < NUMBER_OF_PAGE; i++)
				document.addPage(new PDPage());
		}
		return document;
	}

	public static PDDocument createAndSavePdfWithBlankPages(final int NUMBER_OF_PAGE, String absolutePath) {
		PDDocument document = null;
		if (absolutePath != null && absolutePath.length() > 0 && absolutePath != "") {
			document = new PDDocument();
			if (document != null) {
				for (int i = 0; i < NUMBER_OF_PAGE; i++)
					document.addPage(new PDPage());
				savePDF(document, absolutePath);
			}
		}
		return document;
	}

	public static int getNumberOfPages(byte[] barr) throws IOException {
		int numberOfPages = 0;
		try (PDDocument doc = PDDocument.load(barr)) {
			numberOfPages = getNumberOfPages(doc);
		}
		return numberOfPages;
	}

	public static int getNumberOfPages(PDDocument document) throws IOException {
		return document.getNumberOfPages();
	}

	public static List<PDDocument> splitDocument(PDDocument document, int index) throws IOException {
		List<PDDocument> splitted = null;
		if (document != null && index > 0 && getNumberOfPages(document) > index) {
			Iterator<PDPage> iterator = document.getPages().iterator();
			int i = 0;
			splitted = new ArrayList<>();
			PDDocument doc1 = new PDDocument();
			while (i < index && iterator.hasNext()) {
				doc1.addPage(iterator.next());
				++i;
			}
			splitted.add(doc1);
			PDDocument doc2 = new PDDocument();
			while (iterator.hasNext()) {
				doc2.addPage(iterator.next());
				++i;
			}
			splitted.add(doc2);
		}
		return splitted;
	}

	public static void writeText(PDDocument document, int pageIndex, String text, PDType1Font font, int tx, int ty) {
		if (document != null && pageIndex < document.getNumberOfPages()) {
			PDPageContentStream pageContentStream = getPageContentStream(document, document.getPage(pageIndex));
			if (pageContentStream != null && text != null) {
				try {
					pageContentStream.beginText();
					pageContentStream.newLineAtOffset(tx, ty);
					pageContentStream.setFont(font, 40);
					pageContentStream.showText(text);
					pageContentStream.endText();

				} catch (IOException e) {
					log.error("Error in writing in pdf");
				} finally {
					closePageContentStream(pageContentStream);
				}
			}
		}
	}

	private static PDPageContentStream getPageContentStream(PDDocument document, PDPage page) {
		PDPageContentStream contentStream = null;
		if (document != null && page != null) {
			try {
				contentStream = new PDPageContentStream(document, page);
			} catch (IOException e) {
				log.error("Error in creating contentStrem for doc={} e page={}", document, page);
			}
		}
		return contentStream;
	}

	private static void closePageContentStream(PDPageContentStream contentStream) {
		if (contentStream != null) {
			try {
				contentStream.close();
			} catch (IOException e) {
				log.error("Error in closing contentStrem");
			}
		}
	}
}
