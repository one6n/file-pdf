package it.one6n.file_pdf;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PdfUtils {
	
	//private static final Logger log = LoggerFactory.getLogger(PdfUtils.class);

    public static void savePDF(PDDocument document, String absolutePath) {
    	if(document != null && absolutePath != null) {
	    	try {
	    		document.save(absolutePath);
	    		log.info("Saved document={} in path={}", document, absolutePath);
	    	}catch(IOException e) {
	    		log.error("Error in save pdf={}, path={}",document.toString(), absolutePath);
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
	
	public static PDDocument createAndSavePdfWithBlankPages(final int NUMBER_OF_PAGE, String absolutePath) {
		PDDocument document = new PDDocument();
		if(document != null) {
	    	for(int i = 0; i < NUMBER_OF_PAGE; i++)
	    		document.addPage(new PDPage());
	    	savePDF(document, absolutePath);
		}
    	return document;
	}	
    
	public static List<PDDocument> splitDocument(PDDocument document, Integer delimiter) {
		List<PDDocument> splittedDocuments = null;
		if(document != null && delimiter <= document.getNumberOfPages()) {
			Splitter splitter = new Splitter();
			List<PDDocument> pages = null;
			try {
				pages = splitter.split(document);
			} catch (IOException e) {
				log.error("Error in splitting document={}", document.toString());
			}
			Iterator<PDDocument> pagesIterator = pages.iterator();
			splittedDocuments = Arrays.asList(new PDDocument [] {new PDDocument(), new PDDocument()});
			int counter = 0;
			while(pagesIterator.hasNext() && counter < delimiter) {
				splittedDocuments.get(0).addPage(pagesIterator.next().getPage(0));
				counter++;
			}
			while(pagesIterator.hasNext())
				splittedDocuments.get(1).addPage(pagesIterator.next().getPage(0));
		}
		return splittedDocuments;
	}
    
	public static void writeText(PDDocument document, int pageIndex, String text, PDType1Font font, int tx, int ty) {
		if(document != null && pageIndex < document.getNumberOfPages()) {
			PDPageContentStream pageContentStream = getPageContentStream(document, document.getPage(pageIndex));
			if(pageContentStream != null && text != null) {
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
		if(document != null && page != null) {
			try {
				contentStream = new PDPageContentStream(document, page);
			} catch (IOException e) {
				log.error("Error in creating contentStrem for doc={} e page={}", document, page);
			}
		}
		return contentStream;
	}
	
	private static void closePageContentStream(PDPageContentStream contentStream) {
		if(contentStream != null) {
			try {
				contentStream.close();
			} catch (IOException e) {
				log.error("Error in closing contentStrem");
			}
		}
	}
}
