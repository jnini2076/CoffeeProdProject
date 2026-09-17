import { USERNAME } from './../state';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import {
  LucideAngularModule,
  Coffee,
  ShoppingCart,
  X,
  CreditCard,
} from 'lucide-angular';
import { CartService, CartItem } from '../cart/cart.service';
import { Header } from '../header/header';
import { Account } from '../createaccount/account';

@Component({
  selector: 'app-payment',
  imports: [CommonModule, FormsModule, RouterModule, Header, LucideAngularModule],
  templateUrl: './payment.html',
  styleUrl: './payment.css',
})
export class Payment implements OnInit, OnDestroy {
  cartItems: CartItem[] = [];
  total: number = 0;
  private sub!: Subscription;

  cardNumber: string = '1234 1234 1234 1234';
  cardHolderName: string = 'John Doe';
  firstname: string | null = sessionStorage.getItem("firstname");
  expiryDate: string = '12/26';
  cvv: string = '123';
  customerName: string = '';

  errorMessage: string = '';
  isSubmitting: boolean = false;

  readonly coffee = Coffee;
  readonly cartIcon = ShoppingCart;
  readonly xIcon = X;
  readonly creditCard = CreditCard;
  readonly USERNAME = USERNAME;

  constructor(
    private cartService: CartService,
    private accountService: Account,
    private router: Router
  ) {}

  ngOnInit(): void {
    document.body.style.backgroundColor = 'rgb(255 251 235)';
    this.sub = this.cartService.cartItems$.subscribe(items => {
      this.cartItems = items;
      this.total = this.cartService.getTotal();
    });
    const user = sessionStorage.getItem('loggedin');
    if (user) {
      this.customerName = user;
    }
  }

  ngOnDestroy(): void {
    document.body.style.backgroundColor = 'rgb(254 243 199)';
    this.sub.unsubscribe();
  }

  removeItem(index: number): void {
    this.cartService.removeItem(index);
  }

  onSubmit(): void {
    if (this.cartItems.length === 0) {
      this.errorMessage = 'Your cart is empty. Please add items before placing an order.';
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';

    const payload = {
      customerName: this.customerName || 'Guest',
      items: this.cartItems,
      totalAmount: this.total,
      cardNumber: this.cardNumber,
      cardHolderName: this.cardHolderName,
      expiryDate: this.expiryDate,
      cvv: this.cvv,
    };

    this.accountService.PostOrderPayment(payload).subscribe({
      next: () => {
        const orderTotal = this.total;
        this.cartService.clearCart();
        this.isSubmitting = false;
        this.router.navigate(['/receipt'], {
          state: {
            cardHolderName: this.cardHolderName,
            total: orderTotal,
          },
        });
      },
      error: (err: any) => {
        console.error(err);
        this.errorMessage = 'Something went wrong. Please try again.';
        this.isSubmitting = false;
      },
    });
  }
}
