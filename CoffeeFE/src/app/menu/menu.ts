import { USERNAME } from './../state';
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { Account } from '../createaccount/account';
import { Coffees, CoffeeItems } from './differentCoffees';
import { Router, RouterModule } from '@angular/router';
import { Header } from '../header/header';
import { Coffee, Cookie, IceCreamCone, LucideAngularModule } from 'lucide-angular';
import { CartService } from '../cart/cart.service';

@Component({
  selector: 'app-menu',
  imports: [CommonModule, RouterModule, Header, LucideAngularModule],
  templateUrl: './menu.html',
  styleUrl: './menu.css',
})
export class Menu {
  constructor(
    private sf:Account,
    private cdr: ChangeDetectorRef,
    private Router:Router,
    private cartService: CartService,
  ){}
  User:String | null = sessionStorage.getItem("loggedin");
  firstname:String ="";
  readonly USERNAME = USERNAME;
  CoffeeMenu = CoffeeItems;
  coffee = Coffee;
  ice = IceCreamCone;
  cookie = Cookie;



  ngOnInit(){
    document.body.style.backgroundColor="rgb(255 251 235)";
      if(!this.User){
        return;
      }

    this.sf.LoggedIn(this.User).subscribe({
      next:(value:any) => {
          console.log(value);
          this.firstname = value;
          sessionStorage.setItem("firstname",value);
          this.cdr.detectChanges();
      }
    })
  }


  addToCart(item: Coffees): void {
    this.cartService.addItem({ name: item.name, price: item.price });
  }

  ngOnDestroy(){
    document.body.style.backgroundColor="rgb(254 243 199)";
  }





}

