import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import RevealDirective from 'app/reveal/reveal.directive';
import emailjs from '@emailjs/browser';

@Component({
  selector: 'app-contact',
  standalone: true,
  templateUrl: './contact.component.html',
  styleUrl: './contact.component.scss',
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    RevealDirective
  ]
})
export default class ContactComponent {
  isSending = false;
  successMessage = '';
  errorMessage = '';
  showSuccessModal = false;

  formData = {
    name: '',
    surname: '',
    phone: '',
    email: '',
    service: '',
    source: '',
    message: ''
  };

  sendEmail() {
    this.successMessage = '';
    this.errorMessage = '';

    if (
      !this.formData.name.trim() ||
      !this.formData.surname.trim() ||
      !this.formData.phone.trim() ||
      !this.formData.email.trim() ||
      !this.formData.message.trim()
    ) {
      this.errorMessage = 'Por favor, completa todos los campos obligatorios.';
      return;
    }

    this.isSending = true;

    const templateParams = {
      name: this.formData.name,
      surname: this.formData.surname,
      phone: this.formData.phone,
      email: this.formData.email,
      service: this.formData.service || 'No especificado',
      source: this.formData.source || 'No especificado',
      message: this.formData.message
    };

    emailjs
      .send(
        'service_gqwo7vl',
        'template_yktzonc',
        templateParams,
        '3inQaw-bYEWbii6O0'
      )
      .then(() => {
        this.resetForm();
        this.showSuccessModal = true;
      })
      .catch((error) => {
        console.error('Error al enviar el correo:', error);
        this.errorMessage = 'Hubo un error al enviar la solicitud. Inténtalo de nuevo.';
      })
      .finally(() => {
        this.isSending = false;
      });
  }

  closeSuccessModal() {
    this.showSuccessModal = false;
  }

  resetForm() {
    this.formData = {
      name: '',
      surname: '',
      phone: '',
      email: '',
      service: '',
      source: '',
      message: ''
    };
  }
}