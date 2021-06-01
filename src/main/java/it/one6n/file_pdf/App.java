package it.one6n.file_pdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.fontbox.util.BoundingBox;
//import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.PropertyConfigurator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
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
	    	int index = 0;
	    	int tx = 200;
			int ty = 450;
			String text = "Hello world";
	    	PDPage page = loadedDocument.getPage(index);
	    	PDPageContentStream firstPageContentStream = PdfUtils.getPageContentStream(loadedDocument, page);
	    	PdfUtils.writeText(firstPageContentStream, text, PDType1Font.TIMES_BOLD_ITALIC,tx, ty);
	    	PdfUtils.closePageContentStream(firstPageContentStream);
	    	PdfUtils.savePDF(loadedDocument, loadPath);
	    	PdfUtils.closeDocument(loadedDocument);
			/*
			 * int modifyPageIndex = 3; PDPage modifiedPage =
			 * loadedDocument.getPage(modifyPageIndex);
			 * PdfUtils.closeDocument(loadedDocument);
			 */
	    	
	    	PdfUtils.closeDocument(loadedDocument);
    	}
    }


}
