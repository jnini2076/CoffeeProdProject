import { Injectable } from '@angular/core';
import { apiRoutes } from '../environments/mainenvironment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Account {

  private AccountUrl = apiRoutes.accountRoute;
  private QRcodeUrl = apiRoutes.QrCodeRoute;
  private SetupUrl = apiRoutes.SetUpAccountRoute;
  private SetipLoginURL = apiRoutes.Login;
  private finalURL = apiRoutes.FinalVerfication;
  private LoggedInURL = apiRoutes.LoggedInCredentials;
  private LogoutUrl = apiRoutes.Logout;
  private ForgotPasswordURL = apiRoutes.ForgotPassword;
  private DeleteURL = apiRoutes.Delete;
  private ApplicationURL = apiRoutes.Application;
  private OrderPaymentURL = apiRoutes.OrderPayment;

  constructor(private http:HttpClient){}


  PostAccount(formdata:any):Observable<any>{

     return  this.http.post(this.AccountUrl,formdata);

  }

  GetQRcode(username:any):Observable<any>{
    return this.http.get(this.QRcodeUrl + username);
  }

  PostVerificationCode(verify:any):Observable<any>{
    return this.http.post(this.SetupUrl, verify);
  }

  Verify(loginCredentials:any):Observable<any>{
    return this.http.post(this.SetipLoginURL,loginCredentials);
  }

  finalVerfify(data:any):Observable<any>{
    return this.http.post(this.finalURL,data,{withCredentials:true});
  }

  LoggedIn(user:any):Observable<any>{
    return this.http.get(this.LoggedInURL + user, {withCredentials:true});
  }

  Logout():Observable<any>{
    return this.http.post(this.LogoutUrl, {}, {withCredentials:true});
  }

  Forgotpassword(formdata:any):Observable<any>{
    return this.http.post(this.ForgotPasswordURL,formdata);
  }

  DeleteAccount(username:any):Observable<any>{
    return this.http.delete(this.DeleteURL + username,{withCredentials:true});
  }

  PostApplication(formdata:any):Observable<any>{
    return this.http.post(this.ApplicationURL, formdata);
  }

  PostOrderPayment(payload:any):Observable<any>{
    return this.http.post(this.OrderPaymentURL, payload);
  }





}







