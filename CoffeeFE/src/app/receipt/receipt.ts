import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { LucideAngularModule, Coffee, CheckCircle, Clock } from 'lucide-angular';
import { Header } from '../header/header';

@Component({
  selector: 'app-receipt',
  imports: [CommonModule, RouterModule, Header, LucideAngularModule],
  templateUrl: './receipt.html',
  styleUrl: './receipt.css',
})
export class Receipt implements OnInit, OnDestroy {
  cardHolderName: string = '';
  total: number = 0;
  pickupTime: string = '';

  readonly coffee = Coffee;
  readonly checkCircle = CheckCircle;
  readonly clock = Clock;

  constructor(private router: Router) {}

  ngOnInit(): void {
    document.body.style.backgroundColor = 'rgb(255 251 235)';
    const state = history.state;
    if (state?.cardHolderName) {
      this.cardHolderName = state.cardHolderName;
      this.total = state.total;
    } else {
      this.router.navigate(['/menu']);
      return;
    }
    const now = new Date();
    now.setMinutes(now.getMinutes() + 15);
    this.pickupTime = now.toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
      hour12: true,
    });
  }

  ngOnDestroy(): void {
    document.body.style.backgroundColor = 'rgb(254 243 199)';
  }

  goBackToMenu(): void {
    this.router.navigate(['/menu']);
  }
}
