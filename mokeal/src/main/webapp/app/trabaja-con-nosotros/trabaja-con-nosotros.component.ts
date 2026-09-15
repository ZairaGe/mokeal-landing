import { Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import NavbarComponent from 'app/layouts/navbar/navbar';
import Footer from 'app/layouts/footer/footer';
import { FormsModule } from '@angular/forms';



@Component({
  selector: 'app-trabaja-con-nosotros',
  standalone: true,
  imports: [RouterLink, NavbarComponent, Footer, FormsModule],
  templateUrl: './trabaja-con-nosotros.component.html',
  styleUrl: './trabaja-con-nosotros.component.scss'
})
export class TrabajaConNosotrosComponent {
  enviando = signal(false);
  enviado = signal(false);
  error = signal(false);
  arrastrando = signal(false);
  archivoNombre = signal<string | null>(null);

  onDragOver(event: DragEvent) {
    event.preventDefault();
    this.arrastrando.set(true);
  }

  onDragLeave(event: DragEvent) {
    event.preventDefault();
    this.arrastrando.set(false);
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    this.arrastrando.set(false);

    const file = event.dataTransfer?.files?.[0];
    if (file && file.type === 'application/pdf') {
      this.archivoNombre.set(file.name);

      // sincroniza el input real para que el FormData lo incluya al enviar
      const input = (event.currentTarget as HTMLElement).querySelector('input[type="file"]') as HTMLInputElement;
      const dataTransfer = new DataTransfer();
      dataTransfer.items.add(file);
      input.files = dataTransfer.files;
    }
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    this.archivoNombre.set(input.files?.[0]?.name ?? null);
  }

  onSubmit(event: Event) {
    event.preventDefault();
    const form = event.target as HTMLFormElement;
    const formData = new FormData(form);

    this.enviando.set(true);
    this.error.set(false);

    fetch('/', { method: 'POST', body: formData })
      .then(() => {
        this.enviado.set(true);
        form.reset();
        this.archivoNombre.set(null);
      })
      .catch(() => this.error.set(true))
      .finally(() => this.enviando.set(false));
  }

  ngOnInit(): void {

    window.scrollTo(0, 0);
  }
}