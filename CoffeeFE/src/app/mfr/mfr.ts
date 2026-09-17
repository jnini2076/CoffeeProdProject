import { Coffee, LucideAngularModule } from 'lucide-angular';
import { QRCodeComponent } from 'angularx-qrcode';
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Account } from '../createaccount/account';
import { Qrcode } from './QrcodeStuff';
import { Header } from '../header/header';

@Component({
  selector: 'app-mfr',
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    QRCodeComponent,
    Header,
    LucideAngularModule,
  ],
  templateUrl: './mfr.html',
  styleUrls: ['./mfr.css'],
})
export class Mfr {
  coffee = Coffee;
  constructor(
    private fb: FormBuilder,
    private sf: Account,
    private cdr: ChangeDetectorRef,
    private route: ActivatedRoute,
    private router: Router,
  ) {}
  Form!: FormGroup;

  ready: boolean = false;
  data!: Qrcode;
  Qrcode: string = '';
  otpauthUrl: string = '';
  User!: string | null;
  username: string = '';
  notValid!: string;
  failure = false;

  ngOnInit() {
    document.body.style.backgroundColor = 'rgb(255 251 235)';
    this.route.paramMap.subscribe((params) => {
      this.username = params.get('username') || '';
    });

    this.Form = this.fb.group({
      verificationCode: ['', Validators.required],
    });
    this.sf.GetQRcode(this.username).subscribe({
      next: (value: any) => {
        console.log('Url obtained');
        console.log(value);
        setTimeout(() => {
          this.data = value;
          this.Qrcode = value.qrcodeurl;

          this.otpauthUrl = `otpauth://totp/${encodeURIComponent(value.appName + ':' + this.username)}?secret=${value.secret}&issuer=${encodeURIComponent(value.appName)}`;

          this.ready = true;
          console.log('OTPAuth URL:', this.otpauthUrl);
          console.log(this.data);
          this.cdr.detectChanges();
        }, 25);
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

  submit() {
    if (this.Form.invalid) {
      return;
    }

    const formdata = new FormData();
    formdata.append('verificationCode', this.Form.value.verificationCode);
    formdata.append('username', this.username);
    this.sf.PostVerificationCode(formdata).subscribe({
      next: (results: any) => {
        console.log(results);
        console.log('we DID IT');
        this.router.navigate(['login']);
      },
      error: (er) => {
        this.failure = true;
        this.notValid = er.error.message;
        this.cdr.detectChanges();
        console.log(er);
        console.log('ehhhh wrong');
      },
    });
  }

  ngOnDestroy() {
    document.body.style.backgroundColor = '';
  }
}
