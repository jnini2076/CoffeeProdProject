import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { Coffee, Lock, LucideAngularModule, Phone, UserPlus } from 'lucide-angular';
import { Account } from './account';
import { Header } from '../header/header';


@Component({
  selector: 'app-createaccount',
  imports: [CommonModule,ReactiveFormsModule,LucideAngularModule,RouterModule,Header],
  templateUrl: './createaccount.html',
  styleUrl: './createaccount.css',
  standalone: true,
})
export class Createaccount {
   readonly user = UserPlus;
   coffee = Coffee;
   phone = Phone;
   lock= Lock;
   success = false;
   duplicateError = false;
   Form!:FormGroup;
   constructor(private Fb:FormBuilder, private sf:Account, private router:Router){}

   ngOnInit(){
    document.body.style.backgroundColor = 'rgb(255 251 235)';
    this.Form = this.Fb.group({
      firstname:["",Validators.required],
      lastname:["",Validators.required],
      phonenumber:["",Validators.required],
      username:["", [Validators.required, Validators.minLength(5)]],
      password:["", [Validators.required, Validators.minLength(8)]],
    })
   }
    submit(){
      if(this.Form.invalid){
        return;
      }
        this.sf.PostAccount(this.Form.value).subscribe({
          next:() => {
            console.log("account has been posted");
            const username = this.Form.value.username;
            this.success = true;
            console.log(this.Form.value.username);
            setTimeout(() =>{
            this.router.navigate(["login/account/mfr", username]);
            }, 1000);

          },
          error:(er:any) => {
            console.log(er);
            this.duplicateError = true;
          }
        })
      }



   ngOnDestroy(){
    document.body.style.backgroundColor = '';
   }


}
