import { isLoggedIn, USERNAME } from './../state';
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { LucideAngularModule, UserX, Coffee } from 'lucide-angular';
import { Account } from '../createaccount/account';
import { Header } from '../header/header';

@Component({
  selector: 'app-delete',
  imports: [CommonModule, ReactiveFormsModule, RouterModule, LucideAngularModule, Header],
  templateUrl: './delete.html',
  styleUrl: './delete.css',
})
export class Delete {
  readonly user = UserX;
  coffee = Coffee;
  Form!: FormGroup;
  User: String | null = sessionStorage.getItem('loggedin');
  invalidDelete = false;
  isLoggedIn = isLoggedIn;
  USERNAME = USERNAME;

  constructor(
    private Fb: FormBuilder,
    private Router: Router,
    private SF: Account,
  ) {}

  ngOnInit() {
    document.body.style.backgroundColor = 'rgb(255 251 235)';

    this.Form = this.Fb.group({
      data: ['', Validators.required],
    });
  }

  submit() {
    if (this.Form.invalid) {
      console.log('invalid form');
      return;
    }
    if (this.Form.value.data != 'delete') {
      console.log('didnt type delete');
      this.invalidDelete = true;
      return;
    }
    this.invalidDelete = false;

    this.SF.DeleteAccount(this.User).subscribe({
      next: (value: any) => {
        this.isLoggedIn.set(false);
        this.USERNAME.set(false);
        this.SF.Logout();
        alert('account has been deleted');
        this.Router.navigate(['/']);

      },
    });
  }
}
