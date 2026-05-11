package com.devsu.service;

import com.devsu.model.Account;
import com.devsu.model.Movement;
import com.devsu.repository.AccountRepository;
import com.devsu.repository.MovementRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReportService {

  private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.WHITE);
  private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
  private static final Font CELL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
  private static final Font LABEL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
  private static final BaseColor HEADER_COLOR = new BaseColor(44, 62, 80);
  private static final BaseColor ROW_ALT_COLOR = new BaseColor(236, 240, 241);

  private final AccountRepository accountRepository;
  private final MovementRepository movementRepository;

  public ReportService(AccountRepository accountRepository, MovementRepository movementRepository) {
    this.accountRepository = accountRepository;
    this.movementRepository = movementRepository;
  }

  public byte[] generateReport(String accountNumber, String startDate, String endDate) {
    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Cuenta no encontrada: " + accountNumber));

    List<Movement> movements = movementRepository.findByAccountAndDateBetween(account, startDate, endDate);

    try {
      return buildPdf(account, movements, startDate, endDate);
    } catch (DocumentException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating PDF report");
    }
  }

  private byte[] buildPdf(Account account, List<Movement> movements, String startDate, String endDate)
      throws DocumentException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Document document = new Document(PageSize.A4, 36, 36, 54, 36);
    PdfWriter.getInstance(document, out);
    document.open();

    PdfPTable titleTable = new PdfPTable(1);
    titleTable.setWidthPercentage(100);
    PdfPCell titleCell = new PdfPCell(new Phrase("Cuenta", TITLE_FONT));
    titleCell.setBackgroundColor(HEADER_COLOR);
    titleCell.setPadding(12);
    titleCell.setBorder(Rectangle.NO_BORDER);
    titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    titleTable.addCell(titleCell);
    document.add(titleTable);
    document.add(Chunk.NEWLINE);

    PdfPTable infoTable = new PdfPTable(2);
    infoTable.setWidthPercentage(100);
    infoTable.setWidths(new float[] { 1, 2 });
    addInfoRow(infoTable, "Numero de Cuenta:", account.getAccountNumber());
    addInfoRow(infoTable, "Tipo de Cuenta:", account.getAccountType());
    addInfoRow(infoTable, "Balance Inicial:", String.format("$%.2f", account.getInitialBalance()));
    addInfoRow(infoTable, "Cliente:", account.getClient() != null ? account.getClient().getName() : "-");
    addInfoRow(infoTable, "Desde:", startDate + " Hasta " + endDate);
    document.add(infoTable);
    document.add(Chunk.NEWLINE);

    PdfPTable table = new PdfPTable(5);
    table.setWidthPercentage(100);
    table.setWidths(new float[] { 2, 2, 1.5f, 1.5f, 1.5f });

    addTableHeader(table, "Fecha");
    addTableHeader(table, "Tipo de Movimiento");
    addTableHeader(table, "Cantidad");
    addTableHeader(table, "Balance");
    addTableHeader(table, "Cuenta");

    if (movements.isEmpty()) {
      PdfPCell emptyCell = new PdfPCell(new Phrase("No se detectaron movimiento para este rango de fecha.", CELL_FONT));
      emptyCell.setColspan(5);
      emptyCell.setPadding(8);
      emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(emptyCell);
    } else {
      boolean alternate = false;
      for (Movement m : movements) {
        BaseColor rowColor = alternate ? ROW_ALT_COLOR : BaseColor.WHITE;
        addTableCell(table, m.getDate(), rowColor);
        addTableCell(table, m.getMovementType(), rowColor);
        addTableCell(table, String.format("$%.2f", m.getAmount()), rowColor);
        addTableCell(table, String.format("$%.2f", m.getBalance()), rowColor);
        addTableCell(table, account.getAccountNumber(), rowColor);
        alternate = !alternate;
      }
    }

    document.add(table);
    document.close();
    return out.toByteArray();
  }

  private void addInfoRow(PdfPTable table, String label, String value) {
    PdfPCell labelCell = new PdfPCell(new Phrase(label, LABEL_FONT));
    labelCell.setBorder(Rectangle.NO_BORDER);
    labelCell.setPaddingBottom(4);
    table.addCell(labelCell);

    PdfPCell valueCell = new PdfPCell(new Phrase(value, CELL_FONT));
    valueCell.setBorder(Rectangle.NO_BORDER);
    valueCell.setPaddingBottom(4);
    table.addCell(valueCell);
  }

  private void addTableHeader(PdfPTable table, String text) {
    PdfPCell cell = new PdfPCell(new Phrase(text, HEADER_FONT));
    cell.setBackgroundColor(HEADER_COLOR);
    cell.setPadding(8);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(cell);
  }

  private void addTableCell(PdfPTable table, String text, BaseColor bgColor) {
    PdfPCell cell = new PdfPCell(new Phrase(text, CELL_FONT));
    cell.setBackgroundColor(bgColor);
    cell.setPadding(6);
    table.addCell(cell);
  }
}
