package com.example.demo.service.impl;

import com.example.demo.dto.ClassroomDTO;
import com.example.demo.dto.ImportClassroomDTO;
import com.example.demo.dto.ImportResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.ClassroomService;
import com.example.demo.service.QuestionService;
import com.example.demo.util.Utils;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionCategoryRepository questionCategoryRepository;
    private final QuestionRepository questionRepository;
//    @Autowired
//    private JavaMailSender mailSender;

    @Override
    public ResponseEntity<Resource> downloadTemplate(HttpServletRequest request) {

        String userId = request.getHeader("Userid");


        // lay data cho vao danh muc
        List<String> categories = questionCategoryRepository.findByUserId(userId);

        try (InputStream is = new ClassPathResource("excel/questionTemplate.xlsx").getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet specificationSheet = workbook.getSheet("Specification");
            // Creating header row
            // Populating the data rows
            int rowNum = 1;
            for (String category : categories) {
                Row row = specificationSheet.createRow(rowNum++);
                row.createCell(0).setCellValue(category);
            }

            // fill xong data specification sheet
            Sheet importDataSheet = workbook.getSheet("Import data");

            DataValidationHelper validationHelper = importDataSheet.getDataValidationHelper();

            // Create data validation for column B (Names)
            DataValidationConstraint nameConstraint = validationHelper.createFormulaListConstraint("Specification!$A$2:$A$" + categories.size() + 1);
            CellRangeAddressList nameAddressList = new CellRangeAddressList(1, 50, 1, 1);
            DataValidation nameValidation = validationHelper.createValidation(nameConstraint, nameAddressList);

            // Add the validations to the sheet
            importDataSheet.addValidationData(nameValidation);


            // Write the output to a file
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                workbook.write(bos);
                ByteArrayResource resource = new ByteArrayResource(bos.toByteArray());

                // Get the file's media type (if necessary)
                String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

                // Prepare response with the file
                return ResponseEntity.ok()
                        .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"questions.xlsx\"")
                        .body(resource);
            }
        } catch (Exception e) {
            // Handle error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ImportResponse importExcel(MultipartFile file, HttpServletRequest request) {
        String userId = request.getParameter("userId");
        ImportResponse response = new ImportResponse();
        List<String> errors = new ArrayList<>();
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int startRow = 1; //bỏ qua 1 dòng heading
            List<Question> rs = new ArrayList<>();
            for (int i = startRow; i < sheet.getLastRowNum(); i++) {
                Question questionDTO = new Question();
                Row currentRow = sheet.getRow(i);

                if (!isRowNotEmpty(currentRow)) break;
                Iterator<Cell> cellsInRow = currentRow.iterator();
                int cellIdx = 0;
                boolean categoryIsBlank = false;
                boolean contentIsBlank = false;
                boolean optionsIsBlank = false;
                boolean correctOptionIsBlank = false;
                String categoryName = "", content = "", options = "", correctOptions = "";
                for (int j = 0; j < currentRow.getLastCellNum(); j++) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            String number = currentCell.getStringCellValue().trim();
                            if (!number.isEmpty()&&!Utils.isLong(number)) {
                                errors.add("Line " + (i + 1) + ": Invalid number");
                            }
                            break;
                        case 1:
                            categoryName = currentCell.getStringCellValue().trim();
                            if (Utils.isNullOrEmpty(categoryName)) {
                                errors.add("Line " + (i + 1) + ": Category name is required");
                                categoryIsBlank= true;
                            }
                            break;
                        case 2:
                            content = currentCell.getStringCellValue().trim();
                            if (Utils.isNullOrEmpty(content)) {
                                errors.add("Line " + (i + 1) + ": Content is required");
                                contentIsBlank = true;
                            } else {
                                questionDTO.setContent(content);
                            }
                            break;
                        case 3:
                            options = currentCell.getStringCellValue().trim();
                            if (Utils.isNullOrEmpty(options)) {
                                errors.add("Line " + (i + 1) + ": Options is required");
                                optionsIsBlank = true;
                            } else {
                                String[] optionsList = options.split("||");
                                if (optionsList.length <= 1){
                                    errors.add("Line " + (i + 1) + ": Options should be more than one");
                                }else{
                                    StringBuilder optionString = new StringBuilder("[");
                                    for(String option : optionsList){
                                        optionString.append("\"").append(option).append("\",");
                                    }
                                    optionString.append("]");
                                    questionDTO.setOptions(optionString.toString());
                                }
                            }
                            break;
                        case 4:
                            correctOptions = currentCell.getStringCellValue().trim();
                            if (Utils.isNullOrEmpty(correctOptions)) {
                                errors.add("Line " + (i + 1) + ": Correct options is required");
                                correctOptionIsBlank = true;
                            }
                            else {
                                try{
                                    Integer num = Integer.parseInt(correctOptions);
                                    questionDTO.setCorrectOption(num);
                                }catch (Exception exception){
                                    errors.add("Line " + (i + 1) + ": Invalid number");
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                if (!categoryIsBlank) {
                    QuestionCategory questionCategory = questionCategoryRepository.findByCategoryName(categoryName, userId);
                    if (questionCategory == null) {
                        errors.add("Line " + (i + 1) + ": Category not found");
                    } else {
                        questionDTO.setQuesCategoryId(questionCategory.getId());
                    }
                }

                rs.add(questionDTO);
            }
            if (errors.isEmpty()){
                questionRepository.saveAll(rs);
                response.setSuccess(true);
                return response;
            } else {
                response.setSuccess(false);
                response.setError(errors);
                return response;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isRowNotEmpty(Row row) {
        if (row == null) {
            return false;
        }

        for (int cellNum = row.getFirstCellNum(); cellNum <= row.getLastCellNum(); cellNum++) {
            if (row.getCell(cellNum) != null && !row.getCell(cellNum).toString().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

//    @Override
//    public ResponseEntity<Resource> exportStudentList(long id) {
//        ClassroomDTO classroomDTO = classroomRepository.findByIdAsDTO(id);
//        List<Long> studentIds = studentClassroomRepository.findStudentsByClassroomId(id);
//        List<User> students = userRepository.findAllById(studentIds);
//        try (InputStream is = new ClassPathResource("excel/studentListByClassroom.xlsx").getInputStream();
//             Workbook workbook = new XSSFWorkbook(is)) {
//            Sheet specificationSheet = workbook.getSheetAt(0);
//            // Creating header row
//            // Populating the data rows
//            int rowNum = 1;
//            for (User student : students) {
//                Row row = specificationSheet.createRow(rowNum);
//                row.createCell(0).setCellValue(rowNum++);
//                row.createCell(1).setCellValue(student.getName());
//                row.createCell(2).setCellValue(student.getEmail());
//            }
//
//            // Write the output to a file
//            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
//                workbook.write(bos);
//                ByteArrayResource resource = new ByteArrayResource(bos.toByteArray());
//
//                // Get the file's media type (if necessary)
//                String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
//
//                // Prepare response with the file
//                return ResponseEntity.ok()
//                        .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"students.xlsx\"")
//                        .body(resource);
//            }
//        } catch (Exception e) {
//            // Handle error
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
}
