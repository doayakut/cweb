package cweb.strategy;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
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
import cweb.jpa.enums.PageType;
import cweb.jpa.enums.Question;
import cweb.jpa.service.UserService;

public class Export implements BaseStgy {
	public static ArrayList<Class<? extends User>> auths;
	public BaseServlet servlet;

	private Workbook wb;
	private List<Sheet> sheets;

	private CellStyle styleBold;
	private CellStyle styleBorder;
	private CellStyle styleBorderRight;

	public Export(BaseServlet s) {
		super();
		servlet = s;
	}

	@Override
	public boolean authenticate() {
		return true;
	}

	@Override
	public void execute() throws IOException {
		prepSheets();
		List<User> users = new ArrayList<User>(servlet.getUserservice()
				.findAllUsers());

		Collections.sort(users);
		for (int i = 0; i < users.size(); i++) {

			User u = users.get(i);

			if (!u.getName().startsWith("test")) {

				System.out.println("Writing: " + u.getName());
				addToSheets(u, i + 3);
			}

		}
		SimpleDateFormat sdf = new SimpleDateFormat("yy MM dd HH:mm:ss");	
		servlet.getResponse().setContentType("text/plain");
		servlet.getResponse().setHeader(
				"Content-Disposition",
				"attachment; filename=color-data-" + sdf.format(new Date())
						+ ".xls");
		wb.write(servlet.getResponse().getOutputStream());

	}

	private void prepSheets() {

		wb = new HSSFWorkbook();

		sheets = new ArrayList<Sheet>();
		for (Question q : Question.list) {
			sheets.add(wb.createSheet(q.toString()));
		}

		styleBold = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		styleBold.setFont(font);

		styleBorder = wb.createCellStyle();
		styleBorder.setBorderBottom(CellStyle.BORDER_THIN);
		styleBorder.setBottomBorderColor(IndexedColors.BLACK.getIndex());

		styleBorderRight = wb.createCellStyle();
		styleBorderRight.setBorderRight(CellStyle.BORDER_THIN);
		styleBorderRight.setRightBorderColor(IndexedColors.BLACK.getIndex());

		prepDataSheets();

	}

	private void prepDataSheets() {
		for (Sheet s : sheets) {

			Row r = s.createRow(0);
			createCell(r, 0, "User", true);

			createCell(r, 1, "CV Type", true);

			createCell(r, 2, "", true);
			createCell(r, 3, "Method Order", true);
			createCell(r, 4, "", true);
			createCell(r, 5, "", true);
			createCell(r, 6, "Page Type Order", true);
			createCell(r, 7, "", true);
			
			s.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
			s.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
			s.addMergedRegion(new CellRangeAddress(0, 2, 2, 4));
			s.addMergedRegion(new CellRangeAddress(0, 2, 5, 7));
			
			createCell(r, 8, Method.list.get(0).toString().toUpperCase());
			createCell(r, 26, Method.list.get(1).toString().toUpperCase());
			createCell(r, 44, Method.list.get(2).toString().toUpperCase());
			
			s.addMergedRegion(new CellRangeAddress(0, 0, 8, 25));
			s.addMergedRegion(new CellRangeAddress(0, 0, 26, 43));
			s.addMergedRegion(new CellRangeAddress(0, 0, 44, 61));

			r = s.createRow(1);

			createCell(r, 8, PageType.list.get(0).toString().toUpperCase());
			createCell(r, 14, PageType.list.get(1).toString().toUpperCase());
			createCell(r, 20, PageType.list.get(2).toString().toUpperCase());
			createCell(r, 26, PageType.list.get(0).toString().toUpperCase());
			createCell(r, 32, PageType.list.get(1).toString().toUpperCase());
			createCell(r, 38, PageType.list.get(2).toString().toUpperCase());
			createCell(r, 44, PageType.list.get(0).toString().toUpperCase());
			createCell(r, 50, PageType.list.get(1).toString().toUpperCase());
			createCell(r, 56, PageType.list.get(2).toString().toUpperCase());

			s.addMergedRegion(new CellRangeAddress(1, 1, 8, 13));
			s.addMergedRegion(new CellRangeAddress(1, 1, 14, 19));
			s.addMergedRegion(new CellRangeAddress(1, 1, 20, 25));
			s.addMergedRegion(new CellRangeAddress(1, 1, 26, 31));
			s.addMergedRegion(new CellRangeAddress(1, 1, 32, 37));
			s.addMergedRegion(new CellRangeAddress(1, 1, 38, 43));
			s.addMergedRegion(new CellRangeAddress(1, 1, 44, 49));
			s.addMergedRegion(new CellRangeAddress(1, 1, 50, 55));
			s.addMergedRegion(new CellRangeAddress(1, 1, 56, 61));
			

			r = s.createRow(2);
			
			
			int offset = 8;
			for (int i = 0; i < 54; i++) {
				int count = i % 6 + 1; 
				createCell(r, i + offset, (float) count);
			}
		}
	}

	private void addToSheets(User u, int index) {

		for (Sheet s : sheets) {
			if (s.getSheetName().equalsIgnoreCase("question18")
					|| s.getSheetName().equalsIgnoreCase("question19")
					|| s.getSheetName().equalsIgnoreCase("question20")
					|| s.getSheetName().equalsIgnoreCase("question21")) {

			} else {
				Row r = s.createRow(index);
				Method m1 = Method.list.get(u.getMethodOrder().get(0));
				Method m2 = Method.list.get(u.getMethodOrder().get(1));
				Method m3 = Method.list.get(u.getMethodOrder().get(2));
				PageType pt1 = PageType.list.get(u.getPageOrder().get(0));
				PageType pt2 = PageType.list.get(u.getPageOrder().get(1));
				PageType pt3 = PageType.list.get(u.getPageOrder().get(2));

				createCell(r, 0, u.getName());
				createCell(r, 1, u.getCvtype());
				createCell(r, 2, m1.toString().toLowerCase());
				createCell(r, 3, m2.toString().toLowerCase());
				createCell(r, 4, m3.toString().toLowerCase());
				createCell(r, 5, pt1.toString().toLowerCase());
				createCell(r, 6, pt2.toString().toLowerCase());
				createCell(r, 7, pt3.toString().toLowerCase());

				// David, Kuln, Ours = offset
				int methodOffset = 18;
				// Simple, Complex1, Complex11
				int pagetypeOffset = 6;
				int pageOffset = 1;
				int offset = 8;

				EntityManager em = servlet.getEntityManager();
				Query queryAnswers = em
						.createQuery("SELECT a FROM Answer a where a.user=:user and a.question=:question");
				queryAnswers.setParameter("user", u);
				queryAnswers.setParameter("question",
						Question.valueOf(s.getSheetName()));
				@SuppressWarnings("unchecked")
				List<Answer> answers = queryAnswers.getResultList();

				UserService us = servlet.getUserservice();

				int count = 0;
				for (Answer a : answers) {
					if (count >= 54)
						break;

					int curr = a.getViewIndex() - 1;

					System.out
							.println(a.getQuestion().toString() + ": " + curr);

					int curr_m = getMethodColumnIndex(us.getMethod(u, curr));
					int curr_pt = getPageTypeColumnIndex(us
							.getPageType(u, curr));
					int curr_index = servlet.getUserservice().getPageIndex(u,
							curr);

					createCell(r,
							curr_m * methodOffset + curr_pt * pagetypeOffset
									+ curr_index * pageOffset + offset, 
									(int) (a.getValue() * 10));

					count++;
				}
			}
		}
	}


	private int getMethodColumnIndex(Method m) {
		switch (m) {
		case DAVID:
			return 0;
		case KULN:
			return 1;
		case OURS:
			return 2;
		default:
			return 5;
		}
	}

	private int getPageTypeColumnIndex(PageType pt) {
		switch (pt) {
		case SIMPLE:
			return 0;
		case COMPLEX1:
			return 1;
		case COMPLEX11:
			return 2;
		default:
			return 30;
		}

	}

	private Cell createCell(Row r, int index, int i) {
		Cell cell = r.createCell(index);
		cell.setCellValue(i);
		return cell;
		
	}
	private Cell createCell(Row r, int index, String valueStr) {
		if (valueStr == null)
			return null;
		Cell cell = r.createCell(index);
		cell.setCellValue(valueStr);
		return cell;
	}

	private Cell createCell(Row r, int index, Float valueF) {
		if (valueF == null)
			return null;
		Cell cell = r.createCell(index);
		cell.setCellValue(valueF);
		return cell;
	}

	private Cell createCell(Row r, int index, String valueStr, boolean bold) {
		Cell cell = createCell(r, index, valueStr);
		if (bold)
			cell.setCellStyle(styleBold);
		return cell;
	}

	private Cell createCell(Row r, int index, Float valueF, boolean bold) {
		Cell cell = createCell(r, index, valueF);
		if (bold)
			cell.setCellStyle(styleBold);
		return cell;
	}

	private Cell createCell(Row r, int index, String valueStr, String commentStr) {
		Cell cell = createCell(r, index, valueStr, true);

		CreationHelper factory = wb.getCreationHelper();

		Drawing drawing = r.getSheet().createDrawingPatriarch();

		// When the comment box is visible, have it show in a 1x3 space
		ClientAnchor anchor = factory.createClientAnchor();
		anchor.setCol1(cell.getColumnIndex());
		anchor.setCol2(cell.getColumnIndex() + 4);
		anchor.setRow1(r.getRowNum());
		anchor.setRow2(r.getRowNum() + 3);

		// Create the comment and set the text+author
		Comment comment = drawing.createCellComment(anchor);
		RichTextString str = factory.createRichTextString(commentStr);
		comment.setString(str);

		// Assign the comment to the cell
		cell.setCellComment(comment);

		return cell;
	}

	private Cell createCell(Row r, int index, Float valueF, String commentStr,
			boolean bold) {
		Cell cell = createCell(r, index, valueF, bold);

		CreationHelper factory = wb.getCreationHelper();

		Drawing drawing = r.getSheet().createDrawingPatriarch();

		// When the comment box is visible, have it show in a 1x3 space
		ClientAnchor anchor = factory.createClientAnchor();
		anchor.setCol1(cell.getColumnIndex());
		anchor.setCol2(cell.getColumnIndex() + 3);
		anchor.setRow1(r.getRowNum());
		anchor.setRow2(r.getRowNum() + 2);

		// Create the comment and set the text+author
		Comment comment = drawing.createCellComment(anchor);
		RichTextString str = factory.createRichTextString(commentStr);
		comment.setString(str);

		// Assign the comment to the cell
		cell.setCellComment(comment);

		return cell;
	}

	@SuppressWarnings("unused")
	private Cell createCell(Row r, int index, Float valueF, String commentStr) {
		return createCell(r, index, valueF, commentStr, false);
	}

	private float roundFloat(float undone) {
		BigDecimal bd = new BigDecimal(undone);
		BigDecimal rounded = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
		return rounded.floatValue();
	}

}
