package com.coffeewa.coffeewebapp.accounts;




import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.coffeewa.coffeewebapp.config.JwtUtil;
import com.coffeewa.coffeewebapp.service.TotpService;

import jakarta.transaction.Transactional;


@Service
public class accountservice {

    private  final accountrepo accountRepo;

    private  final Logger logger = LogManager.getLogger(accountservice.class);

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwt;


    private final TotpService totpService ;



    public accountservice(accountrepo accountRepo,PasswordEncoder passwordEncoder,  TotpService totpService, JwtUtil jwt){

        this.accountRepo = accountRepo;

        this.passwordEncoder = passwordEncoder;

        this.totpService = totpService;
        this.jwt = jwt;
    }

    @Transactional
    public <accountResponseDTO> CreateanAccount(accountDTO dto){

       account possibleAccount = accountRepo.findByUsernameIgnoreCase(dto.getUsername());
                        orElseThrow(() -> new DuplicateResourceException("Username already exists"));

       

       Optional<account> request = accountRepo.findByPhonenumber(dto.getPhonenumber());
       if(request.isPresent()){
        logger.info("this phone number is already in use with an account");
        return Optional.empty();
       }

       String hashed = passwordEncoder.encode(dto.getPassword());

       account ent = new account();
       ent.setFirstname(dto.getFirstname());
        ent.setLastname(dto.getLastname());
        ent.setPhonenumber(dto.getPhonenumber());
        ent.setUsername(dto.getUsername());
        ent.setPassword(hashed);


        String totpSecret = totpService.generateSecretKey();
        ent.setTotpSecret(totpSecret);
        ent.setTotpEnabled(false);
        ent.setRole("USER");

       logger.info("account has been created with Totp secret");
       accountRepo.save(ent);
       return Optional.of(ent);
    }



    public Optional <account> verifyAccount(String username, String password){

        Optional<account> verifyUser = accountRepo.findByUsernameIgnoreCase(username);

        if(verifyUser.isEmpty()){
            logger.warn("invalid username or password");
             return Optional.empty();
        }

        account account = verifyUser.get();
        String hashpassword = account.getPassword();

        boolean matches = passwordEncoder.matches(password, hashpassword);
        if(matches){
            logger.info("login successful");

            return Optional.of(account);
        }
        else{
            logger.info("login failed");

            return Optional.empty();
        }
    }



    public boolean deleteAccount(String username){
      Optional <account> deletedaccount = accountRepo.findByUsernameIgnoreCase(username);
        if(deletedaccount.isEmpty()){
            logger.info("account doesn't exist");
            return false;
        }
        account accountToDelete = deletedaccount.get();
        accountRepo.delete(accountToDelete);
        logger.info("account has been deleted: ");
        return true;
    }



    public Optional<account> Login(String username,String password){

        Optional<account> accountverify = verifyAccount(username, password);

        if(!accountverify.isPresent()){
            logger.info("invalid password/username");
            return Optional.empty();
        }


        account user = accountverify.get();

        if(!user.getTotpEnabled()){
            logger.info("user has not done the TOTP setup first");
            return Optional.empty();
        }

        logger.info("successful login");

        return Optional.of(user);
    }


    public Optional<String> VerifytotpCode(String username, int verificationCode){
        Optional<account> accountOpt = accountRepo.findByUsernameIgnoreCase(username);
        if(accountOpt.isEmpty()){
            logger.info("no account found with this username");
            return Optional.empty();
        }

        account user = accountOpt.get();

        if(!user.getTotpEnabled()){
            logger.info("Totp not enabled for this user");
            return Optional.empty();
        }


      boolean isValid =   totpService.verifyCode(user.getTotpSecret(), verificationCode);

      if(!isValid){
        logger.info("wrong password");
        return Optional.empty();
      }
       Optional<String> firstname = getAccountFirstName(username);
        Long Id = user.getId();

        String role = user.getRole();
        String token = jwt.generateJWT(Id, username, firstname,role);

        user.setVerified(true);
        accountRepo.save(user);
        logger.info("user has successfully logged in");
        return Optional.of(token);

    }


        @Cacheable("users")
        public Optional<String> getAccountFirstName(String username){

            StopWatch sw = new StopWatch();
            sw.start("findByUsername");
            logger.info("this shouldnt hit");
            Optional<account> yo = accountRepo.findByUsernameIgnoreCase(username);
            sw.stop();
            logger.info("Timing: {}", sw.prettyPrint());

            if(yo.isEmpty()){
                logger.info("username doesnt exist");
                return Optional.empty();
            }
            account yeet = yo.get();

            String findname = yeet.getFirstname();
            if (findname == null) {
                logger.warn("Firstname is null for user: " + username);
                return Optional.of("Unknown");
            }
            return Optional.of(findname);

        }


        public Map<String,String> setUpTotpInfo(String username){
            Optional<account> possibleAccount = accountRepo.findByUsernameIgnoreCase(username);

            if(possibleAccount.isEmpty()){
                logger.info("username doesnt exist");
                return null;
            }

            account user = possibleAccount.get();
            String qrcodeURL = totpService.getQRCodeUrl(username,user.getTotpSecret(), "JonathanCoffeeWebApp");

            Map<String,String> setUpInfo = new HashMap<>();

            setUpInfo.put("secret",user.getTotpSecret());
            setUpInfo.put("qrcodeurl", qrcodeURL);
            setUpInfo.put("appName", "JonathanCoffeeWebApp");

            return setUpInfo;
        }



        public boolean completeTotpSetup(String username, int verificationCode){
                Optional<account> UserAccount = accountRepo.findByUsernameIgnoreCase(username);
                if(UserAccount.isEmpty()){
                    logger.info("incorrect username or code");
                    return false;
                }

                account user = UserAccount.get();

                Boolean isValid = totpService.verifyCode(user.getTotpSecret(), verificationCode);
                if(!isValid){
                    logger.info("incorrect verfication code");
                    return false;
                }

                user.setTotpEnabled(true);
                accountRepo.save(user);
                logger.info("setup completed for user");
                return true;
        }


        public boolean forgotPassword(String username, int verificationCode,String newPassword){
           Optional<account> user = accountRepo.findByUsernameIgnoreCase(username);

            if(!user.isPresent()){
                logger.info("username didnt exist");
                return false;
            }
                account aUser = user.get();

              Boolean isValid = totpService.verifyCode(aUser.getTotpSecret(), verificationCode);

              if(!isValid){
                logger.info("user didnt enter the correct code");
                return false;
              }

              String hashedpass = passwordEncoder.encode(newPassword);
              aUser.setPassword(hashedpass);
              logger.info("new password has been set");
              accountRepo.save(aUser);
              return true;


        }


    }


