import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, ElementRef, QueryList, ViewChildren } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { Key, LucideAngularModule } from 'lucide-angular';
import { Account } from '../createaccount/account';
import { isLoggedIn, USERNAME } from '../state';
import { Header } from '../header/header';

@Component({
  selector: 'app-authenticate',
  imports: [ReactiveFormsModule, CommonModule, RouterModule, LucideAngularModule, Header],
  templateUrl: './authenticate.html',
  styleUrl: './authenticate.css',
  standalone: true,
})
export class Authenticate {
  constructor(private fb: FormBuilder, private sf: Account, private router: Router, private  cdr:ChangeDetectorRef) {}

  key = Key;
  Form!: FormGroup;
  username: string | null = sessionStorage.getItem('username');
  digitBoxes = new Array(6);
  error!: string;

  @ViewChildren('digitInput') digitInputs!: QueryList<ElementRef<HTMLInputElement>>;

  ngOnInit() {
    document.body.style.backgroundColor = 'rgb(255 251 235)';
    this.Form = this.fb.group({
      verficationCode: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  onDigitInput(event: Event, index: number) {
    const input = event.target as HTMLInputElement;
    const value = input.value.replace(/[^0-9]/g, '');
    input.value = value;

    this.updateFormControl();

    if (value && index < 5) {
      this.digitInputs.toArray()[index + 1].nativeElement.focus();
    }
  }

  onKeyDown(event: KeyboardEvent, index: number) {
    if (event.key === 'Backspace') {
      const input = event.target as HTMLInputElement;
      if (!input.value && index > 0) {
        this.digitInputs.toArray()[index - 1].nativeElement.focus();
      }
      setTimeout(() => this.updateFormControl(), 0);
    }
  }

  private updateFormControl() {
    const code = this.digitInputs
      .toArray()
      .map(el => el.nativeElement.value)
      .join('');
    this.Form.patchValue({ verficationCode: code });
    this.Form.get('verficationCode')?.updateValueAndValidity();
    this.cdr.detectChanges();
  }

  submit() {
    if (this.Form.invalid) return;
    if (!this.username) {
      console.log("username ain't there");
      return;
    }

    const formdata = new FormData();
    formdata.append('username', this.username);
    formdata.append('verificationCode', this.Form.value.verficationCode);

    this.sf.finalVerfify(formdata).subscribe({
      next: () => {
        console.log('you are in!');
        sessionStorage.setItem('loggedin', this.username!);
        USERNAME.set(true);
        sessionStorage.removeItem('username');
        isLoggedIn.set(true);
        this.router.navigate(['login/authenticate/menu']);
      },
      error: er => {
        console.log(er);
        this.error = er.error.message;
        console.log(this.error);
        this.cdr.detectChanges();
      },
    });
  }

  ngOnDestroy() {
    document.body.style.backgroundColor = 'rgb(255 231 255)';
  }
}
