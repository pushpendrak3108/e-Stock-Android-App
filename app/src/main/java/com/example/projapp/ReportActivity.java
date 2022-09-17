package com.example.projapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    //firebase auth to check if user is authenticated.
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    //creating a list of objects constants
    List<tr_model> list;

    public static File pFile;
    private File payfile;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        pdfView = findViewById(R.id.payment_pdf_viewer);
        list = new ArrayList<>();

        //create files in charity care folder
        payfile = new File(getExternalFilesDir(null) + "/reports");

        //check if they exist, if not create them(directory)
        if ( !payfile.exists()) {
            payfile.mkdirs();
        }
        pFile = new File(payfile,"report.pdf");

        Button sellButton = findViewById(R.id.sell);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdetails("OUT");
            }
        });
        Button purchaseButton = findViewById(R.id.purchase);
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdetails("IN");
            }
        });
    }
    //function to fetch payment data from the database
    private void getdetails(String type)
    {
        db.collection("users").document(fAuth.getCurrentUser().getUid()).collection("transactions").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    list.clear();
                    for(DocumentSnapshot snapshot: task.getResult()) {
                        tr_model trmodel = new tr_model(snapshot.getString("stockid"), snapshot.getString("stock"), snapshot.getString("price"), snapshot.getString("quantity"), snapshot.getString("type"), snapshot.getString("Date and Time"));
                        if (snapshot.getString("type").equals(type)) {
                            list.add(trmodel);
                       }
                    }
                    try {
                        createPaymentReport(list);
                        DisplayReport();
                    } catch (DocumentException | FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ReportActivity.this, "Oops something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void createPaymentReport(  List<tr_model> list) throws DocumentException, FileNotFoundException{
        BaseColor colorWhite = WebColors.getRGBColor("#ffffff");
        BaseColor colorBlue = WebColors.getRGBColor("#056FAA");
        BaseColor grayColor = WebColors.getRGBColor("#425066");



        Font white = new Font(Font.FontFamily.HELVETICA, 15.0f, Font.BOLD, colorWhite);
        FileOutputStream output = new FileOutputStream(pFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{6, 6, 20, 10, 8, 20});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        Chunk typeText = new Chunk("Type", white);
        PdfPCell typeCell = new PdfPCell(new Phrase(typeText));
        typeCell.setFixedHeight(50);
        typeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        typeCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk noText = new Chunk("ID", white);
        PdfPCell noCell = new PdfPCell(new Phrase(noText));
        noCell.setFixedHeight(50);
        noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        noCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk nameText = new Chunk("Stock Name", white);
        PdfPCell nameCell = new PdfPCell(new Phrase(nameText));
        nameCell.setFixedHeight(50);
        nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        nameCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk quantityText = new Chunk("Quantity", white);
        PdfPCell quantityCell = new PdfPCell(new Phrase(quantityText));
        quantityCell.setFixedHeight(50);
        quantityCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        quantityCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk amountText = new Chunk("Price", white);
        PdfPCell amountCell = new PdfPCell(new Phrase(amountText));
        amountCell.setFixedHeight(50);
        amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        amountCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk DnTText = new Chunk("Date and Time", white);
        PdfPCell DnTCell = new PdfPCell(new Phrase(DnTText));
        DnTCell.setFixedHeight(50);
        DnTCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        DnTCell.setVerticalAlignment(Element.ALIGN_CENTER);

        table.addCell(typeCell);
        table.addCell(noCell);
        table.addCell(nameCell);
        table.addCell(quantityCell);
        table.addCell(amountCell);
        table.addCell(DnTCell);
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();


        for (PdfPCell cell : cells) {
            cell.setBackgroundColor(grayColor);
        }
        for (int i = 0; i < list.size(); i++) {
            tr_model pay = list.get(i);
            String type = pay.getType();
            String id = pay.getStockid();
            String name = pay.getStockname();
            String quantity = pay.getQuantity();
            String price = pay.getPrice();
            String dateandtime = pay.getDateandtime();

            table.addCell(type);
            table.addCell(id);
            table.addCell(name);
            table.addCell(quantity);
            table.addCell(price);
            table.addCell(dateandtime);

        }

        PdfWriter.getInstance(document, output);
        document.open();
        document.add(table);

        document.close();
    }

    private void DisplayReport()
    {
        pdfView.fromFile(pFile)
                .pages(0,2,1,3,3,3)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .load();


    }
}