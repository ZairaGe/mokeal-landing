import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router'; // Importamos Router y RouterModule
import { TranslateDirective } from 'app/shared/language';

@Component({
  standalone: true,
  selector: 'jhi-footer',
  templateUrl: './footer.html', // Verifica que el nombre sea exacto
  styleUrl: './footer.scss',
  imports: [TranslateDirective, RouterModule], // Necesario para navegar
})
export default class Footer {
  private router = inject(Router); // Solo necesitamos el Router

  login(): void {
    // Te lleva directamente a la página de inicio de sesión
    this.router.navigate(['/login']);
  }
}