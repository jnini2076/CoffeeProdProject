package com.coffeewa.coffeewebapp.accounts;




import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coffeewa.coffeewebapp.dtos.ErrorResponseDTO;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RequestMapping("/Account")
@RestController
public class AccountController {

    private accountservice accountservice;



    public AccountController(accountservice accountservice){
        this.accountservice = accountservice;
    }

    @PostMapping
    public ResponseEntity<Void> SaveAccount(@RequestBody @Valid accountDTO accountDto){
          Optional <account> entity = accountservice.CreateanAccount(accountDto);

     if(!entity.isPresent())
     {
            return ResponseEntity.badRequest().build();
     }
        account user = entity.get();
        accountservice.setUpTotpInfo(user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/TotpInfo/{username}")
    public ResponseEntity<Map<String,String>> createQRCODE(@PathVariable String username){
        Map<String,String> hm = accountservice.setUpTotpInfo(username);
        if(hm.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(hm);
    }

    @RateLimiter(name = "authApi", fallbackMethod = "totpLimitFallback")
    @PostMapping("/completeSetup")
    public ResponseEntity<ErrorResponseDTO> completeTOTP(@RequestParam String username, @RequestParam int verificationCode ){
        boolean isValid = accountservice.completeTotpSetup(username, verificationCode);
        if(!isValid){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDTO("UnAuthorized", "invalid verification code"));
        }
        return ResponseEntity.accepted().build();
    }

     @RateLimiter(name = "authApi", fallbackMethod = "rateLimitFallback")
    @PostMapping("/login")
    public ResponseEntity<ErrorResponseDTO> LoginIntoAccount(@RequestBody @Valid passworddto passwordDTO) {

            Optional<account> possibeLogin = accountservice.Login(passwordDTO.getUsername(), passwordDTO.getPassword());

            if(possibeLogin.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO("Not Found", "Invalid username or password"));
            }

            return ResponseEntity.status(HttpStatus.ACCEPTED).build();

    }

    @SuppressWarnings("unchecked")
    @RateLimiter(name = "authApi", fallbackMethod = "totpLimitFallback")
    @PostMapping("/finalVerfication")
    public <T> ResponseEntity<T> OfficialLogin(@RequestParam String username, @RequestParam int verificationCode){
       Optional <String> isValid = accountservice.VerifytotpCode(username,verificationCode);
        if(isValid.isEmpty()){
            return (ResponseEntity<T>) ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDTO("Unauthorized", "invalid verification code" ));
        }

        ResponseCookie cookie = ResponseCookie.from("jwt",isValid.get())
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(Duration.ofHours(5))
        .sameSite("None")
        .partitioned(true)
        .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> Logout(HttpServletRequest request, HttpServletResponse response){

        if(request.getSession(false)!=null){
            request.getSession(false).invalidate();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Cookie cookie = new Cookie("authToken", "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);

          return  ResponseEntity.status(HttpStatus.ACCEPTED).header(HttpHeaders.SET_COOKIE, response.toString()).build();


    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/Delete/{username}")
    public ResponseEntity<Void> DeleteAccount(@PathVariable String username){

      boolean deletedAccount =  accountservice.deleteAccount(username);
        if(deletedAccount ==false){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(null);


    }


    @GetMapping("/{username}")
    public ResponseEntity<Optional<String>> AccountFirstname(@PathVariable String username){
        Optional<String> hey = accountservice.getAccountFirstName(username);
        if(!hey.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
            return ResponseEntity.ok(hey);
    }

    @RateLimiter(name = "authApi", fallbackMethod = "rateLimitFallbackk")
    @PostMapping("/forgotpassword")
    public ResponseEntity<ErrorResponseDTO> forgotPassword(@RequestBody @Valid forgotpasswordDTO forgotpasswordDTO){
        Boolean possibleAccount = accountservice.forgotPassword(forgotpasswordDTO.getUsername(), forgotpasswordDTO.getVerificationCode(), forgotpasswordDTO.getNewpassword());

        if(!possibleAccount){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO("Unauthorized","invalid username or verification code or invalid new password" ));
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();

    }

    public ResponseEntity<ErrorResponseDTO> rateLimitFallback(passworddto passwordDTO, Throwable t) {
        return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .body(new ErrorResponseDTO("Too many requests", "Rate limit exceeded wait 5 mins to try again"));
    }

    public ResponseEntity<ErrorResponseDTO> rateLimitFallbackk(forgotpasswordDTO forgotpasswordDTO, Throwable t) {
        return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .body(new ErrorResponseDTO("Too many requests", "Rate limit exceeded wait 5 mins to try again"));
    }

        public ResponseEntity<ErrorResponseDTO> totpLimitFallback(String username, int verificationCode , Throwable t) {
        return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .body(new ErrorResponseDTO("Too many requests", "Rate limit exceeded wait 5 mins to try again"));
    }


}
