package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.entity.Staff;
import com.agri_supplies_shop.enums.Status;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.AccountRepository;
import com.agri_supplies_shop.repository.StaffRepository;
import com.agri_supplies_shop.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {

    JavaMailSender sender;
    StaffRepository staffRepository;
    AccountRepository accountRepository;

    @Override
    @Transactional
    public void sendCreateAccountEmail(String fullName, String email) throws MessagingException {
        if (accountRepository.findByUserName(email).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Staff staff = staffRepository.findByEmail(email);
        if (staff == null) {
            staff = Staff.builder()
                    .fullName(fullName)
                    .email(email)
                    .status(Status.WAITING)
                    .createdAt(ZonedDateTime.now())
                    .build();
            staffRepository.save(staff);
        }

//MIME - HTML message
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject("Hãy xác thực để tạo tài khoản");
        helper.setText(
                "<html>" +
                        "<head><meta charset='UTF-8'></head>" +
                        "<body>" +
                        "<h2>Dear " + fullName + ",</h2>" +
                        "<p>Chào mừng bạn đến với cửa hàng vật tư nông nghiệp Agris.</p>" +
                        "<p>Hãy click vào nút bên dưới để kích hoạt tài khoản:</p>" +
                        generateConfirmationLink(email, staff.getId()) +
                        "<br/><br/>" +
                        "Phi Âu,<br/>" +
                        "Quản lý cửa hàng" +
                        "</body>" +
                        "</html>", true);

        sender.send(message);
    }

    private String generateConfirmationLink(String email, Long staffId) {
        return
                "<a style='text-decoration: none;' class='btn-success' " +
                        "href=http://localhost:3000/staff_account_confirm_email?email="
                        + email + "&staffId=" + staffId +
                        ">" +
                        "<button style='background-color: #009b49;\n" +
                        "    color: white;\n" +
                        "    border: none;\n" +
                        "    padding: 8px;\n" +
                        "    cursor: pointer;\n" +
                        "    border-radius: 4px;'>" +
                        "Kích hoạt tài khoản" +
                        "</button>" +
                        "</a>";
    }
}
