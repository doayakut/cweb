package cweb.strategy;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import cweb.BaseServlet;
import cweb.jpa.Answer;
import cweb.jpa.User;
import cweb.jpa.enums.Method;
import cweb.jpa.enums.Order;
import cweb.jpa.enums.PageType;
import cweb.jpa.enums.Question;
import cweb.jpa.enums.Questionnaire;
import cweb.jpa.service.AnswerService;
import cweb.jpa.service.UserService;

public class Export implements BaseStgy {
	public static ArrayList<Class<? extends User>> auths;
	public BaseServlet servlet;

	private Workbook wb;
	private Sheet sheet;

	private CellStyle style_header;
	private CellStyle style_numeric;
	private CellStyle style_gray;
	private Font f_bold;
	private DataFormat dataFormat;
	
	private AnswerService as;

	public Export(BaseServlet s) {
		super();
		servlet = s;
		as = s.getAnsservice();
		prepSheets();
	}

	@Override
	public boolean authenticate() {
		return true;
	}

	@Override
	public void execute() throws IOException {
		List<User> users = new ArrayList<User>(servlet.getUserservice()
				.findAllUsers());

		Collections.sort(users);
		int row_count = 1;
		for (int i = 0, offset = 0; i < users.size(); i++, offset = 0) {

			User u = users.get(i);

			if (!u.getName().startsWith("test")) {

				System.out.print("Writing: " + u.getName());
				Row r = sheet.createRow(row_count);
				
				row_count++;
				
				offset = writeName(r, u, offset) + 1;
				offset = writeOrder(r, u, offset) + 1;
				offset = writeAnswers(r, u, offset) + 1;
				System.out.println(" done");
			}
			u = null;
		}
		
		servlet.getResponse().setContentType("text/plain");
		servlet.getResponse().setHeader("Content-Disposition",
				"attachment; filename=data-" + getDate() + ".xls");
		wb.write(servlet.getResponse().getOutputStream());

	}

	private void prepSheets() {

		wb = new HSSFWorkbook();
		sheet = wb.createSheet();

		HSSFPalette palette = ((HSSFWorkbook) wb).getCustomPalette();
		palette.setColorAtIndex(HSSFColor.GREY_25_PERCENT.index, (byte) 230,
				(byte) 230, (byte) 230);

		style_gray = wb.createCellStyle();
		style_gray.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex());
		style_gray.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style_header = wb.createCellStyle();
		f_bold = wb.createFont();
		f_bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style_header.setFont(f_bold);
		style_header.setAlignment(CellStyle.ALIGN_CENTER);

		style_numeric = wb.createCellStyle();
		dataFormat = wb.createDataFormat();
		style_numeric.setDataFormat(dataFormat.getFormat("0.00"));
		
	}

	private int writeName(Row r, User u, int offset) {

		r.createCell(0).setCellValue(u.getName());
		r.createCell(1).setCellValue(u.getCvtype());
		return offset + 2;
	
	}
	
	private int writeOrder(Row r, User u, int offset) {
		String str = "";
		List<Integer> mo = u.getMethodOrder().getOrder();
		List<Integer> pto = u.getPageOrder().getOrder();
		
		List<Method> mlist = new ArrayList<Method>();
		for(Integer i: mo){
			mlist.add(Method.list.get(i));
		}
		List<PageType> ptlist = new ArrayList<PageType>();
		for(Integer i: pto){
			ptlist.add(PageType.list.get(i));
		}
		
		for (Method m : mlist)
			str += m.toString().toLowerCase() + " - ";
		str = str.substring(0, str.length() - 3);
		str += " & ";
		for (PageType f : ptlist)
			str += f.toString().toLowerCase() + " - ";
		str = str.substring(0, str.length() - 3);

		r.createCell(offset).setCellValue(str);

		return offset + 1;
	}

	private int writeAnswers(Row r, User u, int offset){
		int width = Method.length * PageType.length;
		int q_offset = 0;
		for (Questionnaire qn: Questionnaire.list){
			if(qn != Questionnaire.Questionnaire6){ // Every question will have 9 separate average value depending on the answer
				for(Question q: qn.getQuestionList()){
					System.out.println(q.name());
					// Calculate index by Method.ordinal * PageType.length + PageType.ordinal
					float[] totals = new float[width];
					int[] counts = new int[width];
					
					List<Answer> answers = as.getAnswers(u,q);
					
					for(Answer a: answers){

						Method m = servlet.getUserservice().getMethod(u,a.getViewIndex() - 1);
						PageType pt = servlet.getUserservice().getPageType(u,a.getViewIndex() - 1);
					
						totals[m.ordinal() * PageType.length + pt.ordinal()] += a.getValue();
						counts[m.ordinal() * PageType.length + pt.ordinal()] += 1;
						
					}
					for (int j = 0; j < width; j++) {
						if (counts[j] != 0) {
							Cell cell = r.createCell(offset + q_offset * 9 + j);
							cell.setCellValue(totals[j] / counts[j]);
							cell.setCellStyle(style_numeric);
						}
					}
					q_offset++;
				}
			}
			else{ // Last questionnaire is independent on webpage coloring
				
			}
		}
		return offset;
	}
	private Cell createBoldCell(Row row, int col) {
		Cell cell = row.createCell(col);
		cell.setCellStyle(style_header);
		return cell;
	}

	private Cell createCell(Row row, int col) {
		Cell cell = row.createCell(col);
		return cell;
	}

	private Cell createGrayCell(Row row, int col) {
		Cell cell = row.createCell(col);
		cell.setCellStyle(style_gray);
		return cell;
	}

	private String getDate() {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_kk-mm-ss");
		Date date = new Date();
		return df.format(date);
	}


}
