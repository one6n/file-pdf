package it.one6n.file_pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
		PdfUtils.savePDF(document, path);
		assertNotNull(PdfUtils.loadPDF(file));
	}
	
	@Test
	public void testLoadDocumentPath() {
		String path = null;
		assertNull(PdfUtils.loadPDF(path));
		path = "src" + File.separator + "test" +
		File.separator + "resources" + File.separator + "file.pdf";
		PDDocument document = new PDDocument();
		PdfUtils.savePDF(document, path);
		assertNotNull(PdfUtils.loadPDF(path));
	}
}
