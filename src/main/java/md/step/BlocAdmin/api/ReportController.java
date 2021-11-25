package md.step.BlocAdmin.api;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/api/v1/report")
public class ReportController {


    private static final SimpleDateFormat filenameDate = new SimpleDateFormat("ddMMyyyyHHmmss");
    private static final SimpleDateFormat readableDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @RequestMapping(value = "/export", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getExport() {
        ITextRenderer renderer = new ITextRenderer();
        ByteArrayOutputStream boas = null;
        try {
            String inputFile = "files/templates/autodebit.html";
            String outputFile = "files/generated/autodebit_"+filenameDate.format(new Date())+".pdf";

            String html = new String(Files.readAllBytes(Paths.get(inputFile)));
            final Document document = Jsoup.parse(html);
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            document.body().select(".DOC_GENERATED_DATE").html(readableDate.format(new Date()));

            renderer.setDocumentFromString(document.html());
            renderer.layout();

            try (OutputStream os = Files.newOutputStream(Paths.get(outputFile))) {
                renderer.createPDF(os);
                os.close();

                PdfReader reader = new PdfReader(outputFile);
                boas = new ByteArrayOutputStream();
                PdfStamper stamper = new PdfStamper(reader, boas);
                stamper.setPageAction(PdfWriter.PAGE_OPEN, new PdfAction(PdfAction.PRINTDIALOG), 1);
                stamper.close();
            } catch (DocumentException ex) {
                Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   catch (IOException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(boas.toByteArray(), headers, HttpStatus.OK);
        return response;
    }
}
