package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.dto.request.OtpValidateRequest;
import com.agri_supplies_shop.entity.Account;
import com.agri_supplies_shop.entity.Customer;
import com.agri_supplies_shop.entity.Staff;
import com.agri_supplies_shop.enums.PredefinedRole;
import com.agri_supplies_shop.enums.Status;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.AccountRepository;
import com.agri_supplies_shop.repository.CustomerRepository;
import com.agri_supplies_shop.repository.RoleRepository;
import com.agri_supplies_shop.repository.StaffRepository;
import com.agri_supplies_shop.service.AuthenticationService;
import com.agri_supplies_shop.service.EmailService;
import com.agri_supplies_shop.service.OtpService;
import com.agri_supplies_shop.utils.EmailValidation;
import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {

    JavaMailSender sender;
    StaffRepository staffRepository;
    AccountRepository accountRepository;
    OtpService otpGenerator;
    CustomerRepository customerRepository;
    RoleRepository roleRepository;
    AuthenticationService authenticationService;

    @Async
    public void send(String to, String email, String subject) {
        try {
            MimeMessage mimeMessage = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject(subject);
            sender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("gửi mail sai rồi");
        }
    }


    @Override
    public Boolean generateOTPForResetPassword(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@"
                + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$";
        if (EmailValidation.patternMatches(email, regexPattern)) {
            // generate otp
            Integer otpValue = otpGenerator.generateOTP(email);
            System.out.println(otpValue);
            if (otpValue == -1) {
                return false;
            }
            Account account = new Account();
            Staff staff = staffRepository.findByEmail(email);
            if (staff == null) {
                Customer customer = customerRepository.findByEmail(email).orElseThrow(
                        () -> new AppException(ErrorCode.CUSTOMER_NOT_EXISTED)
                );
                account = customer.getAccount();
            } else
                account = staff.getAccount();
            // fetch user e-mail from database
            // generate emailDTO object
            send(email, buildSendOtpEmail(account.getUserName(), otpValue), "Lấy lại mật khẩu");
            // send generated e-mail
            return true;
        } else {
            throw new AppException(ErrorCode.INVALID_EMAIL);
        }
    }

    @Override
    public Boolean validateOTP(OtpValidateRequest request) throws ExecutionException {
        // get OTP from cache
        Integer cacheOTP = otpGenerator.getOPTByKey(request.getKey());
        if (cacheOTP != null && cacheOTP.equals(request.getOtp())) {
            otpGenerator.clearOTPFromCache(request.getKey());
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void sendCreateAccountEmail(String fullName, String email) throws MessagingException, JOSEException {
        Optional<Account> account = accountRepository.findByUserName(email);
        if (account.isPresent()) {
            if (account.get().getStatus() != Status.WAITING) {
                throw new AppException(ErrorCode.STAFF_EXISTED);
            }
        } else {
            account = Optional.ofNullable(Account.builder()
                    .role(roleRepository.findByName(PredefinedRole.ADMIN_ROLE.getName()))
                    .createdAt(ZonedDateTime.now())
                    .status(Status.WAITING)
                    .userName(email)
                    .build());
            accountRepository.save(account.get());
        }

        Staff staff = staffRepository.findByEmail(email);
        if (staff == null) {
            staff = Staff.builder()
                    .fullName(fullName)
                    .status(Status.WAITING)
                    .email(email)
                    .account(account.get())
                    .createdAt(ZonedDateTime.now())
                    .build();
            staffRepository.save(staff);
        }
        String token = authenticationService.generateToken(account.get());
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
                        generateConfirmationLink(email, staff.getId(), token) +
                        "<br/><br/>" +
                        "Phi Âu,<br/>" +
                        "Quản lý cửa hàng" +
                        "</body>" +
                        "</html>", true);

        sender.send(message);
    }


    private String generateConfirmationLink(String email, Long staffId, String token) {
        return
                "<a style='text-decoration: none;' class='btn-success' " +
                        "href=http://localhost:3000/staff_account_confirm_email?email="
                        + email + "&staffId=" + staffId + "&token=" + token +
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

    private String buildSendOtpEmail(String name, int otp) {
        return "<h1 style=\"color: #009B49; text-align: center;\"> Agris</h1>\n" +
                "<h2 style=\"color: #2e6c80; text-align: center;\">Thay đổi mật khẩu:</h2>\n" +
                "<p>Xin chào " + name + "<br />Chúng tôi là team Agris</p>\n" +
                "<p>Quý khách nhận được mail này là vì quý khách (cũng có thể là ai đó giả mạo danh nghĩa quý khách) thực hiện thay đổi mật khẩu tại Agris, vui lòng ghi lại mã otp dưới đây để tiếp tục tạo mật khẩu mới.</p>\n" +
                "<p style='font-size:20px; font-weight:bold;'>" + otp + "&nbsp;</p>\n" +
                "<p>Mã opt sẽ có hiệu lực trong vòng 5 phút.</p>\n" +
                "<p>Nếu đây không phải do quý khách thực hiện thao tác, có thể yên tâm bỏ qua nội dung mail này.</p>\n" +
                "<p>Cảm ơn quý khách đã sử dụng dịch vụ của chúng tôi, xin chúc quý khách có một ngày tốt lành.</p>\n" +
                "<p>Xin vui lòng không trả lời mail từ hệ thống.</p>\n" +
                "<p><strong>&nbsp;</strong></p>";
    }
}
