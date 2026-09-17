import { CommonModule, AsyncPipe } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import {
  LucideAngularModule,
  CoffeeIcon,
  BriefcaseBusiness,
  LogIn,
  ShoppingCart,
  Trash2,
  UserRoundCog,
} from 'lucide-angular';
import { isLoggedIn, USERNAME } from '../state';
import { Account } from '../createaccount/account';
import { CartService } from '../cart/cart.service';

@Component({
  selector: 'app-header',
  imports: [CommonModule, LucideAngularModule, RouterModule],
  templateUrl: './header.html',
  styleUrls: ['./header.css'],
  standalone: true,
})
export class Header {
  readonly Coffee = CoffeeIcon;
  readonly BriefcaseBusiness = BriefcaseBusiness;
  readonly Login = LogIn;
  readonly Cart = ShoppingCart;
  readonly Trash = Trash2;
  readonly user = UserRoundCog;

  isLoggedIn = isLoggedIn;

  constructor(
    private sf: Account,
    private cdr: ChangeDetectorRef,
    private router: Router,
    private cartService: CartService,
  ) {}

  Logout() {
    this.sf.Logout().subscribe({
      next: (value: any) => {
        console.log(value);
        this.isLoggedIn.set(false);
        sessionStorage.removeItem('loggedin');
        this.cdr.detectChanges();
        USERNAME.set(false);
      },
      error: (er: any) => {
        console.log(er);
      },
    });
  }

  goToCart() {
    this.router.navigate(['/payment']);
  }

  clearCart() {
    this.cartService.clearCart();
  }
}
