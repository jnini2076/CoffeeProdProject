import { Component, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { LucideAngularModule } from 'lucide-angular';
import { Coffee, ShieldQuestionMark } from 'lucide-angular/src/icons';
import { Account } from '../createaccount/account';
import { Header } from '../header/header';

@Component({
  selector: 'app-forgotpassword',
  imports: [RouterModule, LucideAngularModule, ReactiveFormsModule, Header],
  templateUrl: './forgotpassword.html',
  styleUrl: './forgotpassword.css',
  standalone: true,
})
export class Forgotpassword {
  readonly shield = ShieldQuestionMark;
  coffee = Coffee;
  Form!: FormGroup;
  success = false;
  invalidCredentials = false;

  constructor(
    private Fb: FormBuilder,
    private SF: Account,
    private router: Router,
    private cdr:ChangeDetectorRef
  ) {}

  ngOnInit() {
    document.body.style.backgroundColor = 'rgb(255 251 235)';
    this.Form = this.Fb.group({
      username: ['', Validators.required],
      verificationCode: ['', Validators.required],
      newpassword: ['', Validators.required],
    });
  }

  submit() {
    if (!this.Form.valid) {
      console.log('form is not valid');
      return;
    }
    this.SF.Forgotpassword(this.Form.value).subscribe({
      next: () => {
        this.success = true;
        this.cdr.detectChanges();
        console.log('success updating the password');
          setTimeout(() => {
             this.router.navigate(['/login']);
          }, 300);



      },
      error: (er) => {
        console.log(er);
        this.invalidCredentials = true;
        this.cdr.detectChanges();
      },
    });
  }

  onCodeInput(event: any, index: number) {
    const value = event.target.value;
    if (value && index < 5) {
      const nextInput = document.querySelector(`#code${index + 1}`) as HTMLInputElement;
      if (nextInput) nextInput.focus();
    }
    this.updateVerificationCode();
  }

  onKeyDown(event: any, index: number) {
    if (event.key === 'Backspace' && !event.target.value && index > 0) {
      const prevInput = document.querySelector(`#code${index - 1}`) as HTMLInputElement;
      if (prevInput) prevInput.focus();
    }
  }

  updateVerificationCode() {
    const digits = [];
    for (let i = 0; i < 6; i++) {
      const input = document.querySelector(`#code${i}`) as HTMLInputElement;
      digits.push(input?.value || '');
    }
    this.Form.patchValue({ verificationCode: digits.join('') });
  }

  ngOnDestroy() {
    document.body.style.backgroundColor = 'rgb(255 251 235)';
  }
}
