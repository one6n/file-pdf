package it.one6n.file_pdf;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfUtils {
	
	private static final Logger log = LoggerFactory.getLogger(PdfUtils.class);
	
	public static PDDocument createAndSavePdfWithBlankPages(final int NUMBER_OF_PAGE, String absolutePath) {
		PDDocument document = new PDDocument();
		if(document != null) {
	    	for(int i = 0; i < NUMBER_OF_PAGE; i++)
	    		document.addPage(new PDPage());
	    	savePDF(document, absolutePath);
		}
    	return document;
	}	
	
	public static void closeDocument(PDDocument document) {
		if(document != null) {
			try {
				document.close();
				log.info("Closed document={}", document.toString());
				}
			catch (IOException e) {
				log.error("Error in closing document={}", document.toString());
			}
		}
	}
    
    public static PDDocument loadPDF(String absolutePath) {
    	PDDocument loadedPDF = null;
    	if(absolutePath != null) {
	    	File file = new File(absolutePath);
	    	
	    	try {
	    		loadedPDF = PDDocument.load(file);
	    		log.info("Loaded document={}", loadedPDF.toString());
	    	}
	    	catch(IOException e) {log.error("Error during the load of file={}", absolutePath);}
    	}
    	return loadedPDF;
    }

    public static void savePDF(PDDocument document, String absolutePath) {
    	if(document != null && absolutePath != null) {
	    	try {
	    		document.save(absolutePath);
	    		log.info("Saved document={} in path={}", document, absolutePath);
	    	}catch(IOException e) {
	    		log.error("Error in save pdf={}",document.toString());
	    	}
    	}
    }
    
    public static PDDocument loadPDF(File file) {
    	PDDocument loadedPDF = null;
	    if(file != null) {
	    	try {
	    		loadedPDF = PDDocument.load(file);
	    		log.info("Loaded document={}", loadedPDF.toString());
	    	}
	    	catch(IOException e) {log.error("Error during the load of file={}", file);}
	    }
    	return loadedPDF;
    }
    
	public static PDPageContentStream getPageContentStream(PDDocument document, PDPage page) {
		PDPageContentStream contentStream = null;
		if(document != null && page != null) {
			try {
				contentStream = new PDPageContentStream(document, page);
			} catch (IOException e) {
				log.error("Error in creating contentStrem for doc={} e page={}", document, page);
			}
		}
		return contentStream;
	}
	
	public static void closePageContentStream(PDPageContentStream contentStream) {
		if(contentStream != null) {
			try {
				contentStream.close();
			} catch (IOException e) {
				log.error("Error in closing contentStrem");
			}
		}
	}
	public static void writeText(PDPageContentStream pageContentStream, String text, PDType1Font font, int tx, int ty) {
		if(pageContentStream != null && text != null) {
			try {
				pageContentStream.beginText();
				pageContentStream.newLineAtOffset(tx, ty);
				pageContentStream.setFont(font, 40);
				pageContentStream.showText(text);
				pageContentStream.endText();
			} catch (IOException e) {
				log.error("Error in writing in pdf");
			}
		}
	}
}
