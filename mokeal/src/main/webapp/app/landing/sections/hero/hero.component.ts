import { CommonModule } from '@angular/common';
import { Component, HostListener } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import RevealDirective from 'app/reveal/reveal.directive';

@Component({
    selector: 'app-hero',
    standalone: true,
    templateUrl: './hero.component.html',
    styleUrl: './hero.component.scss',
    imports: [
        CommonModule,
        RouterModule,
        FormsModule,
    RevealDirective,]
})
export default class HeroComponent {
    isScrolled = false;

    @HostListener('window:scroll')
    onScroll(): void {
        this.isScrolled = window.scrollY > 10;
    }

    scrollToContact(): void {
        const el = document.getElementById('contacto');
        if (el) {
            el.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    }
}