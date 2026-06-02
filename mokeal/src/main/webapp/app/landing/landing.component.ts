import { Component, ViewEncapsulation, OnInit, inject } from '@angular/core'; // Añadidos OnInit e inject
import { ActivatedRoute, RouterModule } from '@angular/router'; // Añadido ActivatedRoute
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import Footer from 'app/layouts/footer/footer';
import NavbarComponent from 'app/layouts/navbar/navbar';
import HeroComponent from './sections/hero/hero.component';
import ServicesComponent from './sections/services/services.component';
import ReviewsComponent from './sections/reviews/reviews.component';
import ContactComponent from './sections/contact/contact.component';
import DifComponent from './sections/differentiators/dif.component';

@Component({
  selector: 'jhi-landing',
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.scss',
  encapsulation: ViewEncapsulation.None,
  standalone: true,
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
export default class LandingComponent implements OnInit {
  // Implementar OnInit
  private route = inject(ActivatedRoute); // Inyectar la ruta activa

  formEnviado = false;
  contactForm = {
    nombre: '',
    telefono: '',
    email: '',
    tipoServicio: '',
    mensaje: '',
  };

  ngOnInit(): void {
    // Escuchar cambios en el fragmento (#servicios, #nosotros, etc.)
    this.route.fragment.subscribe(frag => {
      if (frag) {
        this.scrollToElement(frag);
      }
    });
  }

  private scrollToElement(id: string): void {
    // Usamos un pequeño timeout para asegurar que el DOM esté listo
    setTimeout(() => {
      const element = document.getElementById(id);
      if (element) {
        element.scrollIntoView({
          behavior: 'smooth',
          block: 'start',
          inline: 'nearest',
        });
      }
    }, 100);
  }

  submitForm(): void {
    this.formEnviado = true;
    setTimeout(() => {
      this.formEnviado = false;
      this.contactForm = { nombre: '', telefono: '', email: '', tipoServicio: '', mensaje: '' };
    }, 5000);
  }
}
