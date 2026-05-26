import { Component, ViewEncapsulation } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'jhi-landing',
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.scss',
  encapsulation: ViewEncapsulation.None,
  imports: [RouterModule, FormsModule],
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
