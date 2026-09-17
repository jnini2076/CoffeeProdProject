import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Clock, Coffee, IceCreamCone, Leaf, Mail, MapPin, Phone, Star } from 'lucide-angular';
import { Cookie, LucideAngularModule } from 'lucide-angular/src/icons';
@Component({
  selector: 'app-home',
  imports: [RouterModule,CommonModule,LucideAngularModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  coffee = Coffee;
  star = Star;
  iceCream = IceCreamCone;
  cookie = Cookie;
  leaf = Leaf;
  map = MapPin;
  phone=Mail;
  clock = Clock
  constructor(

  ){}

  ngOnInit(){
    document.body.style.backgroundColor="rgb(255 255 255)";
  }


  ngOnDestroy(){
    document.body.style.backgroundColor="rgb(255 255 255)";
  }



}
