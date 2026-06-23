import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import RevealDirective from 'app/reveal/reveal.directive';

@Component({
  selector: 'app-services',
  standalone: true,
  templateUrl: './services.component.html',
  styleUrl: './services.component.scss',
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    RevealDirective]
})
export default class ServicesComponent {}