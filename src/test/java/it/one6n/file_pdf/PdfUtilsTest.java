package it.one6n.file_pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

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
}
