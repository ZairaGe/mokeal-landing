import { Component, HostListener, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
  standalone: true,
  imports: [
    RouterLink,
    FontAwesomeModule,
  ],
})
export default class Navbar {
  private readonly router = inject(Router);

  isScrolled = false;
  isMenuOpen = false;
  isNavbarCollapsed = true;

  @HostListener('window:scroll')
  onScroll(): void {
    this.isScrolled = window.scrollY > 10;
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  scrollToSection(id: string): void {
    this.collapseNavbar();
    this.isMenuOpen = false;
    const el = document.getElementById(id);
    if (el) {
      el.scrollIntoView({ behavior: 'smooth', block: 'start' });
    } else if (this.router.url !== '/') {
      this.router.navigate(['/'], { fragment: id });
    }
  }

  closeMenuAndScroll(sectionId: string): void {
    this.isMenuOpen = false;
    this.scrollToSection(sectionId);
  }
}