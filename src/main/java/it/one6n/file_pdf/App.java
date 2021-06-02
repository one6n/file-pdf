package it.one6n.file_pdf;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
//import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.PropertyConfigurator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class App 
{	
	//in resources the logger get automatically the configuration file.
	//private static final String LOGGER_PROPERTIES_FILE = System.getProperty("user.dir") + "/src/main/resources/log4j.properties";
	
	private static final Logger log = LoggerFactory.getLogger(App.class);
	
    public static void main( String[] args )
    {	
    	//LoggerInfo
    	//BasicConfigurator.configure(); // for basic configuration
    	//PropertyConfigurator.configure(LOGGER_PROPERTIES_FILE); for specifying the properties file
    	String fileName = "MyPdf.pdf";
    	int numberOfPages = 5;
    	
    	PDDocument document = PdfUtils.createAndSavePdfWithBlankPages(numberOfPages,fileName);
    	PdfUtils.closeDocument(document);
    	
    	String loadPath = "MyPdf.pdf";
    	PDDocument loadedDocument = PdfUtils.loadPDF(loadPath);
    	if( loadedDocument != null) {
	    	int pageIndex = 0;
	    	int tx = 200;
			int ty = 450;
			String text = "Hello world";
	    	PdfUtils.writeText(loadedDocument, pageIndex, text, PDType1Font.TIMES_BOLD_ITALIC,tx, ty);
	    	PdfUtils.writeText(loadedDocument, 2, "Hello World 2", PDType1Font.TIMES_BOLD_ITALIC,tx, ty);
	    	PdfUtils.savePDF(loadedDocument, loadPath);
	    	
	    	Integer delimiter = 2;
	    	Splitter splitter = new Splitter();
	    	String folderPath = "";
	    	try {
				List<PDDocument> pages = splitter.split(loadedDocument);
				Iterator<PDDocument> pagesIterator = pages.iterator();
				PDDocument [] splittedDocuments = new PDDocument [] {new PDDocument(), new PDDocument()};
 				int counter = 0;
				while(pagesIterator.hasNext() && counter < delimiter) {
					splittedDocuments[0].addPage(pagesIterator.next().getPage(0));
					counter++;
				}
				while(pagesIterator.hasNext())
					splittedDocuments[1].addPage(pagesIterator.next().getPage(0));
				for(int i = 0; i < splittedDocuments.length; i++)
					PdfUtils.savePDF(splittedDocuments[i], folderPath + "splitted_" + (i+ 1) + ".pdf");
			} catch (IOException e) {
				log.error("Error in splitting document={}", loadedDocument.toString());
			}
	    	
			/*
			 * int modifyPageIndex = 3; PDPage modifiedPage =
			 * loadedDocument.getPage(modifyPageIndex);
			 * PdfUtils.closeDocument(loadedDocument);
			 */
	    	
	    	PdfUtils.closeDocument(loadedDocument);
    	}
    }


}
