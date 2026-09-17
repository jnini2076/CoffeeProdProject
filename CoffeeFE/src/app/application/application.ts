import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import {
  LucideAngularModule,
  Coffee,
  User,
  Briefcase,
  FileText,
  CheckCircle,
  Upload,
  ChevronRight,
  ChevronLeft,
  MapPin,
} from 'lucide-angular';
import { Header } from '../header/header';
import { Account } from '../createaccount/account';

@Component({
  selector: 'app-application',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, LucideAngularModule, Header],
  templateUrl: './application.html',
  styleUrl: './application.css',
})
export class Application implements OnInit, OnDestroy {
  coffee = Coffee;
  user = User;
  briefcase = Briefcase;
  fileText = FileText;
  checkCircle = CheckCircle;
  upload = Upload;
  chevronRight = ChevronRight;
  chevronLeft = ChevronLeft;
  mapPin = MapPin;

  currentStep = 1;
  totalSteps = 6;
  resumeFile: File | null = null;
  resumeFileName = '';
  resumeError = '';
  submitting = false;
  submitted = false;
  roleTitle = '';
  good:boolean = false;

  applicationForm!: FormGroup;

  steps = [
    { number: 1, label: 'Personal' },
    { number: 2, label: 'Position' },
    { number: 3, label: 'Experience' },
    { number: 4, label: 'About You' },
    { number: 5, label: 'Resume' },
    { number: 6, label: 'Review' },
  ];

  howDidYouHearOptions = [
    'Indeed',
    'LinkedIn',
    'Friend or Family',
    'Walk-in',
    'Social Media',
    'Other',
  ];

  usStates = [
    'AL', 'AK', 'AZ', 'AR', 'CA', 'CO', 'CT', 'DE', 'FL', 'GA',
    'HI', 'ID', 'IL', 'IN', 'IA', 'KS', 'KY', 'LA', 'ME', 'MD',
    'MA', 'MI', 'MN', 'MS', 'MO', 'MT', 'NE', 'NV', 'NH', 'NJ',
    'NM', 'NY', 'NC', 'ND', 'OH', 'OK', 'OR', 'PA', 'RI', 'SC',
    'SD', 'TN', 'TX', 'UT', 'VT', 'VA', 'WA', 'WV', 'WI', 'WY',
  ];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private sf: Account,
  ) {}

  ngOnInit() {
    document.body.style.backgroundColor = 'rgb(255 251 235)';

    this.applicationForm = this.fb.group({
      personal: this.fb.group({
        firstName: ['', Validators.required],
        lastName: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        phone: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
        address: ['', Validators.required],
        city: ['', Validators.required],
        state: ['', Validators.required],
        zip: ['', [Validators.required, Validators.pattern(/^\d{5}$/)]],
      }),
      position: this.fb.group({
        positionApplying: ['', Validators.required],
        employmentType: ['', Validators.required],
        desiredStartDate: ['', Validators.required],
        hoursAvailable: ['', [Validators.required, Validators.min(1), Validators.max(40)]],
      }),
      experience: this.fb.group({
        hasPreviousExperience: ['', Validators.required],
        previousEmployer: [''],
        previousJobTitle: [''],
        previousStartDate: [''],
        previousEndDate: [''],
        reasonForLeaving: [''],
        additionalExperience: [''],
      }),
      about: this.fb.group({
        whyJoin: ['', [Validators.required, Validators.minLength(50)]],
        aboutYourself: ['', [Validators.required, Validators.minLength(50)]],
        howDidYouHear: ['', Validators.required],
      }),
    });

    this.route.queryParams.subscribe(params => {
      this.roleTitle = params['role'] || '';
      this.applicationForm.get('position.positionApplying')?.setValue(this.roleTitle);
    });
  }

  get personalGroup(): FormGroup {
    return this.applicationForm.get('personal') as FormGroup;
  }

  get positionGroup(): FormGroup {
    return this.applicationForm.get('position') as FormGroup;
  }

  get experienceGroup(): FormGroup {
    return this.applicationForm.get('experience') as FormGroup;
  }

  get aboutGroup(): FormGroup {
    return this.applicationForm.get('about') as FormGroup;
  }

  fc(group: string, control: string): AbstractControl {
    return this.applicationForm.get(`${group}.${control}`)!;
  }

  hasError(group: string, control: string, error: string): boolean {
    const ctrl = this.fc(group, control);
    return ctrl.hasError(error) && ctrl.touched;
  }

  isInvalid(group: string, control: string): boolean {
    const ctrl = this.fc(group, control);
    return ctrl.invalid && ctrl.touched;
  }

  isStepValid(): boolean {
    switch (this.currentStep) {
      case 1: return this.personalGroup.valid;
      case 2: return this.positionGroup.valid;
      case 3: return this.experienceGroup.valid;
      case 4: return this.aboutGroup.valid;
      case 5: return this.resumeFile !== null;
      case 6: return true;
      default: return false;
    }
  }

  nextStep() {
    if (this.isStepValid()) {
      this.currentStep++;
      window.scrollTo({ top: 0, behavior: 'smooth' });
    } else {
      this.markCurrentStepTouched();
    }
  }

  prevStep() {
    if (this.currentStep > 1) {
      this.currentStep--;
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  markCurrentStepTouched() {
    switch (this.currentStep) {
      case 1: this.personalGroup.markAllAsTouched(); break;
      case 2: this.positionGroup.markAllAsTouched(); break;
      case 3: this.experienceGroup.markAllAsTouched(); break;
      case 4: this.aboutGroup.markAllAsTouched(); break;
      case 5:
        if (!this.resumeFile) {
          this.resumeError = 'Please upload your resume to continue.';
        }
        break;
    }
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      const allowedTypes = [
        'application/pdf',
        'application/msword',
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
      ];
      if (!allowedTypes.includes(file.type)) {
        this.resumeError = 'Please upload a PDF or Word document (.pdf, .doc, .docx).';
        this.resumeFile = null;
        this.resumeFileName = '';
        return;
      }
      if (file.size > 5 * 1024 * 1024) {
        this.resumeError = 'File size must be under 5MB.';
        this.resumeFile = null;
        this.resumeFileName = '';
        return;
      }
      this.resumeFile = file;
      this.resumeFileName = file.name;
      this.resumeError = '';
    }
  }

  removeResume() {
    this.resumeFile = null;
    this.resumeFileName = '';
    this.resumeError = '';
  }

  private fileToBase64(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        const base64 = (reader.result as string).split(',')[1];
        resolve(base64);
      };
      reader.onerror = reject;
    });
  }

  async submitApplication() {
    if (this.applicationForm.invalid || !this.resumeFile) return;

    this.submitting = true;

    const base64 = await this.fileToBase64(this.resumeFile);
    const v = this.applicationForm.value;

    const dto = {
      firstName:             v.personal.firstName,
      lastName:              v.personal.lastName,
      email:                 v.personal.email,
      phone:                 v.personal.phone,
      address:               v.personal.address,
      city:                  v.personal.city,
      state:                 v.personal.state,
      zip:                   v.personal.zip,
      positionApplying:      v.position.positionApplying,
      employmentType:        v.position.employmentType,
      desiredStartDate:      v.position.desiredStartDate,
      hoursAvailable:        v.position.hoursAvailable,
      hasPreviousExperience: v.experience.hasPreviousExperience,
      previousEmployer:      v.experience.previousEmployer      || null,
      previousJobTitle:      v.experience.previousJobTitle      || null,
      previousStartDate:     v.experience.previousStartDate     || null,
      previousEndDate:       v.experience.previousEndDate       || null,
      reasonForLeaving:      v.experience.reasonForLeaving      || null,
      additionalExperience:  v.experience.additionalExperience  || null,
      whyJoin:               v.about.whyJoin,
      aboutYourself:         v.about.aboutYourself,
      howDidYouHear:         v.about.howDidYouHear,
      resumeFileName:        this.resumeFile.name,
      resumeBase64:          base64,
    };

    this.sf.PostApplication(dto).subscribe({
      next: () => {
        console.log('Application submitted successfully');
        this.submitting = false;
        this.submitted = true;
        this.good = true;
        window.scrollTo({ top: 0, behavior: 'smooth' });
        setTimeout(() => this.router.navigate(['/']), 6000);
      },
      error: (err) => {
        console.error('Application submission failed', err);
        this.submitting = false;
      },
    });
  }

  ngOnDestroy() {
    document.body.style.backgroundColor = '';
  }
}
