import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';

export interface CartItem {
  name: string;
  price: number;
}

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private cartSubject = new BehaviorSubject<CartItem[]>([]);

  cartItems$ = this.cartSubject.asObservable();
  cartCount$ = this.cartSubject.pipe(map(items => items.length));

  addItem(item: CartItem): void {
    const current = this.cartSubject.getValue();
    this.cartSubject.next([...current, item]);
  }

  removeItem(index: number): void {
    const current = this.cartSubject.getValue();
    this.cartSubject.next(current.filter((_, i) => i !== index));
  }

  clearCart(): void {
    this.cartSubject.next([]);
  }

  getTotal(): number {
    return this.cartSubject.getValue().reduce((sum, item) => sum + item.price, 0);
  }

  getItemCount(): number {
    return this.cartSubject.getValue().length;
  }
}
