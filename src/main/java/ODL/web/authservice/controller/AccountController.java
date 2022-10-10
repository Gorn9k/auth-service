package ODL.web.authservice.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ODL.web.authservice.dto.ResetPasswordDTO;
import ODL.web.authservice.dto.user.AccountDTO;
import ODL.web.authservice.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/account")
@Api(tags = "Account controller", description = "Service for new users registration")
public class AccountController {

    UserService userService;

    @PostMapping
    @ApiOperation(value = "Register new user")
    public ResponseEntity<AccountDTO> registerUser(@Valid @RequestBody AccountDTO accountDTO) {
        return ResponseEntity.ok(userService.registerAccount(accountDTO));
    }

    @GetMapping("/confirm")
    @ApiOperation(value = "Confirm registered user (get URI in email message)")
    public String confirmMail(
            @RequestParam(value = "uid") @ApiParam(example = "Some token", required = true) String token) {
        userService.confirmMail(token);
        return "redirect:https://2ch.hk/";
    }

    @ResponseBody
    @PostMapping("/resend-mail")
    @ApiOperation(value = "Resend mail with confirm token to email")
    public void resendMail(
            @RequestParam(value = "email") @ApiParam(example = "example@email.com", required = true) String email) {
        userService.resendConfirmMail(email);
    }

    @PostMapping("/reset-password")
    @ApiOperation(value = "Set new password")
    public ResponseEntity<AccountDTO> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        return ResponseEntity.ok(userService.resetPassword(resetPasswordDTO));
    }

    @ResponseBody
    @PostMapping("/drop-password")
    @ApiOperation(value = "Drop password")
    public void dropPassword(
            @RequestParam @ApiParam(example = "example@email.com", required = true, value = "User email for drop password") String email) {
        userService.dropPassword(email);
    }

}
