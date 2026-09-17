package com.coffeewa.coffeewebapp.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.coffeewa.coffeewebapp.accounts.account;
import com.coffeewa.coffeewebapp.accounts.accountdto;
import com.coffeewa.coffeewebapp.accounts.accountrepo;
import com.coffeewa.coffeewebapp.accounts.accountservice;
import com.coffeewa.coffeewebapp.config.JwtUtil;
import com.coffeewa.coffeewebapp.service.TotpService;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private accountrepo accountRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TotpService totpService;

    @Mock
    private JwtUtil jwt;

    @InjectMocks
    private accountservice accountService;

    private accountdto testAccountDto;
    private account existingAccount;

    @BeforeEach
    void setUp() {
        testAccountDto = new accountdto();
        testAccountDto.setUsername("testuser");
        testAccountDto.setPassword("plainPassword123");
        testAccountDto.setFirstname("John");
        testAccountDto.setLastname("Doe");
        testAccountDto.setPhonenumber("1234567890");

        existingAccount = new account();
        existingAccount.setUsername("existinguser");
        existingAccount.setPhonenumber("9876543210");
    }

    @Test
    @DisplayName("Should successfully create new account when username and phone are available")
    void testPostAccount_SuccessfulCreation() {
        when(accountRepo.findByUsernameIgnoreCase("testuser")).thenReturn(Optional.empty());
        when(accountRepo.findByPhonenumber("1234567890")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword123")).thenReturn("hashedPassword123");
        when(totpService.generateSecretKey()).thenReturn("TOTP_SECRET_123");
        when(accountRepo.save(any(account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<account> result = accountService.postAccount(testAccountDto);

        assertThat(result).isPresent();
        account createdAccount = result.get();

        assertThat(createdAccount.getUsername()).isEqualTo("testuser");
        assertThat(createdAccount.getFirstname()).isEqualTo("John");
        assertThat(createdAccount.getLastname()).isEqualTo("Doe");
        assertThat(createdAccount.getPhonenumber()).isEqualTo("1234567890");
        assertThat(createdAccount.getPassword()).isEqualTo("hashedPassword123");
        assertThat(createdAccount.getTotpSecret()).isEqualTo("TOTP_SECRET_123");
        assertThat(createdAccount.getTotpEnabled()).isFalse();

        verify(accountRepo).findByUsernameIgnoreCase("testuser");
        verify(accountRepo).findByPhonenumber("1234567890");
        verify(passwordEncoder).encode("plainPassword123");
        verify(totpService).generateSecretKey();
        verify(accountRepo).save(any(account.class));
    }

    @Test
    @DisplayName("Should return empty when username already exists")
    void testPostAccount_UsernameAlreadyExists() {
        when(accountRepo.findByUsernameIgnoreCase("testuser")).thenReturn(Optional.of(existingAccount));

        Optional<account> result = accountService.postAccount(testAccountDto);

        assertThat(result).isEmpty();

        verify(accountRepo).findByUsernameIgnoreCase("testuser");
        verify(accountRepo, never()).findByPhonenumber(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(totpService, never()).generateSecretKey();
        verify(accountRepo, never()).save(any(account.class));
    }

    @Test
    @DisplayName("Should return empty when phone number already exists")
    void testPostAccount_PhoneNumberAlreadyExists() {
        when(accountRepo.findByUsernameIgnoreCase("testuser")).thenReturn(Optional.empty());
        when(accountRepo.findByPhonenumber("1234567890")).thenReturn(Optional.of(existingAccount));

        Optional<account> result = accountService.postAccount(testAccountDto);

        assertThat(result).isEmpty();

        verify(accountRepo).findByUsernameIgnoreCase("testuser");
        verify(accountRepo).findByPhonenumber("1234567890");
        verify(passwordEncoder, never()).encode(anyString());
        verify(totpService, never()).generateSecretKey();
        verify(accountRepo, never()).save(any(account.class));
    }

    @Test
    @DisplayName("Should properly pass account data to repository save method")
    void testPostAccount_VerifyAccountSaveData() {
        when(accountRepo.findByUsernameIgnoreCase("testuser")).thenReturn(Optional.empty());
        when(accountRepo.findByPhonenumber("1234567890")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword123")).thenReturn("hashedPassword123");
        when(totpService.generateSecretKey()).thenReturn("TOTP_SECRET_123");

        ArgumentCaptor<account> accountCaptor = ArgumentCaptor.forClass(account.class);

        accountService.postAccount(testAccountDto);

        verify(accountRepo).save(accountCaptor.capture());
        account savedAccount = accountCaptor.getValue();

        assertThat(savedAccount.getUsername()).isEqualTo("testuser");
        assertThat(savedAccount.getPassword()).isEqualTo("hashedPassword123");
        assertThat(savedAccount.getTotpSecret()).isEqualTo("TOTP_SECRET_123");
        assertThat(savedAccount.getTotpEnabled()).isFalse();
    }

    @Test
    @DisplayName("Should handle null or invalid DTO gracefully")
    void testPostAccount_WithInvalidDto() {
        accountdto invalidDto = new accountdto();
        invalidDto.setUsername("user");
        invalidDto.setPassword("pass");

        when(accountRepo.findByUsernameIgnoreCase("user")).thenReturn(Optional.empty());
        when(accountRepo.findByPhonenumber(null)).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("hashedPass");
        when(totpService.generateSecretKey()).thenReturn("SECRET");

        Optional<account> result = accountService.postAccount(invalidDto);

        assertThat(result).isPresent();
        account createdAccount = result.get();
        assertThat(createdAccount.getUsername()).isEqualTo("user");
        assertThat(createdAccount.getFirstname()).isNull();
        assertThat(createdAccount.getLastname()).isNull();
    }

    @Test
    @DisplayName("Should delete an account when it exists")
    void shouldDeleteAccount() {
        when(accountRepo.findByUsernameIgnoreCase("existinguser")).thenReturn(Optional.of(existingAccount));

        boolean result = accountService.deleteAccount("existinguser");

        assertThat(result).isTrue();
        verify(accountRepo).delete(existingAccount);
    }

    @Test
    @DisplayName("Should return false when account to delete does not exist")
    void shouldReturnFalseWhenAccountToDeleteNotFound() {
        when(accountRepo.findByUsernameIgnoreCase("ghost")).thenReturn(Optional.empty());

        boolean result = accountService.deleteAccount("ghost");

        assertThat(result).isFalse();
        verify(accountRepo, never()).delete(any());
    }


    @Test
    @DisplayName("Should return account when credentials are valid")
    void testVerifyAccount_Success() {
        existingAccount.setPassword("hashedPassword");
        when(accountRepo.findByUsernameIgnoreCase("existinguser")).thenReturn(Optional.of(existingAccount));
        when(passwordEncoder.matches("plainPassword", "hashedPassword")).thenReturn(true);

        Optional<account> result = accountService.verifyAccount("existinguser", "plainPassword");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(existingAccount);
    }

    @Test
    @DisplayName("Should return empty when username is not found during verify")
    void testVerifyAccount_UsernameNotFound() {
        when(accountRepo.findByUsernameIgnoreCase("unknown")).thenReturn(Optional.empty());

        Optional<account> result = accountService.verifyAccount("unknown", "password");

        assertThat(result).isEmpty();
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Should return empty when password does not match")
    void testVerifyAccount_WrongPassword() {
        existingAccount.setPassword("hashedPassword");
        when(accountRepo.findByUsernameIgnoreCase("existinguser")).thenReturn(Optional.of(existingAccount));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        Optional<account> result = accountService.verifyAccount("existinguser", "wrongPassword");

        assertThat(result).isEmpty();
    }


    @Test
    @DisplayName("Should return account when credentials valid and TOTP is enabled")
    void testLogin_Success() {
        existingAccount.setPassword("hashedPassword");
        existingAccount.setTotpEnabled(true);
        when(accountRepo.findByUsernameIgnoreCase("existinguser")).thenReturn(Optional.of(existingAccount));
        when(passwordEncoder.matches("plainPassword", "hashedPassword")).thenReturn(true);

        Optional<account> result = accountService.Login("existinguser", "plainPassword");

        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("Should return empty when Login credentials are invalid")
    void testLogin_BadCredentials() {
        when(accountRepo.findByUsernameIgnoreCase("existinguser")).thenReturn(Optional.empty());

        Optional<account> result = accountService.Login("existinguser", "wrong");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return empty when TOTP is not yet enabled at Login")
    void testLogin_TotpNotEnabled() {
        existingAccount.setPassword("hashedPassword");
        existingAccount.setTotpEnabled(false);
        when(accountRepo.findByUsernameIgnoreCase("existinguser")).thenReturn(Optional.of(existingAccount));
        when(passwordEncoder.matches("plainPassword", "hashedPassword")).thenReturn(true);

        Optional<account> result = accountService.Login("existinguser", "plainPassword");

        assertThat(result).isEmpty();
    }



    @Test
    @DisplayName("Should return first name when account exists")
    void testGetAccountFirstName_Found() {
        existingAccount.setFirstname("Alice");
        when(accountRepo.findByUsernameIgnoreCase("existinguser")).thenReturn(Optional.of(existingAccount));

        Optional<String> result = accountService.getAccountFirstName("existinguser");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("Should return empty when account not found for first name lookup")
    void testGetAccountFirstName_NotFound() {
        when(accountRepo.findByUsernameIgnoreCase("unknown")).thenReturn(Optional.empty());

        Optional<String> result = accountService.getAccountFirstName("unknown");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return 'Unknown' when firstname field is null")
    void testGetAccountFirstName_NullFirstname() {
        existingAccount.setFirstname(null);
        when(accountRepo.findByUsernameIgnoreCase("existinguser")).thenReturn(Optional.of(existingAccount));

        Optional<String> result = accountService.getAccountFirstName("existinguser");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo("Unknown");
    }


    @Test
    @DisplayName("Should return TOTP setup map when account exists")
    void testSetUpTotpInfo_Found() {
        existingAccount.setTotpSecret("SECRET123");
        when(accountRepo.findByUsernameIgnoreCase("existinguser")).thenReturn(Optional.of(existingAccount));
        when(totpService.getQRCodeUrl("existinguser", "SECRET123", "JonathanCoffeeWebApp"))
                .thenReturn("otpauth://totp/JonathanCoffeeWebApp:existinguser?secret=SECRET123");

        Map<String, String> result = accountService.setUpTotpInfo("existinguser");

        assertThat(result).isNotNull();
        assertThat(result.get("secret")).isEqualTo("SECRET123");
        assertThat(result.get("qrcodeurl")).contains("SECRET123");
        assertThat(result.get("appName")).isEqualTo("JonathanCoffeeWebApp");
    }

    @Test
    @DisplayName("Should return null when account not found for TOTP setup")
    void testSetUpTotpInfo_NotFound() {
        when(accountRepo.findByUsernameIgnoreCase("unknown")).thenReturn(Optional.empty());

        Map<String, String> result = accountService.setUpTotpInfo("unknown");

        assertThat(result).isNull();
    }


    @Test
    @DisplayName("Should complete TOTP setup and flip totpEnabled to true")
    void testCompleteTotpSetup_Success() {
        existingAccount.setTotpSecret("SECRET");
        when(accountRepo.findByUsernameIgnoreCase("existinguser")).thenReturn(Optional.of(existingAccount));
        when(totpService.verifyCode("SECRET", 123456)).thenReturn(true);

        boolean result = accountService.completeTotpSetup("existinguser", 123456);

        assertThat(result).isTrue();
        assertThat(existingAccount.getTotpEnabled()).isTrue();
        verify(accountRepo).save(existingAccount);
    }

    @Test
    @DisplayName("Should return false when account not found during TOTP setup")
    void testCompleteTotpSetup_UserNotFound() {
        when(accountRepo.findByUsernameIgnoreCase("unknown")).thenReturn(Optional.empty());

        boolean result = accountService.completeTotpSetup("unknown", 123456);

        assertThat(result).isFalse();
        verify(accountRepo, never()).save(any());
    }

    @Test
    @DisplayName("Should return false when TOTP code is invalid during setup")
    void testCompleteTotpSetup_InvalidCode() {
        existingAccount.setTotpSecret("SECRET");
        when(accountRepo.findByUsernameIgnoreCase("existinguser")).thenReturn(Optional.of(existingAccount));
        when(totpService.verifyCode("SECRET", 999999)).thenReturn(false);

        boolean result = accountService.completeTotpSetup("existinguser", 999999);

        assertThat(result).isFalse();
        verify(accountRepo, never()).save(any());
    }


    @Test
    @DisplayName("Should reset password when TOTP code is valid")
    void testForgotPassword_Success() {
        existingAccount.setTotpSecret("SECRET");
        when(accountRepo.findByUsernameIgnoreCase("existinguser")).thenReturn(Optional.of(existingAccount));
        when(totpService.verifyCode("SECRET", 123456)).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("hashedNewPassword");

        boolean result = accountService.forgotPassword("existinguser", 123456, "newPassword");

        assertThat(result).isTrue();
        assertThat(existingAccount.getPassword()).isEqualTo("hashedNewPassword");
        verify(accountRepo).save(existingAccount);
    }

    @Test
    @DisplayName("Should return false when user not found for password reset")
    void testForgotPassword_UserNotFound() {
        when(accountRepo.findByUsernameIgnoreCase("unknown")).thenReturn(Optional.empty());

        boolean result = accountService.forgotPassword("unknown", 123456, "newPassword");

        assertThat(result).isFalse();
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("Should return false when TOTP code is invalid for password reset")
    void testForgotPassword_InvalidCode() {
        existingAccount.setTotpSecret("SECRET");
        when(accountRepo.findByUsernameIgnoreCase("existinguser")).thenReturn(Optional.of(existingAccount));
        when(totpService.verifyCode("SECRET", 999999)).thenReturn(false);

        boolean result = accountService.forgotPassword("existinguser", 999999, "newPassword");

        assertThat(result).isFalse();
        verify(passwordEncoder, never()).encode(anyString());
        verify(accountRepo, never()).save(any());
    }
}



