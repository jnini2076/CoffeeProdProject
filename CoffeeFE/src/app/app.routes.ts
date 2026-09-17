import { Delete } from './delete/delete';
import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Menu } from './menu/menu';
import { Createaccount } from './createaccount/createaccount';
import { Mfr } from './mfr/mfr';
import { Authenticate } from './authenticate/authenticate';
import { Notfound } from './notfound/notfound';
import { Forgotpassword } from './forgotpassword/forgotpassword';
import { Home } from './home/home';
import { Career } from './career/career';
import { Application } from './application/application';
import { Payment } from './payment/payment';
import { Receipt } from './receipt/receipt';



export const routes: Routes = [


  {path:"", component:Home},
  {path:"home", component:Home},
  { path: "menu", component: Menu },
  {path: "login", component: Login},
  {path:"career", component:Career},
   {path: "delete", component:Delete},
  {path:"apply", component:Application},
  {path: "login/account", component: Createaccount},
  {path: "login/account/mfr/:username", component:Mfr},
  {path: "login/authenticate", component:Authenticate},
  {path: "login/authenticate/menu", component:Menu},
  {path: "login/forgotpassword", component:Forgotpassword},
  {path: "login/authenticate/menu/delete", component:Delete},
  {path: "payment", component:Payment},
  {path: "receipt", component:Receipt},
  {path: "**", component:Notfound},

];
