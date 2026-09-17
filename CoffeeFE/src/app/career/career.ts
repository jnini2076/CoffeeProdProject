import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { LucideAngularModule, Coffee, DollarSign, Clock, Heart, ChevronDown, ChevronUp, MapPin } from 'lucide-angular';
import { Header } from '../header/header';

interface JobPosition {
  id: string;
  title: string;
  type: string;
  pay: string;
  description: string;
  requirements: string[];
}

interface Perk {
  icon: any;
  label: string;
}

@Component({
  selector: 'app-career',
  imports: [CommonModule, RouterModule, LucideAngularModule, Header],
  templateUrl: './career.html',
  styleUrl: './career.css',
})
export class Career {
  constructor(private router: Router) {}

  coffee = Coffee;
  dollarSign = DollarSign;
  clock = Clock;
  heart = Heart;
  chevronDown = ChevronDown;
  chevronUp = ChevronUp;
  mapPin = MapPin;

  expandedRole: string | null = 'barista-lead';

  openPositions: JobPosition[] = [
    {
      id: 'barista-lead',
      title: 'Barista Lead',
      type: 'Full-Time',
      pay: '$20 – $24/hr + tips',
      description: 'Lead the floor during service, support the team, and keep operations running smoothly while delivering top-tier hospitality.',
      requirements: [
        '1+ year of café or food-service leadership',
        'Strong communication & problem-solving skills',
        'Comfortable opening or closing the shop independently',
        'Food-handler certification (or willingness to obtain)',
      ],
    }
  ];

  perks: Perk[] = [
    { icon: Coffee, label: 'Free drinks every shift' },
    { icon: DollarSign, label: 'Competitive pay + tips' },
    { icon: Clock, label: 'Flexible scheduling' },
    { icon: Heart, label: 'Warm, supportive team' },
  ];

  ngOnInit() {
    document.body.style.backgroundColor = 'rgb(255 251 235)';
  }

  toggleRole(id: string) {
    this.expandedRole = this.expandedRole === id ? null : id;
  }

  applyForRole(roleTitle: string) {
    this.router.navigate(['/apply'], { queryParams: { role: roleTitle } });
  }

  ngOnDestroy() {
    document.body.style.backgroundColor = '';
  }
}
