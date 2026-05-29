import { Component, ViewEncapsulation } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import Footer from 'app/layouts/footer/footer';
import NavbarComponent from 'app/layouts/navbar/navbar';  
import  HeroComponent  from './sections/hero/hero.component';
import ServicesComponent  from './sections/services/services.component';
import  ReviewsComponent  from './sections/reviews/reviews.component';
import ContactComponent  from './sections/contact/contact.component';
import DifComponent from './sections/differentiators/dif.component';

@Component({
  selector: 'jhi-landing',
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.scss',
  encapsulation: ViewEncapsulation.None,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    Footer,
    NavbarComponent,
    HeroComponent,
    ServicesComponent,
    DifComponent,
    ReviewsComponent,
    ContactComponent,
  ],
})
export default class LandingComponent {
  formEnviado = false;

  contactForm = {
    nombre: '',
    telefono: '',
    email: '',
    tipoServicio: '',
    mensaje: '',
  };

  submitForm(): void {
    this.formEnviado = true;
    setTimeout(() => {
      this.formEnviado = false;
      this.contactForm = { nombre: '', telefono: '', email: '', tipoServicio: '', mensaje: '' };
    }, 5000);
  }
}
