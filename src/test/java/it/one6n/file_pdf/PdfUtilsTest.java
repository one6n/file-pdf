package it.one6n.file_pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;

public class PdfUtilsTest {

	@Test
	public void testLoadDocumentFile() {
		File file = null;
		assertNull(PdfUtils.loadPDF(file));
		String path = "src" + File.separator + "test" +
				File.separator + "resources" + File.separator + "file.pdf";
		file = new File(path);
		PDDocument document = new PDDocument();
		String title = "title";
		document.getDocumentInformation().setTitle(title);
		PdfUtils.savePDF(document, path);
		PDDocument loadedDocument = PdfUtils.loadPDF(file);
		assertNotNull(loadedDocument);
		assertEquals(document.getDocumentInformation().getTitle(), loadedDocument.getDocumentInformation().getTitle());
		PdfUtils.closeDocument(document);
		PdfUtils.closeDocument(loadedDocument);
		if(file.exists())
			file.delete();
	}
	
	@Test
	public void testLoadDocumentPath() {
		String path = null;
		assertNull(PdfUtils.loadPDF(path));
		path = "src" + File.separator + "test" +
				File.separator + "resources" + File.separator + "file.pdf";
		PDDocument document = new PDDocument();
		String title = "title";
		document.getDocumentInformation().setTitle(title);
		PdfUtils.savePDF(document, path);
		PDDocument loadedDocument = PdfUtils.loadPDF(path);
		assertNotNull(loadedDocument);
		assertEquals(document.getDocumentInformation().getTitle(), loadedDocument.getDocumentInformation().getTitle());
		PdfUtils.closeDocument(document);
		PdfUtils.closeDocument(loadedDocument);
		File file = new File(path);
		if(file.exists())
			file.delete();
	}
	
	@Test
	public void testSavePDF() {
		String fileName = "src" + File.separator + "test" +
				File.separator + "resources" + File.separator + "file.pdf";
		File file =  new File(fileName);
		assertFalse(file.exists());
		PDDocument document = new PDDocument();
		PdfUtils.savePDF(document, fileName);
		assertTrue(file.exists());
		if(file.exists())
			file.delete();
	}
	@Test
	public void testSplitDocument() {
		Integer numberOfPages = 5;
		String fileName = "src" + File.separator + "test" +
				File.separator + "resources" + File.separator + "file.pdf";
		PDDocument document = PdfUtils.createAndSavePdfWithBlankPages(numberOfPages, fileName);
		List<PDDocument> splittedDocuments = null;
		assertNull(splittedDocuments);
		Integer delimiter = 3;
		splittedDocuments = PdfUtils.splitDocument(document, delimiter);
		assertNotNull(splittedDocuments);
		assertEquals(splittedDocuments.size(), 2);
		assertEquals(splittedDocuments.get(0).getNumberOfPages(), 3);
		assertEquals(splittedDocuments.get(1).getNumberOfPages(), 2);
		PdfUtils.closeDocument(splittedDocuments.get(0));
		PdfUtils.closeDocument(splittedDocuments.get(1));
		splittedDocuments = null;
		assertNull(splittedDocuments);
		delimiter = 6;
		splittedDocuments = PdfUtils.splitDocument(document, delimiter);
		assertNull(splittedDocuments);
		delimiter = 0;
		splittedDocuments = PdfUtils.splitDocument(document, delimiter);
		assertEquals(splittedDocuments.size(), 1);
		assertEquals(splittedDocuments.get(0).getNumberOfPages(), 5);
		PdfUtils.closeDocument(splittedDocuments.get(0));
		delimiter = 5;
		splittedDocuments = null;
		splittedDocuments = PdfUtils.splitDocument(document, delimiter);
		assertEquals(splittedDocuments.size(), 1);
		assertEquals(splittedDocuments.get(0).getNumberOfPages(), 5);
		PdfUtils.closeDocument(splittedDocuments.get(0));
		
		File file = new File(fileName);
		if(file.exists())
			file.delete();
		
	}
}
