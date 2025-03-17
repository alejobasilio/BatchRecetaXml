package com.alejobasilio.receta_xml.reader;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;

import com.alejobasilio.receta_xml.model.Receta;

public class RecetasReader extends AbstractItemStreamItemReader<Receta> {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private int rowCount = 1;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        try {
            workbook = new XSSFWorkbook(new FileInputStream("src/main/resources/input/recetas.xlsx"));
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
            throw new ItemStreamException("Error al abrir el archivo", e);
        }
    }

    @Override
    public Receta read() throws Exception {
    	if (rowCount >= sheet.getLastRowNum()) {
            return null;
        }

        XSSFRow row = sheet.getRow(rowCount);
        Receta receta = new Receta();
        row.getCell(0).setCellType(CellType.STRING);
        row.getCell(1).setCellType(CellType.STRING);
        row.getCell(2).setCellType(CellType.STRING);
        row.getCell(3).setCellType(CellType.STRING);
        row.getCell(4).setCellType(CellType.STRING);
        row.getCell(5).setCellType(CellType.STRING);

        receta.setId(Long.parseLong(row.getCell(0).getStringCellValue()));
        receta.setNombre(row.getCell(1).getStringCellValue());
        receta.setIngrediente(row.getCell(2).getStringCellValue());
        receta.setCantidad(Integer.parseInt(row.getCell(3).getStringCellValue()));

        if (row.getCell(4) != null && !row.getCell(4).getStringCellValue().isEmpty() && row.getCell(5) != null && !row.getCell(5).getStringCellValue().isEmpty()) {
            receta.setIngredienteOpc(null);
            receta.setCantidadOpc(null);
        } else {
            receta.setIngredienteOpc("");
            receta.setCantidadOpc(0);
        }

        rowCount++;
        return receta;
    }

    @Override
    public void close() throws ItemStreamException {
        try {
            workbook.close();
        } catch (IOException e) {
            throw new ItemStreamException("Error al cerrar el archivo", e);
        }
    }
}
