import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { Coffee, LogIn, LucideAngularModule } from 'lucide-angular';
import { Account } from '../createaccount/account';
import { Header } from '../header/header';


@Component({
  selector: 'app-login',
  imports: [LucideAngularModule, CommonModule, ReactiveFormsModule, RouterModule,Header],
  templateUrl: './login.html',
  styleUrl: './login.css',
  standalone: true,
})
export class Login implements OnInit, OnDestroy {
  readonly Login = LogIn;
  coffee= Coffee;
  Form!: FormGroup;
  error!: string;
  error2 = false;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private serviceFile: Account,
    private cdr: ChangeDetectorRef

  ) {}

  ngOnInit() {
    document.body.style.backgroundColor = 'rgb(255 251 235)';
    this.Form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });

  }



  ngOnDestroy() {
    document.body.style.backgroundColor = '';
  }

  submit() {
    if(this.Form.invalid){
      return;
    }

    this.serviceFile.Verify(this.Form.value).subscribe({
      next: (value:any) =>
      {
        sessionStorage.setItem("username", this.Form.value.username);
        console.log(this.Form.value.username);
        console.log("successful login");
        this.error2 = false;
        this.router.navigate(["login/authenticate"]);
      },
      error: er => {
        console.log(er);

        this.error = er.error.message;
        this.error2 = true;
        this.cdr.detectChanges();

        console.log(this.error);
      }
    })
  }
}
