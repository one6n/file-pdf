package it.one6n.pdfutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PdfUtilsTest {

	private static final String BASE_TEST_RESOURCE_PATH = "src" + File.separator + "test" + File.separator + "resources"
			+ File.separator + "testFile";

	@Test
	public void testLoadDocumentFile() {
		File file = null;
		assertNull(PdfUtils.loadPDF(file));
		String path = BASE_TEST_RESOURCE_PATH + File.separator + "file.pdf";
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
		if (file.exists())
			file.delete();
	}

	@Test
	public void testLoadDocumentByteArray() {
		byte[] barr = null;
		assertNull(PdfUtils.loadPDF(barr));
		String testFileFullname = BASE_TEST_RESOURCE_PATH + File.separator + "test.pdf";
		PdfUtils.createAndSavePdfWithBlankPages(2, testFileFullname);
		File testFile = new File(testFileFullname);
		try {
			barr = Files.readAllBytes(testFile.toPath());
			PDDocument doc = PdfUtils.loadPDF(barr);
			assertNotNull(doc);
			assertEquals(2, doc.getNumberOfPages());
			PdfUtils.closeDocument(doc);
			if (testFile.exists())
				testFile.delete();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Test
	public void testLoadDocumentPath() {
		String path = null;
		assertNull(PdfUtils.loadPDF(path));
		path = BASE_TEST_RESOURCE_PATH + File.separator + "file.pdf";
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
		if (file.exists())
			file.delete();
	}

	@Test
	public void testSavePDF() {
		String fileName = BASE_TEST_RESOURCE_PATH + File.separator + "file.pdf";
		File file = new File(fileName);
		assertFalse(file.exists());
		PDDocument document = new PDDocument();
		PdfUtils.savePDF(document, fileName);
		assertTrue(file.exists());
		if (file.exists())
			file.delete();
		fileName = "";
		file = new File(fileName);
		PdfUtils.savePDF(document, fileName);
		assertFalse(file.exists());
		PdfUtils.closeDocument(document);
	}

	@Test
	public void testSplitDocument() {
		int numberOfPages = 5;
		PDDocument document = PdfUtils.createPdfWithBlankPages(numberOfPages);
		List<PDDocument> splittedDocuments = null;
		assertNull(splittedDocuments);
		int delimiter = 3;
		try {
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
			assertNull(splittedDocuments);
			delimiter = 5;
			splittedDocuments = null;
			splittedDocuments = PdfUtils.splitDocument(document, delimiter);
			assertNull(splittedDocuments);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Test
	public void testMergeDocuments() throws IOException {
		PDDocument merged = null;
		try (PDDocument doc1 = PdfUtils.createPdfWithBlankPages(2);
				PDDocument doc2 = PdfUtils.createPdfWithBlankPages(1)) {
			merged = PdfUtils.mergeDocuments(doc1);
			assertNotNull(merged);
			assertEquals(2, merged.getNumberOfPages());
			merged.close();
			merged = PdfUtils.mergeDocuments(doc1, doc2);
			assertNotNull(merged);
			assertEquals(3, merged.getNumberOfPages());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			merged.close();
		}
	}

	@Test
	public void testCreatePdfWithBlankPages() {
		int numberOfPages = 5;
		PDDocument document = null;
		assertNull(document);
		document = PdfUtils.createPdfWithBlankPages(numberOfPages);
		assertNotNull(document);
		assertEquals(numberOfPages, document.getNumberOfPages());
		PdfUtils.closeDocument(document);
		document = null;
		numberOfPages = 0;
		document = PdfUtils.createPdfWithBlankPages(numberOfPages);
		assertNotNull(document);
		assertEquals(numberOfPages, document.getNumberOfPages());
		PdfUtils.closeDocument(document);
		document = null;
		numberOfPages = -1;
		document = PdfUtils.createPdfWithBlankPages(numberOfPages);
		assertNotNull(document);
		assertEquals(0, document.getNumberOfPages());
		PdfUtils.closeDocument(document);

	}

	@Test
	public void testcreateAndSavePdfWithBlankPages() {
		int numberOfPages = 5;
		String fileName = BASE_TEST_RESOURCE_PATH + File.separator + "file.pdf";
		File file = new File(fileName);
		PDDocument document = null;
		assertNull(document);
		document = PdfUtils.createAndSavePdfWithBlankPages(numberOfPages, fileName);
		assertNotNull(document);
		assertEquals(numberOfPages, document.getNumberOfPages());
		assertTrue(file.exists());
		PdfUtils.closeDocument(document);
		if (file.exists())
			file.delete();
		document = null;
		numberOfPages = 0;
		document = PdfUtils.createAndSavePdfWithBlankPages(numberOfPages, fileName);
		assertNotNull(document);
		assertEquals(numberOfPages, document.getNumberOfPages());
		assertTrue(file.exists());
		PdfUtils.closeDocument(document);
		if (file.exists())
			file.delete();
		document = null;
		numberOfPages = -1;
		document = PdfUtils.createAndSavePdfWithBlankPages(numberOfPages, fileName);
		assertNotNull(document);
		assertEquals(0, document.getNumberOfPages());
		PdfUtils.closeDocument(document);
		assertTrue(file.exists());
		PdfUtils.closeDocument(document);
		if (file.exists())
			file.delete();

	}

	@Test
	public void testWriteText() {
		PDDocument document = PdfUtils.createPdfWithBlankPages(1);
		PDFTextStripper pdfStripper = null;
		String readedText = null;
		try {
			pdfStripper = new PDFTextStripper();
			readedText = pdfStripper.getText(document);
		} catch (IOException e) {
		} finally {
			PdfUtils.closeDocument(document);
		}
		System.out.println(readedText);
		assertTrue(readedText.isBlank());
		String text = "Hello World";
		document = PdfUtils.createPdfWithBlankPages(1);
		PdfUtils.writeText(document, 0, text, PDType1Font.TIMES_ITALIC, 200, 200);
		try {
			readedText = pdfStripper.getText(document);
		} catch (IOException e) {
		} finally {
			PdfUtils.closeDocument(document);
		}
		assertNotNull(readedText);
		assertEquals(text, readedText.substring(0, readedText.length() - 2));
	}
}
