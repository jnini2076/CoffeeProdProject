import { environment } from "./environment";
const API_URL = environment.apiUrl;


export const apiRoutes={

  accountRoute:`${API_URL}/Account`,
  QrCodeRoute:`${API_URL}/Account/TotpInfo/`,
  SetUpAccountRoute:`${API_URL}/Account/completeSetup`,
  Login:`${API_URL}/Account/login`,
  FinalVerfication:`${API_URL}/Account/finalVerfication`,
  LoggedInCredentials:`${API_URL}/Account/`,
  Logout:`${API_URL}/Account/logout`,
  ForgotPassword:`${API_URL}/Account/forgotpassword`,
  Delete:`${API_URL}/Account/Delete/`,
  Application:`${API_URL}/Application`,
  OrderPayment:`${API_URL}/Order`


}
