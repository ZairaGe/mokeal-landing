import { Component, inject, signal } from '@angular/core'; // Añadimos signal para mejor rendimiento
import { Router, RouterLink } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';

import { AccountService } from 'app/core/auth/account.service';
import { TranslateDirective } from 'app/shared/language';
import { CommonModule } from '@angular/common'; // Importante para el [class.active] y @if

@Component({
  selector: 'jhi-home',
  templateUrl: './home.html',
  styleUrl: './home.scss',
  standalone: true,
  imports: [CommonModule, TranslateDirective, TranslateModule, RouterLink],
})
export default class Home {
  public readonly account = inject(AccountService).account;
  private readonly router = inject(Router);

  // Estado para controlar qué pestaña se ve
  // Usamos un string para identificar: 'hoy', 'trabajadores', 'tarifas', 'facturas'
  activeTab = 'hoy';

  setTab(tabName: string): void {
    this.activeTab = tabName;
  }

  login(): void {
    this.router.navigate(['/login']);
  }
}
